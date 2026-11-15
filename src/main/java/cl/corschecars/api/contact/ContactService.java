package cl.corschecars.api.contact;

import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {
    private final ContactMessageRepository repository;

    public ContactService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ContactResponse create(ContactRequest request) {
        ContactMessage message = new ContactMessage(
                request.nombre().trim(),
                request.email().trim().toLowerCase(Locale.ROOT),
                request.mensaje().trim());
        ContactMessage saved = repository.save(message);
        return new ContactResponse(saved.getId(), saved.getCreatedAt());
    }
}
