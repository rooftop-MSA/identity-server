package org.rooftop.identity.service

import io.kotest.matchers.shouldBe
import org.springframework.context.ApplicationEventPublisher
import kotlin.reflect.KClass

class TestEventPublisher(private val publishedEvents: MutableMap<KClass<out Any>, Int>) :
    ApplicationEventPublisher {

    override fun publishEvent(event: Any) {
        publishedEvents[event::class] = publishedEvents.getOrDefault(event, 0) + 1
    }

    fun <T : KClass<out Any>> verify(exactly: Int, event: T) {
        publishedEvents.getOrDefault(event, 0) shouldBe exactly
    }

    fun clear() = publishedEvents.clear()
}
