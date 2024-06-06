package com.alura.literartura.model;

import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
@Table(name = "Libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String titulo;
    @ManyToOne
    private  Autor autor;
    @Enumerated(EnumType.STRING)
    private IdiomaOpciones idioma;
    private  int descargas;

    public Libro(){}

    public Libro(DatosLibros libro, Autor autor) {
        this.titulo = libro.titulo();
        this.idioma = libro.idiomas().stream()
                .map(IdiomaOpciones :: obtenerNombrePorCodigo)
                .collect(Collectors.toList())
                .get(0);
        this.descargas = libro.descargas();
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public IdiomaOpciones getIdioma() {
        return idioma;
    }

    public void setIdioma(IdiomaOpciones idioma) {
        this.idioma = idioma;
    }

    public int getDescargas() {
        return descargas;
    }

    public void setDescargas(int descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return

                ", titulo='" + titulo + '\'' +
                ", autor=" + autor +
                ", idioma=" + idioma +
                ", descargas=" + descargas ;

    }
}
