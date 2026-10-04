package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.UsuarioDTO;
import com.hablemosconlasmanos.api.dto.UsuarioRequest;
import com.hablemosconlasmanos.api.entity.Rol;
import com.hablemosconlasmanos.api.entity.Usuario;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.exception.ReglaNegocioException;
import com.hablemosconlasmanos.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll(Sort.by("nombre")).stream().map(UsuarioDTO::de).toList();
    }

    @Transactional
    public UsuarioDTO crear(UsuarioRequest r) {
        if (r.password() == null || r.password().isBlank()) {
            throw new IllegalArgumentException("La contrasena es obligatoria");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(r.email())) {
            throw new ReglaNegocioException("Ya existe un usuario con el email " + r.email());
        }
        Usuario u = Usuario.builder()
                .nombre(r.nombre())
                .email(r.email().trim().toLowerCase(Locale.ROOT))
                .password(passwordEncoder.encode(r.password()))
                .rol(r.rol())
                .activo(r.activo() == null || r.activo())
                .build();
        return UsuarioDTO.de(usuarioRepository.save(u));
    }

    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioRequest r) {
        Usuario u = buscar(id);
        usuarioRepository.findByEmailIgnoreCase(r.email())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new ReglaNegocioException("Ya existe un usuario con el email " + r.email());
                });
        boolean activo = r.activo() == null || r.activo();
        if (u.getRol() == Rol.ADMIN && u.isActivo() && (r.rol() != Rol.ADMIN || !activo)) {
            validarQuedaOtroAdmin();
        }
        u.setNombre(r.nombre());
        u.setEmail(r.email().trim().toLowerCase(Locale.ROOT));
        u.setRol(r.rol());
        u.setActivo(activo);
        if (r.password() != null && !r.password().isBlank()) {
            u.setPassword(passwordEncoder.encode(r.password()));
        }
        return UsuarioDTO.de(u);
    }

    @Transactional
    public void eliminar(Long id) {
        Usuario u = buscar(id);
        if (u.getRol() == Rol.ADMIN && u.isActivo()) {
            validarQuedaOtroAdmin();
        }
        usuarioRepository.delete(u);
    }

    /** Impide quedarse sin ningun administrador activo. */
    private void validarQuedaOtroAdmin() {
        if (usuarioRepository.countByRolAndActivoTrue(Rol.ADMIN) <= 1) {
            throw new ReglaNegocioException("Debe quedar al menos un administrador activo");
        }
    }

    private Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> RecursoNoEncontradoException.de("Usuario", id));
    }
}
