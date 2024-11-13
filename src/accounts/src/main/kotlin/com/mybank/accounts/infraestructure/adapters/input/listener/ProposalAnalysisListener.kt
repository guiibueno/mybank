package com.mybank.accounts.infraestructure.adapters.input.listener

import com.mybank.accounts.application.port.input.AccountRegisterPort
import org.springframework.stereotype.Service

@Service
class ProposalAnalysisListener(
    private val accountRegisterPort: AccountRegisterPort) {

}