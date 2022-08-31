package com.exam.montani.service;

import com.exam.montani.model.BookDetail;

public interface BookDetailService {

    BookDetail getBookDetails(String isbn);

    boolean isIsbnValid(String isbn);

    String convertIsbn(String isbn);
}
