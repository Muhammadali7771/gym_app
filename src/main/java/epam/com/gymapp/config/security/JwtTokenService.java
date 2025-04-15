package epam.com.gymapp.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenService {
   public static final String SECRET_KEY = "7134743777217A25432A462D4A614E645267556B58703272357538782F413F44";

   public String generateToken(String username) {
       return Jwts.builder()
               .subject(username)
               .issuer("https://gym.com")
               .issuedAt(new Date())
               .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
               .signWith(signKey())
               .compact();
   }

   public boolean isValid(String token) {
       try {
           Claims claims = getClaims(token);
           Date expiration = claims.getExpiration();
           return expiration.after(new Date());
       } catch (Exception e) {
           e.printStackTrace();
       }
       return false;
   }

   public String getUsername(String token) {
       Claims claims = getClaims(token);
       return claims.getSubject();
   }

   private Claims getClaims(String token) {
       return Jwts.parser()
               .setSigningKey(signKey())
               .build()
               .parseSignedClaims(token)
               .getPayload();
   }

   private Key signKey(){
       byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
       return Keys.hmacShaKeyFor(bytes);
   }
}
