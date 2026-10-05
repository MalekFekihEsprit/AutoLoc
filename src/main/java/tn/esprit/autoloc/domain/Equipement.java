package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "idEquipement")
@EqualsAndHashCode(exclude = "idEquipement")
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long idEquipement;

    @Column(nullable = false, length = 100)
    String libelle;
}