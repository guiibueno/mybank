package com.mybank.accounts.infraestructure.adapters.input.rest.data.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class AccountCreateRequest(val name: String,
                                 @JsonProperty("birth_date")val birthDate: LocalDate,
                                 val documents: List<TypeNumberDto>,
                                 val address: AccountCreateAddressDto?,
                                 @JsonProperty("contact_info") val contactInfos: List<TypeNumberDto>?) {

}

data class TypeNumberDto (val type: String,
                          val number: String) {

}

data class AccountCreateAddressDto (val street: String,
                                    val number: String,
                                    val city: String,
                                    val state: String) {

}