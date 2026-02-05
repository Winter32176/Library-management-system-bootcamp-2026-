package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/lib")
public class LibController {
    @GetMapping("/books")
    public List<BookDto> getAllBooks() {
        var a= LibraryWorker.getAllBooks();
        return a;
    }

    @GetMapping("/books/{isbn}")
    public BookDto getByIsbn(@PathVariable String isbn) {
        return LibraryWorker.getBookByISBN(isbn);
    }

    @GetMapping("/search/shelf/{shelf}")
    public List<BookDto> getByShelf(@PathVariable String shelf) {
        return LibraryWorker.getBooksByShelf(shelf);
    }

    @GetMapping("/search/section/{section}")
    public List<BookDto> getBySection(@PathVariable String section) {
        return LibraryWorker.getBooksBySection(section);
    }

    @PostMapping("/add")
    public void addBook(@RequestBody BookDto book) {
        LibraryWorker.addBook(book);
    }

    @DeleteMapping("/delete/{isbn}")
    public void deleteByIsbn(@PathVariable String isbn) {
        LibraryWorker.deleteBook(isbn);
    }

    @PutMapping("/update/{isbn}")
    public void updateBook(@PathVariable String isbn, @RequestBody BookDto book) {
       LibraryWorker.updateBook(book, isbn);
    }

}
