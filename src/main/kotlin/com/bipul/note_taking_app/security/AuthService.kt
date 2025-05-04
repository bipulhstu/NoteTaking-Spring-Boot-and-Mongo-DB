package com.bipul.note_taking_app.security

import com.bipul.note_taking_app.database.model.User
import com.bipul.note_taking_app.database.repository.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service


@Service
class AuthService (
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder
){

    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    fun register(email: String, password: String) : User {
        return userRepository.save(
            User(
                email = email,
                hashedPassword = hashEncoder.encode(password)
            )
        )
    }

    fun login(email: String, password: String) : TokenPair{
        val user = userRepository.findByEmail(email)
            ?: throw BadCredentialsException("Invalid Credentials.")

        if(!hashEncoder.matches(password, user.hashedPassword)) {
            throw BadCredentialsException("Invalid Credentials.")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toString())

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }
}