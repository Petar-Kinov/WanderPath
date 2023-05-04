package com.example.wanderpath.data.model

class User(
    val username: String,
    val email: String,
    val password: String,
    val registrationTokens : MutableList<String>
)

{
    constructor() : this(username = "",email = "" , password = "" , registrationTokens = mutableListOf<String>())
}