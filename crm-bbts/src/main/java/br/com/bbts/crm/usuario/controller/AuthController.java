package br.com.bbts.crm.usuario.controller;

import br.com.bbts.crm.security.JwtService;
import br.com.bbts.crm.usuario.dto.LoginRequest;
import br.com.bbts.crm.usuario.dto.LoginResponse;
import br.com.bbts.crm.usuario.entity.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.senha()));
        Usuario usuario = (Usuario) auth.getPrincipal();
        String token = jwtService.gerarToken(usuario);
        return ResponseEntity.ok(new LoginResponse(
                token, "Bearer", usuario.getUsername(), usuario.getNome(),
                usuario.getPerfil().name(), jwtService.getExpirationMs()));
    }
}
