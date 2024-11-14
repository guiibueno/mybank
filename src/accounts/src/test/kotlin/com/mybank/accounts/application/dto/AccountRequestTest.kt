package com.mybank.accounts.application.dto

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.util.Assert
import java.time.LocalDate
import kotlin.test.assertFailsWith

class AccountRequestTest {


    @Test
    fun `should valid when params is ok`(){
        val name = "João da Silva"
        val birthDate = LocalDate.of(LocalDate.now().year - 18, LocalDate.now().month, LocalDate.now().dayOfMonth)
        val documents = ArrayList<DocumentDto>()
        documents.add(DocumentDto("CPF", "123.123.132-11"))

        val request = AccountRequest(name, birthDate, documents, null, null)

        Assert.notNull(request, "Valid request!")
        Assertions.assertEquals(name, request.name)
        Assertions.assertEquals(birthDate, request.birthDate)
        Assertions.assertEquals(documents[0].number, "123.123.132-11")
    }

    @Test
    fun `should invalid when name is empty`(){
        val name = ""
        val birthDate = LocalDate.of(1998, 3, 1)
        val documents = ArrayList<DocumentDto>()
        documents.add(DocumentDto("CPF", "123.123.132-11"))

        assertFailsWith<IllegalArgumentException>(
            block = {
                val request = AccountRequest(name, birthDate, documents, null, null)
            }
        )
    }

    @Test
    fun `should invalid when age is less than eighteen`(){
        val name = "João da Silva"
        val birthDate = LocalDate.of(LocalDate.now().year - 18, LocalDate.now().month, LocalDate.now().dayOfMonth + 1)
        val documents = ArrayList<DocumentDto>()
        documents.add(DocumentDto("CPF", "123.123.132-11"))

        assertFailsWith<IllegalArgumentException>(
            block = {
                val request = AccountRequest(name, birthDate, documents, null, null)
            }
        )
    }

    @Test
    fun `should valid when age is greater than or equals eighteen`(){
        val name = "João da Silva"
        val birthDate = LocalDate.of(LocalDate.now().year - 18, LocalDate.now().month, LocalDate.now().dayOfMonth)
        val documents = ArrayList<DocumentDto>()
        documents.add(DocumentDto("CPF", "123.123.132-11"))

        val request = AccountRequest(name, birthDate, documents, null, null)

        Assert.notNull(request, "Valid request!")
    }

    @Test
    fun `should invalid when the document list does not contains a CPF`(){
        val name = "João da Silva"
        val birthDate = LocalDate.of(LocalDate.now().year - 18, LocalDate.now().month, LocalDate.now().dayOfMonth + 1)
        val documents = ArrayList<DocumentDto>()
        documents.add(DocumentDto("RG", "123.123.132-11"))
        documents.add(DocumentDto("Passaporte", "123.123.132-11"))
        documents.add(DocumentDto("Seilaoq", "123.123.132-11"))

        assertFailsWith<IllegalArgumentException>(
            block = {
                val request = AccountRequest(name, birthDate, documents, null, null)
            }
        )
    }

    @Test
    fun `should invalid when the document list contains a CPF`(){
        val name = "João da Silva"
        val birthDate = LocalDate.of(LocalDate.now().year - 18, LocalDate.now().month, LocalDate.now().dayOfMonth)
        val documents = ArrayList<DocumentDto>()
        documents.add(DocumentDto("CPF", "123.123.132-11"))
        documents.add(DocumentDto("RG", "123.123.132-11"))
        documents.add(DocumentDto("Passaporte", "123.123.132-11"))
        documents.add(DocumentDto("Seilaoq", "123.123.132-11"))

        val request = AccountRequest(name, birthDate, documents, null, null)

        Assert.notNull(request, "Valid request!")
    }
}