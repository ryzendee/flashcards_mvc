package com.app.flashcards.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(
        name = "card_folders"
)
public class CardFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CardFolder(String name, String description, String imageUrl, User user) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.user = user;
    }
}
