package com.dolu.mobibus.core.data.repository

import com.dolu.mobibus.core.data.local.BusTicketDao
import com.dolu.mobibus.core.data.local.UserDao
import com.dolu.mobibus.core.data.remote.BusTicketApi
import com.dolu.mobibus.core.domain.model.ANONYMOUS_USER_ID
import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.model.User
import com.dolu.mobibus.core.domain.model.defaultTicketList
import com.dolu.mobibus.core.domain.repository.CoreRepository
import com.dolu.mobibus.core.util.NetworkResource
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CoreRepositoryImpl @Inject constructor(
    private val api: BusTicketApi,
    private val dao: BusTicketDao,
    private val userDao: UserDao
) : CoreRepository {

    val _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    override suspend fun getBusTicketInfos(): Flow<NetworkResource<List<BusTicket>>> = flow {
        // TODO: Fetch bus price from the api
        emit(NetworkResource.Loading())
        emit(NetworkResource.Loading(data = defaultTicketList))

        val ticketInfos = dao.getAllBusTicketInfos().map { it.toBusTicket() }
        emit(NetworkResource.Success(data = ticketInfos))

        try {
            val remoteTicketInfos = api.getBusTicketInfos()
            dao.deleteBusTicketInfos(remoteTicketInfos.map { it.id })
            dao.insertBusTicketInfos(remoteTicketInfos.map { it.toEntity() })
        } catch(e: HttpException) {
            emit(NetworkResource.Error(
                message = "Oops, something went wrong!",
                data = ticketInfos
            ))
        } catch(e: IOException) {
            emit(NetworkResource.Error(
                message = "Couldn't reach server, check your internet connection.",
                data = ticketInfos
            ))
        }

        val newTicketInfos = dao.getAllBusTicketInfos().map { it.toBusTicket() }
        emit(NetworkResource.Success(newTicketInfos))
    }

    override suspend fun getActiveUser(): StateFlow<User> {
        var activeUser = userDao.getUserById(ANONYMOUS_USER_ID)

        if (activeUser == null) {
            userDao.deleteUser(user.value.id)
            userDao.insertUser(user.value.toEntity())
            activeUser = userDao.getUserById(ANONYMOUS_USER_ID)
        }

        activeUser?.let {
            _user.emit(it.toUser())
        }

        return user
    }

    override suspend fun addBusTicketToCart(ticket: BusTicket): User {
        val activeUser = getActiveUser().value
        val updatedCart = activeUser.cart
            .toMutableList()
            .apply {
                add(ticket)
            }

        val userToUpdate = activeUser.copy(
            id = activeUser.id,
            purchasedTicket = activeUser.purchasedTicket,
            cart = updatedCart,
            transactions = activeUser.transactions
        )

        userDao.deleteUser(userToUpdate.id)
        userDao.insertUser(userToUpdate.toEntity())

        //TODO maybe better to throw exception if we don't find the user in database
        return userDao.getUserById(userToUpdate.id)?.toUser()?: userToUpdate
    }

    override suspend fun removeBusTicketFromCart(ticket: BusTicket): User {
        val activeUser = getActiveUser().value
        val updatedCart = activeUser.cart
            .toMutableList()
            .apply {
                remove(ticket)
            }

        val userToUpdate = activeUser.copy(
            id = activeUser.id,
            purchasedTicket = activeUser.purchasedTicket,
            cart = updatedCart,
            transactions = activeUser.transactions
        )

        userDao.deleteUser(userToUpdate.id)
        userDao.insertUser(userToUpdate.toEntity())

        //TODO maybe better to throw exception if we don't find the user in database
        return userDao.getUserById(userToUpdate.id)?.toUser()?: userToUpdate
    }

    override suspend fun clearUserCart(): User {
        val activeUser = getActiveUser().value

        val userToUpdate = activeUser.copy(
            id = activeUser.id,
            purchasedTicket = activeUser.purchasedTicket,
            cart = emptyList(),
            transactions = activeUser.transactions
        )

        userDao.deleteUser(userToUpdate.id)
        userDao.insertUser(userToUpdate.toEntity())

        //TODO maybe better to throw exception if we don't find the user in database
        return userDao.getUserById(userToUpdate.id)?.toUser()?: userToUpdate
    }
}