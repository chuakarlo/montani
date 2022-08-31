package com.exam.montani.controller;

import com.exam.montani.model.BookDetail;
import com.exam.montani.service.BookDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookDetailService bookDetailService;

    public BookController(BookDetailService bookDetailService) {
        this.bookDetailService = bookDetailService;
    }

    @GetMapping("/details/{isbn}")
    public ResponseEntity<BookDetail> getDetails(@PathVariable String isbn) {
        isbn = isbn.replaceAll("[^0-9]", "");
        if (!bookDetailService.isIsbnValid(isbn)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        BookDetail bookDetail = bookDetailService.getBookDetails(isbn);

        if (bookDetail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(bookDetail);
        }
    }

    @GetMapping("/convert/{isbn}")
    public ResponseEntity<String> convertIsbn(@PathVariable String isbn) {
        isbn = isbn.replaceAll("[^0-9]", "");
        if (!bookDetailService.isIsbnValid(isbn)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String converted = bookDetailService.convertIsbn(isbn);

        if (converted == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(converted);
        }
    }
}
