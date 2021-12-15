package com.dolu.mobibus.core.ui.home;

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.model.Period
import com.dolu.mobibus.core.domain.model.Price
import com.dolu.mobibus.core.domain.model.User
import com.dolu.mobibus.core.domain.use_case.GetBusTicketInfos
import com.dolu.mobibus.core.domain.use_case.GetUserUseCase
import com.dolu.mobibus.core.domain.use_case.UpdateUserCartContent
import com.dolu.mobibus.core.util.NetworkResource
import com.dolu.mobibus.feature_order.util.PaymentStatus
import com.dolu.mobibus.feature_order.util.parseBundleToPaymentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBusTicketInfos: GetBusTicketInfos,
    private val updateUserCartContent: UpdateUserCartContent,
    private val getActiveUser: GetUserUseCase,
): ViewModel() {

    private val _ticketInfoList = mutableStateOf(BusTicketInfoState())
    val ticketInfoList: State<BusTicketInfoState> = _ticketInfoList

    private val _currentUser = mutableStateOf(User())
    val currentUser: State<User> = _currentUser

    private val _paymentTicket = mutableStateOf(PaymentTicketState())
    val paymentTicket: State<PaymentTicketState> = _paymentTicket

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getBusTicketInfos().collectLatest { response ->
                viewModelScope.launch(Dispatchers.Main) {
                    when(response) {
                        is NetworkResource.Error -> {
                            _ticketInfoList.value = ticketInfoList.value.copy(
                                busTicketInfoItems = response.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.emit(UIEvent.ShowSnackbar(
                                response.message ?: "Unknown error"
                            ))
                        }
                        is NetworkResource.Loading -> {
                            _ticketInfoList.value = ticketInfoList.value.copy(
                                busTicketInfoItems = response.data ?: emptyList(),
                                isLoading = true
                            )
                        }
                        is NetworkResource.Success -> {
                            _ticketInfoList.value = ticketInfoList.value.copy(
                                busTicketInfoItems = response.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
            getActiveUser().onEach {
                _currentUser.value = currentUser.value.copy(
                    purchasedTicket = it.purchasedTicket,
                    cart = it.cart,
                    transactions = it.transactions
                )
            }.collectLatest {

            }
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        viewModelScope.launch {
            when(event) {
                is HomeScreenEvent.AddTicketToCart -> {
                    launch(Dispatchers.IO) {
                        updateUserCartContent
                            .addTicket(event.ticket)
                            .let {
                                _currentUser.value = currentUser.value.copy(
                                    purchasedTicket = it.purchasedTicket,
                                    cart = it.cart,
                                    transactions = it.transactions
                                )
                            }
                    }
                }
                is HomeScreenEvent.RemoveTicketToCart -> {
                    launch(Dispatchers.IO) {
                        updateUserCartContent
                            .removeTicket(event.ticket)
                            .let {
                                _currentUser.value = currentUser.value.copy(
                                    purchasedTicket = it.purchasedTicket,
                                    cart = it.cart,
                                    transactions = it.transactions
                                )
                            }
                    }
                }
                HomeScreenEvent.ValidateOrder -> {
                    _eventFlow.emit(UIEvent.ValidateOrder)
                }
                is HomeScreenEvent.OnPaymentResponse -> {
                    val parsedResponse = parseBundleToPaymentResponse(event.paymentResponseBundle)
                    when (parsedResponse.status) {
                        PaymentStatus.OK -> {
                            _eventFlow.emit(UIEvent.ShowPaymentTicket(parsedResponse.clientTicket))
                            _paymentTicket.value = PaymentTicketState(parsedResponse.transactionId, parsedResponse.clientTicket)
                            launch(Dispatchers.IO) {
                                Log.v("HomeScreenVM", "will clear cart")
                                updateUserCartContent.clearUserCart()
                                getActiveUser()
                            }
                        }
                        PaymentStatus.Unknown -> {
                            _eventFlow.emit(UIEvent.ShowSnackbar("The payment failed, please retry later"))
                        }
                    }

                }
                is HomeScreenEvent.NewPaymentTicketAvailable -> {

                }
            }
        }
    }

    fun getTicketCountInCartByValidity(validity: Period): List<BusTicket> {
        return _currentUser.value.cart.filter {
            it.validityPeriod == validity
        }
    }

    fun getCartTotal(): Price {
        val total: Double = _currentUser.value.cart.fold(0.0) {
            acc, busTicket -> acc+busTicket.price.value
        }
        return Price(total, "EUR")
    }

    sealed class UIEvent {
        object ValidateOrder : UIEvent()
        data class ShowSnackbar(val message: String): UIEvent()
        data class ShowPaymentTicket(val message: String): UIEvent()
    }

}
