package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reservation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"idReservation", "client", "vehicule", "contrat"})
@EqualsAndHashCode(exclude = {"idReservation", "client", "vehicule", "contrat"})
public class Reservation {
    @ManyToOne(fetch = FetchType.LAZY)
    Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    Vehicule vehicule;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    Contrat contrat;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    Long idReservation;

    @Column(nullable = false)
    LocalDate dateDebut;

    @Column(nullable = false)
    LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    StatutReservation statut;
}