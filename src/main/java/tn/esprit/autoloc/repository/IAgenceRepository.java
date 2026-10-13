package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.*;
import java.util.List;

public interface IAgenceRepository extends JpaRepository<Agence, Long> {
    List<Agence> findByVille(String ville);
    List<Agence> findByNomContainingIgnoreCase(String nom);
    boolean existsByNom(String nom);
}