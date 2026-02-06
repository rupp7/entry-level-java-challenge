package com.challenge.api.service;

import com.challenge.api.model.CreateEmployeeRequest;
import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeImpl;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final Map<UUID, Employee> store = new ConcurrentHashMap<>();

    public EmployeeService() {
        Employee a = buildEmployee("Sean", "Rupp", "sean.rupp@gmail.com", 30, 90000, "AssociateEngineer");
        Employee b = buildEmployee("Billy", "Bob", "billy.bob@gmail.com", 40, 110000, "Senior Engineer");
        store.put(a.getUuid(), a);
        store.put(b.getUuid(), b);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(store.values());
    }

    public Employee getEmployeeByUuid(UUID uuid) {
        Employee employee = store.get(uuid);
        if (employee == null) {
            throw new NoSuchElementException("Employee not found for uuid=" + uuid);
        }
        return employee;
    }

    public Employee createEmployee(CreateEmployeeRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        if (isBlank(req.getFirstName()) || isBlank(req.getLastName()) || isBlank(req.getEmail())) {
            throw new IllegalArgumentException("firstName, lastName, and email are required");
        }

        Employee employee = buildEmployee(
                req.getFirstName(),
                req.getLastName(),
                req.getEmail(),
                req.getAge(),
                req.getSalary(),
                req.getJobTitle());

        store.put(employee.getUuid(), employee);
        return employee;
    }

    private static Employee buildEmployee(
            String firstName, String lastName, String email, Integer age, Integer salary, String jobTitle) {
        EmployeeImpl e = new EmployeeImpl();
        e.setUuid(UUID.randomUUID());
        e.setFirstName(firstName);
        e.setLastName(lastName);
        e.setFullName(firstName + " " + lastName);
        e.setEmail(email);
        e.setAge(age);
        e.setSalary(salary);
        e.setJobTitle(jobTitle);
        e.setContractHireDate(Instant.now());
        e.setContractTerminationDate(null);
        return e;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
