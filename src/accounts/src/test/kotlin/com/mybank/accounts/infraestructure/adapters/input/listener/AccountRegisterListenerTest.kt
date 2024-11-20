package com.mybank.accounts.infraestructure.adapters.input.listener

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.input.AccountRegisterPort
import com.mybank.accounts.utils.AccountRequestMock
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.math.BigDecimal
import java.util.*
import kotlin.test.Test

class AccountRegisterListenerTest {
    private var mockAccountRequest: AccountRequestMock = AccountRequestMock()

    private var accountRegisterListener: AccountRegisterListener

    @MockK
    private lateinit var accountRegisterPort: AccountRegisterPort

    init {
        MockKAnnotations.init(this)
        accountRegisterListener = spyk(
            AccountRegisterListener(accountRegisterPort)
        )
    }

    @Test
    fun `should process account register when event is received`() {
        val event = mockAccountRequest.GetAccountRequest()
        val kafkaMessage = ConsumerRecord<String, AccountRequest>("topic", 0, 123L, "key", event)

        every { accountRegisterPort.invoke(any()) } returns AccountDTO(UUID.randomUUID(), BigDecimal.ZERO)

        accountRegisterListener.handle(kafkaMessage)

        coVerify (exactly = 1) {
            accountRegisterPort.invoke(any())
        }
    }
}