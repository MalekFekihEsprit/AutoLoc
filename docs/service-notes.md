# Notes — Couche Service, IoC et Injection de dépendances

## A. Modes d'injection comparés

| Critère | Constructeur (`@RequiredArgsConstructor` + `final`) | Attribut (`@Autowired` sur champ) |
|---|---|---|
| Champ final possible ? | Oui | Non |
| Dépendance visible dans la signature ? | Oui, dans le constructeur généré | Non |
| Utilisable avec `new` hors conteneur ? | Oui, si on passe la dépendance | Non — champ null |
| Signalé par SonarQube ? | Non | Oui — règle `java:S6813` |
| Détection d'un constructeur manquant ? | Immédiate à la compilation | À l'exécution seulement |

Choix retenu : injection par constructeur, générée par `@RequiredArgsConstructor` de Lombok.

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| `ContratServiceImpl` | `IContratRepository` | Constructeur | CRUD complet, refuse un id non null, refuse un montant négatif, supprime en cascade les paiements. |
| `AgenceServiceImpl` | `IAgenceRepository` | Constructeur | CRUD, refuse une agence sans nom. |
| `EmployeeServiceImpl` | `IEmployeeRepository` | Constructeur | CRUD, refuse un employé sans nom ou prénom. |
| `VehiculeServiceImpl` | `IVehiculeRepository` | Constructeur | CRUD, refuse un tarif journalier négatif. |
| `EquipementServiceImpl` | `IEquipementRepository` | Constructeur | CRUD, refuse un équipement sans libellé. |
| `ClientServiceImpl` | `IClientRepository` | Constructeur | CRUD, refuse un client sans nom ou sans e-mail. |
| `ReservationServiceImpl` | `IReservationRepository` | Constructeur | CRUD, refuse une date de fin antérieure à la date de début. |
| `PaiementServiceImpl` | `IPaiementRepository` | Constructeur | Lecture seule : `findById` et `findAll`. La création/modification/suppression passe par le Contrat (composition). |
| `MaintenanceServiceImpl` | `IMaintenanceRepository` | Constructeur | CRUD, refuse une date de fin antérieure à la date de début. |

## C. Messages d'erreur du conteneur

### Message 1 — `NoSuchBeanDefinitionException`
**Cause probable :** `ContratServiceImpl` n'est pas annotée `@Service` (ou est placée hors du package `tn.esprit.autoloc`).
**Correction :** ajouter `@Service` sur la classe, ou déplacer le fichier sous `tn.esprit.autoloc`.

### Message 2 — `NoUniqueBeanDefinitionException` (2 beans du même type)
**Correction 1 :** marquer un des deux beans comme candidat par défaut avec `@Primary` sur la classe (`EmailNotificateur`).
**Correction 2 :** choisir explicitement le bean à l'injection avec `@Qualifier("emailNotificateur")` sur le paramètre du constructeur du client.

### Message 3 — `BeanCurrentlyInCreationException` (dépendance circulaire)
**Pourquoi l'injection par constructeur rend le cycle impossible à contourner :** chaque bean exige que son collaborateur soit entièrement construit avant lui. Si A a besoin de B et B a besoin de A, aucun des deux ne peut être instancié en premier, donc Spring refuse de démarrer.
**Piste de conception :** extraire la responsabilité commune dans un troisième service (par exemple `ReservationClientService`) dont dépendent les deux services concernés. Chacun dépend alors du nouveau service, et le cycle disparaît.

## D. Anomalies SonarQube for IDE relevées

| # | Règle | Fichier | Explication | Correction |
|---|---|---|---|---|
| 1 | `java:S6813` | `ContratServiceImpl` (avant restauration) | Injection par attribut : dépendance cachée, champ non final. | Restauration de l'injection par constructeur (`@RequiredArgsConstructor` + champ `final`). |
| 2 | `java:S112` | `ContratServiceImpl.findById` (expérience) | Lever une exception générique (`RuntimeException`) au lieu d'une exception métier. | Utilisation de `ResourceNotFoundException`, plus expressive et filtrable côté web. |
| 3 | `java:S1128` | `<fichier>` | Import inutilisé. | Suppression de l'import. |

## E. Questions de compréhension

**1. Où est le `new ContratServiceImpl` et qui l'exécute ?**
Il n'y en a aucun dans le code. Spring, en scannant le package, trouve la classe annotée `@Service`, appelle son constructeur généré par `@RequiredArgsConstructor`, et lui passe le proxy de `IContratRepository`. C'est le conteneur IoC qui exécute le `new`.

**2. Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?**
Pour rester découplé de l'implémentation. On peut remplacer `ContratServiceImpl` par une autre implémentation (`ContratServiceMock`, version cache, etc.) sans modifier le contrôleur. Le contrôleur ne connaît que le contrat d'interface.

**3. Pourquoi un service singleton doit-il rester sans état ? Donner un exemple de bug.**
Un singleton est partagé par tous les threads. Si un service stockait un attribut modifiable (par exemple `private Contrat contratCourant`), deux requêtes simultanées écraseraient mutuellement leur valeur et l'une des deux recevrait la donnée de l'autre. La classe n'a donc que des champs `final` (dépendances) et aucune donnée d'état.

**4. Pourquoi `update` charge-t-il l'entité existante avant d'appeler `save` au lieu de lui passer directement l'objet reçu ?**
Si on passe directement l'objet reçu, `save` déclenche un `merge` qui écrase toutes les colonnes, y compris `idContrat`, la référence `reservation` et la collection `paiements`. Avec `orphanRemoval = true`, vider la collection depuis une requête partielle déclencherait la suppression des paiements existants. En chargeant d'abord, on met à jour uniquement les champs modifiables et on préserve les associations.

**5. Différences `@Component` / `@Bean` et `@Primary` / `@Qualifier`. Pourquoi `IPaiementService` n'expose-t-il ni création, ni modification, ni suppression ?**
`@Component` se pose sur la classe (détection automatique par scan). `@Bean` se pose sur une méthode d'une classe `@Configuration` : on déclare le bean manuellement, utile pour les classes qu'on ne peut pas annoter (bibliothèque externe, `DataSource`, `ObjectMapper`...).
`@Primary` marque un bean comme candidat par défaut quand plusieurs existent du même type, posé sur la classe. `@Qualifier("nom")` choisit explicitement un bean, posé sur le point d'injection (paramètre de constructeur ou champ).
`IPaiementService` est en lecture seule car un paiement n'existe que rattaché à son `Contrat` : il est créé, modifié et supprimé à travers le contrat (cascade `ALL` + `orphanRemoval`). Exposer des méthodes d'écriture directes permettrait de contourner cette règle métier.