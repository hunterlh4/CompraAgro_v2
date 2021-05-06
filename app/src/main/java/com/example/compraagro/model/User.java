package com.example.compraagro.model;

public class User {

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(String departamento) {
        Departamento = departamento;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getTipoUsuario() {
        return TipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        TipoUsuario = tipoUsuario;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    private String Nombres;
    private String Apellidos;
    private String Departamento;
    private String Dni;
    private String Email;
    private String Telefono;
    private String TipoUsuario;
    public User() {

    }
    public User(String nombres, String apellidos, String departamento, String dni, String email, String telefono, String tipoUsuario, String token) {
        Nombres = nombres;
        Apellidos = apellidos;
        Departamento = departamento;
        Dni = dni;
        Email = email;
        Telefono = telefono;
        TipoUsuario = tipoUsuario;
        Token = token;
    }

    private String Token;
}
