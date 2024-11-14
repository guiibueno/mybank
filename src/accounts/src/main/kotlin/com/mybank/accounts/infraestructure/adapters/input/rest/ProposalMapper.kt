package com.mybank.accounts.infraestructure.adapters.input.rest

import com.mybank.accounts.application.dto.*
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.ProposalCreateRequest
import com.mybank.accounts.infraestructure.adapters.input.rest.data.response.ProposalGetStatusResponse

class  ProposalMapper {
    fun convertToPortInput(dto: ProposalCreateRequest) : AccountRequest {

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

    fun convertFromPortDTO(dto: ProposalDTO) : ProposalGetStatusResponse {
        return ProposalGetStatusResponse(dto.id, dto.status, dto.createdAt, dto.updatedAt, dto.accountId)
    }
}