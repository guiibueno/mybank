package com.mybank.accounts.domain.exception

class DistributedLockException(message: String, cause: Throwable? = null): RuntimeException(message, cause)