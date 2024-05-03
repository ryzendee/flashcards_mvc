package com.app.flashcards.controller;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.facade.cardfolder.CardFolderServiceFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CardFolderController {
    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "10";

    private final CardFolderServiceFacade folderServiceFacade;

    @GetMapping("/folders")
    public String getCardFoldersView(@SessionAttribute Long userId,
                                          @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                          Model model) {

        Page<CardFolderDtoResponse> foldersPage = folderServiceFacade.getCardFolderPageByUserId(userId, page, size);

        model.addAttribute("foldersPageList", foldersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", foldersPage.getTotalPages());

        return "cardfolder/cardfolder-main-view";
    }

    @GetMapping("/folders-add")
    public String getAddFolderView(Model model) {
        CardFolderCreateDtoRequest folderDtoRequest = new CardFolderCreateDtoRequest();

        model.addAttribute("cardFolder", folderDtoRequest);

        return "cardfolder/cardfolder-add-view";
    }

    @PostMapping("/folders-add")
    public String saveCreatedFolder(@Valid @ModelAttribute("cardFolder") CardFolderCreateDtoRequest cardFolderCreateDtoRequest,
                                    BindingResult bindingResult,
                                    @SessionAttribute Long userId,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "cardfolder/cardfolder-add-view";
        }

        folderServiceFacade.createCardFolder(userId, cardFolderCreateDtoRequest);

        return "redirect:/folders";
    }

    @PreAuthorize("@customSecurityExpression.isFolderOwner(#userId, #folderId)")
    @GetMapping("/folders-update")
    public String getUpdateFolderView(@SessionAttribute Long userId,
                                      @RequestParam Long folderId,
                                      Model model) {

        CardFolderUpdateDtoRequest cardFolderUpdateDtoRequest = new CardFolderUpdateDtoRequest();
        cardFolderUpdateDtoRequest.setId(folderId);
        model.addAttribute("cardFolder", cardFolderUpdateDtoRequest);

        return "cardfolder/cardfolder-update-view";
    }


    @PreAuthorize("@customSecurityExpression.isFolderOwner(#userId, #request.id)")
    @PostMapping("/folders-update")
    public String updateFolder(@Valid @ModelAttribute("cardFolder") CardFolderUpdateDtoRequest request,
                               BindingResult bindingResult,
                               @SessionAttribute Long userId,
                               Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "cardfolder/cardfolder-update-view";
        }

        folderServiceFacade.updateCardFolder(userId, request);

        return "redirect:/folders";
    }

    @PreAuthorize("@customSecurityExpression.isFolderOwner(#userId, #folderId)")
    @PostMapping("/folders-delete")
    public String deleteFolder(@SessionAttribute Long userId,
                               @RequestParam Long folderId) {
        folderServiceFacade.deleteById(folderId);

        return "redirect:/folders";
    }
}
