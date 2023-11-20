package org.rooftop.identity.controller

import org.rooftop.api.identity.*
import org.rooftop.identity.domain.UserUsecase
import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserLoginRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
internal class UserController(private val userUsecase: UserUsecase) {

    @GetMapping(value = ["/v1/users"])
    @ResponseStatus(HttpStatus.OK)
    fun getUser(@RequestParam("name") name: String): UserGetRes {
        val userResponse = userUsecase.getByName(name)

        return userGetRes {
            this.id = userResponse.id
            this.name = userResponse.name
        }
    }

    @PostMapping(value = ["/v1/users"])
    @ResponseStatus(HttpStatus.OK)
    fun createUser(@RequestBody request: UserCreateReq) {
        return userUsecase.createUser(
            UserCreateRequest(
                request.name,
                request.userName,
                request.password
            )
        )
    }

    @PutMapping(value = ["/v1/users"])
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(@RequestBody request: UserUpdateReq) {
        userUsecase.updateUser(
            UserUpdateRequest(
                request.id,
                request.newName,
                request.newUserName,
                request.newPassword
            )
        )
    }

    @DeleteMapping(value = ["/v1/users"])
    @ResponseStatus(HttpStatus.OK)
    fun deleteUser(
        @RequestHeader("id") id: Long,
        @RequestHeader("password") password: String,
    ) = userUsecase.deleteUser(id, password)

    @PostMapping(value = ["/v1/logins"])
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestBody request: UserLoginReq): UserLoginRes {
        val token = userUsecase.login(
            UserLoginRequest(
                request.name,
                request.password
            )
        )
        return userLoginRes {
            this.token = token
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    private fun handleIllegalArgumentException(exception: IllegalArgumentException): ErrorRes =
        errorRes { message = exception.message!! }
}
