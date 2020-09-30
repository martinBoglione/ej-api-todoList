package org.api

import io.javalin.http.Context
import org.model.DraftNote
import org.model.NotFound
import org.model.ToDoListSystem

data class OkResponse(val status: String = "Ok")
data class ErrorResponse(val message: String)

class NoteController(private val toDoListSystem: ToDoListSystem) {

    val userId = "u_1"

    fun getNote(ctx: Context) {
        val noteId = ctx.pathParam("id")
        try {
            ctx.json(toDoListSystem.getNote(userId, noteId))
        } catch (e: NotFound) {
            ctx.status(404).json(ErrorResponse(e.message!!))
        }
        
    }

    fun createNote(ctx: Context) {
        val draftNote = ctx.body<DraftNote>()
        toDoListSystem.addNote(userId, draftNote)
        ctx.json(draftNote)
    }

    fun modifyNote(ctx: Context) {
        try {
            val noteId = ctx.pathParam("id")
            val draftNote = ctx.body<DraftNote>()
            toDoListSystem.editNote(userId, noteId, draftNote)
            ctx.json(OkResponse())
        } catch (e: NotFound) {
            ctx.status(404).json(ErrorResponse(e.message!!))
        }
    }

    fun deleteNote(ctx: Context) {
        val noteId = ctx.pathParam("id")
        toDoListSystem.removeNote(userId, noteId)
        ctx.json(OkResponse())
    }

}
