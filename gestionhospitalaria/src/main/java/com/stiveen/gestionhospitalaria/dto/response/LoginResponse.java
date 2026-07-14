package com.stiveen.gestionhospitalaria.dto.response;

public class LoginResponse {

    private String token;
    private String usuario;
    private String nombreCompleto;
    private String rol;

    public LoginResponse(
            String token,
            String usuario,
            String nombreCompleto,
            String rol
    ) {
        this.token = token;
        this.usuario = usuario;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getRol() {
        return rol;
    }
}