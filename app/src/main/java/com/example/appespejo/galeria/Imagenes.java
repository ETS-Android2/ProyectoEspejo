package com.example.appespejo.galeria;

import com.bumptech.glide.module.AppGlideModule;

public class Imagenes {
    String fotoUrl;

    public Imagenes(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
