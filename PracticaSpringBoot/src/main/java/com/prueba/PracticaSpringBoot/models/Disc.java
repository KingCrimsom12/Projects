package com.prueba.PracticaSpringBoot.models;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "disc")
public class Disc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true ,nullable = false)
    private Long id;
    private String title;
    private int tracks;
    private double price;
    private String author;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate year;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getYear() {
        return year;
    }

    public void setYear(LocalDate year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Disc disc = (Disc) o;
        return Objects.equals(id, disc.id) && Objects.equals(title, disc.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
