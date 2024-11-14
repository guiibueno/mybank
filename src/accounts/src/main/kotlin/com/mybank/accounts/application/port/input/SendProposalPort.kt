package com.mybank.accounts.application.port.input
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.dto.AccountRequest

interface SendProposalPort {
    operator fun invoke(request: AccountRequest): ProposalDTO?
}