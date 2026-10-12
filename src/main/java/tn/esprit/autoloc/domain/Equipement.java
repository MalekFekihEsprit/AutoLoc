package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "equipement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"idEquipement", "vehicules"})
@EqualsAndHashCode(exclude = {"idEquipement", "vehicules"})
public class Equipement {
    @ManyToMany(mappedBy = "equipements", fetch = FetchType.LAZY)
    Set<Vehicule> vehicules = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long idEquipement;

    @Column(nullable = false, length = 100)
    String libelle;
}