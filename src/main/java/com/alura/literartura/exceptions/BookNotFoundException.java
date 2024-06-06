package com.alura.literartura.exceptions;

public class BookNotFoundException extends  Exception{
    public BookNotFoundException(String mensaje){
        super(mensaje);
    }
}
