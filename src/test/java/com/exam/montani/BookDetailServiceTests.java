package com.exam.montani;

import com.exam.montani.domain.Author;
import com.exam.montani.domain.Book;
import com.exam.montani.domain.Publisher;
import com.exam.montani.model.BookDetail;
import com.exam.montani.repository.BookRepository;
import com.exam.montani.service.BookDetailService;
import com.exam.montani.service.impl.BookDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookDetailServiceTests {

    String validISBN13 = "9780306406157";
    String validISBN10 = "0306406152";
    @MockBean
    private BookRepository bookRepository;
    @InjectMocks
    private BookDetailService bookDetailService;

    @BeforeEach
    public void setup() {
        Author author1 = Author.builder()
                .firstName("Author")
                .lastName("1")
                .middleName("")
                .build();

        Author author2 = Author.builder()
                .firstName("Author")
                .lastName("2")
                .middleName("-")
                .build();

        Publisher publisher = Publisher.builder()
                .name("Publisher")
                .build();

        Book book = Book.builder()
                .title("Title")
                .isbn(validISBN13)
                .listPrice(BigDecimal.ZERO)
                .publicationYear("2022")
                .publisher(publisher)
                .imageUrl("")
                .edition("1st")
                .authors(new HashSet<>(Arrays.asList(author2, author1)))
                .build();

        bookRepository = mock(BookRepository.class);
        when(bookRepository.findOneByIsbn(validISBN13)).thenReturn(book);
        bookDetailService = new BookDetailServiceImpl(bookRepository);
    }

    @Test
    public void givenValidISBN10_thenReturnTrue() {
        assertThat(bookDetailService.isIsbnValid(validISBN10)).isEqualTo(true);
    }

    @Test
    public void givenInvalidISBN10_thenReturnFalse() {
        String isbn = "0306406157";
        assertThat(bookDetailService.isIsbnValid(isbn)).isEqualTo(false);
    }

    @Test
    public void givenValidISBN13_thenReturnTrue() {
        assertThat(bookDetailService.isIsbnValid(validISBN13)).isEqualTo(true);
    }

    @Test
    public void givenInvalidISBN13_thenReturnFalse() {
        String isbn = "0000306406157";
        assertThat(bookDetailService.isIsbnValid(isbn)).isEqualTo(false);
    }

    @Test
    public void givenValidISBN13_thenReturnConvertedISBN10() {
        assertThat(bookDetailService.convertIsbn(validISBN13)).isEqualTo("0-306-40615-2");
    }

    @Test
    public void givenValidISBN10_thenReturnConvertedISBN13() {
        assertThat(bookDetailService.convertIsbn(validISBN10)).isEqualTo("978-0-306-40615-7");
    }

    @Test
    public void givenInvalidISBN_thenReturnNull() {
        String isbn = "306406157";
        assertThat(bookDetailService.convertIsbn(isbn)).isNull();
    }

    @Test
    public void givenValidISBN13_thenReturnBookDetail() {
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

        assertThat(bookDetailService.getBookDetails(validISBN13))
                .usingRecursiveComparison().isEqualTo(bookDetail);
    }

    @Test
    public void givenInvalidISBN13_thenReturnNull() {
        assertThat(bookDetailService.getBookDetails("123123")).isNull();
    }
}
