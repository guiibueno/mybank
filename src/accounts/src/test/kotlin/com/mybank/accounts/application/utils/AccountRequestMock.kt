package com.mybank.accounts.application.utils

import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.dto.AddressDTO
import com.mybank.accounts.application.dto.ContactInfoDTO
import com.mybank.accounts.application.dto.DocumentDTO
import java.time.LocalDate

class AccountRequestMock {
    fun GetAccountRequest() : AccountRequest{
        val name = "João da Silva"
        val birthDate = LocalDate.of(LocalDate.now().year - 18, LocalDate.now().month, LocalDate.now().dayOfMonth)

        val documents = ArrayList<DocumentDTO>()
        documents.add(DocumentDTO("CPF", "123.123.132-11"))

        val address = AddressDTO("Rua dos Bobos", "0", "São Paulo", "SP")

        val contactInfos = ArrayList<ContactInfoDTO>()
        contactInfos.add(ContactInfoDTO("EMAIL", "teste@teste.com"))

        return AccountRequest(name, birthDate, documents, address, contactInfos)
    }
}