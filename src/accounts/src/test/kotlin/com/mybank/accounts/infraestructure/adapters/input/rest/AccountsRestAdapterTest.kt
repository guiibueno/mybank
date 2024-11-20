package com.mybank.accounts.infraestructure.adapters.input.rest

import com.mybank.accounts.application.dto.*
import com.mybank.accounts.application.port.input.AccountRegisterPort
import com.mybank.accounts.application.port.input.GetAccountPort
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.AccountCreateAddressDto
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.AccountCreateRequest
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.TypeNumberDto
import com.mybank.accounts.utils.AccountRequestMock
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.Test

class AccountsRestAdapterTest {
    val accountRequestMock = AccountRequestMock()

    private var accountsRestAdapter: AccountsRestAdapter

    @MockK
    private lateinit var accountRegisterPort: AccountRegisterPort
    @MockK
    private lateinit var getAccountPort: GetAccountPort

    init {
        MockKAnnotations.init(this)
        accountsRestAdapter = spyk(
            AccountsRestAdapter(accountRegisterPort, getAccountPort)
        )
    }

    @Test
    fun `should returns 200 when account exists`() {
        val accountId = UUID.randomUUID()

        val accountMock = AccountDTO(accountId, BigDecimal.ZERO)

        every { getAccountPort.invoke(any<UUID>()) } returns accountMock

        val response = accountsRestAdapter.getById(accountId)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK,response.statusCode)
        Assertions.assertEquals(accountId, response.body?.id)
        Assertions.assertEquals(accountMock.balance, response.body?.balance)

        coVerify (exactly = 1) {
            getAccountPort.invoke(any<UUID>())
        }
    }

    @Test
    fun `should returns 404 when account not exists`() {
        val accountId = UUID.randomUUID()

        every { getAccountPort.invoke(any<UUID>()) } returns null

        val response = accountsRestAdapter.getById(accountId)

        Assertions.assertNotNull(response)
        Assertions.assertNull(response.body)
        Assertions.assertEquals(HttpStatus.NOT_FOUND,response.statusCode)

        coVerify (exactly = 1) {
            getAccountPort.invoke(any<UUID>())
        }
    }

    @Test
    fun `should returns 202 when account register is requested`() {
        val name = "João da Silva"
        val birthDate = LocalDate.of(LocalDate.now().year - 18, LocalDate.now().month, LocalDate.now().dayOfMonth)

        val documents = ArrayList<TypeNumberDto>()
        documents.add(TypeNumberDto("CPF", "123.123.132-11"))

        val address = AccountCreateAddressDto("Rua dos Bobos", "0", "São Paulo", "SP")

        val contactInfos = ArrayList<TypeNumberDto>()
        contactInfos.add(TypeNumberDto("EMAIL", "teste@teste.com"))

        val request = AccountCreateRequest(name, birthDate, documents, address, contactInfos)

        every { accountRegisterPort.invokeAsync(any()) } returns Unit

        val response = accountsRestAdapter.createAsync(request)

        Assertions.assertNotNull(response)
        Assertions.assertNull(response.body)
        Assertions.assertEquals(HttpStatus.ACCEPTED,response.statusCode)

        coVerify (exactly = 1) {
            accountRegisterPort.invokeAsync(any())
        }
    }

    @Test
    fun `should returns 200 when are accounts for the document`() {
        val accounts = ArrayList<AccountDTO>()
        accounts.add(AccountDTO(UUID.randomUUID(), BigDecimal.ZERO))

        every { getAccountPort.invoke(any(), any()) } returns accounts

        val response = accountsRestAdapter.findByDocument("CPF", "123.123.123.11")

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK,response.statusCode)
        Assertions.assertEquals(accounts.count(), response.body?.count())

        coVerify (exactly = 1) {
            getAccountPort.invoke(any(), any())
        }
    }

    @Test
    fun `should returns 204 when there are no accounts for the document`() {
        val accounts = ArrayList<AccountDTO>()

        every { getAccountPort.invoke(any(), any()) } returns accounts

        val response = accountsRestAdapter.findByDocument("CPF", "123.123.123.11")

        Assertions.assertNotNull(response)
        Assertions.assertNull(response.body)
        Assertions.assertEquals(HttpStatus.NO_CONTENT,response.statusCode)

        coVerify (exactly = 1) {
            getAccountPort.invoke(any(), any())
        }
    }
}