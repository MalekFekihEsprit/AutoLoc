package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"idVehicule", "agence", "equipements", "maintenances"})
@EqualsAndHashCode(exclude = {"idVehicule", "agence", "equipements", "maintenances"})
public class Vehicule {
    @ManyToOne(fetch = FetchType.LAZY)
    Agence agence;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "vehicule_equipement",
            joinColumns = @JoinColumn(name = "vehicule_id_vehicule"),
            inverseJoinColumns = @JoinColumn(name = "equipement_id_equipement")
    )
    Set<Equipement> equipements = new HashSet<>();

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    List<Maintenance> maintenances = new ArrayList<>();

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