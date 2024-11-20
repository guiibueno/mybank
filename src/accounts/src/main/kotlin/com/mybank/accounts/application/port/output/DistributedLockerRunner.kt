package com.mybank.accounts.application.port.output

interface DistributedLockerRunner {
    fun<T> tryRunLocked(key: String, optionalLock: Boolean, fn: () -> T): T
}