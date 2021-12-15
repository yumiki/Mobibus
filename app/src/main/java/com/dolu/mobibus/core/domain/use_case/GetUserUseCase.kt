package com.dolu.mobibus.core.domain.use_case

import com.dolu.mobibus.core.domain.model.User
import com.dolu.mobibus.core.domain.repository.CoreRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val repository: CoreRepository
) {
    suspend operator fun invoke(): Flow<User> {
       return repository.getActiveUser()
    }
}