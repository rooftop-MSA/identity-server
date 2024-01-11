package org.rooftop.identity.controller

import org.rooftop.api.identity.*
import org.rooftop.identity.domain.account.UserUsecase
import org.rooftop.identity.domain.account.request.UserCreateRequest
import org.rooftop.identity.domain.account.request.UserLoginRequest
import org.rooftop.identity.domain.account.request.UserUpdateRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
internal class UserController(private val userUsecase: UserUsecase) {

    @GetMapping("/v1/users")
    @ResponseStatus(HttpStatus.OK)
    fun getUserByName(@RequestParam("name") name: String): Mono<UserGetByNameRes> {
        return userUsecase.getByName(name)
            .map { response ->
                userGetByNameRes {
                    this.id = response.id
                    this.name = response.name
                }
            }
    }

    @GetMapping("/v1/users/{id}")
    fun getUserById(@PathVariable("id") id: Long): Mono<UserGetByIdRes> {
        return userUsecase.getById(id)
            .map {
                userGetByIdRes {
                    this.id = it.id
                    this.name = it.name
                }
            }
    }

    @PostMapping("/v1/users")
    @ResponseStatus(HttpStatus.OK)
    fun createUser(@RequestBody request: UserCreateReq): Mono<Unit> {
        return userUsecase.createUser(
            UserCreateRequest(
                request.name,
                request.userName,
                request.password
            )
        )
    }

    @PutMapping("/v1/users")
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(@RequestBody request: UserUpdateReq): Mono<Unit> {
        return userUsecase.updateUser(
            UserUpdateRequest(
                request.id,
                request.newName,
                request.newUserName,
                request.newPassword
            )
        )
    }

    @DeleteMapping("/v1/users")
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(
        @RequestHeader("id") id: Long,
        @RequestHeader("password") password: String,
    ): Mono<Unit> = userUsecase.deleteUser(id, password)

    @GetMapping("/v1/users/tokens")
    @ResponseStatus(HttpStatus.OK)
    fun getUserByToken(
        @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
    ): Mono<UserGetByTokenRes> = userUsecase.getByToken(token)
        .map { userResponse ->
            userGetByTokenRes {
                this.id = userResponse.id
                this.name = userResponse.name
            }
        }

    @PostMapping("/v1/logins")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody request: UserLoginReq): Mono<UserLoginRes> {
        return userUsecase.login(
            UserLoginRequest(
                request.name,
                request.password
            )
        ).map {
            userLoginRes {
                token = it
            }
        }
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    private fun handleIllegalArgumentException(exception: IllegalArgumentException): Mono<ErrorRes> =
        Mono.just(errorRes { message = exception.message!! })
}
