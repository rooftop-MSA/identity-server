package org.rooftop.identity.domain

internal interface Token {

    fun getToken(id: Long): String

    fun getId(token: String): Long
}
