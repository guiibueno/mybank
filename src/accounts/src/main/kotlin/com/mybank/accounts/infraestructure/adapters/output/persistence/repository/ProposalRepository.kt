package com.mybank.accounts.infraestructure.adapters.output.persistence.repository

import com.mybank.accounts.domain.entity.ProposalEntity
import org.springframework.data.repository.CrudRepository

interface ProposalRepository : CrudRepository<ProposalEntity, String> {
}