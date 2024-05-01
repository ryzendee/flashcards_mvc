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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FlashcardController {
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "10";

    private final FlashcardFacade flashcardFacade;

    @GetMapping("/{folderId}/flashcards-quiz")
    public String getQuizView(@PathVariable Long folderId,
                              Model model) {
        List<FlashcardDtoResponse> flashcards = flashcardFacade.getListByFolderId(folderId);

        model.addAttribute("flashcardsList", flashcards);

        return "flashcard/flashcard-quiz";
    }

    @GetMapping("/{folderId}/flashcards")
    public String getFlashcardsView(@PathVariable Long folderId,
                                    @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                    @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                    Model model) {

        Page<FlashcardDtoResponse> flashcardsPage = flashcardFacade.getPageByFolderId(folderId, page, size);

        model.addAttribute("folderId", folderId);
        model.addAttribute("flashcardsPageList", flashcardsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", flashcardsPage.getTotalPages());

        return "flashcard/flashcard-main-view";
    }

    @GetMapping("/{folderId}/flashcards-add")
    public String getAddFlashcardView(@PathVariable Long folderId,
                                      Model model) {

        FlashcardCreateDtoRequest createDtoRequest = new FlashcardCreateDtoRequest();
        createDtoRequest.setFolderId(folderId);

        model.addAttribute("flashcard", createDtoRequest);

        return "flashcard/flashcard-add-view";
    }

    @PostMapping("/flashcards-add")
    public String saveCreatedFlashcard(@ModelAttribute @Valid FlashcardCreateDtoRequest request,
                                       @SessionAttribute Long userId,
                                       BindingResult bindingResult,
                                       Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "flashcard/flashcard-add-view";
        }

        flashcardFacade.createFlashcard(userId, request);

        return String.format("redirect:/%s/flashcards", request.getFolderId());
    }

    @GetMapping("/{folderId}/flashcards-update")
    public String getUpdateFlashcardView(@PathVariable Long folderId,
                                         @RequestParam Long flashcardId,
                                         Model model) {

        FlashcardUpdateDtoRequest updateDtoRequest = new FlashcardUpdateDtoRequest();
        updateDtoRequest.setId(flashcardId);
        updateDtoRequest.setFolderId(folderId);

        model.addAttribute("flashcard", updateDtoRequest);

        return "flashcard/flashcard-update-view";
    }

    @PostMapping("/flashcards-update")
    public String saveUpdatedFlashcard(@ModelAttribute @Valid FlashcardUpdateDtoRequest request,
                                       @SessionAttribute Long userId,
                                       BindingResult bindingResult,
                                       Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "flashcard/flashcard-add-view";
        }

        flashcardFacade.updateFlashcard(userId, request);

        return String.format("redirect:/%s/flashcards", request.getFolderId());
    }

    @PostMapping("/{folderId}/flashcards-delete")
    public String deleteFlashcard(@PathVariable Long folderId,
                                  @RequestParam Long flashcardId) {

        flashcardFacade.deleteById(flashcardId);

        return String.format("redirect:/%s/flashcards", folderId);
    }

}
