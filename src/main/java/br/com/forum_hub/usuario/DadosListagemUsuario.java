package br.com.forum_hub.usuario;

public record DadosListagemUsuario(
    Long id,
    String email,
    String nomeCompleto,
    String nomeUsuario,
    String miniBiografia,
    String biografia
) {
    public DadosListagemUsuario(Long id, String email, String nomeCompleto, String nomeUsuario, String miniBiografia, String biografia) {
        this.id = id;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.miniBiografia = miniBiografia;
        this.biografia = biografia;
    }
}
