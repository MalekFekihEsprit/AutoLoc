# Notes — Couche Repository Spring Data JPA

## Choix de l'interface

Toutes les interfaces du package `repository` étendent `JpaRepository<Entité, Long>`.

| Interface | Étend | Justification |
|---|---|---|
| `IAgenceRepository` | `JpaRepository<Agence, Long>` | CRUD complet, `findAll()` renvoie une `List`, tri et pagination disponibles. |
| `IClientRepository` | `JpaRepository<Client, Long>` | Idem. |
| `IContratRepository` | `JpaRepository<Contrat, Long>` | Idem ; `saveAndFlush` utile pour observer la cascade `Contrat → Paiement`. |
| `IEmployeeRepository` | `JpaRepository<Employee, Long>` | Idem. |
| `IEquipementRepository` | `JpaRepository<Equipement, Long>` | Idem. |
| `IMaintenanceRepository` | `JpaRepository<Maintenance, Long>` | Idem. |
| `IPaiementRepository` | `JpaRepository<Paiement, Long>` | Idem ; conservé malgré la composition car le code métier passe par le `Contrat`, mais le repository est nécessaire pour les lectures ciblées. |
| `IReservationRepository` | `JpaRepository<Reservation, Long>` | Idem. |
| `IVehiculeRepository` | `JpaRepository<Vehicule, Long>` | Idem. |

## Pourquoi JpaRepository et non CrudRepository

`CrudRepository` suffit pour un besoin CRUD minimal, mais `findAll()` y renvoie un `Iterable`. Dès qu'on veut des `List` (plus pratiques à parcourir, `size()`, `get(i)`, `stream()`), du tri ou de la pagination, il faut assembler plusieurs interfaces à la main. `JpaRepository` les cumule déjà :

- `ListCrudRepository` → `findAll()` renvoie une `List`
- `ListPagingAndSortingRepository` → `findAll(Sort)` et `findAll(Pageable)`
- `QueryByExampleExecutor`
- Méthodes propres à JPA : `flush()`, `saveAndFlush()`, `getReferenceById()`, `deleteAllInBatch()`

## Limites des suppressions en lot

`deleteAllInBatch()` et `deleteAllByIdInBatch()` exécutent un `DELETE` unique en contournant le contexte de persistance. La cascade `REMOVE` et l'`orphanRemoval` **ne s'appliquent pas**. Supprimer ainsi des `Contrat` laisserait des `Paiement` orphelins (ou échouerait sur la contrainte de clé étrangère). Pour supprimer un contrat avec ses paiements, on utilise `deleteById()` ou `delete()`.

## Anomalies SonarQube for IDE relevées et corrigées

| # | Règle | Fichier | Problème | Correction |
|---|-------|---------|----------|------------|
| 1 | `java:S2208` | `I*Repository.java` | Import avec wildcard `tn.esprit.autoloc.domain.*` | Import explicite `import tn.esprit.autoloc.domain.Agence;` |
| 2 | `java:S106` | `RepositoryTests.java` | Usage de `System.out.println` | Conservé : le PDF Part 2 demande d'afficher les résultats dans la console. Documenté ici. |
| 3 | `java:S6813` | `RepositoryTests.java` | Injection par champ `@Autowired` | Conservé : pratique courante en test Spring. Alternative : injection par constructeur. |
| 4 | `java:S1128` | `AutolocApiApplication.java` | Commentaire résiduel dans le corps de classe | Supprimé. |

Remplace les lignes 1, 3 et 4 par ce que SonarQube te remonte **réellement** dans ton IDE (rule ID, fichier, correction). Il t'en faut au moins 3.