package org.api.model

data class Note(val id: String, var title: String, var description: String)
data class DraftNote(var title: String, var description: String)

class IdGenerator {
    var noteId = 0
        private set
    fun nextNoteId(): String = "n_${++noteId}"
}

class RepeatedException(msg: String): Exception(msg)
class NotFound(msg: String): Exception("Not found $msg")

class ToDoListSystem() {

    val notes = mutableListOf<Note>()
    val idGenerator = IdGenerator()

    fun addNote(note: DraftNote) {
        checkIfTitleIsNotRepeted(note.title)
        notes.add(Note(idGenerator.nextNoteId(), note.title, note.description))
    }

    fun editNote(noteId: String, draftNote: DraftNote) {
        val note = getNote(noteId)
        note.title = draftNote.title
        note.description = draftNote.description
    }

    fun removeNote(noteId: String) {
        notes.removeIf { it.id == noteId }
    }

    private fun checkIfTitleIsNotRepeted(title: String) {
        if (notes.any { it.title == title }) throw RepeatedException("Found note with same title: $title")
    }

    fun getNote(noteId: String): Note = notes.find { it.id == noteId } ?: throw NotFound("Note")
}

fun getToDoListSystem(): ToDoListSystem {
    val toDoListSystem = ToDoListSystem()
    toDoListSystem.addNote(DraftNote("note 1", "description 1"))
    toDoListSystem.addNote(DraftNote("note 2", "description 2"))
    toDoListSystem.addNote(DraftNote("note 3", "description 3"))
    toDoListSystem.addNote(DraftNote("note 4", "description 4"))
    toDoListSystem.addNote(DraftNote("note 5", "description 5"))
    toDoListSystem.addNote(DraftNote("note 6", "description 6"))
    return toDoListSystem
}