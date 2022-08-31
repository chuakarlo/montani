package com.exam.montani.domain;

import com.sun.istack.internal.NotNull;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table
@Getter
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String isbn;

    @NotNull
    @Column(nullable = false)
    private BigDecimal listPrice;

    @NotNull
    @Column(nullable = false)
    private String publicationYear;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Publisher publisher;

    private String imageUrl;

    private String edition;

    @ManyToMany
    @JoinTable(
            name = "author_book",
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "author_id", referencedColumnName = "id")}
    )
    private Set<Author> authors;

    public String getAuthorsAsCSV() {
        List<String> authorList = authors.stream().map(Author::getCompleteName).collect(Collectors.toList());
        return String.join(", ", authorList);
    }
}
