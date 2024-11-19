package com.mybank.accounts.infraestructure.adapters.output.metrics

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.TransactionResultDTO
import com.mybank.accounts.application.port.output.MetricsOutputPort
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MicrometerMetricsOutputPort(
    private val meterRegistry: MeterRegistry
) : MetricsOutputPort {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun accountRegisterRequested() {
        val tags = ArrayList<Pair<String, String>>()
        register("account.requested", tags.toList())
    }

    override fun transactionHandled(transaction: TransactionResultDTO) {
        val tags = arrayOf(
            "transaction_status" to transaction.status
        )

        register("transaction.handled", tags.toList())
    }

    override fun accountCreated(account: AccountDTO) {
        val tags = ArrayList<Pair<String, String>>()
        register("account.created", tags.toList())
    }

    private fun register(name: String, tags: List<Pair<String, String>>) {
        val metricName = "$name.counter"
        try{
            Counter
                .builder(metricName)
                .description("Eventos processados")
                .tags(tags.map { Tag.of( it.first, it.second ) })
                .register(meterRegistry)
                .increment()
        }
        catch (ex: Exception){
            logger.error("Error when exporting metric: $metricName", ex)
        }
    }
}