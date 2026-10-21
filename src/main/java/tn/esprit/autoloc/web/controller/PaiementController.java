package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.service.IPaiementService;

import java.util.List;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final IPaiementService paiementService;

    @GetMapping
    public List<Paiement> findAll() {
        return paiementService.findAll();
    }

    @GetMapping("/{id}")
    public Paiement findById(@PathVariable Long id) {
        return paiementService.findById(id);
    }
}