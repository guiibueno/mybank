package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.input.AccountRegisterPort
import com.mybank.accounts.application.port.input.ProposalAnalysisPort
import com.mybank.accounts.application.port.output.ProposalOutputPort
import com.mybank.accounts.domain.AnalysisStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ProposalAnalysisUseCase (
    val proposalOutputPort: ProposalOutputPort,
    val accountRegisterPort: AccountRegisterPort
) : ProposalAnalysisPort {
    override fun invoke(proposalId: String): ProposalDTO? {
        var proposal = proposalOutputPort.findById(UUID.fromString(proposalId))

        if(proposal != null){
            val account = accountRegisterPort.invoke(proposal.additionalinfos)

            if(account != null){
                proposal = proposalOutputPort.update(proposal.copy(status = AnalysisStatus.APPROVED, accountId = account.id))

                return ProposalDTO(proposal.id, proposal.status, proposal.createdAt, proposal.updatedAt, proposal.accountId, proposal.additionalinfos)
            }
        }
        return null
    }
}