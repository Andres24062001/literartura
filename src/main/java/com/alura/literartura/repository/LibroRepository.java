package com.alura.literartura.repository;

import com.alura.literartura.model.IdiomaOpciones;
import com.alura.literartura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByTituloContainsIgnoreCase(String nombreLibro);


    List<Libro> findByIdioma(IdiomaOpciones idioma);

    Libro findByTitulo(String titulo);

}
