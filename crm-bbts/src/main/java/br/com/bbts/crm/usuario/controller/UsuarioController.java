package br.com.bbts.crm.usuario.controller;

import br.com.bbts.crm.usuario.dto.AlterarSenhaRequest;
import br.com.bbts.crm.usuario.dto.CriarUsuarioRequest;
import br.com.bbts.crm.usuario.dto.UsuarioDTO;
import br.com.bbts.crm.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /** Apenas GERENTE/ADMIN. */
    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioService.listar().stream().map(UsuarioDTO::from).toList();
    }

    @GetMapping("/{id}")
    public UsuarioDTO obter(@PathVariable Long id) {
        return UsuarioDTO.from(usuarioService.obter(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO criar(@Valid @RequestBody CriarUsuarioRequest req) {
        return UsuarioDTO.from(usuarioService.criar(req));
    }

    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        usuarioService.desativar(id);
    }

    @PostMapping("/alterar-senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@AuthenticationPrincipal UserDetails user,
                             @Valid @RequestBody AlterarSenhaRequest req) {
        var usuario = usuarioService.listar().stream()
                .filter(u -> u.getUsername().equals(user.getUsername()))
                .findFirst().orElseThrow();
        usuarioService.alterarSenha(usuario.getId(), req.senhaAtual(), req.novaSenha());
    }

    /** Usado pelo front para confirmar operações sensíveis com senha do vendedor (item 7.2). */
    @PostMapping("/verificar-senha")
    public Map<String, Boolean> verificarSenha(@AuthenticationPrincipal UserDetails user,
                                                @RequestBody Map<String, String> body) {
        boolean ok = usuarioService.verificarSenha(user.getUsername(), body.get("senha"));
        return Map.of("valido", ok);
    }
}
