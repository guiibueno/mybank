package com.mybank.accounts.infraestructure.adapters.input.rest.data.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.*

data class ProposalGetStatusResponse(val id: UUID,
                                     val status: String,
                                     @JsonProperty("created_at") val createdAt: LocalDateTime,
                                     @JsonProperty("updated_at") val updatedAt: LocalDateTime,
                                     @JsonProperty("account_id") val accountId: UUID?) {
    init {

    }
}