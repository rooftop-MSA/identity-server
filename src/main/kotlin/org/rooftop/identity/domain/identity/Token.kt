package org.rooftop.identity.domain.identity

internal interface Token {

    fun getToken(id: Long): String

    fun getId(token: String): Long
}
