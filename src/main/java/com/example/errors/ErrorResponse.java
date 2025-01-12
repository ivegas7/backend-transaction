package com.example.errors;

import lombok.Data;

@Data
public class ErrorResponse {
    private int statusCode;  // Código de estado HTTP
    private String error;     // Descripción del error
    private String message;   // Mensaje detallado sobre el error
    private String path;      // Ruta de la solicitud que causó el error
    private long timestamp;   // Marca temporal del error

    public ErrorResponse(int statusCode, String error, String message, String path) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
        this.path = path;
        this.timestamp = System.currentTimeMillis();
    }
}
