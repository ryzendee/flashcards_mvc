package com.app.flashcards.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(
        name = "flashcards"
)public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String definition;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private CardFolder cardFolder;

    public Flashcard(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }
}
