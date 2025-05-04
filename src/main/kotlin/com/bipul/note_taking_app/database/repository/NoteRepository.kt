package com.bipul.note_taking_app.database.repository

import com.bipul.note_taking_app.database.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository : MongoRepository<Note, ObjectId>{

    fun findByOwnerId(ownerId: ObjectId): List<Note>



}
