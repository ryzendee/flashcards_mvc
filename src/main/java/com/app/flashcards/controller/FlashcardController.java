package com.app.flashcards.controller;

import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.dto.response.FlashcardDtoResponse;
import com.app.flashcards.service.flashcard.FlashcardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FlashcardController {
    private static final String ADD_FLASHCARD_VIEW = "flashcard/flashcard-add-view";
    private static final String UPDATE_FLASHCARD_VIEW = "flashcard/flashcard-update-view";

    private static final String REDIRECT_TO_FLASHCARDS_FORMATTED = "redirect:/%s/flashcards";

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "10";


    private final FlashcardService flashcardService;

    @PreAuthorize("@customSecurityExpression.isFolderOwner(#userId, #folderId)")
    @GetMapping("/{folderId}/flashcards-quiz")
    public String getQuizView(@SessionAttribute Long userId,
                              @PathVariable Long folderId,
                              Model model) {
        List<FlashcardDtoResponse> flashcards = flashcardService.getListByFolderId(folderId);

        model.addAttribute("flashcardsList", flashcards);

        return "flashcard/flashcard-quiz";
    }

    @PreAuthorize("@customSecurityExpression.isFolderOwner(#userId, #folderId)")
    @GetMapping("/{folderId}/flashcards")
    public String getFlashcardsView(@SessionAttribute Long userId,
                                    @PathVariable Long folderId,
                                    @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                    @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                    Model model) {

        Page<FlashcardDtoResponse> flashcardsPage = flashcardService.getPageByFolderId(folderId, page, size);

        model.addAttribute("folderId", folderId);
        model.addAttribute("flashcardsPageList", flashcardsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", flashcardsPage.getTotalPages());

        return "flashcard/flashcard-main-view";
    }

    @PreAuthorize("@customSecurityExpression.isFolderOwner(#userId, #folderId)")
    @GetMapping("/{folderId}/flashcards-add")
    public String getAddFlashcardView(@SessionAttribute Long userId,
                                      @PathVariable Long folderId,
                                      Model model) {

        FlashcardCreateDtoRequest createDtoRequest = new FlashcardCreateDtoRequest();
        createDtoRequest.setFolderId(folderId);
        createDtoRequest.setUserId(userId);

        model.addAttribute("flashcard", createDtoRequest);

        return ADD_FLASHCARD_VIEW;
    }

    @PostMapping("/flashcards-add")
    public String saveCreatedFlashcard(@ModelAttribute("flashcard") @Valid FlashcardCreateDtoRequest request,
                                       @SessionAttribute Long userId,
                                       BindingResult bindingResult,
                                       Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return ADD_FLASHCARD_VIEW;
        }

        flashcardService.createFlashcard(request);

        return String.format(REDIRECT_TO_FLASHCARDS_FORMATTED, request.getFolderId());
    }

    @PreAuthorize("@customSecurityExpression.isFolderOwner(#userId, #folderId)")
    @GetMapping("/{folderId}/flashcards-update")
    public String getUpdateFlashcardView(@SessionAttribute Long userId,
                                         @PathVariable Long folderId,
                                         @RequestParam Long flashcardId,
                                         Model model) {

        FlashcardUpdateDtoRequest updateDtoRequest = new FlashcardUpdateDtoRequest();
        updateDtoRequest.setId(flashcardId);
        updateDtoRequest.setFolderId(folderId);
        updateDtoRequest.setUserId(userId);

        model.addAttribute("flashcard", updateDtoRequest);

        return UPDATE_FLASHCARD_VIEW;
    }

    @PostMapping("/flashcards-update")
    public String saveUpdatedFlashcard(@ModelAttribute("flashcard") @Valid FlashcardUpdateDtoRequest request,
                                       @SessionAttribute Long userId,
                                       BindingResult bindingResult,
                                       Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return UPDATE_FLASHCARD_VIEW;
        }

        flashcardService.updateFlashcard(request);

        return String.format(REDIRECT_TO_FLASHCARDS_FORMATTED, request.getFolderId());
    }

    @PreAuthorize("@customSecurityExpression.isFolderOwner(#userId, #folderId)")
    @PostMapping("/{folderId}/flashcards-delete")
    public String deleteFlashcard(@SessionAttribute Long userId,
                                  @PathVariable Long folderId,
                                  @RequestParam Long flashcardId) {

        flashcardService.deleteFlashcardById(flashcardId);

        return String.format(REDIRECT_TO_FLASHCARDS_FORMATTED, folderId);
    }

}
