package io.github.svbgabriel.transaction.service

import io.github.svbgabriel.transaction.controller.request.ValidateTransactionRequest
import io.github.svbgabriel.transaction.repository.UserRepository
import io.github.svbgabriel.transaction.repository.entity.UserEntity
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TransactionServiceTest {
    private lateinit var userRepository: UserRepository
    private lateinit var transactionService: TransactionService

    @BeforeTest
    fun setUp() {
        userRepository = mockk(relaxed = true)
        transactionService = TransactionService(userRepository)
    }

    @Test
    fun `should return code 07 when the user is not found`() {
        val id = 1L
        every { userRepository.findByIdOrNull(id) } returns null

        val result =
            transactionService.validateTransaction(
                ValidateTransactionRequest(
                    accountId = id,
                    mcc = "",
                    amount = 0.0,
                    merchant = "",
                ),
            )

        assertEquals("07", result.code)
    }

    @Test
    fun `should return code 51 when the user food balance is not enough`() {
        val id = 1L
        every { userRepository.findByIdOrNull(id) } returns
            UserEntity(
                id = id,
                foodBalance = 15.0,
                mealBalance = 15.0,
                cashBalance = 20.0,
            )

        val request =
            ValidateTransactionRequest(
                accountId = id,
                mcc = "5411",
                amount = 20.0,
                merchant = "MARKET  SAO PAULO BR",
            )
        val result = transactionService.validateTransaction(request)

        assertEquals("51", result.code)
    }

    @Test
    fun `should return code 51 when the user food balance is enough`() {
        val id = 1L
        val user =
            UserEntity(
                id = id,
                foodBalance = 15.0,
                mealBalance = 15.0,
                cashBalance = 20.0,
            )

        every { userRepository.findByIdOrNull(id) } returns user

        val request =
            ValidateTransactionRequest(
                accountId = id,
                mcc = "5411",
                amount = 10.0,
                merchant = "MARKET  SAO PAULO BR",
            )

        val updatedUser =
            UserEntity(
                id = id,
                foodBalance = 5.0,
                mealBalance = 15.0,
                cashBalance = 20.0,
            )
        every { userRepository.save(any()) } returns updatedUser

        val result = transactionService.validateTransaction(request)

        assertEquals("00", result.code)
    }

    @Test
    fun `should return code 51 when the user meal balance is not enough`() {
        val id = 1L
        every { userRepository.findByIdOrNull(id) } returns
            UserEntity(
                id = id,
                foodBalance = 15.0,
                mealBalance = 15.0,
                cashBalance = 20.0,
            )

        val request =
            ValidateTransactionRequest(
                accountId = id,
                mcc = "5811",
                amount = 20.0,
                merchant = "RESTAURANT  SAO PAULO BR",
            )
        val result = transactionService.validateTransaction(request)

        assertEquals("51", result.code)
    }

    @Test
    fun `should return code 51 when the user meal balance is enough`() {
        val id = 1L
        val user =
            UserEntity(
                id = id,
                foodBalance = 15.0,
                mealBalance = 15.0,
                cashBalance = 20.0,
            )

        every { userRepository.findByIdOrNull(id) } returns user

        val request =
            ValidateTransactionRequest(
                accountId = id,
                mcc = "5811",
                amount = 10.0,
                merchant = "RESTAURANT  SAO PAULO BR",
            )

        val updatedUser =
            UserEntity(
                id = id,
                foodBalance = 15.0,
                mealBalance = 5.0,
                cashBalance = 20.0,
            )
        every { userRepository.save(any()) } returns updatedUser

        val result = transactionService.validateTransaction(request)

        assertEquals("00", result.code)
    }

    @Test
    fun `should return code 51 when the user cash balance is not enough`() {
        val id = 1L
        every { userRepository.findByIdOrNull(id) } returns
            UserEntity(
                id = id,
                foodBalance = 15.0,
                mealBalance = 15.0,
                cashBalance = 20.0,
            )

        val request =
            ValidateTransactionRequest(
                accountId = id,
                mcc = "9999",
                amount = 30.0,
                merchant = "STORE  SAO PAULO BR",
            )
        val result = transactionService.validateTransaction(request)

        assertEquals("51", result.code)
    }

    @Test
    fun `should return code 00 when the user cash balance is enough`() {
        val id = 1L
        val user =
            UserEntity(
                id = id,
                foodBalance = 15.0,
                mealBalance = 15.0,
                cashBalance = 30.0,
            )

        every { userRepository.findByIdOrNull(id) } returns user

        val request =
            ValidateTransactionRequest(
                accountId = id,
                mcc = "9999",
                amount = 10.0,
                merchant = "STORE  SAO PAULO BR",
            )

        val updatedUser =
            UserEntity(
                id = id,
                foodBalance = 15.0,
                mealBalance = 15.0,
                cashBalance = 0.0,
            )
        every { userRepository.save(any()) } returns updatedUser

        val result = transactionService.validateTransaction(request)

        assertEquals("00", result.code)
    }
}
