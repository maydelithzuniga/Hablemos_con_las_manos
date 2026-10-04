package com.hablemosconlasmanos.api.dto;

import java.time.Instant;

public record TokenDTO(String token, String tipo, Instant expiraEn, UsuarioDTO usuario) {
}
