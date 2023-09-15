package com.devarthursilva.junit5mokito.config;

import com.devarthursilva.junit5mokito.domain.Usuario;
import com.devarthursilva.junit5mokito.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("local")
public class LocalConfig {

    @Autowired
    private UserRepository repository;

    @Bean
    public void startDB() {
        Usuario u1 = new Usuario(null, "Arthur Silva", "arthur@mail.com", "123");
        Usuario u2 = new Usuario(null, "Beatriz Silva", "beatriz@mail.com", "123");

        repository.saveAll(List.of(u1, u2));
    }
}
