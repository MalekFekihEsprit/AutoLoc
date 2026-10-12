package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"idMaintenance", "vehicule"})
@EqualsAndHashCode(exclude = {"idMaintenance", "vehicule"})
public class Maintenance {
    @ManyToOne(fetch = FetchType.LAZY)
    Vehicule vehicule;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long idMaintenance;

    @Column(nullable = false)
    LocalDate dateDebut;

    LocalDate dateFin;

    @Column(length = 255)
    String description;
}