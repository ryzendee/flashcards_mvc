package com.app.flashcards.unit.controller;

import com.app.flashcards.controller.CardFolderController;
import com.app.flashcards.dto.request.CardFolderCreateDtoRequest;
import com.app.flashcards.dto.request.CardFolderUpdateDtoRequest;
import com.app.flashcards.dto.response.CardFolderDtoResponse;
import com.app.flashcards.enums.SessionAttributes;
import com.app.flashcards.service.cardfolder.CardFolderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(CardFolderController.class)
@Import(ThymeleafAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class CardFolderControllerTest {

    private static final Long ID = 1L;
    private static final String SESSION_USER_ID_ATR_NAME = SessionAttributes.USER_ID.getName();
    private static final String NAME = "test-name";
    private static final String DESCRIPTION = "test-descr";
    private static final String IMAGE_URL = "test-imageUrl";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardFolderService cardFolderService;

    @Test
    void getCardFoldersView_withoutRequestParams_returnsFolderMainView() throws Exception{
        Page<CardFolderDtoResponse> expectedPage = getPageWithSingleElement();
        when(cardFolderService.getCardFolderPageByUserId(eq(ID), anyInt(), anyInt()))
                .thenReturn(expectedPage);

        mockMvc.perform(
                get("/folders")
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
        ).andExpectAll(
                status().isOk(),
                model().attributeExists("currentPage"),
                model().attribute("foldersPageList", expectedPage),
                model().attribute("totalPages", expectedPage.getTotalPages()),
                view().name("cardfolder/cardfolder-main-view")
        );

        verify(cardFolderService).getCardFolderPageByUserId(eq(ID), anyInt(), anyInt());
    }

    @Test
    void getCardFoldersView_withRequestParams_returnsFolderMainView() throws Exception {
        int page = 0;
        int size = 10;
        Page<CardFolderDtoResponse> expectedPage = getPageWithSingleElement();
        when(cardFolderService.getCardFolderPageByUserId(ID, page, size))
                .thenReturn(expectedPage);

        mockMvc.perform(
                get("/folders")
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
        ).andExpectAll(
                status().isOk(),
                model().attribute("foldersPageList", expectedPage),
                model().attribute("currentPage", page),
                model().attribute("totalPages", expectedPage.getTotalPages()),
                view().name("cardfolder/cardfolder-main-view")
        );

        verify(cardFolderService).getCardFolderPageByUserId(ID, page, size);
    }

    private Page<CardFolderDtoResponse> getPageWithSingleElement() {
        CardFolderDtoResponse dtoResponse = new CardFolderDtoResponse(
                ID,
                NAME,
                DESCRIPTION,
                IMAGE_URL
        );

        return new PageImpl<>(List.of(dtoResponse));
    }

    @Test
    void saveCreatedFolder_validDto_createAndRedirectToFolders() throws Exception {
        CardFolderCreateDtoRequest createDtoRequest = new CardFolderCreateDtoRequest(
                ID,
                NAME,
                DESCRIPTION,
                null);

        doNothing()
                .when(cardFolderService).createCardFolder(createDtoRequest);

        mockMvc.perform(
                post("/folders-add")
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
                        .flashAttr("cardFolder", createDtoRequest)
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/folders")
        );

        verify(cardFolderService).createCardFolder(createDtoRequest);
    }

    @EmptySource
    @NullSource
    @ParameterizedTest
    void saveCreatedFolder_invalidDto_returnsViewWithErrorsModelAttr(String invalidName) throws Exception {
        CardFolderCreateDtoRequest createDtoRequest = new CardFolderCreateDtoRequest(
                ID,
                invalidName,
                DESCRIPTION,
                null);

        mockMvc.perform(
                post("/folders-add")
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
                        .flashAttr("cardFolder", createDtoRequest)
        ).andExpectAll(
                status().isOk(),
                view().name("cardfolder/cardfolder-add-view"),
                model().attributeExists("errors")
        );
    }

    @Test
    void getUpdateFolderView_existsFolderId_returnsAddFolderView() throws Exception {
        mockMvc.perform(
                get("/folders-update")
                        .param("folderId", ID.toString())
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
        ).andExpectAll(
                status().isOk(),
                view().name("cardfolder/cardfolder-update-view"),
                model().attributeExists("cardFolder")
        );
    }

    @Test
    void updateFolder_validDto_saveAndRedirectToFolders() throws Exception {
        CardFolderUpdateDtoRequest updateDtoRequest = new CardFolderUpdateDtoRequest(
                ID,
                ID,
                NAME,
                DESCRIPTION,
                null);

        doNothing()
                .when(cardFolderService).updateCardFolder(updateDtoRequest);

        mockMvc.perform(
                post("/folders-update")
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
                        .flashAttr("cardFolder", updateDtoRequest)
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/folders")
        );

        verify(cardFolderService).updateCardFolder(updateDtoRequest);
    }

    @EmptySource
    @NullSource
    @ParameterizedTest
    void updateFolder_invalidDto_returnsViewWithErrorsModelAttr(String invalidName) throws Exception {
        CardFolderUpdateDtoRequest updateDtoRequest = new CardFolderUpdateDtoRequest(
                ID,
                ID,
                invalidName,
                DESCRIPTION,
                null);

        mockMvc.perform(
                post("/folders-update")
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
                        .flashAttr("cardFolder", updateDtoRequest)
        ).andExpectAll(
                status().isOk(),
                view().name("cardfolder/cardfolder-update-view"),
                model().attributeExists("errors")
        );
    }

    @Test
    void deleteById_existsId_shouldDelete() throws Exception {
        doNothing()
                .when(cardFolderService).deleteById(ID);

        mockMvc.perform(
                post("/folders-delete")
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
                        .param("folderId", ID.toString())
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/folders")
        );

        verify(cardFolderService).deleteById(ID);
    }
    
    @Test
    void getAddFolderView_withView_returnsView() throws Exception {
        mockMvc.perform(
                get("/folders-add")
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
        ).andExpectAll(
                status().isOk(),
                model().attributeExists("cardFolder"),
                view().name("cardfolder/cardfolder-add-view")
        );
    }
}
