package microservice.user.reactive.login.domain.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DniValidationServiceTest {
    private final DniValidationService service = new DniValidationService();

    @Test
    void isValidDni_Success() {
        assertTrue(service.isValidDni("1020304050"));
    }

    @Test
    void isValidDni_ShouldFail_WhenStartsWithZero() {
        assertFalse(service.isValidDni("0123456789"));
    }

    @Test
    void isValidDni_ShouldFail_WhenTooShort() {
        assertFalse(service.isValidDni("12345"));
    }
}
