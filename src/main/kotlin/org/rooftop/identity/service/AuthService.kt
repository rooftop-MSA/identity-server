package org.rooftop.identity.service

import org.rooftop.identity.domain.identity.AuthUsecase
import org.rooftop.identity.domain.identity.Token
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import javax.security.sasl.AuthenticationException

@Service
internal class AuthService(private val token: Token) : AuthUsecase {

    override fun auth(token: String, requesterId: Long): Mono<Unit> {
        return Mono.fromSupplier { this.token.getId(token) }
            .onErrorMap { authException }
            .filterWhen { isInvalidToken(it, requesterId) }
            .map { throw authException }
    }

    private fun isInvalidToken(tokenId: Long, requesterId: Long): Mono<Boolean> =
        Mono.just(tokenId != requesterId)

    private companion object {
        private val authException =
            AuthenticationException("Authenticate fail cause, does not matched requester")
    }
}
