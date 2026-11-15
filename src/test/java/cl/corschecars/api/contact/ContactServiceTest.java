package cl.corschecars.api.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {
    @Mock
    private ContactMessageRepository repository;

    @InjectMocks
    private ContactService service;

    @Test
    void createTrimsFieldsNormalizesEmailAndPersistsMessage() {
        when(repository.save(any(ContactMessage.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ContactResponse response = service.create(
                new ContactRequest(" Ana Pérez ", " ANA@EXAMPLE.COM ", " Necesito información. "));

        ArgumentCaptor<ContactMessage> savedMessage = ArgumentCaptor.forClass(ContactMessage.class);
        verify(repository).save(savedMessage.capture());
        assertEquals("Ana Pérez", savedMessage.getValue().getNombre());
        assertEquals("ana@example.com", savedMessage.getValue().getEmail());
        assertEquals("Necesito información.", savedMessage.getValue().getMensaje());
        assertNotNull(response.receivedAt());
    }
}
