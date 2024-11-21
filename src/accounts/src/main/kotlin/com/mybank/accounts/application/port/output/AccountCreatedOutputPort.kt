package com.mybank.accounts.application.port.output

import com.mybank.accounts.domain.event.AccountCreatedEvent

interface AccountCreatedOutputPort {
    fun emitEvent(event: AccountCreatedEvent)
}