package com.exam.montani.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookDetail {

    private Book book;

    private String publisher;
}
