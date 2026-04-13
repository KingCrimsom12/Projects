package com.prueba.PracticaSpringBoot.repository;

import com.prueba.PracticaSpringBoot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
       public abstract ArrayList<Book> findByIsbn(String isbn);
       public abstract ArrayList<Book> findByTitle(String title);
       public abstract ArrayList<Book> findByAuthor(String author);
       public abstract ArrayList<Book> findByPublisher(String publisher);
}