package com.example.catalogservice.web;

import com.example.catalogservice.domain.Book;
import com.example.catalogservice.domain.BookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    public Iterable<Book> get(){
        return bookService.viewBookList();
    }
    @GetMapping("/{isbn}")
    public Book getByIsbn(@PathVariable String isbn){
        return bookService.viewBookDetail(isbn);
    }

    @PostMapping
    public Book post(@Valid @RequestBody Book book){
        return bookService.addBookToCatalog(book);
    }
    @DeleteMapping("/{isbn}")
    public void delete(@PathVariable String isbn){
        bookService.removeBookFromCatalog(isbn);
    }
    @PutMapping("/{isbn}")
    public Book put(@PathVariable String isbn, @Valid @RequestBody Book book){
        return bookService.editBookDetails(isbn,book);
    }
}
