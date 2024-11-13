package com.mybank.accounts.application.port.input
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.dto.SendProposalRequest

interface SendProposalPort {
    operator fun invoke(request: SendProposalRequest): ProposalDTO?
}