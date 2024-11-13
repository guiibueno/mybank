package com.mybank.accounts.infraestructure.adapters.input.rest

import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.input.GetProposalStatusPort
import com.mybank.accounts.application.port.input.SendProposalPort
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.ProposalCreateRequest
import com.mybank.accounts.infraestructure.adapters.input.rest.data.response.ProposalCreateResponse
import com.mybank.accounts.infraestructure.adapters.input.rest.data.response.ProposalGetStatusResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/proposals")
class ProposalRestAdapter(private val sendProposalPort: SendProposalPort,
                          private val getProposalPort: GetProposalStatusPort) {
    private val mapper = ProposalMapper()

    @PostMapping
    fun create(@RequestBody request: ProposalCreateRequest): ResponseEntity<ProposalCreateResponse?> {
        try{
            val result: ProposalDTO? = sendProposalPort.invoke(mapper.convertToPortInput(request))

            if(result != null)
                return ResponseEntity(ProposalCreateResponse(result.id), HttpStatus.OK)
        }
        catch (validationException: IllegalArgumentException) {
             return ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping("/{id}")
    fun getStatus(@PathVariable id: UUID): ResponseEntity<ProposalGetStatusResponse?> {
        val response: ProposalGetStatusResponse? = null

        try{
            val result: ProposalDTO? = getProposalPort.invoke(id)
            if(result != null)
                return ResponseEntity(mapper.convertFromPortDTO(result), HttpStatus.OK)
        }
        catch (validationException: IllegalArgumentException) {
            return ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity(response, HttpStatus.NOT_FOUND);
    }
}