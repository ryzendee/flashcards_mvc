package com.app.flashcards.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        log.error("Unexpected exception", ex);
        return "redirect:/access-denied";
    }
}
