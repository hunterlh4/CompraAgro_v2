package com.example.compraagro.model;

public class Fav {
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getIdFavorito() {
        return idFavorito;
    }

    public void setIdFavorito(String idFavorito) {
        this.idFavorito = idFavorito;
    }

    public Fav(String idUsuario, String idProducto, String idFavorito) {
        this.idUsuario = idUsuario;
        this.idProducto = idProducto;
        this.idFavorito = idFavorito;
    }

    private String idUsuario;
    private String idProducto;
    private String idFavorito;
}
