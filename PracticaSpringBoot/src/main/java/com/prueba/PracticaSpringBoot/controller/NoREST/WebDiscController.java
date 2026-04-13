package com.prueba.PracticaSpringBoot.controller.NoREST;

import com.prueba.PracticaSpringBoot.models.Disc;
import com.prueba.PracticaSpringBoot.services.DiscService;
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
public class WebDiscController {
    @Autowired
    private DiscService discService;

    @GetMapping("/discs")
    public String catalogoDiscos(Model model) {
        List<Disc> listado = discService.getDiscs();
        model.addAttribute("listado", listado);
        return "Discs";
    }

    @GetMapping("/discform/{id}")
    public String mostrarFormularioDisco(@PathVariable("id") Long id, Model model) {
        Optional<Disc> disc = discService.findById(id);
        if (disc.isPresent()) {
            model.addAttribute("disc", disc.get());
            return "DiscForm";
        } else {
            return "redirect:/discs";
        }
    }

    @GetMapping("/discform")
    public String formularioDisco(Model model) {
        model.addAttribute("disc", new Disc());
        return "DiscForm";
    }

    @PostMapping("/submitDisc")
    public String submitForm(@ModelAttribute("disc") Disc disc, Model model , RedirectAttributes redirectAttributes) {
        if (disc.getId() != null) {
            Optional<Disc> existingBook = discService.findById(disc.getId());

            if (existingBook.isPresent()) {
                Optional<Disc> bookWithTitle = discService.findByTitle(disc.getTitle());
                if (bookWithTitle.isPresent() && !bookWithTitle.get().getId().equals(disc.getId())) {
                    model.addAttribute("mensaje", "El título ya pertenece a otro libro.");
                    return "DiscForm";
                }

                discService.updateDisc(disc.getId(), disc);
                redirectAttributes.addFlashAttribute("mensaje", "Libro modificado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "El libro con el ID proporcionado no existe.");
            }
        } else {
            Optional<Disc> existingBook = discService.findByTitle(disc.getTitle());

            if (existingBook.isPresent()) {
                model.addAttribute("mensaje", "El título ya existe. Por favor, elija otro título.");
                return "DiscForm";
            }

            discService.addDisc(disc);
            redirectAttributes.addFlashAttribute("mensaje", "Libro agregado correctamente con ID: " + disc.getId());
        }

        return "redirect:/discs";
    }

    @GetMapping("/deleteDisc/{id}")
    public String eliminar(@PathVariable("id") Long id,
                           @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                           @RequestParam(value = "searchValue", required = false) String searchValue,
                           RedirectAttributes redirectAttributes) {
        boolean eliminado = discService.deleteDisc(id);
        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Disco eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el disco");
        }

        if (searchCriteria != null && searchValue != null) {
            return "redirect:/searchDiscs?searchCriteria=" + searchCriteria + "&searchValue=" + searchValue;
        }
        return "redirect:/discs";
    }

    @GetMapping("/searchDiscs")
    public String searchDiscs(
            @RequestParam("searchCriteria") String searchCriteria,
            @RequestParam("searchValue") String searchValue,
            Model model) {

        if (searchValue == null || searchValue.trim().isEmpty()) {
            model.addAttribute("mensaje", "Ingrese un valor para buscar.");
            model.addAttribute("listado", Collections.emptyList());
            return "discs";
        }

        String searchValueLower = searchValue.toLowerCase();
        List<Disc> discs;

        switch (searchCriteria.toLowerCase()) {
            case "title":
                discs = discService.getDiscsByTitle(searchValue.trim()).stream()
                        .filter(disc -> disc.getTitle().toLowerCase().contains(searchValueLower))
                        .collect(Collectors.toList());
                break;
            case "author":
                discs = discService.getDiscsByAuthor(searchValue.trim()).stream()
                        .filter(disc -> disc.getAuthor().toLowerCase().contains(searchValueLower))
                        .collect(Collectors.toList());
                break;
            case "tracks":
                try {
                    int trackCount = Integer.parseInt(searchValue.trim());
                    discs = discService.getDiscByTracks(trackCount);
                } catch (NumberFormatException e) {
                    model.addAttribute("mensaje", "Número de pistas no válido.");
                    discs = Collections.emptyList();
                }
                break;
            case "year":
                try {
                    LocalDate year = LocalDate.parse(searchValue.trim());
                    discs = discService.getDiscsByYear(year);
                } catch (Exception e) {
                    model.addAttribute("mensaje", "Formato de año no válido.");
                    discs = Collections.emptyList();
                }
                break;
            default:
                model.addAttribute("mensaje", "Criterio de búsqueda no válido.");
                discs = Collections.emptyList();
        }

        model.addAttribute("listado", discs);
        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("searchValue", searchValue);

        return "discs";
    }
}
