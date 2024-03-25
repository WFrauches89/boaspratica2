package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.AprovacaoSolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.ReprovacaoSolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoExcetion;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdocaoService {

    @Autowired
    private AdocaoRepository repository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private List<Validacoes> validacoes;


    public void solicitar(SolicitacaoAdocaoDTO dto) {
        Tutor tutor = tutorRepository.getReferenceById(dto.idTutor());

        Pet pet = petRepository.getReferenceById(dto.idPet());

        validacoes.forEach(v -> v.validar(dto));

        Adocao adocao = new Adocao();
        adocao.setData(LocalDateTime.now());
        adocao.setStatus(StatusAdocao.AGUARDANDO_AVALIACAO);
        adocao.setPet(pet);
        adocao.setTutor(tutor);
        adocao.setMotivo(dto.motivo());
        repository.save(adocao);

        String to = adocao.getPet().getAbrigo().getEmail();
        String subject = "Solicitação de adoção";
        String message = "Olá "
                + adocao.getPet().getAbrigo().getNome()
                + "!\n\nUma solicitação de adoção foi registrada hoje para o pet: "
                + adocao.getPet().getNome()
                + ". \nFavor avaliar para aprovação ou reprovação.";

        emailService.sendEmail(to, subject, message);


    }

    public void aprovar(AprovacaoSolicitacaoAdocaoDTO dto) {

        Adocao adocao = repository.getReferenceById(dto.idAdocao());
        adocao.setStatus(StatusAdocao.APROVADO);
        repository.save(adocao);

        String to = adocao.getTutor().getEmail();
        String subject = "Adoção aprovada";
        String message = "Parabéns "
                +adocao.getTutor().getNome() +"!\n\nSua adoção do pet "
                +adocao.getPet().getNome() +", solicitada em "
                +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                +", foi aprovada.\nFavor entrar em contato com o abrigo "
                +adocao.getPet().getAbrigo().getNome()
                +" para agendar a busca do seu pet.";

        emailService.sendEmail(to, subject, message);
    }

    public void reprovar(ReprovacaoSolicitacaoAdocaoDTO dto) {

        Adocao adocao = repository.getReferenceById(dto.idAdocao());
        adocao.setStatus(StatusAdocao.REPROVADO);
        adocao.setJustificativaStatus(dto.justificativa());
        repository.save(adocao);

        String to = adocao.getTutor().getEmail();
        String subject = "Adoção reprovada";
        String message = "Olá "
                +adocao.getTutor().getNome()
                +"!\n\nInfelizmente sua adoção do pet "
                +adocao.getPet().getNome()
                +", solicitada em " +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                +", foi reprovada pelo abrigo "
                +adocao.getPet().getAbrigo().getNome()
                +" com a seguinte justificativa: "
                +adocao.getJustificativaStatus();

        emailService.sendEmail(to, subject, message);
    }


}
