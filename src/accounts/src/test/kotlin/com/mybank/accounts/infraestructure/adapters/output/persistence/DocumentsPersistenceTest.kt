package com.mybank.accounts.infraestructure.adapters.output.persistence

import com.mybank.accounts.application.dto.DocumentDTO
import com.mybank.accounts.domain.entity.CustomerDocumentsEntity
import com.mybank.accounts.infraestructure.adapters.output.persistence.repository.CustomerDocumentsRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList

class DocumentsPersistenceTest {
    private var documentsPersistence: DocumentsPersistence

    @MockK
    private lateinit var documentsRepository: CustomerDocumentsRepository

    init {
        MockKAnnotations.init(this)
        documentsPersistence = spyk(
            DocumentsPersistence(documentsRepository)
        )
    }

    @Test
    fun `should persist documents successfully`() {
        val customerId = UUID.randomUUID().toString()
        val documents = ArrayList<DocumentDTO>()

        documents.add(DocumentDTO("CPF", "123"))

        every { documentsRepository.saveAll<CustomerDocumentsEntity>(any()) } returns ArrayList<CustomerDocumentsEntity>()

        documentsPersistence.save(customerId, documents)

        coVerify (exactly = 1) {
            documentsRepository.saveAll<CustomerDocumentsEntity>(any())
        }
    }

    @Test
    fun `should ignore if documents list is empty`() {
        val customerId = UUID.randomUUID().toString()
        val documents = ArrayList<DocumentDTO>()

        every { documentsRepository.saveAll<CustomerDocumentsEntity>(any()) } returns ArrayList<CustomerDocumentsEntity>()

        documentsPersistence.save(customerId, documents)

        coVerify (exactly = 0) {
            documentsRepository.saveAll<CustomerDocumentsEntity>(any())
        }
    }
}
