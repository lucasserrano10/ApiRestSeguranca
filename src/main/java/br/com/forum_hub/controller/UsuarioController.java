package br.com.forum_hub.controller;

import br.com.forum_hub.domain.topico.DadosCadastroTopico;
import br.com.forum_hub.domain.topico.DadosListagemTopico;
import br.com.forum_hub.usuario.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping("/registrar")
    public ResponseEntity<DadosListagemUsuario> cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder){
        var usuario = usuarioService.cadastrar(dados);
        var uri = uriBuilder.path("/{nomeUsuario}").buildAndExpand(usuario.getNomeUsuario()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario.getId(), usuario.getNomeUsuario(), usuario.getNomeCompleto(), usuario.getNomeUsuario(), usuario.getMiniBiografia(), usuario.getBiografia()));
    }

    @GetMapping("/verificar-conta")
    public ResponseEntity<String> verificarEmail(@RequestParam String codigo){
        usuarioService.verificarEmail(codigo);
        return ResponseEntity.ok("Conta verificada com sucesso !");
    }

    @GetMapping("/{nomeUsuario}")
    public ResponseEntity<DadosListagemUsuario> buscarUsuario(@PathVariable String nomeUsuario){
        var usuario = usuarioService.buscarPorNomeUsuario(nomeUsuario);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario.getId(), usuario.getNomeUsuario(), usuario.getNomeCompleto(), usuario.getNomeUsuario(), usuario.getMiniBiografia(), usuario.getBiografia()));
    }

    @PutMapping("/editarPerfil")
    public ResponseEntity<DadosListagemUsuario> edicaoPerfil(@RequestBody @Valid DadosEdicaoUsuario dados, @AuthenticationPrincipal Usuario logado){
        var usuario = usuarioService.editarPerfil(dados,logado);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario.getId(), usuario.getNomeUsuario(), usuario.getNomeCompleto(), usuario.getNomeUsuario(), usuario.getMiniBiografia(), usuario.getBiografia()));
    }

    @PatchMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid DadosAlteracaoSenha dados, @AuthenticationPrincipal Usuario logado){
        usuarioService.alterarSenha(dados,logado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/desativar")
    public ResponseEntity<Void> desativarUsuario(@AuthenticationPrincipal Usuario logado){
        usuarioService.desativarUsuario(logado);
        return ResponseEntity.noContent().build();
    }
}
