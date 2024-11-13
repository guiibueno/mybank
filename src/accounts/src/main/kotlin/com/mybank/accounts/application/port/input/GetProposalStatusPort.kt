package com.mybank.accounts.application.port.input

import com.mybank.accounts.application.dto.ProposalDTO
import java.util.UUID

interface GetProposalStatusPort {
    operator fun invoke(proposalId: UUID): ProposalDTO?
}