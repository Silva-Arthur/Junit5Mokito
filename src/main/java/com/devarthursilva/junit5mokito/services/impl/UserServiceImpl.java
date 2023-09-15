package com.devarthursilva.junit5mokito.services.impl;

import com.devarthursilva.junit5mokito.domain.Usuario;
import com.devarthursilva.junit5mokito.domain.dto.UsuarioDTO;
import com.devarthursilva.junit5mokito.repositories.UserRepository;
import com.devarthursilva.junit5mokito.services.UserService;
import com.devarthursilva.junit5mokito.services.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Usuario findById(Integer id) {
        Optional<Usuario> obj = repository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll();
    }

    @Override
    public Usuario create(UsuarioDTO obj) {
        return repository.save(mapper.map(obj, Usuario.class));
    }
}
