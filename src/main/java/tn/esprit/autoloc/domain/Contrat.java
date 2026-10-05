package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrat")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "idContrat")
@EqualsAndHashCode(exclude = "idContrat")
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long idContrat;

    @Column(nullable = false)
    LocalDate dateSignature;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal montantTotal;

    @Column(nullable = false)
    Boolean valide;
}