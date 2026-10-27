# Notes — Couche Repository Spring Data JPA

## Choix de l'interface

Toutes les interfaces du package `repository` étendent `JpaRepository<Entité, Long>`.

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
| 2 | `java:S1128` | `AutolocApiApplication.java` | Commentaire résiduel dans le corps de classe | Supprimé. |
