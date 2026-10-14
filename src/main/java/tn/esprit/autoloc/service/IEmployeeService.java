package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Employee;

import java.util.List;

public interface IEmployeeService {
    Employee create(Employee employee);
    Employee findById(Long id);
    List<Employee> findAll();
    Employee update(Long id, Employee employee);
    void deleteById(Long id);
}