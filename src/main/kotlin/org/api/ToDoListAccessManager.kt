package org.api

import io.javalin.core.security.AccessManager
import io.javalin.core.security.Role
import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.http.UnauthorizedResponse
import org.model.NotFound
import org.model.ToDoListSystem
import org.token.NotValidToken
import org.token.TokenController

class ToDoListAccessManager(val toDoListSystem: ToDoListSystem) : AccessManager {

    val tokenController = TokenController()

    override fun manage(handler: Handler, ctx: Context, roles: MutableSet<Role>) {
        val token = ctx.header("Authorization")
        when {
            roles.contains(ToDoRoles.ANYONE) -> handler.handle(ctx)
            token === null -> throw UnauthorizedResponse()
            roles.contains(ToDoRoles.USER) -> {
                try {
                    val userId = tokenController.validateToken(token)
                    toDoListSystem.getUser(userId)
                    ctx.attribute("userId", userId)
                    handler.handle(ctx)
                } catch (e: NotValidToken) {
                    throw UnauthorizedResponse("Not valid Token")
                } catch (e: NotFound) {
                    throw UnauthorizedResponse("Not valid Token")
                }
            }
        }
    }
}
