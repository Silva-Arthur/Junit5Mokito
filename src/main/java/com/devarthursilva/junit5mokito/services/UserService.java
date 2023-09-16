package com.devarthursilva.junit5mokito.services;

import com.devarthursilva.junit5mokito.domain.Usuario;
import com.devarthursilva.junit5mokito.domain.dto.UsuarioDTO;

import java.util.List;

public interface UserService {

    Usuario findById(Integer id);

    List<Usuario> findAll();

    Usuario create(UsuarioDTO obj);

    Usuario update(UsuarioDTO obj);
}
