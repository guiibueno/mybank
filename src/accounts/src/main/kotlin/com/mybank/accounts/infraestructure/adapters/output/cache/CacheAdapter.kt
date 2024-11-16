package com.mybank.accounts.infraestructure.adapters.output.cache

interface CacheAdapter {
    fun setValue (key: String, value: Any, ttl: Long)
    fun <T> getValue (key: String, clazz: Class<T>): T?
}