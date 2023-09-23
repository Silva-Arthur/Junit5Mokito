package com.devarthursilva.junit5mokito.resources;

import com.devarthursilva.junit5mokito.domain.Usuario;
import com.devarthursilva.junit5mokito.domain.dto.UsuarioDTO;
import com.devarthursilva.junit5mokito.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserResource.class)
public class UserResourceWebMvcTest {

    public static final Integer ID = 1;
    public static final String NAME = "Arthur Silva";
    public static final String EMAIL = "arthur@mail.com";
    public static final String PASSWORD = "123";
    public static final String URL = "http://localhost:8080/user";
    public static final String URL_BARRA = "http://localhost:8080/user/";

    private Usuario user = new Usuario();
    private UsuarioDTO userDTO = new UsuarioDTO();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean // usando quando o contexto da aplicação está ativo
    private UserService service;

    @MockBean
    private ModelMapper mapper;


    @BeforeEach
    void setUp() {
        startUser();
    }

    @Test
    void whenFindByIdThenReturnSuccess() throws Exception {
        when(
          service.findById(anyInt())
        ).thenReturn(user);

        when(
          mapper.map(any(), any())
        ).thenReturn(userDTO);

        MvcResult result = mockMvc.perform(get(URL_BARRA + ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(NAME))
                .andDo(print()).andReturn()
        ;

        UsuarioDTO userResponse = mapper.map(
                result.getResponse().getContentAsString(), // obtém o json do objeto de resposta
                UsuarioDTO.class);

        assertEquals(ID, userResponse.getId());
        assertEquals(NAME, userResponse.getName());
        assertEquals(EMAIL, userResponse.getEmail());
        assertEquals(PASSWORD, userResponse.getPassword());
    }

    @Test
    void whenFindAllThenResturnAListOfUsuarioDTO() throws Exception {
        when(
            service.findAll()
        ).thenReturn(List.of(user));

        when(
            mapper.map(any(), any())
        ).thenReturn(userDTO);

        MvcResult result = mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andDo(print())
                .andReturn()
        ;

        TypeReference<List<UsuarioDTO>> listReference = new TypeReference<List<UsuarioDTO>>() {};

        List<UsuarioDTO> usuarios = objectMapper.readValue(result.getResponse().getContentAsString(), listReference);

        assertEquals(ID, usuarios.get(0).getId());
        assertEquals(NAME, usuarios.get(0).getName());
        assertEquals(EMAIL, usuarios.get(0).getEmail());
    }

    @Test
    void whenCreateReturnStatusCreated() throws Exception {
        when(
            service.create(any())
        ).thenReturn(user);

        when(
            mapper.map(any(), any())
        ).thenReturn(userDTO);

        String jsonRequest = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(
                post(URL).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)
            )
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl(URL_BARRA + ID))
            .andDo(print())
        ;

    }

    private void startUser() {
        user = new Usuario(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UsuarioDTO(ID, NAME, EMAIL, PASSWORD);
    }
}
