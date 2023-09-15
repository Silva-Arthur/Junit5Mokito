package com.devarthursilva.junit5mokito.services;

import com.devarthursilva.junit5mokito.domain.Usuario;

public interface UserService {

    Usuario findById(Integer id);
}
