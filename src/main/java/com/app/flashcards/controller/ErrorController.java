package com.app.flashcards.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ErrorController {


    @GetMapping("/access-denied")
    public String getErrorPage(Model model) {
        model.addAttribute("errorMessage", "Something went wrong...");

        return "errors/error-view";
    }
}
