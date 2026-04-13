package com.prueba.PracticaSpringBoot.controller.REST;

import com.prueba.PracticaSpringBoot.models.Films;
import com.prueba.PracticaSpringBoot.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @GetMapping
    public List<Films> getAllFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Films> getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> createOrUpdateFilm(@RequestBody Films film) {
        if (film.getId() == null) {
            Optional<Films> existingFilm = filmService.getFilmsByTitle(film.getTitle()).stream().findFirst();
            if (existingFilm.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El título ya existe. No se puede agregar la película.");
            }
            Films newFilm = filmService.addFilm(film);
            return ResponseEntity.status(HttpStatus.CREATED).body(newFilm);
        } else {
            Optional<Films> existingFilm = filmService.getFilmsByTitle(film.getTitle()).stream().findFirst();
            if (existingFilm.isPresent() && !existingFilm.get().getId().equals(film.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El título ya pertenece a otra película.");
            }
            return ResponseEntity.ok(filmService.updateFilm(film.getId(), film));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        return ResponseEntity.noContent().build();
    }
}

