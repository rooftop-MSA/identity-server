package org.rooftop.identity.domain

internal fun interface AuthUsecase {

    fun auth(token: String, requesterId: Long)

}
