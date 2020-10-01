import org.api.ToDoListAccessManager
import io.javalin.Javalin
import io.javalin.core.util.RouteOverviewPlugin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.security.Role
import io.javalin.http.BadRequestResponse
import org.api.ErrorResponse
import org.api.NoteController
import org.api.UserController
import org.model.getToDoListSystem

enum class ToDoRoles: Role {
    ANYONE, USER
}

class ToDoListApi {

    fun start() {
        val toDoListSystem = getToDoListSystem()
        val noteController = NoteController(toDoListSystem)
        val userController = UserController(toDoListSystem)

        val app = Javalin.create {
            it.defaultContentType = "application/json"
            it.registerPlugin(RouteOverviewPlugin("/routes"))
            it.accessManager(ToDoListAccessManager(toDoListSystem))
        }

        app.routes {
            path("notes") {
                post(noteController::createNote, setOf(ToDoRoles.USER))
                path(":id") {
                    get(noteController::getNote, setOf(ToDoRoles.USER))
                    put(noteController::modifyNote, setOf(ToDoRoles.USER))
                    delete(noteController::deleteNote, setOf(ToDoRoles.USER))
                }
            }
            path("login") {
                post(userController::login, setOf(ToDoRoles.ANYONE))
            }
            path("register") {
                post(userController::register, setOf(ToDoRoles.ANYONE))
            }
        }

        app.exception(BadRequestResponse::class.java) { e, ctx ->
            ctx.status(400).json(ErrorResponse("Not valid"))
        }

        app.start(7000)
    }
}

fun main(args: Array<String>) {
    ToDoListApi().start()
}