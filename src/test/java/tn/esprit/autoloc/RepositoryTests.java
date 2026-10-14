package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepositoryTests {

    @Autowired private IAgenceRepository agenceRepository;
    @Autowired private IClientRepository clientRepository;
    @Autowired private IVehiculeRepository vehiculeRepository;
    @Autowired private IEmployeeRepository employeeRepository;
    @Autowired private IEquipementRepository equipementRepository;
    @Autowired private IMaintenanceRepository maintenanceRepository;

    // ============================================================
    // PARTIE 2 — Agence
    // ============================================================
    @Test
    void testAgenceRepository() {
        Agence agence = new Agence();
        agence.setNom("Agence Test");
        agence.setVille("Tunis");
        agence.setAdresse("12 Rue de Test");
        agence.setTelephone("71234567");

        // save()
        Agence saved = agenceRepository.save(agence);
        System.out.println(">>> SAVE agence -> " + saved + " (id=" + saved.getIdAgence() + ")");
        assertNotNull(saved.getIdAgence());

        // findAll()
        List<Agence> all = agenceRepository.findAll();
        System.out.println(">>> FIND ALL -> " + all);
        assertFalse(all.isEmpty());

        // findById()
        Optional<Agence> found = agenceRepository.findById(saved.getIdAgence());
        System.out.println(">>> FIND BY ID -> " + found);
        assertTrue(found.isPresent());

        // existsById()
        boolean exists = agenceRepository.existsById(saved.getIdAgence());
        System.out.println(">>> EXISTS BY ID -> " + exists);
        assertTrue(exists);

        // count()
        long count = agenceRepository.count();
        System.out.println(">>> COUNT -> " + count);
        assertTrue(count > 0);

        // deleteById() + verify
        agenceRepository.deleteById(saved.getIdAgence());
        System.out.println(">>> AFTER DELETE, findById = " +
                agenceRepository.findById(saved.getIdAgence()).orElse(null));
        assertNull(agenceRepository.findById(saved.getIdAgence()).orElse(null));
    }
    // ============================================================
    // PARTIE 3 — Client
    // ============================================================
    @Test
    void testClientRepository() {
        long stamp = System.currentTimeMillis();

        Client c1 = new Client();
        c1.setNom("Ben Ali");
        c1.setPrenom("Ahmed");
        c1.setEmail("ahmed" + stamp + "@test.tn");
        c1.setTelephone("98111222");
        c1.setNumPermis("P" + stamp + "A");
        c1.setDateInscription(LocalDate.now());

        Client c2 = new Client();
        c2.setNom("Trabelsi");
        c2.setPrenom("Sarra");
        c2.setEmail("sarra" + stamp + "@test.tn");
        c2.setTelephone("98333444");
        c2.setNumPermis("P" + stamp + "B");
        c2.setDateInscription(LocalDate.now());

        Client saved1 = clientRepository.save(c1);
        Client saved2 = clientRepository.save(c2);
        System.out.println(">>> SAVE client 1 -> id=" + saved1.getIdClient());
        System.out.println(">>> SAVE client 2 -> id=" + saved2.getIdClient());

        // findAll()
        List<Client> all = clientRepository.findAll();
        System.out.println(">>> FIND ALL -> " + all);
        assertTrue(all.size() >= 2);

        // findById()
        Optional<Client> found = clientRepository.findById(saved1.getIdClient());
        System.out.println(">>> FIND BY ID -> " + found);
        assertTrue(found.isPresent());

        // existsById() existing
        boolean exists = clientRepository.existsById(saved1.getIdClient());
        System.out.println(">>> EXISTS (existing id) -> " + exists);
        assertTrue(exists);

        // existsById() non-existing
        boolean notExists = clientRepository.existsById(Long.MAX_VALUE);
        System.out.println(">>> EXISTS (unknown id) -> " + notExists);
        assertFalse(notExists);

        // count()
        long count = clientRepository.count();
        System.out.println(">>> COUNT -> " + count);
        assertTrue(count >= 2);

        // cleanup
        clientRepository.deleteById(saved1.getIdClient());
        clientRepository.deleteById(saved2.getIdClient());
    }

    // ============================================================
    // PARTIE 4 — Vehicule
    // ============================================================
    @Test
    void testVehiculeRepository() {
        long stamp = System.currentTimeMillis();

        Vehicule v = new Vehicule();
        v.setImmatriculation("TU" + (stamp % 100000) + "TN");
        v.setMarque("Renault");
        v.setModele("Clio");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("85.00"));
        v.setStatut(StatutVehicule.DISPONIBLE);

        Vehicule saved = vehiculeRepository.save(v);
        System.out.println(">>> SAVE vehicule -> " + saved + " (id=" + saved.getIdVehicule() + ")");

        List<Vehicule> all = vehiculeRepository.findAll();
        System.out.println(">>> FIND ALL -> " + all);

        long count = vehiculeRepository.count();
        System.out.println(">>> COUNT -> " + count);
        assertTrue(count > 0);

        // modification via save()
        BigDecimal oldTarif = saved.getTarifJournalier();
        saved.setTarifJournalier(new BigDecimal("99.00"));
        saved.setStatut(StatutVehicule.LOUE);
        vehiculeRepository.save(saved);

        Vehicule updated = vehiculeRepository.findById(saved.getIdVehicule()).orElseThrow();
        System.out.println(">>> UPDATED -> " + updated
                + " (tarif=" + updated.getTarifJournalier() + ", statut=" + updated.getStatut() + ")");

        assertEquals(new BigDecimal("99.00"), updated.getTarifJournalier());
        assertNotEquals(oldTarif, updated.getTarifJournalier());

        // delete
        vehiculeRepository.deleteById(saved.getIdVehicule());
        assertFalse(vehiculeRepository.existsById(saved.getIdVehicule()));
        System.out.println(">>> DELETE OK");
    }

    // ============================================================
    // PARTIE 5 — 3 repositories en autonomie
    // ============================================================

    // --- Employee ---
    @Test
    void testEmployeeRepository() {
        Employee e = new Employee();
        e.setNom("Gharbi");
        e.setPrenom("Mohamed");
        e.setRole(RoleEmployee.AGENT);

        Employee saved = employeeRepository.save(e);
        System.out.println(">>> SAVE employee -> " + saved + " (id=" + saved.getIdEmployee() + ")");
        assertNotNull(saved.getIdEmployee());

        System.out.println(">>> FIND ALL -> " + employeeRepository.findAll());
        assertTrue(employeeRepository.existsById(saved.getIdEmployee()));

        employeeRepository.deleteById(saved.getIdEmployee());
        assertFalse(employeeRepository.existsById(saved.getIdEmployee()));
        System.out.println(">>> EMPLOYEE TEST OK");
    }
    @Test
    void testEquipementRepository() {
        Equipement eq = new Equipement();
        eq.setLibelle("GPS");

        Equipement saved = equipementRepository.save(eq);
        System.out.println(">>> SAVE equipement -> " + saved + " (id=" + saved.getIdEquipement() + ")");
        assertNotNull(saved.getIdEquipement());

        Optional<Equipement> found = equipementRepository.findById(saved.getIdEquipement());
        System.out.println(">>> FIND BY ID -> " + found);
        assertTrue(found.isPresent());

        equipementRepository.deleteById(saved.getIdEquipement());
        assertFalse(equipementRepository.existsById(saved.getIdEquipement()));
        System.out.println(">>> EQUIPEMENT TEST OK");
    }
    @Test
    void testMaintenanceRepository() {
        long stamp = System.currentTimeMillis();

        Vehicule v = new Vehicule();
        v.setImmatriculation("MT" + (stamp % 100000) + "TN");
        v.setMarque("Peugeot");
        v.setModele("208");
        v.setCategorie(CategorieVehicule.CITADINE);
        v.setTarifJournalier(new BigDecimal("75.00"));
        v.setStatut(StatutVehicule.MAINTENANCE);
        Vehicule savedVehicule = vehiculeRepository.save(v);

        Maintenance m = new Maintenance();
        m.setDateDebut(LocalDate.now());
        m.setDescription("Vidange + filtres");
        m.setVehicule(savedVehicule);

        Maintenance saved = maintenanceRepository.save(m);
        System.out.println(">>> SAVE maintenance -> " + saved + " (id=" + saved.getIdMaintenance() + ")");
        assertNotNull(saved.getIdMaintenance());

        System.out.println(">>> FIND ALL -> " + maintenanceRepository.findAll());
        assertTrue(maintenanceRepository.existsById(saved.getIdMaintenance()));

        maintenanceRepository.deleteById(saved.getIdMaintenance());
        vehiculeRepository.deleteById(savedVehicule.getIdVehicule());
        System.out.println(">>> MAINTENANCE TEST OK");
    }
}