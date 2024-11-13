package com.mybank.accounts.application.dto

import java.time.LocalDate
import java.time.Period

data class SendProposalRequest(val name: String,
                               val birthDate: LocalDate,
                               val documents: List<DocumentDto>,
                               val address: AddressDto?,
                               val contactInfos: List<ContactInfoDto>?) {

    init {
        validate()
    }

    private fun validate() {
        require(isValidName)
        require(isGreaterThanOrEqualEighteenYearsOld)
        require(containsMandatoryDocuments)
    }

    private val isValidName: Boolean
        get() = name != "" && name.length <= 50



    private val containsMandatoryDocuments: Boolean
        get() = documents.stream().map(DocumentDto::type).anyMatch("CPF"::equals)


    private val isGreaterThanOrEqualEighteenYearsOld: Boolean
        get() = Period.between(
                    birthDate,
                    LocalDate.now()
                ).years >= 18
}

data class DocumentDto (val type: String, val number: String) {
    init {
        validate()
    }

    private fun validate() {
        require(isValidType)
        require(isValidNumber)
    }

    private val isValidType: Boolean
        get() = type.length <= 50

    private val isValidNumber: Boolean
        get() = number.length <= 50
}

data class ContactInfoDto (val type: String, val number: String) {
    init {
        validate()
    }

    private fun validate() {
        require(isValidType)
        require(isValidNumber)
    }

    private val isValidType: Boolean
        get() = type.length <= 50

    private val isValidNumber: Boolean
        get() = number.length <= 50
}

data class AddressDto (val street: String,
                       val number: String,
                       val city: String,
                       val state: String) {
    init {
        validate()
    }

    private fun validate() {
        require(isValidStreet)
        require(isValidNumber)
        require(isValidCity)
        require(isValidState)
    }

    private val isValidStreet: Boolean
        get() = street.length <= 50

    private val isValidNumber: Boolean
        get() = number.length <= 10

    private val isValidCity: Boolean
        get() = city.length <= 50

    private val isValidState: Boolean
        get() = state.length <= 2
}