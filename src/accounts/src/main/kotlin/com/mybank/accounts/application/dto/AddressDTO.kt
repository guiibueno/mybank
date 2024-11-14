package com.mybank.accounts.application.dto

data class AddressDTO (val street: String,
                       val number: String,
                       val city: String,
                       val state: String) {
    init {
        validate()
    }

    private fun validate() {
        require(isValidStreet)
        require(isValidNumber)
        require(isValidCity)
        require(isValidState)
    }

    private val isValidStreet: Boolean
        get() = street.length <= 50

    private val isValidNumber: Boolean
        get() = number.length <= 10

    private val isValidCity: Boolean
        get() = city.length <= 50

    private val isValidState: Boolean
        get() = state.length <= 2
}