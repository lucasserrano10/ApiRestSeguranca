package br.com.forum_hub.usuario;

import br.com.forum_hub.domain.perfil.DadosPerfil;
import br.com.forum_hub.domain.perfil.PerfilNome;
import br.com.forum_hub.domain.perfil.PerfilRepository;
import br.com.forum_hub.infra.email.EmailService;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final PerfilRepository perfilRepository;


    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, EmailService emailService, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.perfilRepository = perfilRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCaseAndVerificadoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }

    public Usuario buscarPorNomeUsuario(String nomeUsuario) {
        return usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario cadastrar(@Valid DadosCadastroUsuario dados) {
        var senhaCriptografada = passwordEncoder.encode(dados.senha());
        var perfil = perfilRepository.findByNome(PerfilNome.ESTUDANTE);
        var usuario = new Usuario(dados, senhaCriptografada, perfil);
//        emailService.enviarEmailVerificacao(usuario);
        usuarioRepository.save(usuario);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void verificarEmail(String codigo) {
        var usuario = usuarioRepository.findByToken(codigo).orElseThrow();
        usuario.verificar();
    }

    public Usuario editarPerfil(@Valid DadosEdicaoUsuario dados, Usuario logado) {
        return logado.alterarDados(dados);
    }

    public void alterarSenha(@Valid DadosAlteracaoSenha dados, Usuario logado) {
        if(!passwordEncoder.matches(dados.senhaAtual(), logado.getPassword())){
            throw new RegraDeNegocioException("Senha digitada não confere com senha atual!");
        }

        if(!dados.novaSenha().equals(dados.novaSenhaConfirmacao())){
            throw new RegraDeNegocioException("Senha e confirmação não conferem!");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(dados.novaSenha());
        logado.alterarSenha(novaSenhaCriptografada)
;
    }

    public void desativarUsuario(Usuario logado) {
        logado.desativar();
    }

    @Transactional
    public Usuario adicionarPerfil(Long id, DadosPerfil dados){
        var usuario = usuarioRepository.findById(id).orElseThrow();
        var perfil = perfilRepository.findByNome(dados.perfilNome());
        usuario.adicionarPerfil(perfil);

        return usuario;
    }
}
