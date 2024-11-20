package com.mybank.accounts.infraestructure.adapters.input.rest.utils

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest


@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleMethodArgumentNotValid(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        val requestUri = (request as ServletWebRequest).request.requestURI.toString()

        logger.warn("[$requestUri] Invalid Request: ", ex)

        return ResponseEntity(
            "Invalid request",
            HttpHeaders(),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val requestUri = (request as ServletWebRequest).request.requestURI.toString()

        logger.error("[$requestUri] Error: ", ex)

        return ResponseEntity(
            "An internal server error occurred",
            HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}