package com.stiveen.gestionhospitalaria.dto.response;

public class LoginResponse {

    private String token;
    private String numeroDocumento;
    private String nombreCompleto;
    private String rol;

    public LoginResponse(
            String token,
            String numeroDocumento,
            String nombreCompleto,
            String rol
    ) {
        this.token = token;
        this.numeroDocumento = numeroDocumento;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getRol() {
        return rol;
    }
}