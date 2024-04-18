package com.app.flashcards.service.user;

import com.app.flashcards.dto.auth.SignUpDto;
import com.app.flashcards.entity.User;

public interface UserService {

    User createUser(SignUpDto signUpDto);
    User updateUser(User user);
    void deleteUserById(Long id);

}
