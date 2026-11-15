package cl.corschecars.api.auth;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthService(
            AppUserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException();
        }

        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new PasswordTooLongException();
        }

        AppUser user = repository.save(new AppUser(
                email,
                passwordEncoder.encode(request.password()),
                UserRole.USER));
        return jwtTokenService.issueFor(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        AppUser user = repository.findByEmail(email).orElseThrow(InvalidCredentialsException::new);

        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72
                || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return jwtTokenService.issueFor(user);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
