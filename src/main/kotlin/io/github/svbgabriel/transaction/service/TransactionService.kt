package io.github.svbgabriel.transaction.service

import io.github.svbgabriel.transaction.controller.request.ValidateTransactionRequest
import io.github.svbgabriel.transaction.controller.response.ValidateTransactionResponse
import io.github.svbgabriel.transaction.model.MCC
import io.github.svbgabriel.transaction.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val userRepository: UserRepository,
) {
    fun validateTransaction(request: ValidateTransactionRequest): ValidateTransactionResponse {
        val user =
            userRepository.findByIdOrNull(request.accountId)
                ?: return ValidateTransactionResponse(code = "07")
        val mcc = MCC.getMCCValue(request.mcc)
        return when (mcc) {
            MCC.FOOD -> {
                if (user.foodBalance >= request.amount) {
                    user.foodBalance -= request.amount
                    userRepository.save(user)
                    ValidateTransactionResponse(code = "00")
                } else {
                    ValidateTransactionResponse(code = "51")
                }
            }
            MCC.MEAL -> {
                if (user.mealBalance >= request.amount) {
                    user.mealBalance -= request.amount
                    userRepository.save(user)
                    ValidateTransactionResponse(code = "00")
                } else {
                    ValidateTransactionResponse(code = "51")
                }
            }
            MCC.CASH -> {
                if (user.cashBalance >= request.amount) {
                    user.cashBalance -= request.amount
                    userRepository.save(user)
                    ValidateTransactionResponse(code = "00")
                } else {
                    ValidateTransactionResponse(code = "51")
                }
            }
        }
    }
}
