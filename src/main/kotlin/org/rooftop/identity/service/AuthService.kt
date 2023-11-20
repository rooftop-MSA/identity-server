package org.rooftop.identity.service

import org.rooftop.identity.domain.AuthUsecase
import org.rooftop.identity.domain.Token
import org.springframework.stereotype.Service
import javax.security.sasl.AuthenticationException

@Service
internal class AuthService(private val token: Token) : AuthUsecase {

    override fun auth(token: String, requesterId: Long) {
        val id = this.token.getId(token)
        if (isInvalidToken(id, requesterId)) {
            throw authException
        }
    }

    private fun isInvalidToken(tokenId: Long, requesterId: Long): Boolean =
        tokenId != requesterId

    private companion object {
        private val authException =
            AuthenticationException("Authenticate fail cause, does not matched requester")
    }
}
