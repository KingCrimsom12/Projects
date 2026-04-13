package com.prueba.PracticaSpringBoot.services;

import com.prueba.PracticaSpringBoot.Enum.Format;
import com.prueba.PracticaSpringBoot.models.Disc;
import com.prueba.PracticaSpringBoot.models.Films;
import com.prueba.PracticaSpringBoot.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;

    public List<Films> getFilms() {
        return (ArrayList<Films>) filmRepository.findAll();
    }

    public List<Films> getFilmsByFilmDirector(String filmDirector) {
        return filmRepository.findByFilmDirector(filmDirector);
    }

    public List<Films> getFilmsByYear(LocalDate year) {
        return filmRepository.findByYear(year);
    }

    public List<Films> getFilmsByTitle(String title) {
        return filmRepository.findByTitle(title);
    }

    public List<Films> getFilmsByFormat(Format format) {
        return filmRepository.findByFormat(format);
    }

    public Optional<Films> getFilmById(Long id) {
        return filmRepository.findById(id);
    }

    public Films addFilm(Films film) {
        return filmRepository.save(film);
    }

    public Films updateFilm(Long id, Films filmDetails) {
        return filmRepository.findById(id).map(film -> {
            film.setTitle(filmDetails.getTitle());
            film.setFilmDirector(filmDetails.getFilmDirector());
            film.setYear(filmDetails.getYear());
            film.setPrice(filmDetails.getPrice());
            film.setFormat(filmDetails.getFormat());
            return filmRepository.save(film);
        }).orElseThrow(() -> new RuntimeException("Película no encontrada con ID " + id));
    }


    public boolean deleteFilm(Long id) {
        if (filmRepository.existsById(id)) {
            filmRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
