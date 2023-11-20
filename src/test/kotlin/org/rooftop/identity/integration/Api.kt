package org.rooftop.identity.integration

import org.rooftop.api.identity.*
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

private const val VERSION = "/v1"

internal fun WebTestClient.createUser(
    body: UserCreateReq,
): WebTestClient.ResponseSpec {
    return this.post()
        .uri("$VERSION/users")
        .contentType(MediaType.APPLICATION_PROTOBUF)
        .bodyValue(body)
        .exchange()
}

internal fun WebTestClient.getUserId(
    name: String,
): Long {
    return getUser(name)
        .expectBody(UserGetRes::class.java)
        .returnResult()
        .responseBody?.id ?: throw IllegalStateException("Test fail cause responseBody is null")
}

internal fun WebTestClient.getUser(
    name: String,
): WebTestClient.ResponseSpec {
    return this.get()
        .uri("$VERSION/users?name=$name")
        .accept(MediaType.APPLICATION_PROTOBUF)
        .exchange()
}

internal fun WebTestClient.updateUser(
    body: UserUpdateReq,
): WebTestClient.ResponseSpec {
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
): WebTestClient.ResponseSpec {
    return this.post()
        .uri("$VERSION/logins")
        .contentType(MediaType.APPLICATION_PROTOBUF)
        .accept(MediaType.APPLICATION_PROTOBUF)
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

internal fun WebTestClient.auth(token: String, requesterId: Long): WebTestClient.ResponseSpec {
    return this.get()
        .uri("$VERSION/auths")
        .header(HttpHeaders.AUTHORIZATION, token)
        .header("RequesterId", requesterId.toString())
        .exchange()
}
