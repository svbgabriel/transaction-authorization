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
        val mcc = getMCCByMerchantName(request.merchant) ?: MCC.getMCCValue(request.mcc)
        return if (mcc == MCC.FOOD && user.foodBalance >= request.amount) {
            user.foodBalance -= request.amount
            userRepository.save(user)
            ValidateTransactionResponse(code = "00")
        } else if (mcc == MCC.MEAL && user.mealBalance >= request.amount) {
            user.mealBalance -= request.amount
            userRepository.save(user)
            ValidateTransactionResponse(code = "00")
        } else if (user.cashBalance >= request.amount) {
            user.cashBalance -= request.amount
            userRepository.save(user)
            ValidateTransactionResponse(code = "00")
        } else {
            ValidateTransactionResponse(code = "51")
        }
    }

    private fun getMCCByMerchantName(merchant: String): MCC? {
        val namePart =
            merchant
                .replace("\\s+".toRegex(), " ")
                .split(" ")
                .dropLast(2)
                .joinToString(" ")

        return if (namePart.contains("food", ignoreCase = true) || namePart.contains("market", ignoreCase = true)) {
            MCC.FOOD
        } else if (namePart.contains("meal", ignoreCase = true) || namePart.contains("eat", ignoreCase = true)) {
            MCC.MEAL
        } else {
            null
        }
    }
}
