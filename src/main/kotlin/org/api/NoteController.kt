package org.api

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import org.model.DraftNote
import org.model.NotFound
import org.model.ToDoListSystem

data class OkResponse(val status: String = "Ok")
data class ErrorResponse(val message: String)

class NoteController(private val toDoListSystem: ToDoListSystem) {

    private fun getUserId(ctx: Context): String {
        return ctx.attribute<String>("userId") ?: throw BadRequestResponse("Not found user")
    }

    fun getNote(ctx: Context) {
        val userId = getUserId(ctx)
        val noteId = ctx.pathParam("id")
        try {
            ctx.json(toDoListSystem.getNote(userId, noteId))
        } catch (e: NotFound) {
            ctx.status(404).json(ErrorResponse(e.message!!))
        }
    }

    fun createNote(ctx: Context) {
        val userId = getUserId(ctx)
        val draftNote = ctx.body<DraftNote>()
        toDoListSystem.addNote(userId, draftNote)
        ctx.json(draftNote)
    }

    fun modifyNote(ctx: Context) {
        try {
            val userId = getUserId(ctx)
            val noteId = ctx.pathParam("id")
            val draftNote = ctx.body<DraftNote>()
            toDoListSystem.editNote(userId, noteId, draftNote)
            ctx.json(OkResponse())
        } catch (e: NotFound) {
            ctx.status(404).json(ErrorResponse(e.message!!))
        }
    }

    fun deleteNote(ctx: Context) {
        val userId = getUserId(ctx)
        val noteId = ctx.pathParam("id")
        toDoListSystem.removeNote(userId, noteId)
        ctx.json(OkResponse())
    }

}
