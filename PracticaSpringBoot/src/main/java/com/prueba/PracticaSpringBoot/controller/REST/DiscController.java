package com.prueba.PracticaSpringBoot.controller.REST;

import com.prueba.PracticaSpringBoot.models.Disc;
import com.prueba.PracticaSpringBoot.services.DiscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/discs")
public class DiscController {

    @Autowired
    private DiscService discService;

    @GetMapping
    public List<Disc> getAllDiscs() {
        return discService.getDiscs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disc> getDiscById(@PathVariable Long id) {
        return discService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> createOrUpdateDisc(@RequestBody Disc disc) {
        if (disc.getId() == null) {
            if (!discService.isTitleUnique(disc.getTitle())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El título ya existe. No se puede agregar el disco.");
            }
            Disc newDisc = discService.addDisc(disc);
            return ResponseEntity.status(HttpStatus.CREATED).body(newDisc);
        } else {
            Optional<Disc> existingDisc = discService.findByTitle(disc.getTitle());
            if (existingDisc.isPresent() && !existingDisc.get().getId().equals(disc.getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El título ya pertenece a otro disco.");
            }
            return ResponseEntity.ok(discService.updateDisc(disc.getId(), disc));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDisc(@PathVariable Long id) {
        discService.deleteDisc(id);
        return ResponseEntity.noContent().build();
    }
}

