package com.hablemosconlasmanos.api.service;

import com.hablemosconlasmanos.api.dto.CambioPasswordRequest;
import com.hablemosconlasmanos.api.dto.LoginRequest;
import com.hablemosconlasmanos.api.dto.TokenDTO;
import com.hablemosconlasmanos.api.dto.UsuarioDTO;
import com.hablemosconlasmanos.api.entity.Usuario;
import com.hablemosconlasmanos.api.exception.RecursoNoEncontradoException;
import com.hablemosconlasmanos.api.repository.UsuarioRepository;
import com.hablemosconlasmanos.api.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    /** Hash ficticio para que el tiempo de respuesta no revele si el email existe. */
    private final String hashFicticio;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.hashFicticio = passwordEncoder.encode(UUID.randomUUID().toString());
    }

    public TokenDTO login(LoginRequest r) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(r.email().trim()).orElse(null);
        boolean valido = passwordEncoder.matches(r.password(), usuario != null ? usuario.getPassword() : hashFicticio);
        if (usuario == null || !valido || !usuario.isActivo()) {
            throw new BadCredentialsException("Email o contrasena incorrectos");
        }
        JwtService.Token token = jwtService.generar(usuario);
        return new TokenDTO(token.valor(), "Bearer", token.expiraEn(), UsuarioDTO.de(usuario));
    }

    public UsuarioDTO perfil(String email) {
        return UsuarioDTO.de(buscarPorEmail(email));
    }

    @Transactional
    public void cambiarPassword(String email, CambioPasswordRequest r) {
        Usuario usuario = buscarPorEmail(email);
        if (!passwordEncoder.matches(r.passwordActual(), usuario.getPassword())) {
            throw new BadCredentialsException("La contrasena actual no es correcta");
        }
        usuario.setPassword(passwordEncoder.encode(r.passwordNueva()));
    }

    private Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> RecursoNoEncontradoException.de("Usuario", email));
    }
}
