package com.app.flashcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


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
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cardFolder", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Flashcard> flashcardList = new ArrayList<>();

    public CardFolder(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CardFolder(String name, String description, String imagePath, User user) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.user = user;
    }
}
