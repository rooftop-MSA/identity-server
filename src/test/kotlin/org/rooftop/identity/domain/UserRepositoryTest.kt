package org.rooftop.identity.domain

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@DisplayName("userRepository 클래스의")
internal class UserRepositoryTest(userRepository: UserRepository) : DescribeSpec({

    extension(SpringExtension)

    beforeEach {
        userRepository.saveAndFlush(savedUser)
    }

    describe("findByNameOrNull 메소드는") {

        context("저장된 유저의 name이 주어지면,") {
            it("User를 반환한다.") {
                val result = userRepository.findByNameOrNull(SAVED_USER_NAME)

                result shouldBe savedUser
            }
        }

        context("저장되지 않은 유저의 name이 주어지면,") {
            it("null을 반환한다.") {
                val result = userRepository.findByNameOrNull("unknownName")

                result shouldBe null
            }
        }
    }
}) {
    companion object {
        private const val SAVED_USER_NAME = "saved_user_name"
        private val savedUser = user(id = 1L, name = SAVED_USER_NAME)
    }
}
