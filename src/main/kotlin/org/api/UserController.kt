package org.api

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import org.model.NotFound
import org.model.RepeatedException
import org.model.ToDoListSystem
import org.model.User
import org.token.TokenController

data class UserLoginDTO(val email: String, val password: String)
data class UserRegisterDTO(val name: String, val email: String, val password: String)

class UserDTO(user: User) {
    val email = user.email
    val notes = user.notes
}

class UserController(private val toDoListSystem: ToDoListSystem) {

    val tokenController = TokenController()

    fun login(ctx: Context) {
        val userLogin = ctx.body<UserLoginDTO>()
        try {
            val user = toDoListSystem.login(userLogin.email, userLogin.password)
            ctx.header("Authorization", tokenController.genereteToken(user))
            ctx.json(UserDTO(user))
        } catch (e: NotFound) {
            throw BadRequestResponse()
        }
    }

    fun register(ctx: Context) {
        val userRegister = ctx.body<UserRegisterDTO>()
        try {
            val user = toDoListSystem.register(userRegister.email, userRegister.password)
            ctx.header("Authorization", tokenController.genereteToken(user))
            ctx.json(UserDTO(user))
        } catch (e: RepeatedException) {
            throw BadRequestResponse()
        }
    }
}