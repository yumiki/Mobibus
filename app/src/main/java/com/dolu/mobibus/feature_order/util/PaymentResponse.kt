package com.dolu.mobibus.feature_order.util

import android.os.Bundle


const val CLIENT_TICKET_EXTRA_KEY = "clientTicket"
const val STATUS_EXTRA_KEY = "status"
const val AMOUNT_EXTRA_KEY = "amount"
const val TRANSACTION_ID_EXTRA_KEY = "transactionId"

/**
 *
 *  @param amount In centimes
 */
data class PaymentResponse(
    val clientTicket: String,
    val status: PaymentStatus,
    val amount: Int,
    val transactionId: String
)

sealed class PaymentStatus(val name: String) {
    object OK: PaymentStatus("OK")
    object Unknown: PaymentStatus("Unknown")
}

fun parseBundleToPaymentResponse(bundle: Bundle): PaymentResponse {
    val status = when(bundle.getString(STATUS_EXTRA_KEY, "")) {
        "ok" -> PaymentStatus.OK
        else -> PaymentStatus.Unknown
    }

    return PaymentResponse(
        bundle.getString(CLIENT_TICKET_EXTRA_KEY, ""),
        status,
        amount = bundle.getString(AMOUNT_EXTRA_KEY, "-1").toIntOrNull()?: -1,
        transactionId = bundle.getString(TRANSACTION_ID_EXTRA_KEY,"")
    )
}