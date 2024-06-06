package com.alura.literartura.pricipal;

import com.alura.literartura.exceptions.BookNotFoundException;
import com.alura.literartura.exceptions.InvalidOptionsException;
import com.alura.literartura.model.*;
import com.alura.literartura.repository.AutorRepository;
import com.alura.literartura.repository.LibroRepository;
import com.alura.literartura.service.ConsumoAPI;
import com.alura.literartura.service.ConvierteDatos;
import com.alura.literartura.service.IConvierteDatos;
import com.alura.literartura.validations.Validations;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";

    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    private Boolean isRunningApp = true;
    private Validations validations = new Validations();


    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }


    public void muestraElMenu() {

        while (isRunningApp) {

            try {
                menu();

                System.out.print("Ingresa el número de la opción que desea ejecutar:  ");
                int opcion = teclado.nextInt();
                teclado.nextLine();
                validations.verifyMenuInputIsValid(opcion);


                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarlibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorAno();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        buscarLibrosEnElServidor();
                        break;
                    case 0:
                        isRunningApp = false;
                        System.out.println("Programa finalizado. Cerrando aplicación...");
                        System.exit(0);

                    default:
                        System.out.println("Opción inválida, intente de nuevo");
                }

            } catch (InputMismatchException e) {
                teclado.nextLine();
                System.out.println("Error: Entrada inválida. Intente de nuevo.");
            } catch (InvalidOptionsException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
            }

        }


    }


    public void menu() {
        var menu = """
                   ___________ MENU DE OPCIONES __________________
                    
                    1. Buscar libros por título 
                    2. Listar libros registrados
                    3. Listar autores registrados
                    4. Listar autores vivos en un determinado año
                    5. Listar libros por idiomas
                    6. Buscar libros por título en el servidor
                    
                    0. Salir
                    ______________________________________________
                """;
        System.out.println(menu);

    }

    private void buscarLibroPorTitulo() throws InvalidOptionsException {
        System.out.println("Ingrese el titulo del libro que desea buscar: ");
        var buscarLibrosTitulo = teclado.next();
        teclado.nextLine();
        List<Libro> listaLibro = libroRepository.findByTituloContainsIgnoreCase(buscarLibrosTitulo);

        int count = 1;
        if (!listaLibro.isEmpty()) {
            for (Libro libro : listaLibro) {

                System.out.println("\n------------------- LIBRO " + count + " -------------------");
                mostrarLibro(libro, libro.getAutor());
                System.out.println("\n------------------- ***** -------------------\n");
                count++;

            }

            Libro buscarLibro = null;

            if (listaLibro.stream().count() > 1) {
                System.out.print("Inserte el número que se encuentra en el encabezado para ver el libro que desea, si desea salir insertar 0: ");
                var selectedBook = teclado.nextInt();
                teclado.nextLine();

                validations.verifyGutendexInputIsValid(selectedBook, listaLibro.size());

                buscarLibro = listaLibro.get(selectedBook - 1);

            } else if (listaLibro.stream().count() == 1) {
                buscarLibro = listaLibro.get(0);
            } else {
                System.out.println("No se encontró ese libro en Gutendex :(");

            }

            System.out.println("\n------------------- LIBRO  ------------------- ");
            mostrarLibro(buscarLibro, buscarLibro.getAutor());
            System.out.println("\n------------------- ***** ------------------- \n");

        } else {
            System.out.println("Libro no encontrado en la base de datos");
        }


    }

    private void listarlibrosRegistrados() {

        List<Libro> librosRegistrados = libroRepository.findAll();

        if (!librosRegistrados.isEmpty()) {
            int count = 1;
            System.out.println("\nEstos son todos los libros registrados en la base de datos");
            for (Libro libro : librosRegistrados) {

                System.out.println("\n------------------- LIBRO " + count + " -------------------");
                mostrarLibro(libro, libro.getAutor());
                System.out.println("\n------------------- ***** -------------------\n");
                count++;

            }
        } else {
            System.out.println("No hay libros almacenados en la base de datos");
        }
    }

    private void listarAutoresRegistrados() {

        List<Autor> autores = autorRepository.findAll();
        System.out.println("Esos son todos los autores registrados en la base de datos");
        mostraAutores(autores);

    }

    private void listarAutoresVivosPorAno() throws InvalidOptionsException {

        System.out.println("*Para años antes de Cristo deben ser negativos (ej. -499 para 499 a.C.)");
        System.out.print("Ingresa el año que deseas: ");
        var ano = teclado.nextInt();


        validations.verifyYearsFormat(ano);
        List<Autor> buscarAutores = autorRepository.findByFechaNacimientoLessThanEqualAndFechaMuerteGreaterThanEqual(ano, ano);
        List<String> autorLibros = new ArrayList<>();
        int count = 1;

        mostraAutores(buscarAutores);


    }

    private void listarLibrosPorIdioma() throws InvalidOptionsException {

        System.out.println("""
                Menú de opciones:
                1. Inglés
                2. Alemán
                3. Español
                4. Italiano
                5. Rusia
                6. Chino
                7. Portugués
                """);
        System.out.print("Selecciona el número del idioma de los libros que deseas obtener: ");
        var idioma = teclado.nextInt();
        validations.verifyOptionsForLanguageMeu(idioma);

        IdiomaOpciones idiomaSeleccionado = null;

        switch (idioma) {
            case 1:
                idiomaSeleccionado = IdiomaOpciones.ENGLISH;
                break;
            case 2:
                idiomaSeleccionado = IdiomaOpciones.GERMAN;
                break;
            case 3:
                idiomaSeleccionado = IdiomaOpciones.SPANISH;
                break;
            case 4:
                idiomaSeleccionado = IdiomaOpciones.ITALIAN;
                break;
            case 5:
                idiomaSeleccionado = IdiomaOpciones.RUSSIAN;
                break;
            case 6:
                idiomaSeleccionado = IdiomaOpciones.CHINESE;
                break;
            case 7:
                idiomaSeleccionado = IdiomaOpciones.PORTUGUES;
                break;
            default:
                System.out.println("Error, no se encontrarón libros con ese idioma ");
        }
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idiomaSeleccionado);

        if (!librosPorIdioma.isEmpty()) {
            int count = 1;
            for (Libro libro : librosPorIdioma) {

                System.out.println("\n------------------- LIBRO " + count + " -------------------");
                mostrarLibro(libro, libro.getAutor());
                System.out.println("\n------------------- ***** -------------------\n");
                count++;

            }
        } else {
            System.out.println("No hay libros registrados en ese idioma. ");
        }
    }


    private void buscarLibrosEnElServidor() throws InvalidOptionsException {

        DatosResultados data = obtenerLibroAPI();

        //Verify if the ResultData variable is empty
        if (!data.resultados().isEmpty()) {

            /*
             * If data (ResultData variable) is not empty, we filter to get books are not repeated.
             * foundBookList -  List to have all books info get from Gutendex
             * uniqueTitles  -  Set to save titles that are not repeated
             * uniqueBooks   -  Array to save books that are not repeated
             */

            List<DatosLibros> librosEncontrados = data.resultados();
            Set<String> titulosUnicos = new HashSet<>();
            List<DatosLibros> librosUnicos = new ArrayList<>();

            //Filtering out duplicate books
            for (DatosLibros libros : librosEncontrados) {
                String titulo = libros.titulo();
                if (!titulosUnicos.contains(titulo)) {
                    titulosUnicos.add(titulo);
                    librosUnicos.add(libros);
                }
            }


            System.out.println("\n- | - | - | - | - | - | - ENTRADA A LA BIBLIOTECA - | - | - | - | - | - | -\n");
            int count = 1;


            String idiomaEnEspanol;
            for (DatosLibros libros : librosUnicos) {
                var idiomaEnIngles = libros.idiomas().stream().map(IdiomaOpciones::obtenerNombrePorCodigo)
                        .collect(Collectors.toList()).get(0);
                idiomaEnEspanol = IdiomaOpciones.obtenerNombreEspañolPorCodigo(String.valueOf(idiomaEnIngles));


                System.out.println(
                        "------------------- LIBRO " + count + " -------------------" +


                                "\n   Título: " + libros.titulo() +
                                "\n   Autor: " + libros.autores().get(0).nombre() +
                                "\n   Idioma: " + idiomaEnEspanol +
                                "\n   Número de descargas: " + libros.descargas() +

                                "\n------------------- ***** ------------------- \n"
                );
                count++;

            }
            System.out.println("\n- | - | - | - | - | - | - SALIDA DE LA BIBLIOTECA - | - | - | - | - | - | -\n");

            /*
             * Cases:
             *   - Only a book in uniqueBooks (array)
             *   - Choosing a book in case the uniqueBooks (array) has more than one option
             *   - Book not found.
             *
             * searchedBook - to save a book data searched by user.
             * verifyGutendexInputIsValid - Verify input data from Gutendex
             * */

            DatosLibros buscarLibros = null;

            if (librosUnicos.stream().count() > 1) {
                System.out.print("Inserte el número que se encuentra en el encabezado para almacenar el libro que desea, si desea salir insertar 0: ");
                var selectedBook = teclado.nextInt();
                teclado.nextLine();

                validations.verifyGutendexInputIsValid(selectedBook, librosUnicos.size());

                buscarLibros = librosUnicos.get(selectedBook - 1);

            } else if (librosUnicos.stream().count() == 1) {
                buscarLibros = librosUnicos.get(0);
            } else {
                System.out.println("No se encontró ese libro en Gutendex :(");
            }

            /*
             * authorData   - Get author data from the book retrieved from Gutendex
             * isBookInDB   - searching for book in db using the name retrieved from Gutendex
             * isAuthorInDB - searching for author in db using the author name of book  retrieved from Gutendex
             * */
            DatosAutor datosAutor = buscarLibros.autores().get(0);
            Libro libroDB = libroRepository.findByTitulo(buscarLibros.titulo());
            Autor AutorDB = autorRepository.findByNombre(datosAutor.nombre());

            /*
             * Cases:
             *       - It's not a match with any book in db -> Create an instance to save the retrieved book in database.
             *       - It's a match with a book in db -> Pop Message about the retrieved book is in database
             * */

            if (libroDB == null) {
                Autor autor;
                if (AutorDB == null) {

                    autor = new Autor(datosAutor);
                    autorRepository.save(autor);
                } else {
                    autor = AutorDB;
                }

                saveLibros(buscarLibros, autor);
                mostrarDatosLibro(buscarLibros, autor);

            } else {
                System.out.println("\n--- El libro ya se encuentra en la base de datos.  ---\n");
            }
        } else {
            System.out.println("\nError, no se encontró información sobre el libro o su autor.");
        }
    }


    private DatosResultados obtenerLibroAPI() {

        DatosResultados data = null;

        try {
            System.out.print("Ingrese el título del libro que desea : ");

            var tituloLibro = teclado.nextLine();
            var json = consumoAPI.obtenerDatos(URL_BASE + "/?search=" + tituloLibro.replace(" ", "%20"));
            System.out.println(json);
            data = conversor.obtenerDatos(json, DatosResultados.class);
            validations.verifyIsnotNullData(data, tituloLibro);


        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        } catch (BookNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }


    private void mostrarDatosLibro(DatosLibros libros, Autor autor) {

        String idiomaEnEspanol;

        var idiomaEnIngles = libros.idiomas().stream().map(IdiomaOpciones::obtenerNombrePorCodigo)
                .collect(Collectors.toList()).get(0);
        idiomaEnEspanol = IdiomaOpciones.obtenerNombreEspañolPorCodigo(String.valueOf(idiomaEnIngles));

        System.out.println(


                "\n   Título: " + libros.titulo() +
                        "\n   Autor: " + autor.getNombre() +
                        "\n   Idioma: " + idiomaEnEspanol +
                        "\n   Número de descargas: " + libros.descargas()

        );
    }


    private void mostrarLibro(Libro libro, Autor autor) {

        String idiomaEnEspanol;

        idiomaEnEspanol = IdiomaOpciones.obtenerNombreEspañolPorCodigo(String.valueOf(libro.getIdioma()));

        System.out.println(


                "\n   Título: " + libro.getTitulo() +
                        "\n   Autor: " + autor.getNombre() +
                        "\n   Idioma: " + idiomaEnEspanol +
                        "\n   Número de descargas: " + libro.getDescargas()
        );
    }

    private void mostraAutores(List<Autor> autores) {
        List<String> autorLibros = new ArrayList<>();
        int count = 1;
        if (!autores.isEmpty()) {
            for (Autor autor : autores) {

                System.out.println("\n [" + count + "] \n" +
                        "------------------- Autor -------------------" +"\n"+
                        autor.toString());


                for (Libro libro : autor.getLibros()) {
                    autorLibros.add(libro.getTitulo());
                }

                String librosString = String.join(", ", autorLibros);
                System.out.println("libros: " + librosString);
                System.out.println("\n------------------- ***** -------------------\n");

                count++;
            }
        } else {
            System.out.println("No se encontraron autores registrados en la base de datos.");


        }
    }

    private void saveLibros(DatosLibros libros, Autor autor) {

        Libro newLibro = new Libro(libros, autor);
        libroRepository.save(newLibro);
        System.out.println("--- Se ha guardado el libro " + libros.titulo() + "en la base de datos. ---");

    }
}









