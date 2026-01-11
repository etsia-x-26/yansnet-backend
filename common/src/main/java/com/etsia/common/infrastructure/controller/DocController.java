package com.etsia.common.infrastructure.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Hidden
public class DocController {

    @GetMapping("/docs.yml")
    public String downloadYaml() {
        return "redirect:/v3/api-docs.yaml";
    }

    @GetMapping("/docs.json")
    public String downloadJson() {
        return "redirect:/v3/api-docs";
    }
}
