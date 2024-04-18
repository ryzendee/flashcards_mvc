package com.app.flashcards.entity;

import jakarta.persistence.*;

@Entity
public class FlashcardFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
