package br.unipar.backend.trab1.repository;

import br.unipar.backend.trab1.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
}