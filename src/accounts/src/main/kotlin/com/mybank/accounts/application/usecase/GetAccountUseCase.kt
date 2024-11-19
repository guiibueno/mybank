package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.port.input.GetAccountPort
import com.mybank.accounts.application.port.output.AccountOutputPort
import org.springframework.stereotype.Service
import java.util.*

@Service
class GetAccountUseCase(val accountOutputPort: AccountOutputPort) : GetAccountPort {
    override fun invoke(accountId: UUID): AccountDTO? {
        return accountOutputPort.findById(accountId)
    }

    override fun invoke(documentNumber: String): AccountDTO? {
        TODO("Not yet implemented")
    }
}