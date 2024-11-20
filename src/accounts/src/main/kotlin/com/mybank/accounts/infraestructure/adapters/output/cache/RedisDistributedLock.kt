package com.mybank.accounts.infraestructure.adapters.output.cache

import com.mybank.accounts.application.port.output.DistributedLockerRunner
import com.mybank.accounts.domain.exception.DistributedLockException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.integration.support.locks.LockRegistry
import org.springframework.stereotype.Service
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

@Service
class RedisDistributedLock(
    @Value("\${distributed-lock.timeout.lock:1000}")
    val lockTimeout: Long,
    @Value("\${distributed-lock.retry.count:10}")
    val retryCount: Int,
    @Value("\${distributed-lock.retry.sleep:200}")
    val retrySleep: Long,
    val lockRegistry: LockRegistry
)  : DistributedLockerRunner{
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun <T> tryRunLocked(key: String, optionalLock: Boolean, fn: () -> T): T {
        val lock = tryLock(key)

        try{
            if(lock != null || optionalLock)
                return fn()
        } finally {
            try { lock?.unlock() } catch (e: Exception) {}
        }

        throw DistributedLockException("Could not execute with a lock")
    }

    private fun tryLock(key: String) : Lock? {
        val lock = lockRegistry.obtain(key)

        for(i in 0..retryCount){
            try{
                if(i!=0)
                    sleep(retrySleep)
                if(lock.tryLock(lockTimeout, TimeUnit.MILLISECONDS) )
                    return lock

            }
            catch (ex: Exception) {
                logger.error("Failed to obtain a lock", ex)
            }
        }

        return null
    }
}