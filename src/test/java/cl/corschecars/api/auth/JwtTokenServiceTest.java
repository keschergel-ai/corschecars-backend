package cl.corschecars.api.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

class JwtTokenServiceTest {
    @Test
    void issuedTokenCanBeDecodedWithExpectedSubjectAndRole() {
        byte[] keyBytes = new byte[32];
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");
        JwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));
        JwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        JwtTokenService service = new JwtTokenService(encoder, 3600);

        AuthResponse response = service.issueFor(
                new AppUser("client@example.com", "encoded-password", UserRole.USER));

        var decoded = decoder.decode(response.accessToken());
        assertEquals("client@example.com", decoded.getSubject());
        assertEquals("https://api.corschecars.local", decoded.getIssuer().toString());
        assertEquals(List.of("ROLE_USER"), decoded.getClaimAsStringList("roles"));
        assertEquals(3600, response.expiresIn());
        assertNotNull(decoded.getExpiresAt());
        assertTrue(decoded.getExpiresAt().isAfter(decoded.getIssuedAt()));
    }
}
