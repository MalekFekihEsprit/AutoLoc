package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.*;
import java.util.List;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByRole(RoleEmployee role);
    List<Employee> findByAgenceIdAgence(Long idAgence);
}