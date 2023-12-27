package org.rooftop.identity.domain.account

import java.util.concurrent.atomic.AtomicLong

private val sequenceId = AtomicLong(1)
private val sequenceIdGenerator = { sequenceId.getAndIncrement() }

internal fun user(
    id: Long = sequenceIdGenerator.invoke(),
    name: String = "default_name",
    userName: String = "xb",
    password: String = "default_password",
    isNew: Boolean = false,
    version: Int = 0,
) = User(
    id = id,
    name = name,
    userName = userName,
    password = password,
    isNew = isNew,
    version = version,
)
