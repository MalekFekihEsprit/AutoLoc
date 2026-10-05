package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "idAgence")
@EqualsAndHashCode(exclude = "idAgence")
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long idAgence;

    @Column(nullable = false, length = 50)
    String nom;

    @Column(nullable = false, length = 50)
    String ville;

    @Column(nullable = false, length = 150)
    String adresse;

    @Column(nullable = false, length = 20)
    String telephone;
}