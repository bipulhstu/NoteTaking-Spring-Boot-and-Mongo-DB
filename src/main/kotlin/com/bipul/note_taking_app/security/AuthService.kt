package com.bipul.note_taking_app.security

import com.bipul.note_taking_app.database.model.User
import com.bipul.note_taking_app.database.repository.UserRepository
import org.springframework.stereotype.Service


@Service
class AuthService (
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder
){

    fun register(email: String, password: String) : User {
        return userRepository.save(
            User(
                email = email,
                hashedPassword = hashEncoder.encode(password)
            )
        )
    }
}