package com.prueba.PracticaSpringBoot.services;

import com.prueba.PracticaSpringBoot.models.Book;
import com.prueba.PracticaSpringBoot.models.Disc;
import com.prueba.PracticaSpringBoot.repository.DiscRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DiscService {
    @Autowired
    private DiscRepository discRepository;

    public Optional<Disc> findById(Long id) {
        return discRepository.findById(id);
    }

    public List<Disc> getDiscs() {
        return (ArrayList<Disc>) discRepository.findAll();
    }

    public List<Disc> getDiscsByTitle(String title) {
        return discRepository.findByTitle(title);
    }

    public List<Disc> getDiscByTracks(int Tracks)
    {
        return discRepository.findByTracks(Tracks);
    }

    public Optional<Disc> findByTitle(String title) {
        return discRepository.findByTitle(title).stream().findFirst();
    }

    public List<Disc> getDiscsByAuthor(String author) {
        return discRepository.findByAuthor(author);
    }

    public List<Disc> getDiscsByYear(LocalDate year) {
        return discRepository.findByYear(year);
    }

    public Disc addDisc(Disc disc) {
        return discRepository.save(disc);
    }

    public Disc updateDisc(Long id, Disc discDetails) {
        return discRepository.findById(id).map(disc -> {
            disc.setTitle(discDetails.getTitle());
            disc.setAuthor(discDetails.getAuthor());
            disc.setTracks(discDetails.getTracks());
            disc.setPrice(discDetails.getPrice());
            disc.setYear(discDetails.getYear());
            return discRepository.save(disc);
        }).orElseThrow(() -> new RuntimeException("Disco no encontrado con ID " + id));
    }

    public boolean deleteDisc(Long id) {
        try {
            discRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTitleUnique(String title) {
        return discRepository.findByTitle(title).isEmpty();
    }
}
