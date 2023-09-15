package com.devarthursilva.junit5mokito.services.impl;

import com.devarthursilva.junit5mokito.domain.Usuario;
import com.devarthursilva.junit5mokito.repositories.UserRepository;
import com.devarthursilva.junit5mokito.services.UserService;
import com.devarthursilva.junit5mokito.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public Usuario findById(Integer id) {
        Optional<Usuario> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }
}
