package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.dto.SendProposalRequest
import com.mybank.accounts.application.port.input.SendProposalPort
import org.springframework.stereotype.Service

@Service
class SendProposalUseCase : SendProposalPort {
    override fun invoke(request: SendProposalRequest): ProposalDTO? {
        TODO("Not yet implemented")
    }
}