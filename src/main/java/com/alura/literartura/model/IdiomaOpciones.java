package com.alura.literartura.model;

public enum IdiomaOpciones {
    ENGLISH("en", "inglés"),
    GERMAN("de", "alemán"),
    FRENCH("fr", "francés"),
    SPANISH("es", "español"),
    ITALIAN("it", "italiano"),
    RUSSIAN("ru", "ruso"),
    CHINESE("zh", "chino"),
    PORTUGUES("pt", "portugués");

    private String idiomaCodigo;
    private String idiomaNombre;

    IdiomaOpciones(String languageCode, String languageSpanish){
        this.idiomaCodigo = languageCode;
        this.idiomaNombre = languageSpanish;
    }

    public static IdiomaOpciones obtenerNombrePorCodigo(String code) {
        for (IdiomaOpciones language: IdiomaOpciones.values()){
            if (language.idiomaCodigo.equalsIgnoreCase(code)){
                return language;
            }
        }
        throw new IllegalArgumentException("No se encontró opción de este idioma: "+ code);
    }

    public static String obtenerNombreEspañolPorCodigo(String enumName){
        try {
            IdiomaOpciones language = IdiomaOpciones.valueOf(enumName.toUpperCase());
            return language.idiomaNombre;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No se encontró opción de este idioma: " + enumName, e);
        }
    }

}

