package com.mybank.accounts.application.dto

import java.time.LocalDate
import java.time.Period

data class AccountRequest(val name: String,
                          val birthDate: LocalDate,
                          val documents: List<DocumentDTO>,
                          val address: AddressDTO?,
                          val contactInfos: List<ContactInfoDTO>?) {

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
        get() = documents.stream().map(DocumentDTO::type).anyMatch("CPF"::equals)


    private val isGreaterThanOrEqualEighteenYearsOld: Boolean
        get() = Period.between(
                    birthDate,
                    LocalDate.now()
                ).years >= 18
}