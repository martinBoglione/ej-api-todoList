package org.model

class IdGenerator {
    var userId = 0
        private set
    var noteId = 0
        private set
    fun nextNoteId(): String = "n_${++noteId}"
    fun nextUserId(): String = "u_${++userId}"
}