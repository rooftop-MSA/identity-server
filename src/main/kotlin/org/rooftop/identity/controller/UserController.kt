package org.rooftop.identity.controller

import org.rooftop.identity.domain.UserUsecase
import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserLoginRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
import org.rooftop.identity.domain.response.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
internal class UserController(private val userUsecase: UserUsecase) {

    @GetMapping("/v1/users")
    @ResponseStatus(HttpStatus.OK)
    fun getUser(@RequestParam("name") name: String): Mono<UserResponse> =
        userUsecase.getByName(name)

    @PostMapping("/v1/users")
    @ResponseStatus(HttpStatus.OK)
    fun createUser(@RequestBody request: UserCreateRequest): Mono<Unit> =
        userUsecase.createUser(request)

    @PutMapping("/v1/users")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(@RequestBody request: UserUpdateRequest): Mono<Unit> =
        userUsecase.updateUser(request)

    @DeleteMapping("/v1/users")
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(
        @RequestHeader("id") id: Long,
        @RequestHeader("password") password: String,
    ): Mono<Unit> = userUsecase.deleteUser(id, password)

    @PostMapping("/v1/logins")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody request: UserLoginRequest): Mono<Map<String, String>> =
        userUsecase.login(request).map { mapOf("token" to it) }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ErrorTemplate =
        ErrorTemplate(exception.message!!)

}
