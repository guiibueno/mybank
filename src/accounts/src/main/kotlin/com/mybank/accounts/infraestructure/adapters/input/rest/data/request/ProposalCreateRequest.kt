package com.mybank.accounts.infraestructure.adapters.input.rest.data.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class ProposalCreateRequest(val name: String,
                                 @JsonProperty("birth_date")val birthDate: LocalDate,
                                 val documents: List<DocumentDto>,
                                 val address: AddressDto?,
                                 @JsonProperty("contact_info") val contactInfos: List<ContactInfoDto>?) {

}

data class DocumentDto (val type: String, val number: String) {

}

data class ContactInfoDto (val type: String, val number: String) {

}

data class AddressDto (val street: String,
                       val number: String,
                       val city: String,
                       val state: String) {

}