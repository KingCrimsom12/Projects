package com.prueba.PracticaSpringBoot.repository;

import com.prueba.PracticaSpringBoot.models.Book;
import com.prueba.PracticaSpringBoot.models.Disc;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface DiscRepository extends CrudRepository<Disc, Long> {
    public abstract ArrayList<Disc> findByTitle(String title);
    public abstract ArrayList<Disc> findByTracks(int tracks);
    public abstract ArrayList<Disc> findByAuthor(String author);
    public abstract ArrayList<Disc> findByYear(LocalDate year);
}
