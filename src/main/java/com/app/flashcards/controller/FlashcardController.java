package com.app.flashcards.controller;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.dto.response.FlashcardDtoResponse;
import com.app.flashcards.facade.flashcard.FlashcardFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class FlashcardController {
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "10";

    private final FlashcardFacade flashcardFacade;

    @GetMapping("/flashcards")
    public String getFlashcardsView(@RequestParam Long folderId,
                                    @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                    @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                    Model model) {

        Page<FlashcardDtoResponse> flashcardsPage = flashcardFacade.getPageByFolderId(folderId, page, size);

        model.addAttribute("flashcardsPageList", flashcardsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", flashcardsPage.getTotalPages());

        return "flashcard/flashcard-main-view";
    }

    @GetMapping("/flashcards-add")
    public String getAddFlashcardView(@RequestParam Long folderId,
                                      Model model) {

        FlashcardCreateDtoRequest createDtoRequest = new FlashcardCreateDtoRequest();
        createDtoRequest.setFolderId(folderId);

        model.addAttribute("flashcard", createDtoRequest);

        return "flashcard/flashcard-add-view";
    }

    @PostMapping("/flashcards-add")
    public String saveCreatedFlashcard(@ModelAttribute @Valid FlashcardCreateDtoRequest request,
                                       BindingResult bindingResult,
                                       Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "flashcard/flashcard-add-view";
        }

        flashcardFacade.createFlashcard(request);

        return "redirect:/flashcards";
    }

    @GetMapping("/flashcards-update")
    public String getUpdateFlashcardView(@RequestParam Long flashcardId,
                                         @RequestParam Long folderId,
                                         Model model) {

        FlashcardUpdateDtoRequest updateDtoRequest = new FlashcardUpdateDtoRequest();
        updateDtoRequest.setId(flashcardId);
        updateDtoRequest.setFolderId(folderId);

        model.addAttribute("flashcard", updateDtoRequest);

        return "flashcard/flashcard-update-view";
    }

    @PostMapping("/flashcards-update")
    public String saveUpdatedFlashcard(@ModelAttribute @Valid FlashcardUpdateDtoRequest request,
                                       BindingResult bindingResult,
                                       Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "flashcard/flashcard-add-view";
        }

        flashcardFacade.updateFlashcard(request);

        return "redirect:/flashcards";
    }

    @PostMapping("/flashcards-delete")
    public String deleteFlashcard(@RequestParam Long flashcardId) {

        flashcardFacade.deleteById(flashcardId);

        return "redirect:/flashcards";
    }

}
