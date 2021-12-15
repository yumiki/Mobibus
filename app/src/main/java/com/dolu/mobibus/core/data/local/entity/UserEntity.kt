package com.dolu.mobibus.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.model.Transactions
import com.dolu.mobibus.core.domain.model.User

@Entity
data class UserEntity(
    @PrimaryKey val id: String,
    val cart: List<BusTicket>,
    val purchasedTicket: List<BusTicket>,
    val transactions: List<Transactions>

) {
    fun toUser(): User {
        return User(
            id= id,
            cart = cart,
            purchasedTicket = purchasedTicket,
            transactions = transactions
        )
    }
}