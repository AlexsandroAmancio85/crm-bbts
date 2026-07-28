package br.com.bbts.crm.usuario.service;

import br.com.bbts.crm.common.enums.PerfilUsuario;
import br.com.bbts.crm.exception.BusinessException;
import br.com.bbts.crm.exception.ResourceNotFoundException;
import br.com.bbts.crm.usuario.dto.CriarUsuarioRequest;
import br.com.bbts.crm.usuario.entity.Usuario;
import br.com.bbts.crm.usuario.repository.UsuarioRepository;
import br.com.bbts.crm.vendedor.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VendedorRepository vendedorRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obter(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.of("Usuário", id));
    }

    @Transactional
    public Usuario criar(CriarUsuarioRequest req) {
        if (usuarioRepository.findByUsername(req.username()).isPresent()) {
            throw new BusinessException("Já existe um usuário com o username '" + req.username() + "'.");
        }
        var builder = Usuario.builder()
                .nome(req.nome()).username(req.username())
                .senha(passwordEncoder.encode(req.senha()))
                .perfil(req.perfil()).ativo(true);

        if (req.perfil() == PerfilUsuario.VENDEDOR && req.vendedorId() != null) {
            var vendedor = vendedorRepository.findById(req.vendedorId())
                    .orElseThrow(() -> ResourceNotFoundException.of("Vendedor", req.vendedorId()));
            builder.vendedor(vendedor);
        }
        return usuarioRepository.save(builder.build());
    }

    @Transactional
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = obter(id);
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new BusinessException("Senha atual incorreta.");
        }
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public boolean verificarSenha(String username, String senha) {
        return usuarioRepository.findByUsername(username)
                .map(u -> passwordEncoder.matches(senha, u.getSenha()))
                .orElse(false);
    }

    @Transactional
    public void desativar(Long id) {
        Usuario u = obter(id);
        u.setAtivo(false);
        usuarioRepository.save(u);
    }
}
