package org.rooftop.identity.integration

import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserLoginRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
import org.rooftop.identity.domain.response.UserResponse
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

private const val VERSION = "/v1"

internal fun WebTestClient.createUser(
    body: UserCreateRequest,
): WebTestClient.ResponseSpec {
    return this.post()
        .uri("$VERSION/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .exchange()
}

internal fun WebTestClient.getUserId(
    name: String,
): Long {
    return getUser(name)
        .expectBody(UserResponse::class.java)
        .returnResult()
        .responseBody?.id ?: throw IllegalStateException("Test fail cause responseBody is null")
}

internal fun WebTestClient.getUser(
    name: String,
): WebTestClient.ResponseSpec {
    return this.get()
        .uri("$VERSION/users?name=$name")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
}

internal fun WebTestClient.updateUser(
    body: UserUpdateRequest,
): WebTestClient.ResponseSpec {
    return this.put()
        .uri("$VERSION/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .exchange()
}

internal fun WebTestClient.login(
    body: UserLoginRequest,
): WebTestClient.ResponseSpec {
    return this.post()
        .uri("$VERSION/logins")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(body)
        .exchange()
}

internal fun WebTestClient.deleteUser(
    id: Long,
    password: String,
): WebTestClient.ResponseSpec {
    return this.delete()
        .uri("$VERSION/users")
        .headers {
            it["id"] = id.toString()
            it["password"] = password
        }
        .exchange()
}
