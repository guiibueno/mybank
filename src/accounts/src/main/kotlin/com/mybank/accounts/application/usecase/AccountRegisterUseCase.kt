package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.input.AccountRegisterPort
import com.mybank.accounts.application.port.output.AccountOutputPort
import org.springframework.stereotype.Service

@Service
class AccountRegisterUseCase(val accountOutputPort: AccountOutputPort) : AccountRegisterPort {
    override fun invoke(accountRequest: AccountRequest): AccountDTO? {
        return accountOutputPort.save(accountRequest);
    }
}