package com.mybank.accounts.infraestructure.adapters.output.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.mybank.accounts.application.port.output.CacheOutputPort
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedisCacheOutputPort(
    val mapper: ObjectMapper,
    val client: RedisTemplate<String, String>
) : CacheOutputPort {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun setValue(key: String, value: Any, ttl: Long) {
        try{
            val serializedValue = mapper.writeValueAsString(value)

            client.opsForValue().set(key, serializedValue)
        }
        catch (ex: Exception){
            logger.error("Error to set an value into key $key", ex)
        }
    }

    override fun <T> getValue(key: String, clazz: Class<T>): T? {
        try {
            val cachedValue = client.opsForValue().get(key)
            if(cachedValue != null)
                return mapper.readValue(cachedValue, clazz)
        } catch (ex: Exception){
            logger.error("Error to get value from key $key", ex)
        }

        return null
    }

    override fun expire(key: String) {
        try {
            client.opsForValue().getAndExpire(key, 0, TimeUnit.NANOSECONDS)
        } catch (ex: Exception){
            logger.error("Error to expire value from key $key", ex)
        }
    }
}