package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class LibraryWorker {
    private static final Library library = new Library();
    private static final Random random = new Random();

    static {
        Sample.seedSample();
    }


    public static void deleteBook(String isbn) {
        var existingBookDto = getBookByISBN(isbn);
        if (existingBookDto == null) {
            throw new IllegalArgumentException("Book with this ISBN does not exist: " + isbn);
        }
        removeBook(existingBookDto.section, existingBookDto.isbn, existingBookDto.shelf);

    }

    public static void updateBook(BookDto bookDto, String isbn) {
        var existingBookDto = getBookByISBN(isbn);
        if (existingBookDto == null) {
            throw new IllegalArgumentException("Book with this ISBN does not exist: " + bookDto.isbn);
        }

        removeBook(existingBookDto.section, existingBookDto.isbn, existingBookDto.shelf);

        // Add the updated book
        addBook(bookDto);
    }

    private static void removeBook(String section, String isbn, String shelf) {
        // Remove the existing book
        var varSection = library.sections.get(section);
        if (varSection != null) {
            var varShelf = varSection.shelves.get(shelf);
            if (varShelf != null) {
                varShelf.booksByIsbn.remove(isbn);
            }
        }
    }

    public static void addBook(BookDto dto) {
        if (!isbnChecker(dto.isbn)) {
            throw new IllegalArgumentException("Incorrect ISBN, must be like this 978-3-16-148410-0: " + dto.isbn);
        }

        var section = library.sections.computeIfAbsent(dto.section, name -> new Section(name));
        var shelf = section.shelves.computeIfAbsent(dto.shelf, name -> new Shelf(name));
        var book = BookMapper.toDomain(dto);
        if (shelf.booksByIsbn.containsKey(book.getIsbn()))
            throw new IllegalArgumentException("Book with this ISBN already exists on this shelf: " + book.getIsbn());

        shelf.booksByIsbn.put(book.getIsbn(), book);
    }

    public static List<BookDto> getBooksBySection(String sectionName) {
        var section = library.sections.get(sectionName);
        if (section == null) return List.of();
        return section.shelves.values().stream()
                .flatMap(shelf ->
                        shelf.booksByIsbn.values().stream()
                                .map(book -> BookMapper.toDto(book, section.name, shelf.name, book.getNumberOfLeft(), book.getNumberOfLoaned())))
                .toList();
    }

    public static List<BookDto> getBooksByShelf(String shelfName) {
        String sectionName = "";
        for (var section : library.sections.values()) {
            if (section.shelves.containsKey(shelfName)) {
                sectionName = section.name;
                break;
            }
        }

        if (sectionName.isEmpty()) return List.of();

        var section = library.sections.get(sectionName);
        if (section == null) return List.of();
        return section.shelves.values().stream()
                .flatMap(shelf -> shelf.booksByIsbn.values().stream())
                .map(book -> BookMapper.toDto(book, section.name, shelfName, book.getNumberOfLeft(), book.getNumberOfLoaned()))
                .toList();
    }

    public static BookDto getBookByISBN(String isbn) {
        return library.sections.values().stream()
                .flatMap(section ->
                        section.shelves.values().stream()
                                .flatMap(shelf -> shelf.booksByIsbn.values().stream()
                                        .filter(book -> book.getIsbn().equals(isbn))
                                        .map(book -> BookMapper.toDto(book, section.name, shelf.name, book.getNumberOfLeft(), book.getNumberOfLoaned()))))
                .findFirst()
                .orElse(null);
    }

    public static List<BookDto> getAllBooks() {
        return library.sections.values().stream()
                .flatMap(section ->
                        section.shelves.values().stream()
                                .flatMap(shelf ->
                                        shelf.booksByIsbn.values().stream()
                                                .map(book -> BookMapper.toDto(book, section.name, shelf.name, book.getNumberOfLeft(), book.getNumberOfLoaned()))))
                .toList();
    }


    public static boolean isbnChecker(String isbn) {
        return isbn != null && isbn.matches("^(97[89])[- ]?\\d{1,5}[- ]?\\d{1,7}[- ]?\\d{1,7}[- ]?\\d$");
    }


    public static class Sample {
        private static void seedSample() {
            String s = "04.02.2026";
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate date = LocalDate.parse(s, fmt);

            String section = "Fiction";

            seedShelf(section, "A1", List.of(
                    new Book("978-0-14-312656-0", "George Orwell, J.D. Salinger", "1984", date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 3, 0),
                    new Book("978-0-06-112008-4", "Harper Lee", "To Kill a Mockingbird", date.minusDays(random.nextInt(999)), BookState.LOANED, 0, 5),
                    new Book("978-0-7432-7356-5", "F. Scott Fitzgerald", "The Great Gatsby", date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 2, 0)
            ));

            seedShelf(section, "A2", List.of(
                    new Book("978-0-553-21311-7", "Frank Herbert, J.D. Salinger", "Dune", date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 4, 0),
                    new Book("978-0-345-33968-3", "J.R.R. Tolkien", "The Hobbit", date.minusDays(random.nextInt(999)), BookState.LOANED, 0, 4),
                    new Book("978-0-316-76948-0", "J.D. Salinger", "The Catcher in the Rye", date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 4, 0)
            ));

            seedShelf(section, "A3", List.of(
                    new Book("978-0-452-28423-4", "Ray Bradbury, Fyodor Dostoevsky", "Fahrenheit 451", date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 4, 0),
                    new Book("978-0-141-43960-0", "Jane Austen", "Pride and Prejudice", date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 4, 0),
                    new Book("978-0-14-044926-6", "Fyodor Dostoevsky", "Crime and Punishment", date.minusDays(random.nextInt(999)), BookState.LOANED, 0, 4)
            ));

            section = "Action";

            seedShelf(section, "A1", List.of(
                    new Book("978-0-14-113408-0", "George Orwell", "1984 " + random.nextInt(100), date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 4, 0),
                    new Book("978-0-06-113408-4", "Harper Lee", "To Kill a Mockingbird " + random.nextInt(100), date.minusDays(random.nextInt(999)), BookState.LOANED, 0, 4),
                    new Book("978-0-7432-113408-5", "F. Scott Fitzgerald", "The Great Gatsby " + random.nextInt(100), date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 4, 0)
            ));

            seedShelf(section, "A2", List.of(
                    new Book("978-0-553-113408-7", "Frank Herbert", "Dune " + random.nextInt(100), date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 4, 0),
                    new Book("978-0-345-113408-3", "J.R.R. Tolkien", "The Hobbit " + random.nextInt(100), date.minusDays(random.nextInt(999)), BookState.LOANED, 0, 4),
                    new Book("978-0-316-113408-0", "J.D. Salinger", "The Catcher in the Rye " + random.nextInt(100), date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 4, 0)
            ));

            seedShelf(section, "A3", List.of(
                    new Book("978-0-452-113408-4", "Ray Bradbury", "Fahrenheit 451 " + random.nextInt(100), date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 4, 0),
                    new Book("978-0-141-113408-0", "Jane Austen", "Pride and Prejudice " + random.nextInt(100), date.minusDays(random.nextInt(999)), BookState.IN_STOCK, 4, 0),
                    new Book("978-0-14-113408-6", "Fyodor Dostoevsky", "Crime and Punishment " + random.nextInt(100), date.minusDays(random.nextInt(999)), BookState.LOANED, 0, 4)
            ));
        }

        private static void seedShelf(String sectionName, String shelfName, List<Book> books) {
            var section = LibraryWorker.library.sections.computeIfAbsent(sectionName, Section::new);
            var shelf = section.shelves.computeIfAbsent(shelfName, Shelf::new);

            for (var b : books) {
                shelf.booksByIsbn.put(b.getIsbn(), b);
            }
        }
    }
}