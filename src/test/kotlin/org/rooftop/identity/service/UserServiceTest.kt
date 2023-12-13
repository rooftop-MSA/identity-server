package org.rooftop.identity.service

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import org.rooftop.identity.domain.IdGenerator
import org.rooftop.identity.domain.account.User
import org.rooftop.identity.domain.account.UserRepository
import org.rooftop.identity.domain.account.event.UserCreatedEvent
import org.rooftop.identity.domain.account.request.UserCreateRequest
import org.rooftop.identity.domain.account.user
import org.rooftop.identity.domain.identity.Token
import org.springframework.test.context.ContextConfiguration
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@DisplayName("UserService 클래스의")
@ContextConfiguration(classes = [TestEventPublisher::class])
internal class UserServiceTest(
    private val eventPublisher: TestEventPublisher,
    @MockkBean private val idGenerator: IdGenerator,
    @MockkBean private val token: Token,
    @MockkBean private val userRepository: UserRepository,
) : DescribeSpec({

    val userService: UserService = UserService(userRepository, idGenerator, token, eventPublisher)

    every { idGenerator.generate() } returns 1
    every { userRepository.findByName(any()) } returns Mono.empty()
    every { userRepository.findByName(SAVED_NAME) } returns Mono.just(savedUser)

    beforeEach { eventPublisher.clear() }

    describe("createUser 메소드는") {

        context("유저 생성을 성공하면,") {
            val newUserCreateRequest =
                UserCreateRequest("newUser", "newUserName", "newUserPassword")

            every { userRepository.save(any()) } returns Mono.just(
                user(
                    idGenerator.generate(),
                    isNew = true
                )
            )

            it("UserCreatedEvent 를 발행한다.") {
                userService.createUser(newUserCreateRequest).block()

                eventPublisher.verify(1, UserCreatedEvent::class)
            }
        }

        context("유저 생성을 실패하면,") {
            val duplicateUserCreatedRequest =
                UserCreateRequest(SAVED_NAME, SAVED_USER_NAME, SAVED_USER_PASSWORD)

            every { userRepository.save(any()) } returns Mono.error { RuntimeException() }

            it("UserCreatedEvent가 발행되지 않는다.") {
                val result = userService.createUser(duplicateUserCreatedRequest)

                StepVerifier.create(result)
                    .verifyErrorMessage("Duplicated user name \"$SAVED_NAME\"")

                eventPublisher.verify(0, UserCreatedEvent::class)
            }
        }
    }
}) {

    companion object {
        private const val SAVED_NAME = "helloworld123"
        private const val SAVED_USER_NAME = "Jennifer"
        private const val SAVED_USER_PASSWORD = "Jennifer123"
        private val savedUser = User(1, SAVED_NAME, SAVED_USER_NAME, SAVED_USER_PASSWORD)
    }
}
