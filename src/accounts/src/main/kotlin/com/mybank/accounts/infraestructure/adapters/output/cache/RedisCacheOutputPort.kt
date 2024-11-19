package com.mybank.accounts.infraestructure.adapters.output.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.mybank.accounts.application.port.output.CacheOutputPort
import org.redisson.api.RedissonClient
import org.redisson.client.codec.StringCodec
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class RedisCacheOutputPort(
    val mapper: ObjectMapper,
    val client: RedissonClient
) : CacheOutputPort {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun setValue(key: String, value: Any, ttl: Long) {
        try{
            val serializedValue = mapper.writeValueAsString(value)

            val bucket = client.getBucket<String>(key, StringCodec.INSTANCE)
            bucket.set(serializedValue, Duration.ofSeconds(ttl))
        }
        catch (ex: Exception){
            logger.error("Error to set an value into key $key", ex)
        }
    }

    override fun <T> getValue(key: String, clazz: Class<T>): T? {
        try {
            val bucket = client.getBucket<String>(key, StringCodec.INSTANCE)
            val cachedValue = bucket.get()
            if(cachedValue != null)
                return mapper.readValue(cachedValue, clazz)
        } catch (ex: Exception){
            logger.error("Error to get value from key $key", ex)
        }

        return null
    }

    override fun expire(key: String) {
        try {
            val bucket = client.getBucket<String>(key, StringCodec.INSTANCE)
            bucket.expire(Instant.now())
        } catch (ex: Exception){
            logger.error("Error to expire value from key $key", ex)
        }
    }
}