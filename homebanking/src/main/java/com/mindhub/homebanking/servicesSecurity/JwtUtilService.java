package com.mindhub.homebanking.servicesSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service // Sera un servicio que cumplira con la generacion del token
public class JwtUtilService {

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build(); //Devuelve un token  firmado con la clave proporcionada

    private static final long EXPIRATION_TOKEN = 1000 * 60 * 60;  //1000 milisegundos x 60 segundos x 60 minutos = 1 hora

    public Claims extractAllClaims(String token){ //Va a verificar un token utilizando una clave secreta, luego va a extraer y devolver las claims del token verificado, que seria el payload
        return Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token); // aca tenemos todos los clains del token
        return claimsTFunction.apply(claims); // nos retorna mediante la claimsFunction un claim en particular que le pasemos como parametro
    }

    public String extractUserName(String token) { return extractClaim(token, Claims::getSubject);} // obtiene el claim

    public Date extractExpiration(String token){ return extractClaim(token, Claims::getExpiration); }

    public Boolean isTokenExpired(String token) { return extractExpiration(token).before(new Date()); } // si la fecha de vencimiento es antes que el dia de hoy, si es true esta expirado si es false esta vigente

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TOKEN))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String generateToken(UserDetails userDetails){ //utiliza como parametro el User que se declara en UserDetails
        Map<String, Object> claims = new HashMap<>();//la clave será de tipo string que será el "rol" y el valor será el rol que nosotros vamos a obtener del userDatails
        String rol = userDetails.getAuthorities().iterator().next().getAuthority();
        claims.put("rol", rol);
        return createToken(claims, userDetails.getUsername());
    }

}