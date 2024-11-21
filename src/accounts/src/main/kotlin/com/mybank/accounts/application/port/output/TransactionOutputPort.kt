package com.mybank.accounts.application.port.output

import com.mybank.accounts.domain.event.TransactionEvent

interface TransactionOutputPort {
    fun emitEvent(event: TransactionEvent)
}