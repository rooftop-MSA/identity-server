package org.rooftop.identity.integration

import org.rooftop.api.identity.*
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec

private const val VERSION = "/v1"

internal fun WebTestClient.createUser(
    body: UserCreateReq,
): ResponseSpec {
    return this.post()
        .uri("$VERSION/users")
        .contentType(MediaType.APPLICATION_PROTOBUF)
        .bodyValue(body)
        .exchange()
}

internal fun WebTestClient.getUserById(
    id: Long,
): ResponseSpec {
    return this.get()
        .uri("$VERSION/users/$id")
        .exchange()
}

internal fun WebTestClient.getUserId(
    name: String,
): Long {
    return getUser(name)
        .expectBody(UserGetByNameRes::class.java)
        .returnResult()
        .responseBody?.id ?: throw IllegalStateException("Test fail cause responseBody is null")
}

internal fun WebTestClient.getUser(
    name: String,
): ResponseSpec {
    return this.get()
        .uri("$VERSION/users?name=$name")
        .exchange()
}

internal fun WebTestClient.updateUser(
    body: UserUpdateReq,
): ResponseSpec {
    return this.put()
        .uri("$VERSION/users")
        .contentType(MediaType.APPLICATION_PROTOBUF)
        .bodyValue(body)
        .exchange()
}

internal fun WebTestClient.loginAndGetToken(
    body: UserLoginReq,
): String {
    return login(body)
        .expectBody(UserLoginRes::class.java)
        .returnResult()
        .responseBody?.token ?: throw IllegalStateException("Test fail cause responseBody is null")
}

internal fun WebTestClient.login(
    body: UserLoginReq,
): ResponseSpec {
    return this.post()
        .uri("$VERSION/logins")
        .contentType(MediaType.APPLICATION_PROTOBUF)
        .bodyValue(body)
        .exchange()
}

internal fun WebTestClient.deleteUser(
    id: Long,
    password: String,
): ResponseSpec {
    return this.delete()
        .uri("$VERSION/users")
        .headers {
            it["id"] = id.toString()
            it["password"] = password
        }
        .exchange()
}

internal fun WebTestClient.auth(token: String, requesterId: Long): ResponseSpec {
    return this.get()
        .uri("$VERSION/auths")
        .header(HttpHeaders.AUTHORIZATION, token)
        .header("RequesterId", requesterId.toString())
        .exchange()
}

internal fun WebTestClient.getUserByToken(token: String): ResponseSpec {
    return this.get()
        .uri("$VERSION/users/tokens")
        .header(HttpHeaders.AUTHORIZATION, token)
        .exchange()
}
