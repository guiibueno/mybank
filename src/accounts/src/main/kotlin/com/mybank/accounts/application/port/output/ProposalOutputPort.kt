package com.mybank.accounts.application.port.output

import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.dto.AccountRequest
import java.util.UUID

interface ProposalOutputPort {
    fun save(proposal: AccountRequest): ProposalDTO?
    fun findById(id: UUID): ProposalDTO?
    fun update(proposal: ProposalDTO): ProposalDTO
}