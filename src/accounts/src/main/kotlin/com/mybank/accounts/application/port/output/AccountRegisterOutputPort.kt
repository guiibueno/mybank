package com.mybank.accounts.application.port.output

import com.mybank.accounts.application.dto.AccountRequest

interface AccountRegisterOutputPort {
    fun sendToAsyncProcess(accountRequest: AccountRequest)
}