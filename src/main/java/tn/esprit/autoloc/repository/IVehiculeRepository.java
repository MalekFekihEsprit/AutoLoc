package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface IVehiculeRepository extends JpaRepository<Vehicule, Long> {
    List<Vehicule> findByMarque(String marque);
    List<Vehicule> findByStatut(StatutVehicule statut);
    Optional<Vehicule> findByImmatriculation(String immatriculation);
    List<Vehicule> findByTarifJournalierLessThan(BigDecimal max);
    List<Vehicule> findByCategorieAndStatut(CategorieVehicule categorie, StatutVehicule statut);
    boolean existsByImmatriculation(String immatriculation);
    
    @Query("SELECT v FROM Vehicule v WHERE v.tarifJournalier BETWEEN :min AND :max")
    List<Vehicule> findVehiculesInPriceRange(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
}