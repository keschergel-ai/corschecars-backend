package cl.corschecars.api.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfiguration {
    @Bean
    SecretKey jwtSigningKey(@Value("${app.jwt.secret}") String encodedSecret) {
        final byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(encodedSecret);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("APP_JWT_SECRET debe ser una clave Base64 válida.", exception);
        }

        if (keyBytes.length < 32) {
            throw new IllegalStateException("APP_JWT_SECRET debe contener al menos 32 bytes aleatorios codificados en Base64.");
        }

        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    @Bean
    JwtEncoder jwtEncoder(SecretKey jwtSigningKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSigningKey));
    }

    @Bean
    JwtDecoder jwtDecoder(SecretKey jwtSigningKey) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(jwtSigningKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("https://api.corschecars.local"));
        return decoder;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
