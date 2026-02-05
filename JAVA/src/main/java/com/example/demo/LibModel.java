package com.example.demo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



class Library {
    public final Map<String, Section> sections = new HashMap<>();
}

class Section {
    public final String name;
    public final Map<String, Shelf> shelves = new HashMap<>();

    public Section(String name) {
        this.name = name;
    }
}

class Shelf {
    public final String name;
    public final Map<String, Book> booksByIsbn = new HashMap<>();

    public Shelf(String name) {
        this.name = name;
    }
}

class Book {

    public int getNumberOfLoaned() {
        return numberOfLoaned;
    }

    public Book setNumberOfLoaned(int numberOfLoaned) {
        this.numberOfLoaned = numberOfLoaned;
        return this;
    }

    public int getNumberOfLeft() {
        return numberOfLeft;
    }

    public Book setNumberOfLeft(int numberOfLeft) {
        this.numberOfLeft = numberOfLeft;
        return this;
    }

    public BookState getState() {
        return state;
    }

    public Book setState(BookState state) {
        this.state = state;
        return this;
    }

    public LocalDate getYear() {
        return year;
    }

    public Book setYear(LocalDate year) {
        this.year = year;
        return this;
    }

    public String getName() {
        return name;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getAuthor() {
        return author;
    }

    public Book setAuthor(List<String> author) {
        this.author = author;
        return this;
    }

    public String getIsbn() {
        return isbn;
    }

    public Book setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    private String isbn;
    private List<String> author;
    private String name;
    private LocalDate year;
    private BookState state;
    private int numberOfLeft = 3;
    private int numberOfLoaned = 0;

    public Book(String isbn, String author, String name, LocalDate year, BookState state, int numberOfLeft, int numberOfLoaned) {
        this.isbn = isbn;
        this.author = Arrays.stream(author.split(",\\s*")).toList();
        this.name = name;
        this.year = year;
        this.state = state;
        this.numberOfLeft = numberOfLeft;
        this.numberOfLoaned = numberOfLoaned;

    }
}

class BookMapper {
    static Book toDomain(BookDto dto) {
        return new Book(dto.isbn, dto.author, dto.name, dto.year, dto.state, dto.numberOfLeft, dto.numberOfLoaned);
    }

    static BookDto toDto(Book book, String section, String shelf, int numberOfLeft, int numberOfLoaned) {
        return new BookDto(book.getIsbn(), String.join(", ", book.getAuthor()), book.getName(), book.getYear(), book.getState(), section, shelf, numberOfLeft, numberOfLoaned);
    }
}