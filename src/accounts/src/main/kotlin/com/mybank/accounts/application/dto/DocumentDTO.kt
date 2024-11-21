package com.mybank.accounts.application.dto;

data class DocumentDTO (val type: String, val number: String) {
    init {
        validate()
    }

    private fun validate() {
        require(isValidType)
        require(isValidNumber)
    }

    private val isValidType: Boolean
    get() = type.length <= 50

    private val isValidNumber: Boolean
    get() = number.length <= 50
}
