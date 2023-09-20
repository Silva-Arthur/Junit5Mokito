package com.devarthursilva.junit5mokito.services.impl;

import com.devarthursilva.junit5mokito.domain.Usuario;
import com.devarthursilva.junit5mokito.domain.dto.UsuarioDTO;
import com.devarthursilva.junit5mokito.repositories.UserRepository;
import com.devarthursilva.junit5mokito.services.exceptions.DataIntegratyViolationException;
import com.devarthursilva.junit5mokito.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserServiceImplTest {

    public static final Integer ID = 1;
    public static final String NAME = "Arthur Silva";
    public static final String EMAIL = "arthur@mail.com";
    public static final String PASSWORD = "123";
    public static final String OBJETO_NAO_ENCONTRADO = "Objeto não encontrado";
    public static final int INDEX = 0;
    public static final String E_MAIL_JA_CADASTRADO = "E-mail já cadastrado!";

    @InjectMocks // cria uma instancia real de usuarioservice, porém os outros mocks, ele vai criar de "mentira"
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    @Mock
    private ModelMapper mapper;

    private Usuario user;
    private UsuarioDTO userDTO;
    private Optional<Usuario> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void whenFindByIdThenReturnUsuarioInstance() {
        //mokar a informação
        when( // quando
            repository.findById( //este método for chamado
                    anyInt() //com qualquer Integer
            )
        ).thenReturn(optionalUser); // retorne meu fake optionalUser

        // realizar a requisicao que foi mokada acima
        Usuario response = service.findById(ID); // realiza a requeisicao

        // realizar as validações para o TESTE
        assertNotNull(response); // verifica se a resposta nao é nula
        assertEquals(Usuario.class, response.getClass()); // verifica se a classe de retorno é a esperada
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
    }

    @Test
    void whenFindByIdThenReturnAnObjectNotfountException() {
        when(
            repository.findById(anyInt())
        ).thenThrow(new ObjectNotFoundException(OBJETO_NAO_ENCONTRADO));

        try {
            service.findById(ID);
        } catch (ObjectNotFoundException ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals(OBJETO_NAO_ENCONTRADO, ex.getMessage());
        }
    }


    @Test
    void whenFindAllThenReturnAnListOfUsers() {
        when(
            repository.findAll()
        ).thenReturn(List.of(user));

        List<Usuario> response = service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(Usuario.class, response.get(INDEX).getClass());
        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(NAME, response.get(INDEX).getName());
        assertEquals(EMAIL, response.get(INDEX).getEmail());
        assertEquals(PASSWORD, response.get(INDEX).getPassword());
    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(
            repository.save(any())
        ).thenReturn(user);

        Usuario response = service.create(userDTO);

        assertNotNull(response);
        assertEquals(Usuario.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void whenCreateThenReturnAnDataIntegrityViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);

        try {
            optionalUser.get().setId(2);
            service.create(userDTO);
        } catch (DataIntegratyViolationException ex) {
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals(E_MAIL_JA_CADASTRADO, ex.getMessage());
        }
    }

    @Test
    void whenUdateThenReturnSuccess() {
        when(
                repository.save(any())
        ).thenReturn(user);

        Usuario response = service.update(userDTO);

        assertNotNull(response);
        assertEquals(Usuario.class, response.getClass());
        assertEquals(ID, response.getId());
        assertEquals(NAME, response.getName());
        assertEquals(EMAIL, response.getEmail());
        assertEquals(PASSWORD, response.getPassword());
    }

    @Test
    void whenUpdateThenReturnAnDataIntegrityViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);

        try {
            optionalUser.get().setId(2);
            service.update(userDTO);
        } catch (DataIntegratyViolationException ex) {
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals(E_MAIL_JA_CADASTRADO, ex.getMessage());
        }
    }

    @Test
    void delete() {
    }

    private void startUser() {
        user = new Usuario(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UsuarioDTO(ID, NAME, EMAIL, PASSWORD);
        optionalUser = Optional.of(new Usuario(ID, NAME, EMAIL, PASSWORD));
    }
}