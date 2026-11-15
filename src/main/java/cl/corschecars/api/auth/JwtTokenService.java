package cl.corschecars.api.auth;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {
    private final JwtEncoder encoder;
    private final long expirationSeconds;

    public JwtTokenService(
            JwtEncoder encoder,
            @Value("${app.jwt.expiration-seconds:3600}") long expirationSeconds) {
        this.encoder = encoder;
        this.expirationSeconds = expirationSeconds;
    }

    public AuthResponse issueFor(AppUser user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(expirationSeconds);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("https://api.corschecars.local")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(user.getEmail())
                .claim("roles", List.of("ROLE_" + user.getRole().name()))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new AuthResponse(token, "Bearer", expirationSeconds, user.getEmail(), user.getRole().name());
    }
}
