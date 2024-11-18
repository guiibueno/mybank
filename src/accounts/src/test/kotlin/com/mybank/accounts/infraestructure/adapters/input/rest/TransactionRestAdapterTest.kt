package com.mybank.accounts.infraestructure.adapters.input.rest

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.application.dto.TransactionResultDTO
import com.mybank.accounts.application.port.input.TransactionAuthorizerPort
import com.mybank.accounts.domain.valueobjects.TransactionStatus
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.TransactionRequest
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class TransactionRestAdapterTest {
    private var transactionRestAdapter: TransactionRestAdapter

    @MockK
    private lateinit var  transactionAuthorizerPort: TransactionAuthorizerPort

    init {
        MockKAnnotations.init(this)
        transactionRestAdapter = spyk(
            TransactionRestAdapter(transactionAuthorizerPort)
        )
    }

    @Test
    fun `should returns 200 when transaction processed`() {
        val accountId = UUID.randomUUID()
        val request = TransactionRequest("C", BigDecimal.TEN, "Teste")
        val transactionMock = TransactionResultDTO(LocalDateTime.now(), TransactionStatus.APPROVED, request.type[0], request.amount, BigDecimal.TEN)


        every { transactionAuthorizerPort.invoke(any<TransactionRequestDTO>()) } returns transactionMock

        val response = transactionRestAdapter.handleTransaction(accountId, request)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK,response.statusCode)
        Assertions.assertEquals(transactionMock.type, request.type[0])
        Assertions.assertEquals(transactionMock.amount, response.body?.amount)
        Assertions.assertEquals(TransactionStatus.APPROVED, response.body?.status)

        coVerify (exactly = 1) {
            transactionAuthorizerPort.invoke(any())
        }
    }

    @Test
    fun `should returns 422 when transaction is not processed`() {
        val accountId = UUID.randomUUID()
        val request = TransactionRequest("C", BigDecimal.TEN, "Teste")

        every { transactionAuthorizerPort.invoke(any<TransactionRequestDTO>()) } returns null

        val response = transactionRestAdapter.handleTransaction(accountId, request)

        Assertions.assertNotNull(response)
        Assertions.assertNull(response.body)
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,response.statusCode)

        coVerify (exactly = 1) {
            transactionAuthorizerPort.invoke(any())
        }
    }
}