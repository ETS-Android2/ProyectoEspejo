package com.example.appespejo;


import java.util.Date;

public class Imagen{
    private String url;
    private long tiempo;

    public Imagen() {
    }

    public Imagen(String url, long tiempo) {
        this.url = url;
        tiempo = new Date().getTime();
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }
}
