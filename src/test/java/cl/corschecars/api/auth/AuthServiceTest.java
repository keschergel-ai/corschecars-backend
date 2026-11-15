package cl.corschecars.api.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AppUserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private AuthService service;

    @Test
    void registerNormalizesEmailAndStoresEncodedPassword() {
        when(repository.existsByEmail("client@example.com")).thenReturn(false);
        when(passwordEncoder.encode("SecurePass123")).thenReturn("encoded-password");
        when(repository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AuthResponse expected = new AuthResponse("token", "Bearer", 3600, "client@example.com", "USER");
        when(jwtTokenService.issueFor(any(AppUser.class))).thenReturn(expected);

        AuthResponse actual = service.register(new RegisterRequest(" Client@Example.com ", "SecurePass123"));

        ArgumentCaptor<AppUser> savedUser = ArgumentCaptor.forClass(AppUser.class);
        verify(repository).save(savedUser.capture());
        assertEquals("client@example.com", savedUser.getValue().getEmail());
        assertEquals("encoded-password", savedUser.getValue().getPasswordHash());
        assertEquals(UserRole.USER, savedUser.getValue().getRole());
        assertEquals(expected, actual);
    }

    @Test
    void registerRejectsAnExistingEmail() {
        when(repository.existsByEmail("client@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyRegisteredException.class,
                () -> service.register(new RegisterRequest("client@example.com", "SecurePass123")));

        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void loginRejectsAnIncorrectPassword() {
        AppUser user = new AppUser("client@example.com", "encoded-password", UserRole.USER);
        when(repository.findByEmail("client@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("WrongPass123", "encoded-password")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> service.login(new LoginRequest("client@example.com", "WrongPass123")));

        verify(jwtTokenService, never()).issueFor(any(AppUser.class));
    }
}
