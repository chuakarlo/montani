package com.exam.montani.mapper;

import com.exam.montani.domain.Book;
import com.exam.montani.model.BookDetail;

public class BookDetailMapper {

    public static BookDetail toModel(Book book) {
        return BookDetail.builder()
                .book(
                        com.exam.montani.model.Book.builder()
                                .title(book.getTitle())
                                .isbn(book.getIsbn())
                                .listPrice(book.getListPrice())
                                .publicationYear(book.getPublicationYear())
                                .imageUrl(book.getImageUrl())
                                .edition(book.getEdition())
                                .authors(book.getAuthorsAsCSV())
                                .build()
                )
                .publisher(book.getPublisher().getName())
                .build();
    }
}
