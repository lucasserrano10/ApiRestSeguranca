package br.com.forum_hub.controller;

import br.com.forum_hub.domain.autenticacao.*;
import br.com.forum_hub.usuario.Usuario;
import br.com.forum_hub.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<DadosToken> efetuarLogin(@Valid @RequestBody DadosLogin dados){
        var authToken = new UsernamePasswordAuthenticationToken(dados.email(),dados.password());
        var authentication = authenticationManager.authenticate(authToken);
        String token = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        String refreshToken = tokenService.gerarRefreshToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosToken(token,refreshToken));
    }

    @PostMapping("/atualizar-token")
    public ResponseEntity<DadosToken> atualizarToken(@Valid @RequestBody DadosRefreshToken dados){
        var refreshToken = dados.refreshToken();
        Long idUsuario = Long.valueOf(tokenService.verificarToken(refreshToken));
        var usuario = usuarioRepository.findById(idUsuario).orElseThrow();

        String token = tokenService.gerarToken(usuario);
        String novoRefreshToken = tokenService.gerarRefreshToken(usuario);
        return ResponseEntity.ok(new DadosToken(token,novoRefreshToken));
    }

}
