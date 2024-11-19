package com.mybank.accounts.infraestructure.adapters.input.rest.mapper

import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.dto.AddressDTO
import com.mybank.accounts.application.dto.ContactInfoDTO
import com.mybank.accounts.application.dto.DocumentDTO
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.AccountCreateRequest
import java.util.*
import kotlin.collections.ArrayList

class AccountMapper {
    fun convertToPortInput(dto: AccountCreateRequest) : AccountRequest {

        val documents = ArrayList<DocumentDTO>()
        val contactInfos = ArrayList<ContactInfoDTO>()
        var address: AddressDTO? = null

        dto.documents.forEach { it ->
            documents.add(DocumentDTO(it.type, it.number))
        }

        if(dto.address != null)
            address = AddressDTO(dto.address.street, dto.address.number, dto.address.city, dto.address.state)

        if(dto.contactInfos != null)
            dto.contactInfos.forEach { it ->
                contactInfos.add(ContactInfoDTO(it.type, it.number))
            }

        return AccountRequest(dto.name, dto.birthDate, documents, address, contactInfos)
    }
}