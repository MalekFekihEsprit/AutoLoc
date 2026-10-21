package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "paiement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"idPaiement", "contrat"})
@EqualsAndHashCode(exclude = {"idPaiement", "contrat"})
public class Paiement {
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("contrat-paiements")
    Contrat contrat;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long idPaiement;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal montant;

    @Column(nullable = false)
    LocalDate datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    ModePaiement modePaiement;
}