package com.mybank.accounts.infraestructure.adapters.output.persistence.repository

import com.mybank.accounts.domain.entity.AccountEntity
import com.mybank.accounts.domain.entity.CustomerEntity
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class AccountRepositoryTest {
    companion object {
        @Container
        @ServiceConnection
        val db = PostgreSQLContainer("postgres:latest")
            .withDatabaseName("accounts-tests")
            .withInitScript("ddl.sql")

        @JvmStatic
        @DynamicPropertySource
        fun registerDBContainer(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", db::getJdbcUrl)
            registry.add("spring.datasource.username", db::getUsername)
            registry.add("spring.datasource.password", db::getPassword)
        }
    }
    @Autowired
    private lateinit var accountRepository: AccountRepository
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    private lateinit var customer: CustomerEntity
    private lateinit var account: AccountEntity

    @BeforeEach
    internal fun setUp() {
        customer = customerRepository.save(CustomerEntity( null, LocalDateTime.now(), "Teste", LocalDate.now()))
        account = accountRepository.save(AccountEntity(null, true, LocalDateTime.now(), customer.id!!, BigDecimal.ZERO))
    }

    @Test
    fun `should generate id when a account is persisted`() {
        val accountEntity = AccountEntity(null, true, LocalDateTime.now(), customer.id!!, BigDecimal.ZERO)

        val createdAccount = accountRepository.save(accountEntity)
        Assertions.assertNotNull(createdAccount.id)
    }

    @Test
    fun `should return entity when id found`() {
        val searchedAccount = accountRepository.findById(account.id!!)

        Assertions.assertFalse(searchedAccount.isEmpty)
        Assertions.assertEquals(account.id!!, searchedAccount.get().id)
    }

    @Test
    fun `should return empty when id not found`() {
        val searchedAccount = accountRepository.findById(UUID.randomUUID().toString())

        Assertions.assertTrue(searchedAccount.isEmpty)
    }

    @Test
    fun `should returns true and update balance when credit transaction`() {
        val accountId = account.id!!
        val transactionType = 'C'
        val amount = BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        val updateBalanceResult = accountRepository.updateBalance(accountId, transactionType, amount)

        Assertions.assertNotNull(updateBalanceResult)
        Assertions.assertTrue(updateBalanceResult!!.success)
        Assertions.assertEquals(amount, updateBalanceResult.balance)
    }

    @Test
    fun `should returns true and update balance when debit transaction and account have funds`() {
        val accountWithBalanceForDebit = accountRepository.save(AccountEntity(null, true, LocalDateTime.now(), customer.id!!, BigDecimal.TEN))

        val accountId = accountWithBalanceForDebit.id!!
        val transactionType = 'D'
        val amount = BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        val expectedBalance = (accountWithBalanceForDebit.balance - amount).setScale(2, BigDecimal.ROUND_HALF_EVEN);

        val updateBalanceResult = accountRepository.updateBalance(accountId, transactionType, amount)

        Assertions.assertNotNull(updateBalanceResult)
        Assertions.assertTrue(updateBalanceResult!!.success)
        Assertions.assertEquals(expectedBalance, updateBalanceResult.balance)
    }

    @Test
    fun `should returns false and not update balance when debit transaction and account don't have funds`() {
        val accountWithBalanceForDebit = accountRepository.save(AccountEntity(null, true, LocalDateTime.now(), customer.id!!, BigDecimal.ONE))

        val accountId = accountWithBalanceForDebit.id!!
        val transactionType = 'D'
        val amount = BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        val updateBalanceResult = accountRepository.updateBalance(accountId, transactionType, amount)

        Assertions.assertNotNull(updateBalanceResult)
        Assertions.assertFalse(updateBalanceResult!!.success)
        Assertions.assertEquals(accountWithBalanceForDebit.balance.setScale(2, BigDecimal.ROUND_HALF_EVEN), updateBalanceResult.balance)
    }

    @Test
    fun `should return null when account not exists for update balance`() {
        val accountId = UUID.randomUUID().toString()
        val transactionType = 'D'
        val amount = BigDecimal.TEN.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        val updateBalanceResult = accountRepository.updateBalance(accountId, transactionType, amount)

        Assertions.assertNull(updateBalanceResult)
    }
}