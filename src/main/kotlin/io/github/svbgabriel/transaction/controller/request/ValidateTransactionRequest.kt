package io.github.svbgabriel.transaction.controller.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class ValidateTransactionRequest(
    @JsonProperty("id")
    val id: String = UUID.randomUUID().toString(),
    @JsonProperty("account")
    val accountId: Long,
    @JsonProperty("totalAmount")
    val amount: Double,
    @JsonProperty("merchant")
    val merchant: String,
    @JsonProperty("mcc")
    val mcc: String,
)
