package com.example.catalogservice.domain;

import com.example.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findAllBooks() {
        var book1 = Book.of("1234561235", "Title", "Author", 12.90, "Polarsophia");
        var book2 = Book.of("1234561236", "Another Title", "Author", 12.90, "Polarsophia");
        jdbcAggregateTemplate.insert(book1);
        jdbcAggregateTemplate.insert(book2);

        Iterable<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(2);
        assertThat(books).allMatch(book -> book.isbn().equals(book1.isbn()) || book.isbn().equals(book2.isbn()));
    }

    @Test
    void findBookByIsbnWhenExisting(){
        var bookIsbn = "1234561237";
        var book = Book.of(bookIsbn, "Title", "Author", 12.90, "Polarsophia");
        jdbcAggregateTemplate.insert(book);

        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);
        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(book.isbn());
    }

    @Test
    void findBookByIsbnWhenNotExisting(){
        Optional<Book> actualBook = bookRepository.findByIsbn("1234561237");
        assertThat(actualBook).isEmpty();
    }

    @Test
    void existsByIsbnWhenExisting() {
        var bookIsbn = "1234561239";
        var bookToCreate = Book.of(bookIsbn, "Title", "Author", 12.90, "Polarsophia");
        jdbcAggregateTemplate.insert(bookToCreate);

        boolean existsByIsbn = bookRepository.existsByIsbn(bookIsbn);

        assertThat(existsByIsbn).isTrue();
    }

    @Test
    void existsByIsbnWhenNotExisting(){
        boolean existsByIsbn = bookRepository.existsByIsbn("1234561239");
        assertThat(existsByIsbn).isFalse();
    }

    @Test
    void deleteByIsbn(){
        var bookIsbn = "1234561241";
        var bookToCreate = Book.of(bookIsbn, "Title", "Author", 12.90, "Polarsophia");
        var persistedBook = jdbcAggregateTemplate.insert(bookToCreate);

        bookRepository.deleteByIsbn(bookIsbn);
        assertThat(jdbcAggregateTemplate.findById(persistedBook.id(),Book.class)).isNull();
    }
}
