package es.cic.curso2025.proy009.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.cic.curso2025.proy009.controller.ModificacionSecurityException;

@RestControllerAdvice
public class MiControllerAdvice {
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ModificacionSecurityException.class)
    public String manejarModificacionSecurity(ModificacionSecurityException ex) {
        return ex.getMessage();
    }



}
