package org.rooftop.identity.infra

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import org.springframework.test.context.ContextConfiguration
import java.util.concurrent.Executors

@DisplayName("TsidGenerator 클래스의")
@ContextConfiguration(classes = [TsidIdGenerator::class])
internal class TsidIdGeneratorTest(private val idGenerator: TsidIdGenerator) : DescribeSpec({

    describe("generateId 메소드는") {

        context("여러 사용자가 동시에 호출해도,") {
            val idGenerateSize = 200
            val executor = Executors.newCachedThreadPool()
            val runners = idRunners(idGenerator, idGenerateSize)

            it("id가 겹치지 않는다.") {
                val futures = executor.invokeAll(runners)

                val result = futures.map {
                    it.get()
                }.toSet()

                result shouldHaveSize idGenerateSize
            }
        }
    }
})
