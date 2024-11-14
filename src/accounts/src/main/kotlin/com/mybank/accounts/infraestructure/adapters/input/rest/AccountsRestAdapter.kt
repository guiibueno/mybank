package com.mybank.accounts.infraestructure.adapters.input.rest

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.input.GetAccountPort
import com.mybank.accounts.infraestructure.adapters.input.rest.data.response.ProposalGetStatusResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/accounts")
class AccountsRestAdapter(
    val getAccountPort: GetAccountPort
) {
    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<AccountDTO?> {
        try{
            val result: AccountDTO? = getAccountPort.invoke(id)
            if(result != null)
                return ResponseEntity(result, HttpStatus.OK)
        }
        catch (validationException: IllegalArgumentException) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
}