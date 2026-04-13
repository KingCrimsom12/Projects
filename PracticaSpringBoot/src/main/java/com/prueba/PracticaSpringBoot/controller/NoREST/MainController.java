package com.prueba.PracticaSpringBoot.controller.NoREST;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/index")
    public String catalogo() {
        return "index";
    }
}
