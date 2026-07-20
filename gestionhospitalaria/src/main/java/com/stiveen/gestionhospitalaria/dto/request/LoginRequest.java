package com.stiveen.gestionhospitalaria.dto.request;

public class LoginRequest {

    private String numeroDocumento;
    private String password;


    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}