package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatut(StatutReservation statut);
    List<Reservation> findByDateDebutBetween(LocalDate from, LocalDate to);
    List<Reservation> findByClientIdClient(Long idClient);
    long countByStatut(StatutReservation statut);

    @Query("SELECT r FROM Reservation r WHERE r.client.nom = :nom")
    List<Reservation> findReservationsByClientNom(@Param("nom") String nom);
}