package com.prueba.PracticaSpringBoot.controller.NoREST;

import com.prueba.PracticaSpringBoot.models.Book;
import com.prueba.PracticaSpringBoot.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class WebBookController
{
    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public String catalogoLibros(Model model)
    {
        List<Book> listado = bookService.getBooks();
        model.addAttribute("listado", listado);
        return "books";
    }

    @GetMapping("/bookform/{id}")
    public String mostrarFormularioLibro(@PathVariable("id") Long id, Model model)
    {
        Optional<Book> book = bookService.findById(id);
        if(book.isPresent())
        {
            model.addAttribute("book", book.get());
            return "BookForm";
        }
        else
        {
            return "redirect:/books";
        }
    }

    @GetMapping("/bookform")
    public String formularioLibro (Model model)
    {
        model.addAttribute("book", new Book());
        return "BookForm";
    }

    @PostMapping("/submitLibro")
    public String submitForm(@ModelAttribute("book") Book book, Model model ,RedirectAttributes redirectAttributes) {
        if (book.getId() != null) {
            Optional<Book> existingBook = bookService.findById(book.getId());

            if (existingBook.isPresent()) {
                Optional<Book> bookWithTitle = bookService.findByTitle(book.getTitle());
                if (bookWithTitle.isPresent() && !bookWithTitle.get().getId().equals(book.getId())) {
                    model.addAttribute("mensaje", "El título ya pertenece a otro libro.");
                    return "BookForm";
                }

                bookService.updateBook(book.getId(), book);
                redirectAttributes.addFlashAttribute("mensaje", "Libro modificado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "El libro con el ID proporcionado no existe.");
            }
        } else {
            Optional<Book> existingBook = bookService.findByTitle(book.getTitle());

            if (existingBook.isPresent()) {
                model.addAttribute("mensaje", "El título ya existe. Por favor, elija otro título.");
                return "BookForm";
            }

            bookService.addBook(book);
            redirectAttributes.addFlashAttribute("mensaje", "Libro agregado correctamente con ID: " + book.getId());
        }

        return "redirect:/books";
    }

    @GetMapping("/deleteBooks/{id}")
    public String eliminar(@PathVariable("id") Long id,
                           @RequestParam(value = "searchCriteria", required = false) String searchCriteria,
                           @RequestParam(value = "searchValue", required = false) String searchValue,
                           RedirectAttributes redirectAttributes) {
        boolean eliminado = bookService.deleteBook(id);
        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Libro eliminado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el libro");
        }

        if (searchCriteria != null && searchValue != null) {
            return "redirect:/searchBooks?searchCriteria=" + searchCriteria + "&searchValue=" + searchValue;
        }
        return "redirect:/books";
    }

    @GetMapping("/searchBooks")
    public String searchBooks(
            @RequestParam("searchCriteria") String searchCriteria,
            @RequestParam("searchValue") String searchValue,
            Model model) {

        if (searchValue == null || searchValue.trim().isEmpty()) {
            model.addAttribute("mensaje", "Ingrese un valor para buscar.");
            model.addAttribute("listado", Collections.emptyList());
            return "Books";
        }

        String searchValueLower = searchValue.toLowerCase();
        List<Book> books = switch (searchCriteria.toLowerCase()) {
            case "title" -> bookService.getBooksByTitle(searchValue.trim()).stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(searchValueLower))
                    .collect(Collectors.toList());
            case "isbn" -> bookService.getBooksByIsbn(searchValue.trim()).stream()
                    .filter(book -> book.getIsbn().toLowerCase().contains(searchValueLower))
                    .collect(Collectors.toList());
            case "author" -> bookService.getBooksByAuthor(searchValue.trim()).stream()
                    .filter(book -> book.getAuthor().toLowerCase().contains(searchValueLower))
                    .collect(Collectors.toList());
            case "publisher" -> bookService.getBooksByPublisher(searchValue.trim()).stream()
                    .filter(book -> book.getPublisher().toLowerCase().contains(searchValueLower))
                    .collect(Collectors.toList());
            default -> {
                model.addAttribute("mensaje", "Criterio de búsqueda no válido.");
                yield Collections.emptyList();
            }
        };

        model.addAttribute("listado", books);
        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("searchValue", searchValue);

        return "Books";
    }
}
