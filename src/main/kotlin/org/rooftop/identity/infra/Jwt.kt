package org.rooftop.identity.infra

import io.jsonwebtoken.Jwts
import org.rooftop.identity.domain.Token
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
internal class Jwt : Token {

    override fun getToken(id: Long): String = Jwts.builder()
        .issuer(ISSUER)
        .subject(SUBJECT)
        .expiration(neverExpiration)
        .claim(USER_ID, id)
        .signWith(key)
        .compact()

    override fun getId(token: String): Long = Jwts.parser()
        .verifyWith(key)
        .requireIssuer(ISSUER)
        .requireSubject(SUBJECT)
        .build()
        .parseSignedClaims(token)
        .payload[USER_ID].toString().toLong()

    companion object {
        private const val ISSUER = "rooftop-msa:identity-server"
        private const val SUBJECT = "user-jwt"
        private const val USER_ID = "id"

        private val key = Jwts.SIG.HS256.key().build();
        private val neverExpiration: Date = Date.from(Instant.now().plus(30, ChronoUnit.DAYS))
    }
}
