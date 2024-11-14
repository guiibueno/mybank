package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.input.SendProposalPort
import com.mybank.accounts.application.port.output.ProposalAnalysisOutputPort
import com.mybank.accounts.application.port.output.ProposalOutputPort
import org.springframework.stereotype.Service

@Service
class SendProposalUseCase(
    private val proposalOutputPort: ProposalOutputPort,
    private val proposalAnalysisOutputPort: ProposalAnalysisOutputPort
) : SendProposalPort {

        override fun invoke(request: AccountRequest): ProposalDTO? {
            val proposal = proposalOutputPort.save(request)
            
            if(proposal != null){
                proposalAnalysisOutputPort.sendToAnalysis(proposal)
                return proposal
            }

            return null;
    }
}