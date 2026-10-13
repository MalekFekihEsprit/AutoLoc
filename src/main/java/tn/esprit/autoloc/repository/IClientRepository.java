package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    List<Client> findByNomAndPrenom(String nom, String prenom);
    List<Client> findByNomContainingIgnoreCase(String fragment);
    boolean existsByNumPermis(String numPermis);
    long countByDateInscriptionAfter(LocalDate date);
}