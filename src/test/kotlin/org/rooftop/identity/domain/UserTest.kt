package org.rooftop.identity.domain

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

@DisplayName("User 클래스의")
internal class UserTest : DescribeSpec({

    describe("validPassword 메소드는") {
        val user = user(1L, "default_user", "default_password")

        context("올바른 비밀번호가 들어오면,") {
            val validPassword = "default_password"

            it("인증을 성공한다.") {
                val result = shouldNotThrow<IllegalArgumentException> {
                    user.validPassword(validPassword)
                }

                result shouldBe Unit
            }
        }

        context("올바르지 않은 비밀번호가 들어오면,") {
            val invalidPassword = "invalidPassword"

            it("인증에 실패한다.") {
                val result = shouldThrow<IllegalArgumentException> {
                    user.validPassword(invalidPassword)
                }

                result::class shouldBe IllegalArgumentException::class
                result shouldHaveMessage "Invalid password"
            }
        }
    }

    describe("생성자는") {

        context("name이 20자를 초과할경우,") {

            it("IllegalArgumentException을 던진다.") {
                shouldThrow<IllegalArgumentException> {
                    user(name = "123456789_123456789_123456789")
                }
            }
        }
    }
})
