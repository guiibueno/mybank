package com.mybank.accounts.infraestructure.adapters.output.persistence.repository

import com.mybank.accounts.domain.entity.CustomerDocumentsEntity
import org.springframework.data.repository.CrudRepository

interface CustomerDocumentsRepository : CrudRepository<CustomerDocumentsEntity, String> {
}