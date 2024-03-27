package br.com.alura.adopet.api.repository;

import br.com.alura.adopet.api.dto.PetDTO;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByAdotadoFalse();

    List<Pet> findByAbrigo(Abrigo abrigo);
}
