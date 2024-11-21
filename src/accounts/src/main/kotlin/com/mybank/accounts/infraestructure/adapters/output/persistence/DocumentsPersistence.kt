package com.mybank.accounts.infraestructure.adapters.output.persistence

import com.mybank.accounts.application.dto.DocumentDTO
import com.mybank.accounts.domain.entity.CustomerDocumentsEntity
import com.mybank.accounts.infraestructure.adapters.output.persistence.repository.CustomerDocumentsRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DocumentsPersistence(
    val customerDocumentsRepository: CustomerDocumentsRepository) {

    fun save(customerId: String, documents: List<DocumentDTO>){
        val documentsEntities = ArrayList<CustomerDocumentsEntity>()
        documents.forEach{
            documentsEntities.add(CustomerDocumentsEntity(null, LocalDateTime.now(), customerId, it.type, it.number))
        }
        if(documentsEntities.isNotEmpty())
            customerDocumentsRepository.saveAll(documentsEntities)
    }
}