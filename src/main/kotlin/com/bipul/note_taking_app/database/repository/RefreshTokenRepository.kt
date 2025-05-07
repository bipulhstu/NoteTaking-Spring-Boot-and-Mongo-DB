package com.bipul.note_taking_app.database.repository

import com.bipul.note_taking_app.database.model.RefreshToken
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository: MongoRepository<RefreshToken, ObjectId> {

    fun findByUserIdAndHashedToken(userId: ObjectId, hashedToken: String): RefreshToken?

    fun deleteTokenByUserIdAndHashedToken(userId: ObjectId, hashedToken: String)
}