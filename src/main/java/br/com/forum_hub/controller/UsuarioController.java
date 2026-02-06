package br.com.forum_hub.controller;

import br.com.forum_hub.domain.topico.DadosCadastroTopico;
import br.com.forum_hub.domain.topico.DadosListagemTopico;
import br.com.forum_hub.usuario.DadosCadastroUsuario;
import br.com.forum_hub.usuario.DadosListagemUsuario;
import br.com.forum_hub.usuario.Usuario;
import br.com.forum_hub.usuario.UsuarioService;
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

}
