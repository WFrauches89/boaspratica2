package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.AprovacaoSolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.ReprovacaoSolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.service.AdocaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    @Autowired
    private AdocaoService adocaoService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> solicitar(@RequestBody @Valid SolicitacaoAdocaoDTO dto) {
        try{
            this.adocaoService.solicitar(dto);
            return ResponseEntity.ok("Adoção solicitada com sucesso!");
        }catch (ValidacaoException v){
            return ResponseEntity.badRequest().body(v.getMessage());
        }
    }

    @PutMapping("/aprovar")
    @Transactional
    public ResponseEntity<String> aprovar(@RequestBody @Valid AprovacaoSolicitacaoAdocaoDTO dto) {
        this.adocaoService.aprovar(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reprovar")
    @Transactional
    public ResponseEntity<String> reprovar(@RequestBody @Valid ReprovacaoSolicitacaoAdocaoDTO dto) {
        this.adocaoService.reprovar(dto);
        return ResponseEntity.ok().build();
    }

}
