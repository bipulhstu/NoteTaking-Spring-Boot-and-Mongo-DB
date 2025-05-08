package com.bipul.note_taking_app.database.model

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.Indexed
import java.time.Instant

@Document("refresh_tokens")
data class RefreshToken(
    val userId: ObjectId,
    @Indexed(expireAfter = "1s")
    val expiresAt: Instant,
    val hashedToken: String,
    val createdAt: Instant = Instant.now()
)
