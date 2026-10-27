package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.service.IContratService;

import java.util.List;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion des contrats de location")
public class ContratController {

    private final IContratService contratService;

    @GetMapping
    @Operation(summary = "Lister tous les contrats", description = "Récupère la liste complète des contrats enregistrés dans la base.")
    public List<Contrat> findAll() {
        return contratService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un contrat par ID", description = "Récupère les détails d'un contrat spécifique via son identifiant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat trouvé avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public Contrat findById(@PathVariable Long id) {
        return contratService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un nouveau contrat", description = "Crée un contrat et ses paiements associés (si fournis dans le corps de la requête).")
    @ApiResponse(responseCode = "201", description = "Contrat créé avec succès")
    public Contrat create(@RequestBody Contrat contrat) {
        return contratService.create(contrat);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un contrat", description = "Met à jour les informations d'un contrat existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrat modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public Contrat update(@PathVariable Long id, @RequestBody Contrat contrat) {
        return contratService.update(id, contrat);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un contrat", description = "Supprime définitivement un contrat et ses paiements (cascade).")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrat supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Contrat introuvable")
    })
    public void deleteById(@PathVariable Long id) {
        contratService.deleteById(id);
    }
}