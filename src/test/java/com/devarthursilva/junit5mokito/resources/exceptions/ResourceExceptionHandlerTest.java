package com.devarthursilva.junit5mokito.resources.exceptions;

import com.devarthursilva.junit5mokito.services.exceptions.DataIntegratyViolationException;
import com.devarthursilva.junit5mokito.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
class ResourceExceptionHandlerTest {

    public static final String OBJETO_NAO_ECONTRADO = "Objeto não econtrado!";
    public static final String E_MAIL_JA_CADASTRADO = "E-mail já cadastrado!";
    public static final String USER_2 = "/user-2";
    @InjectMocks
    private ResourceExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenObjectNotFountThenReturnAResponseEntity() {
        ResponseEntity<StandardError> response =
                exceptionHandler.objectNotFount(
                        new ObjectNotFoundException(OBJETO_NAO_ECONTRADO),
                        new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertEquals(OBJETO_NAO_ECONTRADO, response.getBody().getError());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertNotEquals(USER_2, response.getBody().getPath());
    }

    @Test
    void whenDataIntegrityViolationExceptionReturnAResponseEntity() {
        ResponseEntity<StandardError> response =
                exceptionHandler.dataIntegrityViolationException(
                        new DataIntegratyViolationException(E_MAIL_JA_CADASTRADO),
                        new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertEquals(E_MAIL_JA_CADASTRADO, response.getBody().getError());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertNotEquals(USER_2, response.getBody().getPath());
        assertNotEquals(LocalDateTime.now(), response.getBody().getTimestamp());
    }
}