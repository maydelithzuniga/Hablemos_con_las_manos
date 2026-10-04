package com.hablemosconlasmanos.api.repository;

import com.hablemosconlasmanos.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    long countByRolAndActivoTrue(Rol rol);
}
