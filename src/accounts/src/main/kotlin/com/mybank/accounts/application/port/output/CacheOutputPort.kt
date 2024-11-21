package com.mybank.accounts.application.port.output

interface CacheOutputPort {
    fun setValue (key: String, value: Any, ttl: Long)
    fun <T> getValue (key: String, clazz: Class<T>): T?
    fun expire (key: String)
}