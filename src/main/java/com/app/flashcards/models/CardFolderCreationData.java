package com.app.flashcards.models;

import com.app.flashcards.entity.User;

public record CardFolderCreationData(User user, String imageUrl) {
}
