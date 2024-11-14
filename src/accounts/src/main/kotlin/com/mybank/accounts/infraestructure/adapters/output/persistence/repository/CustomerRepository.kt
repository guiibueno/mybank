package com.mybank.accounts.infraestructure.adapters.output.persistence.repository

import com.mybank.accounts.domain.entity.CustomerEntity
import org.springframework.data.repository.CrudRepository

interface CustomerRepository : CrudRepository<CustomerEntity, String> {
}