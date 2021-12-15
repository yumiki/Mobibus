package com.dolu.mobibus.core.ui.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dolu.mobibus.core.ui.components.BusTicketCard
import com.dolu.mobibus.core.ui.components.MobibusBottomBar
import com.dolu.mobibus.core.ui.components.MobibusTopAppBar
import android.content.ComponentName
import kotlin.math.roundToInt


@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.ticketInfoList.value
    val scaffoldState = rememberScaffoldState()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        Log.e("HomeScreen", "Result: $it")

    }

    Box {
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