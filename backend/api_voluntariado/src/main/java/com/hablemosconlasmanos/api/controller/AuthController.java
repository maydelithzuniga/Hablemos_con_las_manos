package com.hablemosconlasmanos.api.controller;

import com.hablemosconlasmanos.api.dto.CambioPasswordRequest;
import com.hablemosconlasmanos.api.dto.LoginRequest;
import com.hablemosconlasmanos.api.dto.TokenDTO;
import com.hablemosconlasmanos.api.dto.UsuarioDTO;
import com.hablemosconlasmanos.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** Login del panel de administracion: devuelve el token JWT. */
    @PostMapping("/login")
    public TokenDTO login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/yo")
    public UsuarioDTO yo(@AuthenticationPrincipal Jwt jwt) {
        return authService.perfil(jwt.getSubject());
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cambiarPassword(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CambioPasswordRequest request) {
        authService.cambiarPassword(jwt.getSubject(), request);
    }
}
