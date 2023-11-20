package org.rooftop.identity.controller

import org.rooftop.api.identity.ErrorRes
import org.rooftop.api.identity.errorRes
import org.rooftop.identity.domain.AuthUsecase
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.security.sasl.AuthenticationException

@RestController
internal class AuthController(private val authUsecase: AuthUsecase) {

    @GetMapping(value = ["/v1/auths"])
    @ResponseStatus(HttpStatus.OK)
    fun auth(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
        @RequestHeader("RequesterId") requesterId: Long,
    ) {
        authUsecase.auth(token, requesterId)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException::class)
    private fun handleAuthenticationException(exception: AuthenticationException): ErrorRes =
        errorRes { message = exception.message!! }
}
