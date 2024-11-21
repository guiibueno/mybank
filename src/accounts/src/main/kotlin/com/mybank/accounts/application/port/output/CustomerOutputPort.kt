package com.mybank.accounts.application.port.output

import com.mybank.accounts.domain.entity.CustomerEntity
import java.time.LocalDate

interface CustomerOutputPort {
    fun save(name: String, birthDate: LocalDate) : CustomerEntity
}