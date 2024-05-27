package com.app.flashcards.unit.controller;

import com.app.flashcards.controller.FlashcardController;
import com.app.flashcards.dto.request.FlashcardCreateDtoRequest;
import com.app.flashcards.dto.request.FlashcardUpdateDtoRequest;
import com.app.flashcards.dto.response.FlashcardDtoResponse;
import com.app.flashcards.enums.SessionAttributes;
import com.app.flashcards.service.flashcard.FlashcardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(FlashcardController.class)
@Import(ThymeleafAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
public class FlashcardControllerTest {

    private static final String SESSION_USER_ID_ATR_NAME = SessionAttributes.USER_ID.getName();
    private static final Long ID = 1L;
    private static final String NAME = "test-flashcard-name";
    private static final String DEFINITION = "test-flashcard-definition";
    private static final String IMAGE_URL = "test-flashcard-imageUrl";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlashcardService flashcardService;

    @Test
    void getFlashcardsPage_withoutRequestParams_returnsFlashcardMainView() throws Exception {
        Page<FlashcardDtoResponse> expectedPage = getPageWithSingleElement();
        when(flashcardService.getPageByFolderId(eq(ID), anyInt(), anyInt()))
                .thenReturn(expectedPage);

        mockMvc.perform(
                get("/{folderId}/flashcards", ID)
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
        ).andExpectAll(
                status().isOk(),
                model().attributeExists("currentPage"),
                model().attribute("flashcardsPageList", expectedPage),
                model().attribute("totalPages", expectedPage.getTotalPages()),
                view().name("flashcard/flashcard-main-view")
        );

        verify(flashcardService).getPageByFolderId(eq(ID), anyInt(), anyInt());
    }

    @Test
    void getFlashcardsPage_withRequestParams_returnsFlashcardMainView() throws Exception{
        int page = 0;
        int size = 10;
        Page<FlashcardDtoResponse> expectedPage = getPageWithSingleElement();
        when(flashcardService.getPageByFolderId(ID, page, size))
                .thenReturn(expectedPage);

        mockMvc.perform(
                get("/{folderId}/flashcards", ID)
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
        ).andExpectAll(
                status().isOk(),
                model().attributeExists("currentPage"),
                model().attribute("flashcardsPageList", expectedPage),
                model().attribute("totalPages", expectedPage.getTotalPages()),
                view().name("flashcard/flashcard-main-view")
        );

        verify(flashcardService).getPageByFolderId(ID, page, size);
    }

    private Page<FlashcardDtoResponse> getPageWithSingleElement() {
        FlashcardDtoResponse dtoResponse = getFlashcardDtoResponse();
        return new PageImpl<>(List.of(dtoResponse));
    }

    private FlashcardDtoResponse getFlashcardDtoResponse() {
        return new FlashcardDtoResponse(
                ID,
                ID,
                NAME,
                DEFINITION,
                IMAGE_URL
        );
    }

    @Test
    void getQuizView_withFolderIdInPathVariable_returnsFlashcardQuizView() throws Exception {
        FlashcardDtoResponse dtoResponse = getFlashcardDtoResponse();
        List<FlashcardDtoResponse> flashcardsListForQuiz = List.of(dtoResponse);
        when(flashcardService.getListByFolderId(ID))
                .thenReturn(flashcardsListForQuiz);

        mockMvc.perform(
                get("/{folderId}/flashcards-quiz", ID)
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
        ).andExpectAll(
                status().isOk(),
                model().attribute("flashcardsList", flashcardsListForQuiz),
                view().name("flashcard/flashcard-quiz")
        );

        verify(flashcardService).getListByFolderId(ID);
    }

    @Test
    void getAddFlashcardsView_withFolderIdInPathVariable_returnsFlashcardsAddView() throws Exception {
        mockMvc.perform(
                get("/{folderId}/flashcards-add", ID)
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
        ).andExpectAll(
                status().isOk(),
                model().attributeExists("flashcard"),
                view().name("flashcard/flashcard-add-view")
        );
    }

    @Test
    void saveCreatedFlashcards_validDto_redirectToFlashcards() throws Exception {
        FlashcardCreateDtoRequest createDtoRequest = new FlashcardCreateDtoRequest(
                ID,
                ID,
                NAME,
                DEFINITION,
                null
        );
        doNothing()
                .when(flashcardService).createFlashcard(createDtoRequest);

        mockMvc.perform(
                post("/flashcards-add")
                        .flashAttr("flashcard", createDtoRequest)
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrlTemplate("/{folderId}/flashcards", ID)
        );

        verify(flashcardService).createFlashcard(createDtoRequest);
    }

    @Test
    void getUpdateView_existsFlashcardId_returnsFlashcardUpdateView() throws Exception {
        mockMvc.perform(
                get("/{folderId}/flashcards-update", ID)
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
                        .param("flashcardId", ID.toString())
        ).andExpectAll(
                status().isOk(),
                model().attributeExists("flashcard"),
                view().name("flashcard/flashcard-update-view")
        );
    }

    @Test
    void updateFlashcards_validDto_redirectToFlashcards() throws Exception {
        FlashcardUpdateDtoRequest updateDtoRequest = new FlashcardUpdateDtoRequest(
                ID,
                ID,
                ID,
                NAME,
                DEFINITION,
                null
        );

        doNothing()
                .when(flashcardService).updateFlashcard(updateDtoRequest);

        mockMvc.perform(
                post("/flashcards-update", ID)
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
                        .flashAttr("flashcard", updateDtoRequest)
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrlTemplate("/{folderId}/flashcards", ID)
        );

        verify(flashcardService).updateFlashcard(updateDtoRequest);
    }

    @MethodSource("getInvalidArgsForSaveFlashcard")
    @ParameterizedTest
    void saveCreatedFlashcards_invalidDto_returnFlashcardsAddViewWithErrorsAttr(String invalidName, String invalidDefinition) throws Exception {
        FlashcardCreateDtoRequest createDtoRequest = new FlashcardCreateDtoRequest(
                ID,
                ID,
                invalidName,
                invalidDefinition,
                null
        );

        mockMvc.perform(
                post("/flashcards-add")
                        .flashAttr("flashcard", createDtoRequest)
        ).andExpectAll(
                status().isOk(),
                model().attributeExists("errors"),
                view().name("flashcard/flashcard-add-view")
        );
    }

    @MethodSource("getInvalidArgsForSaveFlashcard")
    @ParameterizedTest
    void updateFlashcards_invalidDto_returnFlashcardsUpdateViewWithErrorsAttr(String invalidName, String invalidDefinition) throws Exception {
        FlashcardUpdateDtoRequest updateDtoRequest = new FlashcardUpdateDtoRequest(
                ID,
                ID,
                ID,
                invalidName,
                invalidDefinition,
                null
        );

        doNothing()
                .when(flashcardService).updateFlashcard(updateDtoRequest);

        mockMvc.perform(
                post("/flashcards-update")
                        .flashAttr("flashcard", updateDtoRequest)
        ).andExpectAll(
                status().isOk(),
                model().attributeExists("errors"),
                view().name("flashcard/flashcard-update-view")
        );
    }

    static Stream<Arguments> getInvalidArgsForSaveFlashcard() {
        return Stream.of(
                //Scenario 1: Name is null
                Arguments.of(null, DEFINITION),

                //Scenario 2: Name is blank
                Arguments.of("   ", DEFINITION),

                //Scenario 3: Definition is null
                Arguments.of(NAME, null),

                //Scenario 4: Definition is blank
                Arguments.of(NAME, "  ")
        );
    }

    @Test
    void deleteFlashcard_existsId_shouldDelete() throws Exception {
        doNothing()
                .when(flashcardService).deleteFlashcardById(ID);

        mockMvc.perform(
                post("/{folderId}/flashcards-delete", ID)
                        .sessionAttr(SESSION_USER_ID_ATR_NAME, ID)
                        .param("flashcardId", ID.toString()
                        )
        ).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrlTemplate("/{folderId}/flashcards", ID)
        );

        verify(flashcardService).deleteFlashcardById(ID);
    }
}
