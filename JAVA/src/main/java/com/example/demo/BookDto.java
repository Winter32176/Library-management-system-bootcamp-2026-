package com.example.demo;

import java.time.LocalDate;

public class BookDto {
    public String isbn;
    public String author;
    public String name;
    public LocalDate year;
    public BookState state;
    public String section;
    public String shelf;
    public int numberOfLeft = 3;
    public int numberOfLoaned = 0;

    public BookDto() {
    }

    public BookDto(String isbn, String author, String name, LocalDate year, BookState state, String section, String shelf, int numberOfLeft, int numberOfLoaned) {

        this.isbn = isbn;
        this.author = author;
        this.name = name;
        this.year = year;
        this.state = state;
        this.section = section;
        this.shelf = shelf;
        this.numberOfLeft = numberOfLeft;
        this.numberOfLoaned = numberOfLoaned;
    }
}