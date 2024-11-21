package com.mybank.accounts.infraestructure.adapters.output.persistence

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.application.port.output.CacheOutputPort
import com.mybank.accounts.domain.entity.AccountEntity
import com.mybank.accounts.domain.entity.CustomerEntity
import com.mybank.accounts.domain.entity.UpdateBalanceEntity
import com.mybank.accounts.infraestructure.adapters.output.persistence.repository.AccountRepository
import com.mybank.accounts.utils.AccountRequestMock
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class AccountPersistenceTest {
    private var mockAccountRequest = AccountRequestMock()

    private var accountPersistence: AccountPersistence
    @MockK
    private lateinit var cache: CacheOutputPort

    @MockK
    private lateinit var customerPersistence: CustomerPersistence

    @MockK
    private lateinit var documentsPersistence: DocumentsPersistence

    @MockK
    private lateinit var accountRepository: AccountRepository

    init {
        MockKAnnotations.init(this)
        accountPersistence = spyk(
            AccountPersistence(cache, customerPersistence, documentsPersistence, accountRepository)
        )
    }

    @Test
    fun `should return data when account is created`() {
        val request = mockAccountRequest.GetAccountRequest()

        every { customerPersistence.save(any(), any()) } returns CustomerEntity(UUID.randomUUID().toString(), LocalDateTime.now(), request.name, request.birthDate)
        every { documentsPersistence.save(any(), any()) } returns Unit
        every { accountRepository.save(any()) } returns AccountEntity(UUID.randomUUID().toString(), true, LocalDateTime.now(), UUID.randomUUID().toString(), BigDecimal.ZERO)
        every { cache.setValue(any(), any(), any()) } returns Unit

        val account = accountPersistence.save(request)

        Assertions.assertNotNull(account)

        coVerify (exactly = 1) {
            customerPersistence.save(any(), any())
            documentsPersistence.save(any(), any())
            accountRepository.save(any())
        }
    }

    @Test
    fun `should return null when account not exists`() {
        val accountId = UUID.randomUUID()

        every { accountRepository.findById(any()) } returns Optional.empty()
        every { cache.getValue(any(), AccountDTO::class.java) } returns null

        val account = accountPersistence.findById(accountId)

        Assertions.assertNull(account)

        coVerify (exactly = 1) {
            accountRepository.findById(any())
            cache.getValue(any(), AccountDTO::class.java)
        }
    }

    @Test
    fun `should return data from repository when account exists but is not cached`() {
        val accountId = UUID.randomUUID()
        val entity = AccountEntity(accountId.toString(), true, LocalDateTime.now(), UUID.randomUUID().toString(), BigDecimal.ZERO)

        every { accountRepository.findById(any()) } returns Optional.of(entity)
        every { cache.getValue(any(), AccountDTO::class.java) } returns null
        every { cache.setValue(any(), any(), any()) } returns Unit

        val account = accountPersistence.findById(accountId)

        Assertions.assertNotNull(account)
        Assertions.assertEquals(accountId, account?.id)

        coVerify (exactly = 1) {
            accountRepository.findById(any())
            cache.getValue(any(), AccountDTO::class.java)
            cache.setValue(any(), any(), any())
        }
    }

    @Test
    fun `should return data from cache when account exists`() {
        val accountId = UUID.randomUUID()
        val entity = AccountEntity(accountId.toString(), true, LocalDateTime.now(), UUID.randomUUID().toString(), BigDecimal.ZERO)

        every { accountRepository.findById(any()) } returns Optional.of(entity)
        every { cache.getValue(any(), AccountDTO::class.java) } returns AccountDTO(UUID.fromString(entity.id), entity.balance)

        val account = accountPersistence.findById(accountId)

        Assertions.assertNotNull(account)
        Assertions.assertEquals(accountId, account?.id)

        coVerify (exactly = 1) {
            cache.getValue(any(), AccountDTO::class.java)
        }

        coVerify (exactly = 0) {
            accountRepository.findById(any())
        }
    }

    @Test
    fun `should return transaction result when balance update is requested`() {
        val request = TransactionRequestDTO(UUID.randomUUID(), 'C', BigDecimal.TEN, "Teste")

        every { accountRepository.updateBalance(any(), any(), any()) } returns UpdateBalanceEntity(true, BigDecimal.TEN)
        every { cache.expire(any()) } returns Unit

        val account = accountPersistence.updateBalance(request)

        Assertions.assertNotNull(account)

        coVerify (exactly = 1) {
            accountRepository.updateBalance(any(), any(), any())
            cache.expire(any())
        }
    }

    @Test
    fun `should return null when balance update is requested but account not exists`() {
        val request = TransactionRequestDTO(UUID.randomUUID(), 'C', BigDecimal.TEN, "Teste")

        every { accountRepository.updateBalance(any(), any(), any()) } returns null
        every { cache.expire(any()) } returns Unit

        val account = accountPersistence.updateBalance(request)

        Assertions.assertNull(account)

        coVerify (exactly = 1) {
            accountRepository.updateBalance(any(), any(), any())
        }

        coVerify (exactly = 0) {
            cache.expire(any())
        }
    }
}