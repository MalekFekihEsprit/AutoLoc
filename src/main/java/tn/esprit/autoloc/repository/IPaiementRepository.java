package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.*;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface IPaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByModePaiement(ModePaiement mode);
    List<Paiement> findByMontantGreaterThan(BigDecimal montant);
    List<Paiement> findByContratIdContrat(Long idContrat);
    @Modifying
    @Transactional
    @Query("UPDATE Paiement p SET p.montant = p.montant * :factor WHERE p.modePaiement = :mode")
    int applyDiscount(@Param("factor") BigDecimal factor, @Param("mode") ModePaiement mode);
}
