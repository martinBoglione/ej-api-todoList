package org.model

data class User(val id: String, val email: String, val password: String, val notes: MutableList<Note> = mutableListOf())
data class Note(val id: String, var title: String, var description: String)
data class DraftNote(var title: String, var description: String)

class RepeatedException(msg: String): Exception(msg)
class NotFound(msg: String): Exception("Not found $msg")

class ToDoListSystem() {

    private val users = mutableListOf<User>()
    private val idGenerator = IdGenerator()

    fun addNote(userId: String, note: DraftNote) {
        val user = getUser(userId)
        checkIfTitleIsNotRepeated(user.notes, note.title)
        user.notes.add(Note(idGenerator.nextNoteId(), note.title, note.description))
    }

    fun editNote(userId: String, noteId: String, draftNote: DraftNote) {
        val note = getNote(userId, noteId)
        note.title = draftNote.title
        note.description = draftNote.description
    }

    fun removeNote(userId: String, noteId: String) {
        val user = getUser(userId)
        user.notes.removeIf { it.id == noteId }
    }

    private fun checkIfTitleIsNotRepeated(notes: MutableList<Note>, title: String) {
        if (notes.any { it.title == title }) throw RepeatedException("Found note with same title: $title")
    }

    fun getNote(userId: String, noteId: String): Note {
        val user = getUser(userId)
        return user.notes.find { it.id == noteId } ?: throw NotFound("Note")
    }

    fun getUser(userId: String): User = users.find { it.id == userId } ?: throw NotFound("User")

    fun login(email: String, password: String): User = users.find { it.email == email && it.password == password } ?: throw NotFound("User")

    fun register(email: String, password: String) : User {
        if (users.any { it.email == email }) throw RepeatedException("Email repeted")
        val user = User(idGenerator.nextUserId(), email, password)
        users.add(user)
        return user
    }
}

fun getToDoListSystem(): ToDoListSystem {
    val toDoListSystem = ToDoListSystem()
    val userJuan = toDoListSystem.register("juan@gmail.com", "juan")
    addNotes(toDoListSystem, userJuan.id)

    val userLean = toDoListSystem.register("lean@gmail.com", "lean")
    addNotes(toDoListSystem, userLean.id)

    val userJuli = toDoListSystem.register("juli@gmail.com", "juli")
    addNotes(toDoListSystem, userJuli.id)

    return toDoListSystem
}

fun addNotes(toDoListSystem: ToDoListSystem, userId: String) {
    for (i in 1..6) toDoListSystem.addNote(userId, DraftNote("$userId - note $i", "description $i"))
}