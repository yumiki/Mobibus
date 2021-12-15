package com.dolu.mobibus.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dolu.mobibus.R
import com.dolu.mobibus.core.domain.model.BusTicket
import com.dolu.mobibus.core.domain.model.Price
import com.dolu.mobibus.core.domain.model.defaultTicketList
import com.dolu.mobibus.core.ui.theme.MobibusTheme

@Preview(
    device = Devices.PIXEL_4,
    showBackground = false
)
@Composable
fun MobibusTopAppBar() {
    TopAppBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Buy your bus ticket",
            style = MaterialTheme.typography.h1,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )
        /*Icon(
            painter = painterResource(id = R.drawable.ic_shopping_cart_24),
            contentDescription = "Go to shopping cart"
        )*/
    }
}

@Preview(
    device = Devices.PIXEL_4
)
@Composable
fun MobibusBottomBar(
    fabShape : RoundedCornerShape = RoundedCornerShape(50),
    cartTotal: Price = Price(0.0, "EUR")
) {
    BottomAppBar(
        elevation = 10.dp,
        cutoutShape = fabShape
    ) {
        Spacer(Modifier.weight(1f, true))
        Text(text = "Total: $cartTotal")
    }
}

@Composable
fun BusTicketCard(
    count: Int = 0,
    busTicket: BusTicket,
    addAction: () -> Unit,
    removeAction: () -> Unit,
) {
    Box {
        Card(
            modifier = Modifier
                .height(200.dp),
            elevation = 10.dp
        ) {
            Box(
                Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .padding(vertical = 50.dp)
                            .fillMaxHeight()
                            .defaultMinSize(minWidth = 0.dp),
                        painter = painterResource(id = R.drawable.ic_bus_24),
                        contentDescription = "Bus icon",
                        contentScale = ContentScale.FillHeight
                    )
                    Column {
                        Text(
                            text = stringResource(id = busTicket.getRessourceNameId()),
                            style = MaterialTheme.typography.h1,
                            fontSize = 30.sp
                        )
                        Text(
                            text = "${busTicket.price}",
                            style = MaterialTheme.typography.h2,
                            fontSize = 20.sp
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    if(count > 0) {
                        Button(onClick = removeAction) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_remove_24),
                                contentDescription = "Remove ${busTicket.validityPeriod.name} to cart"
                            )
                        }
                    }

                    Text(text = "$count")

                    Button(onClick = addAction) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add ${busTicket.validityPeriod.name} to cart"
                        )
                    }
                }
            }

        }
    }

}


@Preview(
    device = Devices.PIXEL_4,
    showBackground = false
)
@Composable
fun BusTicketPreview() {
    MobibusTheme(darkTheme = false) {
        Surface(
            color = MaterialTheme.colors.surface
        ) {
            BusTicketCard(
                busTicket = defaultTicketList.random(),
                addAction = {},
                removeAction = {}
            )
        }
    }
}