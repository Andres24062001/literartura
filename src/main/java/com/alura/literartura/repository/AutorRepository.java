package com.alura.literartura.repository;

import com.alura.literartura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombre(String nombre);

    List<Autor> findByFechaNacimientoLessThanEqualAndFechaMuerteGreaterThanEqual(int fechaNacimiento, int fechaMuerte);

}
