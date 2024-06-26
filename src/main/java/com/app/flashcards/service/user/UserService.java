package com.app.flashcards.service.user;

import com.app.flashcards.dto.request.SignUpDtoRequest;
import com.app.flashcards.entity.User;

public interface UserService {

    User createUser(SignUpDtoRequest signUpDtoRequest);
    User getUserById(Long userId);

}
