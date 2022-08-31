package com.exam.montani.service.impl;

import com.exam.montani.domain.Book;
import com.exam.montani.mapper.BookDetailMapper;
import com.exam.montani.model.BookDetail;
import com.exam.montani.repository.BookRepository;
import com.exam.montani.service.BookDetailService;
import org.springframework.stereotype.Service;

@Service
public class BookDetailServiceImpl implements BookDetailService {

    private final BookRepository bookRepository;

    public BookDetailServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDetail getBookDetails(String isbn) {
        BookDetail bookDetail = null;
        Book book = bookRepository.findOneByIsbn(isbn);

        if (book != null) {
            bookDetail = BookDetailMapper.toModel(book);
        }

        return bookDetail;
    }

    @Override
    public boolean isIsbnValid(String isbn) {
        boolean isValid = isbn.length() == 13 || isbn.length() == 10;

        if (isbn.length() == 10) {
            isValid = checkISBN10(isbn) == 0;
        } else if (isbn.length() == 13) {
            isValid = checkISBN13(isbn) == 0;
        }

        return isValid;
    }

    @Override
    public String convertIsbn(String isbn) {
        String temp;

        if (isbn.length() == 10) {
            temp = "978" + isbn;
            return formatISBN13(temp.substring(0, 12) + getCheckDigitISBN13(temp));
        } else if (isbn.length() == 13 && isbn.startsWith("978")) {
            temp = isbn.substring(3);
            return formatISBN10(temp.substring(0, 9) + getCheckDigitISBN10(temp));
        }

        return null;
    }

    private int checkISBN10(String isbn) {
        int total = 0;

        for (int index = 0; index < 10; index++) {
            total += Integer.parseInt(isbn.substring(index, index + 1)) * (10 - index);
        }

        return total % 11;
    }

    private int checkISBN13(String isbn) {
        int total = 0;
        int multiplier;

        for (int index = 0; index < 13; index++) {
            multiplier = index % 2 == 0 ? 1 : 3;
            total += Integer.parseInt(isbn.substring(index, index + 1)) * multiplier;
        }

        return total % 10;
    }

    private int getCheckDigitISBN10(String isbn) {
        int total = 0;

        for (int index = 0; index < 9; index++) {
            total += Integer.parseInt(isbn.substring(index, index + 1)) * (10 - index);
        }

        return (11 - (total % 11)) % 11;
    }

    private int getCheckDigitISBN13(String isbn) {
        int total = 0;
        int multiplier;

        for (int index = 0; index < 12; index++) {
            multiplier = index % 2 == 0 ? 1 : 3;
            total += Integer.parseInt(isbn.substring(index, index + 1)) * multiplier;
        }

        return 10 - (total % 10);
    }

    private String formatISBN13(String isbn) {
        return isbn.substring(0, 3) + "-" +
                isbn.charAt(3) + "-" +
                isbn.substring(4, 7) + "-" +
                isbn.substring(7, 12) + "-" +
                isbn.substring(12);
    }

    private String formatISBN10(String isbn) {
        return isbn.charAt(0) + "-" +
                isbn.substring(1, 4) + "-" +
                isbn.substring(4, 9) + "-" +
                isbn.substring(9);
    }
}
