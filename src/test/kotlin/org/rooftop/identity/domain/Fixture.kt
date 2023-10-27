package org.rooftop.identity.domain

import java.util.concurrent.atomic.AtomicLong

private val sequenceId = AtomicLong(1)
private val sequenceIdGenerator = { sequenceId.getAndIncrement() }

internal fun user(
    id: Long = sequenceIdGenerator.invoke(),
    name: String = "default_name",
    password: String = "default_password",
) = User(
    id = id,
    name = name,
    password = password
)
