package com.mybank.accounts.application.dto

import java.time.LocalDateTime
import java.util.*

data class ProposalDTO(val id: UUID,
                       val status: String,
                       val createdAt: LocalDateTime,
                       val updatedAt: LocalDateTime,
                       val accountId: UUID?,
                       val additionalinfos:AccountRequest) {
}