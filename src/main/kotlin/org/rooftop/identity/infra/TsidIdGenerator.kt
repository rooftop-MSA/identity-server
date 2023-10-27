package org.rooftop.identity.infra

import com.github.f4b6a3.tsid.TsidFactory
import org.rooftop.identity.domain.IdGenerator
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import java.util.stream.IntStream

@Service
internal class TsidIdGenerator : IdGenerator {

    override fun generate(): Long {
        val factoryId = Thread.currentThread().threadId().toInt()
        val tsidFactory = tsidFactories[factoryId]
            ?: throw IllegalStateException("Cannot find right id generator \"$factoryId\"")

        return tsidFactory.create().toLong()
    }

    private companion object {
        private val tsidFactories: Map<Int, TsidFactory> = mapOf(
            *IntStream.range(0, 251)
                .mapToObj { factoryId -> factoryId to TsidFactory.newInstance256(factoryId) }
                .collect(Collectors.toList())
                .toTypedArray()
        )
    }
}
