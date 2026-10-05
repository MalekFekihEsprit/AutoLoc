package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "idVehicule")
@EqualsAndHashCode(exclude = "idVehicule")
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long idVehicule;

    @Column(nullable = false, unique = true, length = 20)
    String immatriculation;

    @Column(nullable = false, length = 50)
    String marque;

    @Column(nullable = false, length = 50)
    String modele;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    CategorieVehicule categorie;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal tarifJournalier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    StatutVehicule statut;
}