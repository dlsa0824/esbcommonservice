package indi.daniel.esbcommonservice.iam.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import indi.daniel.esbcommonservice.iam.constraint.ValidateJwtToken;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/iamTest")
@ValidateJwtToken
public class IamTestController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
