package org.rooftop.identity.domain.account.request

internal data class UserLoginRequest(
    val name: String,
    val password: String,
)
