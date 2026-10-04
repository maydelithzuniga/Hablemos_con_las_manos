package com.hablemosconlasmanos.api.security;

import com.hablemosconlasmanos.api.entity.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/** Emite los tokens JWT (HS256) que usa el panel de administracion. */
@Service
public class JwtService {

    public static final String ISSUER = "hablemos-con-las-manos";

    private final JwtEncoder jwtEncoder;
    private final long expiracionMinutos;

    public JwtService(JwtEncoder jwtEncoder, @Value("${app.jwt.expiracion-minutos}") long expiracionMinutos) {
        this.jwtEncoder = jwtEncoder;
        this.expiracionMinutos = expiracionMinutos;
    }

    public Token generar(Usuario usuario) {
        Instant ahora = Instant.now();
        Instant expira = ahora.plus(expiracionMinutos, ChronoUnit.MINUTES);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(ahora)
                .expiresAt(expira)
                .subject(usuario.getEmail())
                .claim("nombre", usuario.getNombre())
                .claim("roles", List.of(usuario.getRol().name()))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String valor = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new Token(valor, expira);
    }

    public record Token(String valor, Instant expiraEn) {
    }
}
