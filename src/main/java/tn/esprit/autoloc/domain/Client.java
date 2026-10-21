package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "client")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"idClient", "reservations"})
@EqualsAndHashCode(exclude = {"idClient", "reservations"})
public class Client {
    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnore
    List<Reservation> reservations = new ArrayList<>();

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