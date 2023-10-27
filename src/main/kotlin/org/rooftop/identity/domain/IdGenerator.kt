package org.rooftop.identity.domain

internal fun interface IdGenerator {

    fun generate(): Long
}
