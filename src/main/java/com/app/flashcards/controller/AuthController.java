package com.app.flashcards.controller;

import com.app.flashcards.dto.auth.SignUpDto;
import com.app.flashcards.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @GetMapping("/login")
    public String getLoginView() {
        return "auth/login-view";
    }

    @GetMapping("/signup")
    public String getSignUpView(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "auth/signup-view";
    }

    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute SignUpDto signUpDto,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "auth/signup-view";
        }

        userService.createUser(signUpDto);

        return "redirect:/login";
    }
}
