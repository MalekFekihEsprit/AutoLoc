package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "client")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "idClient")
@EqualsAndHashCode(exclude = "idClient")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long idClient;

    @Column(nullable = false, length = 50)
    String nom;

    @Column(nullable = false, length = 50)
    String prenom;

    @Column(nullable = false, unique = true, length = 100)
    String email;

    @Column(length = 20)
    String telephone;

    @Column(nullable = false, unique = true, length = 30)
    String numPermis;

    @Column(nullable = false)
    LocalDate dateInscription;
}