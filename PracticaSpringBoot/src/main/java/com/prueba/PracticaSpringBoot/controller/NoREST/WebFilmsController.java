package com.prueba.PracticaSpringBoot.controller.NoREST;

import com.prueba.PracticaSpringBoot.Enum.Format;
import com.prueba.PracticaSpringBoot.models.Films;
import com.prueba.PracticaSpringBoot.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class WebFilmsController {

    @Autowired
    private FilmService filmService;

    @GetMapping("/films")
    public String catalogoPeliculas(Model model) {
        List<Films> listado = filmService.getFilms();
        model.addAttribute("listado", listado);
        return "Films";
    }

    @GetMapping("/filmform/{id}")
    public String mostrarFormularioPelicula(@PathVariable("id") Long id, Model model) {
        Optional<Films> film = filmService.getFilmById(id);
        if (film.isPresent()) {
            model.addAttribute("film", film.get());
            return "FilmForm";
        } else {
            return "redirect:/films";
        }
    }

    @GetMapping("/filmform")
    public String formularioPelicula(Model model) {
        model.addAttribute("film", new Films());
        return "FilmForm";
    }

    @PostMapping("/submitFilm")
    public String submitForm(@ModelAttribute("film") Films film, Model model, RedirectAttributes redirectAttributes) {
        if (film.getId() != null) {
            Optional<Films> existingFilm = filmService.getFilmById(film.getId());

            if (existingFilm.isPresent()) {
                Optional<Films> filmWithTitle = filmService.getFilmsByTitle(film.getTitle()).stream().findFirst();
                if (filmWithTitle.isPresent() && !filmWithTitle.get().getId().equals(film.getId())) {
                    model.addAttribute("mensaje", "El título ya pertenece a otra película.");
                    return "FilmForm";
                }

                filmService.updateFilm(film.getId(), film);
                redirectAttributes.addFlashAttribute("mensaje", "Película modificada correctamente");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "La película con el ID proporcionado no existe.");
            }
        } else {
            Optional<Films> existingFilm = filmService.getFilmsByTitle(film.getTitle()).stream().findFirst();

            if (existingFilm.isPresent()) {
                model.addAttribute("mensaje", "El título ya existe. Por favor, elija otro título.");
                return "FilmForm";
            }

            filmService.addFilm(film);
            redirectAttributes.addFlashAttribute("mensaje", "Película agregada correctamente con ID: " + film.getId());
        }

        return "redirect:/films";
    }

    @GetMapping("/deleteFilms/{id}")
    public String eliminar(@PathVariable("id") Long id,
                           @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                           @RequestParam(value = "searchValue", required = false) String searchValue,
                           RedirectAttributes redirectAttributes) {
        boolean eliminado = filmService.deleteFilm(id);
        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Película eliminada correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la película");
        }

        if (searchCriteria != null && searchValue != null) {
            return "redirect:/searchFilms?searchCriteria=" + searchCriteria + "&searchValue=" + searchValue;
        }
        return "redirect:/films";
    }

    @GetMapping("/searchFilms")
    public String searchFilms(
            @RequestParam("searchCriteria") String searchCriteria,
            @RequestParam("searchValue") String searchValue,
            Model model) {

        if (searchValue == null || searchValue.trim().isEmpty()) {
            model.addAttribute("mensaje", "Ingrese un valor para buscar.");
            model.addAttribute("listado", Collections.emptyList());
            return "films";
        }

        String searchValueLower = searchValue.toLowerCase();
        List<Films> films;

        switch (searchCriteria.toLowerCase()) {
            case "title":
                films = filmService.getFilmsByTitle(searchValue.trim()).stream()
                        .filter(film -> film.getTitle().toLowerCase().contains(searchValueLower))
                        .collect(Collectors.toList());
                break;
            case "year":
                try {
                    films = filmService.getFilmsByYear(LocalDate.parse(searchValue.trim()));
                } catch (NumberFormatException e) {
                    model.addAttribute("mensaje", "El año debe ser un número válido.");
                    films = Collections.emptyList();
                }
                break;
            case "format":
                try {
                    Format format = Format.valueOf(searchValue.trim().toUpperCase());
                    films = filmService.getFilmsByFormat(format);
                } catch (IllegalArgumentException e) {
                    model.addAttribute("mensaje", "Formato inválido.");
                    films = Collections.emptyList();
                }
                break;
            case "film":
                films = filmService.getFilmsByFilmDirector(searchValue.trim()).stream()
                        .filter(film -> film.getFilmDirector().toLowerCase().contains(searchValueLower))
                        .collect(Collectors.toList());
                break;
            default:
                model.addAttribute("mensaje", "Criterio de búsqueda no válido.");
                films = Collections.emptyList();
        }

        model.addAttribute("listado", films);
        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("searchValue", searchValue);

        return "films";
    }
}
