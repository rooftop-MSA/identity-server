package org.rooftop.identity.infra

import org.rooftop.identity.domain.IdGenerator
import java.util.concurrent.Callable
import java.util.stream.IntStream

internal fun idRunners(idGenerator: IdGenerator, count: Int): List<Callable<Long>> =
    IntStream.range(0, count)
        .mapToObj { Callable { idGenerator.generate() } }
        .toList()
