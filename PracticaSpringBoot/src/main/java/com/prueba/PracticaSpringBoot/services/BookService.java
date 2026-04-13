package com.prueba.PracticaSpringBoot.services;

import com.prueba.PracticaSpringBoot.models.Book;
import com.prueba.PracticaSpringBoot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public ArrayList<Book> getBooks(){
        return (ArrayList<Book>) bookRepository.findAll();
    }
    public ArrayList<Book> getBooksByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public ArrayList<Book> getBooksByTitle(String titulo) {
        return bookRepository.findByTitle(titulo);
    }

    public Optional<Book> findByTitle(String titulo) {
        return bookRepository.findByTitle(titulo).stream().findFirst();
    }

    public ArrayList<Book> getBooksByAuthor(String autor) {
        return bookRepository.findByAuthor(autor);
    }

    public ArrayList<Book> getBooksByPublisher(String publisher) {
        return bookRepository.findByPublisher(publisher);
    }

    public Book addBook(Book libro){
        return bookRepository.save(libro);
    }

    public Book updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setIsbn(bookDetails.getIsbn());
            book.setPublisher(bookDetails.getPublisher());
            book.setPrice(bookDetails.getPrice());
            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Libro no encontrado con ID " + id));
    }

    public boolean deleteBook(Long id){
        try {
            bookRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTitleUnique(String title) {
        List<Book> books = getBooks();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return false;
            }
        }
        return true;
    }
}
