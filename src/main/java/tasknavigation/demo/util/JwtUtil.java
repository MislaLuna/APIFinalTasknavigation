package tasknavigation.demo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // A chave deve ter pelo menos 256 bits (32 bytes)
    private static final String SECRET_KEY = "mIChAvEsEgReTa123!@#xYz456$%789AbcD";


    // Tempo de expiração: 24 horas
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // Gera a chave de assinatura
    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Gera o token JWT com o email como subject
    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Valida o token e retorna o email, ou null se inválido/expirado
    public static String validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
