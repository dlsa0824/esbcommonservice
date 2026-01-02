package indi.daniel.esbcommonservice.company.controller;

import indi.daniel.esbcommonservice.company.controller.model.response.Response;
import indi.daniel.esbcommonservice.company.repository.model.Employee;
import indi.daniel.esbcommonservice.company.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

//@Validated
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    Random random = new Random();

    @GetMapping("/{employeeNumber}")
    public Response findEmployeeByEmployeeNumber(@PathVariable String employeeNumber) throws Exception {
//        int randomNum = random.nextInt(6);
//        System.out.println(randomNum);
//        if (randomNum == 0) {
//            throw new Exception("random數字為0");
//        }
        Employee employee = employeeService.findEmployeeByEmployeeNumber(employeeNumber);
        Response response = new Response();
        response.setResult(employee);
        return response;
    }
}
