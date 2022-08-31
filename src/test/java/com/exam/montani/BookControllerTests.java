package com.exam.montani;

import com.exam.montani.controller.BookController;
import com.exam.montani.model.BookDetail;
import com.exam.montani.service.BookDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookControllerTests {

    String validISBN13 = "9780306406157";
    String validISBN13NotFound = "9780306406157";
    @MockBean
    private BookDetailService bookDetailService;
    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setup() {
        BookDetail bookDetail = BookDetail.builder()
                .book(
                        com.exam.montani.model.Book.builder()
                                .title("Title")
                                .isbn(validISBN13)
                                .listPrice(BigDecimal.ZERO)
                                .publicationYear("2022")
                                .imageUrl("")
                                .edition("1st")
                                .authors("Author 1, Author - 2")
                                .build()
                )
                .publisher("Publisher")
                .build();

        bookDetailService = mock(BookDetailService.class);
        when(bookDetailService.isIsbnValid(validISBN13)).thenReturn(true);
        when(bookDetailService.getBookDetails(validISBN13)).thenReturn(bookDetail);
        when(bookDetailService.convertIsbn(validISBN13)).thenReturn("0-306-40615-2");
        bookController = new BookController(bookDetailService);
    }

    @Test
    public void givenValidISBN13_thenReturn200() {
        ResponseEntity<BookDetail> bookDetailResponseEntity = bookController.getDetails(validISBN13);
        assertThat(bookDetailResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void givenValidISBN13_whenNotFound_thenReturn404() {
        when(bookDetailService.isIsbnValid(validISBN13NotFound)).thenReturn(true);
        when(bookDetailService.getBookDetails(validISBN13NotFound)).thenReturn(null);

        ResponseEntity<BookDetail> bookDetailResponseEntity = bookController.getDetails(validISBN13NotFound);

        assertThat(bookDetailResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenInvalidISBN13_thenReturn400() {
        ResponseEntity<BookDetail> bookDetailResponseEntity = bookController.getDetails("123123123");
        assertThat(bookDetailResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenValidISBN13_whenConvert_thenReturn200() {
        ResponseEntity<String> stringResponseEntity = bookController.convertIsbn(validISBN13);
        assertThat(stringResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void givenValidISBN13_whenConvertAndNotFound_thenReturn404() {
        when(bookDetailService.isIsbnValid(validISBN13NotFound)).thenReturn(true);
        when(bookDetailService.convertIsbn(validISBN13NotFound)).thenReturn(null);

        ResponseEntity<String> stringResponseEntity = bookController.convertIsbn(validISBN13);

        assertThat(stringResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenInvalidISBN13_whenConvert_thenReturn400() {
        ResponseEntity<String> stringResponseEntity = bookController.convertIsbn("123123123");
        assertThat(stringResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
