package com.mybank.accounts.application.port.input

import com.mybank.accounts.application.dto.ProposalDTO

interface ProposalAnalysisPort {
    operator fun invoke(proposalId: String): ProposalDTO?
}