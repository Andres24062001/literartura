package com.alura.literartura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String nombre;
    private  Integer fechaNacimiento;
    private Integer fechaMuerte;

    @OneToMany(mappedBy = "autor", cascade =  CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor(){}

    public Autor(DatosAutor autor) {
        this.nombre = autor.nombre();
        this.fechaNacimiento = autor.fechaNacimiento();
        this.fechaMuerte = autor.fechaMuerte();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(Integer fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return
                "\n Id=" + Id +
                "\n nombre= " + nombre +
                "\n fechaNacimiento= " + fechaNacimiento +
                "\n fechaMuerte= " + fechaMuerte ;

    }
}
