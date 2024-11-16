package com.mybank.accounts.infraestructure.adapters.output.cache

import com.fasterxml.jackson.databind.ObjectMapper
import org.redisson.api.RedissonClient
import org.redisson.client.codec.StringCodec
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisCacheAdapter(
    val mapper: ObjectMapper,
    val client: RedissonClient
) : CacheAdapter {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun setValue(key: String, value: Any, ttl: Long) {
        try{
            val serializedValue = mapper.writeValueAsString(value)

            val bucket = client.getBucket<String>(key, StringCodec.INSTANCE)
            bucket.set(serializedValue, Duration.ofSeconds(ttl))
        }
        catch (ex: Exception){
            logger.error("Error to set an value into key ${key}", ex)
        }
    }

    override fun <T> getValue(key: String, clazz: Class<T>): T? {
        try {
            val bucket = client.getBucket<String>(key, StringCodec.INSTANCE)
            val cachedValue = bucket.get()
            if(cachedValue != null)
                return mapper.readValue(cachedValue, clazz)
        } catch (ex: Exception){
            logger.error("Error to get value from key ${key}", ex)
        }

        return null
    }
}