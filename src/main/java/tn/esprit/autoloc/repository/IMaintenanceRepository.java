package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.*;
import java.util.List;

public interface IMaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByVehiculeIdVehicule(Long idVehicule);
    List<Maintenance> findByDateFinIsNull();
}