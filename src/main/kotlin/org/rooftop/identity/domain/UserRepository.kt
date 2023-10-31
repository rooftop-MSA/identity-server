package org.rooftop.identity.domain

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

internal interface UserRepository : R2dbcRepository<User, Long> {

    fun findByName(name: String): Mono<User>
}
