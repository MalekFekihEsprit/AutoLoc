# Stratégies de fetch et de cascade — Modèle AutoLoc

## Principe général
Toutes les associations sont configurées en **FetchType.LAZY** afin d'éviter le
chargement en cascade de graphes d'objets volumineux. Les cascades ne sont
appliquées que lorsqu'un enfant ne peut pas exister sans son parent
(relation de composition).

## Tableau récapitulatif

| Association            | Fetch | Cascade | Justification |
|------------------------|---|---|---|
| Contrat → Paiement     | LAZY | ALL + orphanRemoval | Un paiement n'existe que rattaché à son contrat (composition). Supprimer le contrat supprime ses paiements ; retirer un paiement de la collection le supprime aussi. |
| Agence → Vehicule      | LAZY | aucune | Un véhicule survit à la suppression de son agence (il peut être réaffecté). |
| Agence → Employee      | LAZY | aucune | Un employé peut être muté dans une autre agence ; sa survie est indépendante. |
| Vehicule ↔ Equipement  | LAZY | aucune | Les équipements sont partagés entre plusieurs véhicules : pas de notion de propriété unique. |
| Client → Reservation   | LAZY | PERSIST | Enregistrer un nouveau client avec ses réservations en une seule opération. Pas de REMOVE : supprimer un client ne doit pas effacer l'historique des réservations. |
| Reservation → Vehicule | LAZY | aucune | La réservation référence un véhicule existant, mais le véhicule ne dépend pas de la réservation. |
| Reservation ↔ Contrat  | LAZY | ALL (côté Reservation) | Un contrat est créé pour une réservation donnée ; supprimer la réservation entraîne la suppression du contrat associé. |
| Vehicule → Maintenance | LAZY | PERSIST | Enregistrer un véhicule avec ses maintenances à venir en une seule opération. Pas de REMOVE : l'historique des maintenances ne doit pas disparaître avec le véhicule. |

## Pourquoi LAZY partout ?
- Évite les requêtes N+1 et le chargement inutile de graphes.
- Un accès hors transaction à une collection LAZY lèvera une
  `LazyInitializationException` : elle sera résolue en Atelier 7 par des
  requêtes dédiées (fetch join ou `@EntityGraph`).

## Rappels
- `cascade = REMOVE` : supprimer le parent supprime les enfants.
- `orphanRemoval = true` : retirer un enfant de la collection le supprime,
  même sans supprimer le parent.
- Les deux sont complémentaires et adaptés à une composition (Contrat–Paiement).