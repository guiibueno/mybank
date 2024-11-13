package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.input.*
import com.mybank.accounts.application.port.output.ProposalOutputPort
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetProposalStatusUseCase(
    private val proposalOutputPort: ProposalOutputPort
) : GetProposalStatusPort {
    override fun invoke(proposalId: UUID): ProposalDTO? {
        return proposalOutputPort.findById(proposalId)
    }
}