# Notes — API REST AutoLoc

## A. Endpoints

| Contrôleur | Verbe + route | Service appelé | Succès | Erreurs |
|---|---|---|---|---|
| `ContratController` | GET `/api/contrats` | `IContratService.findAll()` | 200 | — |
| `ContratController` | GET `/api/contrats/{id}` | `IContratService.findById(id)` | 200 | 404 |
| `ContratController` | POST `/api/contrats` | `IContratService.create(contrat)` | 201 | 400 |
| `ContratController` | PUT `/api/contrats/{id}` | `IContratService.update(id, contrat)` | 200 | 404, 400 |
| `ContratController` | DELETE `/api/contrats/{id}` | `IContratService.deleteById(id)` | 204 | 404 |
| `PaiementController` | GET `/api/paiements` | `IPaiementService.findAll()` | 200 | — |
| `PaiementController` | GET `/api/paiements/{id}` | `IPaiementService.findById(id)` | 200 | 404 |
| ... (même chose pour les 7 autres contrôleurs) | | | | |

## B. Annotations Jackson

| Association | Annotation retenue | Justification |
|---|---|---|
| `Contrat.paiements` ↔ `Paiement.contrat` | `@JsonManagedReference("contrat-paiements")` / `@JsonBackReference("contrat-paiements")` | Vraie composition : on veut voir les paiements dans le JSON du contrat, mais pas re-sérialiser le contrat depuis chaque paiement. Évite la boucle infinie. |
| `Agence.vehicules` | `@JsonIgnore` | On n'a pas besoin de la liste complète des véhicules dans le JSON d'une agence. Le `@ManyToOne` côté `Vehicule.agence` reste sérialisé pour donner la FK. |
| `Vehicule.maintenances` | `@JsonIgnore` | Idem. |
| `Equipement.vehicules` | `@JsonIgnore` | Côté inverse du ManyToMany ; le propriétaire reste `Vehicule.equipements`. |
| `Client.reservations` | `@JsonIgnore` | On évite d'envoyer toutes les réservations d'un client dans le JSON client. |

**Différence `@JsonBackReference` vs `@JsonIgnore` :** `@JsonBackReference` fait partie d'une paire symétrique avec `@JsonManagedReference` — elle sérialise un côté et ignore l'autre automatiquement, ce qui garantit qu'on n'oublie pas d'ignorer la boucle. `@JsonIgnore` est unilatéral : on ignore ce champ partout, sans contrepartie. Pour une vraie composition (Contrat–Paiement), la paire est préférable ; pour les autres associations où on n'a jamais besoin du côté inverse, `@JsonIgnore` suffit.

## C. Postman et Swagger

**Postman** sert à tester : on écrit une requête (verbe, URL, body), on l'envoie, on lit le code de retour et le corps JSON. On peut sauvegarder une collection réutilisable et y ajouter des scripts d'assertion (After response) pour valider automatiquement les codes de statut.

**Swagger UI** (pas encore étudié) servira à documenter l'API : il génère automatiquement la liste des endpoints à partir des contrôleurs et permet de les essayer depuis le navigateur.

Exemple concret dans l'atelier : le POST `/api/contrats` avec ses paiements imbriqués renvoie 201 et crée les paiements par cascade — c'est Postman qui a permis de le vérifier, en relisant ensuite `GET /api/paiements`.

## D. Anomalies SonarQube

| # | Règle | Fichier                                   | Problème | Correction |
|---|---|-------------------------------------------|---|---|
| 1 | `java:S4684` | Tous les contrôleurs (`*Controller.java`) | Exposition directe des entités JPA via `@RequestBody`. Risque de mass-assignment (un client pourrait envoyer un "id" et écraser une ligne) et couplage fort entre la base de données et l'API. | **Non corrigée volontairement.** Le PDF de l'Atelier 5 précise que c'est provisoire : la correction se fera au Chapitre 10 (Atelier 6) en introduisant des DTO (Data Transfer Objects). |
| 2 | `java:S1128` | `AgenceController.java`                   | Import inutilisé (`java.util.Date` ajouté par erreur). | Import supprimé. |

## E. Questions de compréhension

**1. Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?**
Le `DispatcherServlet` (le Front Controller de Spring MVC). Il consulte le `HandlerMapping` qui, à partir du verbe HTTP et de l'URL, trouve la méthode du contrôleur (`@RequestMapping`, `@GetMapping`...). Il délègue ensuite l'exécution, puis convertit la valeur retournée en JSON via Jackson.

**2. Pourquoi `ContratController` dépend-il de `IContratService` et non de `IContratRepository` ni de `ContratServiceImpl` ?**
On ne dépend jamais de l'implémentation (principe d'inversion des dépendances). Le contrôleur ne connaît que le contrat d'interface du service. Dépendre du repository ferait sauter la couche métier (règles de gestion, exceptions). Dépendre de l'implémentation empêcherait de la remplacer (tests, mock).

**3. Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?**
201 Created : la requête a créé une ressource ; on renvoie aussi son identifiant dans le corps. 204 No Content : la suppression a réussi, mais il n'y a rien à renvoyer dans le corps. 404 Not Found : la ressource demandée n'existe pas — c'est un cas prévu (l'`ResourceNotFoundException` est traduite explicitement par le `GlobalExceptionHandler`). 500 serait une erreur serveur non prévue ; une absence est un état légitime du client, pas un bug.

**4. Pourquoi `PaiementController` n'expose-t-il que des GET ? Quelle réponse reçoit un POST `/api/paiements` ?**
Parce qu'un paiement n'existe que rattaché à son contrat (composition). Sa création et sa suppression passent par la cascade du contrat. Un POST sur `/api/paiements` reçoit **405 Method Not Allowed** : aucune méthode POST n'est mappée sur ce chemin.

**5. Quels risques présente l'exposition directe des entités JPA ? Pourquoi `Contrat.paiements` utilise-t-il `@JsonManagedReference` alors que `Agence.vehicules` utilise `@JsonIgnore` ?**
Risques : couplage fort entre le schéma de la base et le contrat de l'API (changer une colonne change la réponse JSON), fuite de champs internes (clés étrangères, colonnes techniques, données sensibles), boucles infinies, chargements paresseux (`LazyInitializationException`, `ByteBuddyInterceptor`) imprévisibles. Les DTO résoudront ces problèmes (Chapitre 10).
`Contrat.paiements` utilise `@JsonManagedReference` parce qu'on **veut** sérialiser la liste des paiements dans la réponse contrat (composition), tout en cassant la boucle via `@JsonBackReference` côté paiement. `Agence.vehicules` utilise `@JsonIgnore` parce qu'on n'a **jamais** besoin d'envoyer la liste complète des véhicules d'une agence dans le JSON — seule la FK côté `Vehicule.agence` suffit.