package org.rooftop.identity.integration

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.shouldNotBe
import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserLoginRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
import org.rooftop.identity.domain.response.UserResponse
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.test.web.reactive.server.WebTestClient

@EnableR2dbcAuditing
@AutoConfigureWebTestClient
@DisplayName("User API 테스트의")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class IntegrationTest(
    private val webClient: WebTestClient,
    private val r2dbcEntityTemplate: R2dbcEntityTemplate,
) : DescribeSpec({

    extension(SpringExtension)

    afterEach { r2dbcEntityTemplate.clearAll() }

    describe("유저 생성 API는") {

        context("새로운 유저를 생성하는 요청이 들어올경우,") {
            it("새로운 유저를 생성한다.") {
                webClient.createUser(userCreateRequest)
                    .expectStatus().isOk
            }
        }

        context("중복된 유저를 생성하는 요청이 들어올 경우,") {
            webClient.createUser(userCreateRequest)

            it("400 Bad Request를 반환한다.") {
                webClient.createUser(userCreateRequest)
                    .expectStatus().isBadRequest
            }
        }
    }

    describe("유저이름으로 조회 API는") {

        context("저장된 유저를 조회할 경우,") {
            webClient.createUser(userCreateRequest)
            val expectedUserResponse = UserResponse(0L, NAME)

            it("유저 정보를 반환한다.") {
                webClient.getUser(NAME)
                    .expectStatus().isOk
                    .expectBody()
                    .shouldBeEqualToIgnoringFields(expectedUserResponse, UserResponse::id)
            }
        }

        context("저장되지 않은 유저를 조회할 경우 ") {

            it("400 Bad Request를 반환한다.") {
                webClient.getUser(NAME)
                    .expectStatus().isBadRequest
            }
        }
    }

    describe("유저 업데이트 API는") {
        val newName = "NEW_NAME"
        val newPassword = "NEW_PASSWORD"

        context("저장된 유저의 update 요청이 들어올경우,") {
            webClient.createUser(userCreateRequest)

            it("유저를 update한다.") {
                val id = webClient.getUserId(NAME)
                val request = UserUpdateRequest(id, newName, newPassword)

                webClient.updateUser(request).expectStatus().isOk

                webClient.getUser(newName)
                    .expectStatus().isOk
                    .expectBody().shouldBeEqualToComparingFields(UserResponse(id, newName))
            }
        }

        context("저장되지 않은 유저의 id로 update 요청이 들어올경우,") {
            val notExistId = 1L
            val invalidUpdateRequest = UserUpdateRequest(notExistId, newName, newPassword)

            it("400 BadRequest를 던진다.") {
                webClient.updateUser(invalidUpdateRequest)
                    .expectStatus().isBadRequest
            }
        }
    }

    describe("login API는") {
        context("유저의 name과 비밀번호가 주어질경우,") {
            webClient.createUser(userCreateRequest)

            it("token을 반환한다.") {
                webClient.login(userLoginRequest)
                    .expectStatus().isOk
                    .expectBody(Map::class.java)
                    .returnResult().responseBody["token"] shouldNotBe null
            }
        }

        context("유저의 name과 비밀번호가 일치하지 않을경우,") {
            webClient.createUser(userCreateRequest)
            it("IllegalArgumentException을 던진다.") {
                webClient.login(UserLoginRequest(NAME, "invalid_password"))
                    .expectStatus().isBadRequest
            }
        }
    }

    describe("유저 삭제 API는") {

        context("삭제할 유저의 id와 password가 주어지면,") {
            webClient.createUser(userCreateRequest)
            val userId = webClient.getUserId(NAME)

            it("유저의 정보를 삭제한다.") {
                webClient.deleteUser(userId, PASSWORD)
                    .expectStatus().isOk

                webClient.getUser(NAME)
                    .expectStatus().isBadRequest
            }
        }

        context("유저의 password가 일치하지 않으면,") {
            webClient.createUser(userCreateRequest)
            val userId = webClient.getUserId(NAME)
            val password = "invalid_password"

            it("IllegalArgumentException을 던진다.") {
                webClient.deleteUser(userId, password)
                    .expectStatus().isBadRequest
            }
        }

        context("저장되지 않은 유저의 id로 삭제 요청을 할 경우,") {
            webClient.createUser(userCreateRequest)
            val userId = 123L

            it("IllegalArgumentException을 던진다.") {
                webClient.deleteUser(userId, PASSWORD)
                    .expectStatus().isBadRequest
            }
        }
    }
}) {

    companion object {
        private const val NAME = "NAME_123"
        private const val PASSWORD = "12345678910"
        private val userCreateRequest = UserCreateRequest(NAME, PASSWORD)
        private val userLoginRequest = UserLoginRequest(NAME, PASSWORD)
    }
}
