package br.com.forum_hub.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosAlteracaoSenha(@NotBlank String senhaAtual,
                                  @NotBlank String novaSenha,
                                  @NotBlank String novaSenhaConfirmacao) {
}
