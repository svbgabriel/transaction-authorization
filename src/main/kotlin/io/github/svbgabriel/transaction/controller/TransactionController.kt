package io.github.svbgabriel.transaction.controller

import io.github.svbgabriel.transaction.controller.request.ValidateTransactionRequest
import io.github.svbgabriel.transaction.controller.response.ValidateTransactionResponse
import io.github.svbgabriel.transaction.service.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/api/transaction")
class TransactionController(
    private val transactionService: TransactionService,
) {
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    fun validateTransaction(
        @RequestBody request: ValidateTransactionRequest,
    ): ValidateTransactionResponse = transactionService.validateTransaction(request)
}
