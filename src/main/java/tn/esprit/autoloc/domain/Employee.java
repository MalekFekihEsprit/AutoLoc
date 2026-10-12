package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"idEmployee", "agence"})
@EqualsAndHashCode(exclude = {"idEmployee", "agence"})
public class Employee {
    @ManyToOne(fetch = FetchType.LAZY)
    Agence agence;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long idEmployee;

    @Column(nullable = false, length = 50)
    String nom;

    @Column(nullable = false, length = 50)
    String prenom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    RoleEmployee role;
}