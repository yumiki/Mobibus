package com.dolu.mobibus.core.domain.model

data class Transactions(
    val purchasedItems: List<BusTicket>,
    val timestamp: Long
)
