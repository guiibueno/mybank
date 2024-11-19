package com.mybank.accounts.infraestructure.adapters.output.persistence;

import com.mybank.accounts.domain.entity.CustomerEntity
import com.mybank.accounts.infraestructure.adapters.output.persistence.repository.CustomerRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class CustomerPersistenceTest {
    private var customerPersistence: CustomerPersistence

    @MockK
    private lateinit var customerRepository: CustomerRepository

    init {
        MockKAnnotations.init(this)
        customerPersistence = spyk(
            CustomerPersistence(customerRepository)
        )
    }

    @Test
    fun `should return data when customer is created`() {
        val name = "Teste"
        val birthDate  = LocalDate.of(1999, 11, 1)

        every { customerRepository.save(any()) } returns CustomerEntity(UUID.randomUUID().toString(), LocalDateTime.now(), name, birthDate)

        val customer = customerPersistence.save(name, birthDate)

        Assertions.assertNotNull(customer)
        Assertions.assertNotNull(customer.id)
        Assertions.assertEquals(name, customer.name)
        Assertions.assertEquals(birthDate, customer.birthdate)

        coVerify (exactly = 1) {
            customerRepository.save(any())
        }
    }
}
