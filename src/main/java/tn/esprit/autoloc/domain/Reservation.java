package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "idReservation")
@EqualsAndHashCode(exclude = "idReservation")
public class Reservation {

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