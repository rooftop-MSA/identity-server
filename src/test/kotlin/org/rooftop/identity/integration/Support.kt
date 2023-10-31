package org.rooftop.identity.integration

import org.rooftop.identity.domain.User
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate

internal fun R2dbcEntityTemplate.clearAll() = this
    .delete(User::class.java)
    .all()
    .block()
