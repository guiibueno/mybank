package com.mybank.accounts.infraestructure.adapters.input.rest

import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.application.dto.TransactionResultDTO
import com.mybank.accounts.application.port.input.TransactionAuthorizerPort
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.TransactionRequest
import com.mybank.accounts.infraestructure.adapters.input.rest.mapper.TransactionMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/accounts/{accountId}")
class TransactionRestAdapter(
    val transactionAuthorizerPort: TransactionAuthorizerPort
) {
    private val mapper = TransactionMapper()

    @PostMapping("/transactions")
    fun handleTransaction(@PathVariable accountId: UUID,
                          @RequestBody request: TransactionRequest
    ) : ResponseEntity<TransactionResultDTO> {
        try{
            val dto = mapper.convertToPortInput(accountId, request)
            val result: TransactionResultDTO? = transactionAuthorizerPort.invoke(dto)

            if(result != null)
                return ResponseEntity(result, HttpStatus.OK)
        }
        catch (validationException: IllegalArgumentException) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}