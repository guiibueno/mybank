package com.mybank.accounts.infraestructure.adapters.input.listener

import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.input.AccountRegisterPort
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountRegisterListener(
    val accountRegisterPort: AccountRegisterPort
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        id = "\${topics.accounts.requests.name}",
        topics = ["\${topics.accounts.requests.name}"])
    fun handle(record: ConsumerRecord<String, AccountRequest> ) {
        val executionId = UUID.randomUUID()
        logger.info("[${executionId}] - Initializing account register.")

        val account = accountRegisterPort.invoke(record.value());
        if(account == null) {
            logger.info("[${executionId}] - Failed to create account.")
            return
        }

        logger.info("[${executionId}] - Account created ${account.id}.")
    }
}