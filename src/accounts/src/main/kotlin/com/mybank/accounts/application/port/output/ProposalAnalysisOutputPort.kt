package com.mybank.accounts.application.port.output

import com.mybank.accounts.application.dto.ProposalDTO

interface ProposalAnalysisOutputPort {
    fun sendToAnalysis(proposal: ProposalDTO)
}