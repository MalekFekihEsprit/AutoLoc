package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Employee;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
}