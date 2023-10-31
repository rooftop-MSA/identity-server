package org.rooftop.identity.controller

import org.rooftop.identity.domain.UserUsecase
import org.rooftop.identity.domain.request.UserCreateRequest
import org.rooftop.identity.domain.request.UserDeleteRequest
import org.rooftop.identity.domain.request.UserLoginRequest
import org.rooftop.identity.domain.request.UserUpdateRequest
import org.rooftop.identity.domain.response.UserResponse
import org.springframework.web.bind.annotation.*

@RestController
internal class UserController(private val userUsecase: UserUsecase) {

//    @GetMapping("/users")
//    fun getUser(@RequestParam("name") name: String): UserResponse = userUsecase.getByName(name)
//
//    @PostMapping("/users")
//    fun createUser(@RequestBody request: UserCreateRequest) = userUsecase.createUser(request)
//
//    @PutMapping("/users")
//    fun updateUser(@RequestBody request: UserUpdateRequest) = userUsecase.updateUser(request)
//
//    @DeleteMapping("/users")
//    fun deleteUser(@RequestBody request: UserDeleteRequest) = userUsecase.deleteUser(request)
//
//    @PostMapping("/logins")
//    fun login(@RequestBody request: UserLoginRequest): Map<String, String> =
//        mapOf("token" to userUsecase.login(request))
}
