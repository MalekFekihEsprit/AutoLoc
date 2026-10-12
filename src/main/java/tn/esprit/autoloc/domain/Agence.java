package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"idAgence", "vehicules", "employees"})
@EqualsAndHashCode(exclude = {"idAgence", "vehicules", "employees"})
public class Agence {
    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    List<Vehicule> vehicules = new ArrayList<>();

    @OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)
    List<Employee> employees = new ArrayList<>();

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