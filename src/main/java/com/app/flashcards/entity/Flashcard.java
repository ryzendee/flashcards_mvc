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

        @Column(columnDefinition = "TEXT")
        private String imageUrl;

        @ManyToOne
        @JoinColumn(name = "folder_id")
        @ToString.Exclude
        private CardFolder cardFolder;

        public Flashcard(String name, String definition, String imageUrl, CardFolder cardFolder) {
            this.name = name;
            this.definition = definition;
            this.imageUrl = imageUrl;
            this.cardFolder = cardFolder;
        }
    }
