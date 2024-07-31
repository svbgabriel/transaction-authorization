package io.github.svbgabriel.transaction.controller.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ValidateTransactionResponse(
    @JsonProperty("code")
    val code: String,
)
