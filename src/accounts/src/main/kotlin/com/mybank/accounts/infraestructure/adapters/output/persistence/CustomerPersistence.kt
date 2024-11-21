package com.mybank.accounts.infraestructure.adapters.output.persistence

import com.mybank.accounts.domain.entity.CustomerEntity
import com.mybank.accounts.infraestructure.adapters.output.persistence.repository.CustomerRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class CustomerPersistence(private val customerRepository: CustomerRepository) {
    fun save(name: String, birthDate: LocalDate) : CustomerEntity {
        val customerEntity = CustomerEntity(null, LocalDateTime.now(), name, birthDate)
        return customerRepository.save(customerEntity);
    }
}