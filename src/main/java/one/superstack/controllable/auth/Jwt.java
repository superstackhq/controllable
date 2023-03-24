package one.superstack.controllable.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import one.superstack.controllable.util.Json;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class Jwt {

    private final String jwtSecretKey;

    public Jwt(@Value("${jwt.secret.key}") String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public AuthenticatedActor getActor(String token) {
        try {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
            Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());

            return (AuthenticatedActor) Json.decode(
                    Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody().getSubject(),
                    AuthenticatedActor.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getToken(AuthenticatedActor actor) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder().setSubject(Json.encode(actor))
                .setIssuedAt(new Date())
                .signWith(signatureAlgorithm, signingKey).compact();
    }
}