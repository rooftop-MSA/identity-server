package org.rooftop.identity.infra

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.test.context.ContextConfiguration

@DisplayName("Jwt 클래스의")
@ContextConfiguration(classes = [Jwt::class])
internal class JwtTest(jwt: Jwt) : DescribeSpec({

    describe("getToken 메소드는") {

        context("id가 들어오면,") {
            it("id가 담긴 token을 반환한다.") {
                val result = jwt.getToken(DEFAULT_ID)

                jwt.getId(result) shouldBe DEFAULT_ID
            }
        }
    }
}) {
    companion object {
        private const val DEFAULT_ID = 1L
    }
}
