package org.rooftop.identity.domain.identity

import reactor.core.publisher.Mono

internal fun interface AuthUsecase {

    fun auth(token: String, requesterId: Long): Mono<Unit>

}
