package com.bipul.note_taking_app.database.repository

import com.bipul.note_taking_app.database.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, ObjectId> {

    fun findByEmail(email: String): User?


}