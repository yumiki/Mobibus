package com.dolu.mobibus.core.domain.model

import com.dolu.mobibus.core.data.local.entity.UserEntity

const val ANONYMOUS_USER_ID = "Anonymous"

data class User (
    val id: String = ANONYMOUS_USER_ID,
    val purchasedTicket: List<BusTicket> = emptyList(),
    val cart: List<BusTicket> = emptyList(),
    val transactions: List<Transactions> = emptyList()
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            id = id,
            purchasedTicket = purchasedTicket,
            transactions = transactions,
            cart = cart
        )
    }
}


