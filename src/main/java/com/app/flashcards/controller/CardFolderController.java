package com.app.flashcards.controller;

import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.service.cardfolder.CardFolderService;
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

    private static final String ADD_FOLDER_VIEW = "cardfolder/cardfolder-add-view";
    private static final String UPDATE_FOLDER_VIEW = "cardfolder/cardfolder-update-view";

    private static final String REDIRECT_TO_FOLDERS = "redirect:/folders";

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_SIZE = "10";

    private final CardFolderService cardFolderService;

    @GetMapping("/folders")
    public String getCardFoldersView(@SessionAttribute Long userId,
                                          @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                          @RequestParam(defaultValue = DEFAULT_SIZE) int size,
                                          Model model) {

        Page<CardFolderDtoResponse> foldersPage = cardFolderService.getCardFolderPageByUserId(userId, page, size);

        model.addAttribute("foldersPageList", foldersPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", foldersPage.getTotalPages());

        return "cardfolder/cardfolder-main-view";
    }

    @GetMapping("/folders-add")
    public String getAddFolderView(@SessionAttribute Long userId,
                                   Model model) {
        CardFolderCreateDtoRequest folderDtoRequest = new CardFolderCreateDtoRequest();
        folderDtoRequest.setUserId(userId);

        model.addAttribute("cardFolder", folderDtoRequest);

        return ADD_FOLDER_VIEW;
    }

    @PostMapping("/folders-add")
    public String saveCreatedFolder(@Valid @ModelAttribute("cardFolder") CardFolderCreateDtoRequest cardFolderCreateDtoRequest,
                                    BindingResult bindingResult,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return ADD_FOLDER_VIEW;
        }

        cardFolderService.createCardFolder(cardFolderCreateDtoRequest);

        return REDIRECT_TO_FOLDERS;
    }

    @PreAuthorize("@customSecurityExpression.isFolderOwner(#userId, #folderId)")
    @GetMapping("/folders-update")
    public String getUpdateFolderView(@SessionAttribute Long userId,
                                      @RequestParam Long folderId,
                                      Model model) {

        CardFolderUpdateDtoRequest cardFolderUpdateDtoRequest = new CardFolderUpdateDtoRequest();
        cardFolderUpdateDtoRequest.setId(folderId);
        cardFolderUpdateDtoRequest.setUserId(userId);
        model.addAttribute("cardFolder", cardFolderUpdateDtoRequest);

        return UPDATE_FOLDER_VIEW;
    }


    @PreAuthorize("@customSecurityExpression.isFolderOwner(#request.userId, #request.id)")
    @PostMapping("/folders-update")
    public String updateFolder(@Valid @ModelAttribute("cardFolder") CardFolderUpdateDtoRequest request,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return UPDATE_FOLDER_VIEW;
        }

        cardFolderService.updateCardFolder(request);

        return REDIRECT_TO_FOLDERS;
    }

    @PreAuthorize("@customSecurityExpression.isFolderOwner(#userId, #folderId)")
    @PostMapping("/folders-delete")
    public String deleteFolder(@SessionAttribute Long userId,
                               @RequestParam Long folderId) {

        cardFolderService.deleteById(folderId);

        return REDIRECT_TO_FOLDERS;
    }
}
