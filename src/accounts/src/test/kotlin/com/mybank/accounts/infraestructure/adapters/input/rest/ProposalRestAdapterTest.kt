package com.mybank.accounts.infraestructure.adapters.input.rest

import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.input.GetProposalStatusPort
import com.mybank.accounts.application.port.input.SendProposalPort
import com.mybank.accounts.utils.AccountRequestMock
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.AddressDto
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.ContactInfoDto
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.DocumentDto
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.ProposalCreateRequest
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.Test

class ProposalRestAdapterTest {
    private var mockAccountRequest: AccountRequestMock = AccountRequestMock()

    private var proposalRestAdapter: ProposalRestAdapter

    @MockK
    private lateinit var sendProposalPort: SendProposalPort

    @MockK
    private lateinit var getProposalPort: GetProposalStatusPort

    init {
        MockKAnnotations.init(this)
        proposalRestAdapter = spyk(
            ProposalRestAdapter(sendProposalPort, getProposalPort)
        )
    }

    @Test
    fun `should returns 200 when proposal exists`() {
        val proposalId = UUID.randomUUID()

        val proposalMock = ProposalDTO(proposalId, "APPROVED", LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID(), mockAccountRequest.GetAccountRequest())

        every { getProposalPort.invoke(any<UUID>()) } returns proposalMock

        val response = proposalRestAdapter.getStatus(proposalId)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(proposalId, response.body?.id)
        Assertions.assertEquals(proposalMock.accountId, response.body?.accountId)
        Assertions.assertEquals(proposalMock.status, response.body?.status)
        Assertions.assertEquals(proposalMock.createdAt, response.body?.createdAt)
        Assertions.assertEquals(proposalMock.updatedAt, response.body?.updatedAt)
        Assertions.assertEquals(HttpStatus.OK,response.statusCode)

        coVerify (exactly = 1) {
            getProposalPort.invoke(any())
        }
    }

    @Test
    fun `should returns 404 when proposal not exists`() {
        val proposalId = UUID.randomUUID()

        every { getProposalPort.invoke(any<UUID>()) } returns null

        val response = proposalRestAdapter.getStatus(proposalId)

        Assertions.assertNotNull(response)
        Assertions.assertNull(response.body)
        Assertions.assertEquals(HttpStatus.NOT_FOUND,response.statusCode)

        coVerify (exactly = 1) {
            getProposalPort.invoke(any())
        }
    }

    @Test
    fun `should returns 200 when proposal is created`() {
        val proposalId = UUID.randomUUID()

        val documents = ArrayList<DocumentDto>()
        documents.add(DocumentDto("CPF", "123.456.789-19"))
        val contactInfos = ArrayList<ContactInfoDto>()

        val address = AddressDto("Rua 1", "1", "São Paulo", "SP")
        val birthDate = LocalDate.of(LocalDate.now().year - 18, LocalDate.now().month, LocalDate.now().dayOfMonth)

        val request = ProposalCreateRequest("Teste", birthDate, documents, address, contactInfos)
        val proposalMock = ProposalDTO(proposalId, "APPROVED", LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID(), mockAccountRequest.GetAccountRequest())

        every { sendProposalPort.invoke(any<AccountRequest>()) } returns proposalMock

        val response = proposalRestAdapter.create(request)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(proposalId, response.body?.id)
        Assertions.assertEquals(HttpStatus.OK,response.statusCode)

        coVerify (exactly = 1) {
            sendProposalPort.invoke(any())
        }
    }

    @Test
    fun `should returns 422 when proposal not created`() {
        val proposalId = UUID.randomUUID()

        val documents = ArrayList<DocumentDto>()
        documents.add(DocumentDto("CPF", "123.456.789-19"))
        val contactInfos = ArrayList<ContactInfoDto>()

        val address = AddressDto("Rua 1", "1", "São Paulo", "SP")
        val birthDate = LocalDate.of(LocalDate.now().year - 18, LocalDate.now().month, LocalDate.now().dayOfMonth)

        val request = ProposalCreateRequest("Teste", birthDate, documents, address, contactInfos)

        every { sendProposalPort.invoke(any<AccountRequest>()) } returns null

        val response = proposalRestAdapter.create(request)

        Assertions.assertNotNull(response)
        Assertions.assertNull(response.body)
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY,response.statusCode)

        coVerify (exactly = 1) {
            sendProposalPort.invoke(any())
        }
    }
}