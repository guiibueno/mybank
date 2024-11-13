package com.mybank.accounts.infraestructure.adapters.input.rest

import com.mybank.accounts.application.dto.*
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.ProposalCreateRequest
import com.mybank.accounts.infraestructure.adapters.input.rest.data.response.ProposalGetStatusResponse

class  ProposalMapper {
    fun convertToPortInput(dto: ProposalCreateRequest) : SendProposalRequest {

        val documents = ArrayList<DocumentDto>()
        val contactInfos = ArrayList<ContactInfoDto>()
        var address: AddressDto? = null

        dto.documents.forEach { it ->
            documents.add(DocumentDto(it.type, it.number))
        }

        if(dto.address != null)
            address = AddressDto(dto.address.street, dto.address.number, dto.address.city, dto.address.state)

        if(dto.contactInfos != null)
            dto.contactInfos.forEach { it ->
                contactInfos.add(ContactInfoDto(it.type, it.number))
            }

        return SendProposalRequest(dto.name, dto.birthDate, documents, address, contactInfos)
    }

    fun convertFromPortDTO(dto: ProposalDTO) : ProposalGetStatusResponse {
        return ProposalGetStatusResponse(dto.id, dto.status, dto.createdAt, dto.updatedAt, dto.accountId)
    }
}