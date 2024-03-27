package br.com.alura.adopet.api.validacao;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PetAvaliacaoAndamento implements Validacoes{

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Override
    public void validar(SolicitacaoAdocaoDTO dto) {

        var petAguardandoAvaliacao = adocaoRepository.existsByPetIdAndStatus(dto.idPet(), StatusAdocao.AGUARDANDO_AVALIACAO);

        if (petAguardandoAvaliacao) {
                throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
        }

    }
}
