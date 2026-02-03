package br.com.forum_hub.usuario;

public record DadosListagemUsuario(
    Long id,
    String email,
    String nomeCompleto,
    String nomeUsuario,
    String miniBiografia,
    String biografia
) {
}
