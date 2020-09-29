package org.api

import io.javalin.http.Context
import org.api.model.DraftNote
import org.api.model.NotFound
import org.api.model.ToDoListSystem

data class OkResponse(val status: String = "Ok")
data class ErrorResponse(val message: String)

class NoteController(private val toDoListSystem: ToDoListSystem) {

    fun getNote(ctx: Context) {
        val noteId = ctx.pathParam("id")
        try {
            ctx.json(toDoListSystem.getNote(noteId))
        } catch (e: NotFound) {
            ctx.status(404).json(ErrorResponse(e.message!!))
        }
    }

    fun createNote(ctx: Context) {
        val draftNote = ctx.body<DraftNote>()
        toDoListSystem.addNote(draftNote)
        ctx.json(draftNote)
    }

    fun modifyNote(ctx: Context) {
        try {
            val noteId = ctx.pathParam("id")
            val draftNote = ctx.body<DraftNote>()
            toDoListSystem.editNote(noteId, draftNote)
            ctx.json(OkResponse())
        } catch (e: NotFound) {
            ctx.status(404).json(ErrorResponse(e.message!!))
        }
    }

    fun deleteNote(ctx: Context) {
        val noteId = ctx.pathParam("id")
        toDoListSystem.removeNote(noteId)
        ctx.json(OkResponse())
    }

}
