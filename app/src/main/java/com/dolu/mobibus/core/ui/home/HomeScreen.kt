package com.dolu.mobibus.core.ui.home

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dolu.mobibus.core.ui.components.BusTicketCard
import com.dolu.mobibus.core.ui.components.MobibusBottomBar
import com.dolu.mobibus.core.ui.components.MobibusTopAppBar
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

const val PAYMENT_OK = 1
const val TAG = "HomeScreen"

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.ticketInfoList.value
    val scaffoldState = rememberScaffoldState()
    
    val showDialog = remember {
        mutableStateOf(false)
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        Log.v(TAG, "Result: $it")
        when (it.resultCode) {
            PAYMENT_OK,
            Activity.RESULT_OK -> {
                it.data?.extras?.let { extras ->
                    viewModel.onEvent(HomeScreenEvent.OnPaymentResponse(extras))
                    Log.e(TAG, "Success: $extras")
                }
            }
            Activity.RESULT_CANCELED -> {

            }
        }

    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is HomeViewModel.UIEvent.ShowPaymentTicket -> {
                    viewModel.onEvent(HomeScreenEvent.NewPaymentTicketAvailable(event.message))
                    showDialog.value = true
                }
                is HomeViewModel.UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is HomeViewModel.UIEvent.ValidateOrder -> {

                }
            }
        }
    }

    Box {

        if (showDialog.value) {
            val ticket = viewModel.paymentTicket.value
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = {
                    Text(text = "Payment Ticket ${ticket.transactionId}")
                },
                text = {
                    Text(text = ticket.content)
                },
                confirmButton = {
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog.value = false
                        }) {
                        Text("Close")
                    }
                }
            )
        }
        
        Scaffold(
            topBar = { MobibusTopAppBar() },
            scaffoldState = scaffoldState,
            bottomBar = { MobibusBottomBar(cartTotal = viewModel.getCartTotal()) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val cn = ComponentName.createRelative("com.yavin.macewindu", ".PaymentActivity")

                        val paymentIntent = Intent()
                            .apply {
                                component = cn
                                putExtra("amount", (viewModel.getCartTotal().value*100).roundToInt().toString())
                            }
                        launcher.launch(paymentIntent)
                    },
                    shape = RoundedCornerShape(50),
                ) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        "Go to cart screen to validate order"
                    )
                }
            },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
        ) {
            LazyColumn(
                modifier= Modifier.padding(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.busTicketInfoItems.size) { index ->
                    val ticket = state.busTicketInfoItems[index]
                    BusTicketCard(
                        count = viewModel.getTicketCountInCartByValidity(ticket.validityPeriod).size,
                        busTicket = state.busTicketInfoItems[index],
                        addAction = {
                            viewModel.onEvent(HomeScreenEvent.AddTicketToCart(ticket))
                        },
                        removeAction = {
                            viewModel.onEvent(HomeScreenEvent.RemoveTicketToCart(ticket))
                        }
                    )
                }
            }
            if(state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}