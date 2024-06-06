package com.alura.literartura.validations;

import com.alura.literartura.exceptions.BookNotFoundException;
import com.alura.literartura.exceptions.InvalidOptionsException;
import com.alura.literartura.model.DatosResultados;

public class Validations {

    //Validacion de datos de entrada
    public static void verifyMenuInputIsValid(int input) throws Exception {
        if (input < 0 || input > 6) {
            throw new InvalidOptionsException("Opción inválida, intente de nuevo con las opciones disponibles en el menú.");
        }
    }

    //Validacion de Datos para API
    public static void verifyIsnotNullData(DatosResultados data, String bookTitle) throws BookNotFoundException {
        if (data.resultados().isEmpty() || data.resultados() == null){
            throw new BookNotFoundException("Lo sentimos, el libro con título "+ bookTitle + " no se encontró.");
        }
    }

    //Validaciones de datos de entrada para libros en caso de que tengamos múltiples coincidencias en la búsqueda en la API

    public static void verifyGutendexInputIsValid(int input, int elementsNumber) throws InvalidOptionsException {
        if (input < 0 || input > elementsNumber) {
            throw new InvalidOptionsException("Opción inválida, intente de nuevo con las opciones disponibles en el menú.");
        } else if (input == 0) {
            System.out.println("Programa finalizado. Cerrando aplicación...");
            System.exit(0);
        }
    }

  //Validacion de Datos de entrada para el AÑO

    public static  void verifyYearsFormat(int inputYear) throws InvalidOptionsException {
        if (inputYear < -4000 || inputYear > 2024) {
            throw new InvalidOptionsException("Opción inválida, intente con otros valores");

        }
    }

    //Validación de datos de entrada para buscar libros por idioma
    public static void verifyOptionsForLanguageMeu(int input) throws InvalidOptionsException {
        if (input > 7 || input < 1){
            throw new InvalidOptionsException("Opción inválida, seleccione una opción del menú.");
        }
    }


}
