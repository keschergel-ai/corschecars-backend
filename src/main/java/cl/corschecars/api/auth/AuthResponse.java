package cl.corschecars.api.auth;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String email,
        String role) {
}
