package com.mybank.accounts.infraestructure.adapters.input.rest

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.input.AccountRegisterPort
import com.mybank.accounts.application.port.input.GetAccountPort
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.AccountCreateRequest
import com.mybank.accounts.infraestructure.adapters.input.rest.mapper.AccountMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/accounts")
class AccountsRestAdapter(
    val accountRegisterPort: AccountRegisterPort,
    val getAccountPort: GetAccountPort,
) {
    private val mapper = AccountMapper()

    @PostMapping("")
    fun createAsync(@RequestBody request: AccountCreateRequest): ResponseEntity<AccountDTO?> {
        accountRegisterPort.invokeAsync(mapper.convertToPortInput(request))

        return ResponseEntity(null, HttpStatus.ACCEPTED)
    }

    @GetMapping("")
    fun findByDocument(@RequestParam documentNumber: String): ResponseEntity<AccountDTO?> {
        val result: AccountDTO? = getAccountPort.invoke(documentNumber)
        if(result != null)
            return ResponseEntity(result, HttpStatus.OK)

        return ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<AccountDTO?> {
        val result: AccountDTO? = getAccountPort.invoke(id)
        if(result != null)
            return ResponseEntity(result, HttpStatus.OK)

        return ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
}