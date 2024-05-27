package com.app.flashcards.unit.controller;

import com.app.flashcards.controller.ErrorController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ErrorController.class)
@Import(ThymeleafAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class ErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getErrorPage_withView_returnsErrorView() throws Exception {
        mockMvc.perform(
                get("/access-denied")
        ).andExpectAll(
                status().isOk(),
                model().attributeExists("errorMessage"),
                view().name("errors/error-view")
        );
    }
}
