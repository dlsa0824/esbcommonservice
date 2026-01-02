package indi.daniel.esbcommonservice.company.service;

import indi.daniel.esbcommonservice.company.repository.EmployeeRepository;
import indi.daniel.esbcommonservice.company.repository.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee findEmployeeByEmployeeNumber(String employeeNumber) {
        List<Employee> employees = employeeRepository.findByEmployeeNumber(employeeNumber);
        if (employees.isEmpty()) {
            return null;
        } else {
            return employees.getFirst();
        }
    }
}
