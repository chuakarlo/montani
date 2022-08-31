package com.exam.montani.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class Book {

    private String title;

    private String isbn;

    private BigDecimal listPrice;

    private String publicationYear;

    private String imageUrl;

    private String edition;

    private String authors;
}
