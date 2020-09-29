import io.javalin.Javalin
import io.javalin.core.util.RouteOverviewPlugin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.BadRequestResponse
import org.api.ErrorResponse
import org.api.NoteController
import org.api.model.getToDoListSystem

class ToDoListApi {

    fun start() {
        val app = Javalin.create {
            it.defaultContentType = "application/json"
            it.registerPlugin(RouteOverviewPlugin("/routes"))
        }

        val toDoListSystem = getToDoListSystem()
        val noteController = NoteController(toDoListSystem)

        app.routes {
            path("notes") {
                post(noteController::createNote)
                path(":id") {
                    get(noteController::getNote)
                    put(noteController::modifyNote)
                    delete(noteController::deleteNote)
                }
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