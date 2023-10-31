package org.rooftop.identity.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import org.rooftop.identity.infra.R2dbcConfig
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ContextConfiguration
import reactor.test.StepVerifier

@DataR2dbcTest
@DisplayName("userRepository 클래스의")
@ContextConfiguration(classes = [R2dbcConfig::class])
internal class UserRepositoryTest(userRepository: UserRepository) : DescribeSpec({

    beforeEach {
        userRepository.save(user(USER_ID, SAVED_USER_NAME, isNew = true)).block()
    }

    afterEach {
        userRepository.deleteAll().block()
    }

    describe("findByNameOrNull 메소드는") {

        context("저장된 유저의 name이 주어지면,") {
            val savedUser = user(USER_ID, SAVED_USER_NAME)

            it("User를 반환한다.") {
                val result = userRepository.findByName(SAVED_USER_NAME)

                StepVerifier.create(result)
                    .assertNext {
                        it.shouldBeEqualToIgnoringFields(
                            savedUser,
                            User::createdAt,
                            User::modifiedAt
                        )
                    }
                    .verifyComplete()
            }
        }

        context("저장되지 않은 유저의 name이 주어지면,") {
            it("null을 반환한다.") {
                val result = userRepository.findByName("unknownName")

                StepVerifier.create(result)
                    .verifyComplete()
            }
        }
    }
}) {
    companion object {
        private const val USER_ID = 1L
        private const val SAVED_USER_NAME = "saved_user_name"
    }
}
