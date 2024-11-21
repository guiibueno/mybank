package com.mybank.accounts.infraestructure.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.integration.redis.util.RedisLockRegistry
import org.springframework.integration.support.locks.ExpirableLockRegistry
import org.springframework.integration.support.locks.LockRegistry
import org.springframework.integration.support.locks.PassThruLockRegistry
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.util.StopWatch

private const val REDIS     = "'redis'"

@Configuration
class LockRegistryConfig(
    @Value("\${distributed-lock.timeout.connection:1000}")
    val connectionTimeout: Int,
    @Value("\${distributed-lock.timeout.session:30000}")
    val sessionTimeout: Int,
    @Value("\${distributed-lock.key-prefix:}")
    val prefix: String,
    @Value("\${distributed-lock.retry.count:1}")
    val retryCnt: Int,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Bean
    @ConditionalOnExpression("{$REDIS}.contains('\${distributed-lock.type}'.toLowerCase()) == false")
    fun emptyLockRegistry(): LockRegistry {
        return PassThruLockRegistry().also { log.info("Pass-through LockRegistry activated.") }
    }

    @Bean
    @ConditionalOnExpression("'\${distributed-lock.type}'.toLowerCase() == $REDIS")
    fun redisLockRegistry(
        connectionFactory: RedisConnectionFactory,
    ): RedisLockRegistry {
        return RedisLockRegistry(connectionFactory, prefix, sessionTimeout.toLong())
            .also { log.info("Redis LockRegistry activated.") }
    }
}

@Configuration
@ConditionalOnBean(ExpirableLockRegistry::class)
class LockScheduler(
    private val lockRegistry: ExpirableLockRegistry,
    @Value("\${distributed-lock.expire.age:60000}")
    private val expireAge: Int,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @ConditionalOnExpression("'\${distributed-lock.expire.cron:}' != ''")
    @Scheduled(cron="\${distributed-lock.expire.cron:1 1 1 1 1 *}")
    fun expireUnusedLock() {
        StopWatch("Clear unused lock").let {
            it.start()
            lockRegistry.expireUnusedOlderThan(expireAge.toLong())
            log.trace(it.shortSummary())
        }
    }

}