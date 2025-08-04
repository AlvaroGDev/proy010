package es.cic.curso2025.proy009.service;

public class EntidadNoEncontradaException extends RuntimeException {

    public EntidadNoEncontradaException() {
        super("Entidad no encontrada");
    }

    public EntidadNoEncontradaException(String message) {
        super(message);
    }

    public EntidadNoEncontradaException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
