package com.bipul.note_taking_app.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}") private val jwtSecret: String
) {


    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))

     val accessTokenValidityMs = 15L * 60L * 1000L //15 Minutes
     val refreshTokenValidityMs = 30L * 24L * 30L * 1000L // 30 Days

    private fun generateToken(
        userId: String,
        type: String,
        expiry: Long
    ) : String{
        val now = Date()
        val expiryDate = Date(now.time + expiry)

        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId: String) : String{
        return generateToken(userId, "access", accessTokenValidityMs)
    }

    fun generateRefreshToken(userId: String) : String{
        return generateToken(userId, "refresh", refreshTokenValidityMs)
    }


    fun validateAccessToken(token: String) : Boolean{
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] ?: return false
        return tokenType == "access"
    }

    fun validateRefreshToken(token: String) : Boolean{
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] ?: return false
        return tokenType == "refresh"
    }


    // Authorization: Bearer <token>
    fun getUserIdFromToken(token: String) : String{
        val claims = parseAllClaims(token) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(401),
            "Invalid Token."
        )
        return claims.subject
    }

    private fun parseAllClaims(token: String) : Claims? {
        val rawToken = if(token.startsWith("Bearer ")){
            token.removePrefix("Bearer ")
        } else token

        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload

        } catch (e: Exception){
            null
        }
    }

}