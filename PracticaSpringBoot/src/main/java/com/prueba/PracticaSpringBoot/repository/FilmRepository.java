package com.prueba.PracticaSpringBoot.repository;

import com.prueba.PracticaSpringBoot.Enum.Format;
import com.prueba.PracticaSpringBoot.models.Disc;
import com.prueba.PracticaSpringBoot.models.Films;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public interface FilmRepository extends CrudRepository<Films, Long> {
    public abstract ArrayList<Films> findByTitle(String title);
    public abstract ArrayList<Films> findByFormat(Format format);
    public abstract ArrayList<Films> findByFilmDirector(String filmDirector);
    public abstract ArrayList<Films> findByYear(LocalDate year);
}