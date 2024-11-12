# MyBank: Desafio técnico para gestão de contas e saldos

## O Problema

Implementar as interfaces de abertura de conta e efetivação de transações(débito e credito), levando em consideração a garantia da consistencia dos dados, suporte a um alto volume de transações, desacoplamento, baixo tempo de resposta e possibilidade de evolução.

Para garantir esses requisitos, decidi decompor o problema em duas partes "Contas" e "Transações".

## 01 - Contas

Será o microserviço responsavel pela abertura e gestão de contas. 

#### Jornada 01: Abertura de Contas
Visando o desacoplamento da jornada, assim como num banco de verdade, a abertura de contas será originada a partir de uma proposta, que poderá ser aprovada ou negada.
O processamento da proposta deverá ser realizado de forma assincrona e caso aprovada, será aberta uma conta para o cliente.

`POST /proposals`
```json
{
    "name": "João da Silva",
    "birthday_date": "1999-01-01",
    "documents": [
        {
            "type": "CPF",
            "number": "111.111.111-11"
        }
    ],
    "address": {
        "street": "Rua dos Bobos",
        "number": "0A",
        "city": "SÃO PAULO",
        "state": ""
    },
    "contact_info": [
        {
            "type": "PHONE",
            "number": "(11) 4002-8922"
        },
        {
            "type": "EMAIL",
            "number": "joazinhomaismais@email-legal.com.br"
        }
    ]
}
```
Onde
- `name` deve ser uma string de 1 a 50 caracteres **(obrigatório)**.
- `birthday_date` deve ser uma data válida **(obrigatório)**.

- `documents` deve ser um array de objetos com as seguintes propriedades **(obrigatório)**:
    - `type` deve ser uma string de 0 a 50 caracteres.
    - `number` deve ser uma string de 0 a 50 caracteres.

    Obs.: Para o array de documentos é obrigatorio pelo menos um objeto do tipo CPF.

- `address` deve ser um array de objetos com as seguintes propriedades **(opcional)**:
    - `street` deve ser uma string de 0 a 50 caracteres.
    - `number` deve ser uma string de 0 a 10 caracteres.
    - `city` deve ser uma string de 0 a 50 caracteres.
    - `state` deve ser uma string de 0 a 2 caracteres.

- `contact_info` deve ser um array de objetos com as seguintes propriedades **(opcional)**:
    - `type` deve ser uma string de 0 a 50 caracteres.
    - `number` deve ser uma string de 0 a 50 caracteres.

**Resposta**

`HTTP 202 Accepted`
```json
{
    "id" : "69f38532-f59a-432c-bfd3-96cb2d955eda",
}
```
Onde
- `id` deve ser um UUID representando o identificador unico de uma proposta.

**Regras**

Os campos **Nome**, **CPF** e **Data de Nascimento** são obrigatorios e a idade minima é **18 anos**. Caso essas condições não sejam satisfeitas, a API devera retornar Status Code 400 - Bad Request.

A proposta nasce no status "IN_ANALYSIS" e pode evoluir para "APPROVED" ou "REJECTED".

`GET /proposals/[id]`

Onde
- `[id]` (route parameter) deve ser um UUID representando o identificador unico de uma proposta.

**Resposta**

`HTTP 200 OK`
```json
{
  "id": "69f38532-f59a-432c-bfd3-96cb2d955eda",
  "status": "APPROVED",
  "created_at": "2024-11-11T20:30:00",
  "updated_at": "2024-11-11T20:31:00",
  "account_id": "249fdc0a-dc82-4b49-840f-55fbd8b2b544"  
}
```
Onde
- `id` deve ser um UUID representando o identificador unico de uma proposta.
- `status` deve ser o status atual da proposta.
- `created_at` deve ser a data/hora da criação.
- `updated_at` deve ser a data/hora da ultima atualização.
- `account_id` deve ser um UUID representando o identificador unico da conta originada através dessa proposta (se houver).

#### Jornada 02: Gestão de Contas

`GET /accounts/[id]`

Onde
- `[id]` (route parameter) deve ser um UUID representando o identificador unico de uma conta.

**Resposta**

`HTTP 200 OK`
```json
{
  "id": "69f38532-f59a-432c-bfd3-96cb2d955eda",
  "name": "João da Silva",
  "balance": 0
}
```
Onde
- `id` deve ser um UUID representando o identificador unico de uma proposta.
- `name` deve ser o nome do cliente.
- `balance` deve ser o saldo atual.

## 02 - Transações