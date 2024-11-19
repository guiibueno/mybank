package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.input.AccountRegisterPort
import com.mybank.accounts.application.port.output.AccountCreatedOutputPort
import com.mybank.accounts.application.port.output.AccountOutputPort
import com.mybank.accounts.application.port.output.AccountRegisterOutputPort
import com.mybank.accounts.application.port.output.MetricsOutputPort
import com.mybank.accounts.domain.event.AccountCreatedEvent
import org.springframework.stereotype.Service

@Service
class AccountRegisterUseCase(val accountOutputPort: AccountOutputPort,
                             val accountRegisterOutputPort: AccountRegisterOutputPort,
                             val metricsOutputPort: MetricsOutputPort
) : AccountRegisterPort {
    override fun invoke(accountRequest: AccountRequest): AccountDTO? {
        val account = accountOutputPort.save(accountRequest);

        if(account != null){
            metricsOutputPort.accountCreated(account)
        }

        return account;
    }
    override fun invokeAsync(accountRequest: AccountRequest) {
        accountRegisterOutputPort.sendToAsyncProcess(accountRequest)
    }
}