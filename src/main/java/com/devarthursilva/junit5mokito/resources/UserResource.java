package com.devarthursilva.junit5mokito.resources;

import com.devarthursilva.junit5mokito.domain.Usuario;
import com.devarthursilva.junit5mokito.domain.dto.UsuarioDTO;
import com.devarthursilva.junit5mokito.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
public class UserResource {

    public static final String ID = "/{id}";

    @Autowired
    private UserService service;

    @Autowired
    private ModelMapper mapper;

    @GetMapping(value = ID)
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(mapper.map(service.findById(id), UsuarioDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        return ResponseEntity.ok().body(
                service.findAll().stream().map(u -> mapper.map(u, UsuarioDTO.class)).collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@RequestBody UsuarioDTO obj) {
        Usuario usuario = service.create(obj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path(ID).buildAndExpand(
                usuario.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = ID)
    public ResponseEntity<UsuarioDTO> update(@PathVariable Integer id, @RequestBody UsuarioDTO obj) {
        obj.setId(id);
        return ResponseEntity.ok().body(mapper.map(service.update(obj), UsuarioDTO.class));
    }

    @DeleteMapping(value = ID)
    public ResponseEntity<UsuarioDTO> delete(@PathVariable Integer id) {
        service.findById(id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}