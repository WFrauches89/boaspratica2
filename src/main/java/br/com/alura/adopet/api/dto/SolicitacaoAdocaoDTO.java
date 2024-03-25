package br.com.alura.adopet.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitacaoAdocaoDTO(@NotNull Long idTutor,
                                   @NotNull Long idPet,
                                   @NotBlank String motivo) {
}
