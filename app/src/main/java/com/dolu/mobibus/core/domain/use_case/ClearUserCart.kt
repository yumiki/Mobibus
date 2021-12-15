package com.dolu.mobibus.core.domain.use_case

import com.dolu.mobibus.core.domain.model.User
import com.dolu.mobibus.core.domain.repository.CoreRepository

class ClearUserCart(private val repository: CoreRepository) {
    suspend operator fun invoke() : User {
        return repository.clearUserCart()
    }
}