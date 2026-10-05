package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "idEmployee")
@EqualsAndHashCode(exclude = "idEmployee")
public class Employee {

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