package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.input.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetProposalStatusUseCase() : GetProposalStatusPort {
    override fun invoke(proposalId: UUID): ProposalDTO? {
        TODO("Not yet implemented")
    }
}