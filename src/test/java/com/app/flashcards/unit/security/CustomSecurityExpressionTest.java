package com.app.flashcards.unit.security;

import com.app.flashcards.security.CustomSecurityExpression;
import com.app.flashcards.service.cardfolder.CardFolderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomSecurityExpressionTest {

    @InjectMocks
    private CustomSecurityExpression customSecurityExpression;

    @Mock
    private CardFolderService cardFolderService;

    @Test
    void isFolderOwner_existsBothIds_returnsTrue() {
        Long id = 1L;

        when(cardFolderService.existsByIdAndUserId(id, id))
                .thenReturn(true);

        boolean existsByUser = customSecurityExpression.isFolderOwner(id, id);

        assertThat(existsByUser).isTrue();
    }
}
