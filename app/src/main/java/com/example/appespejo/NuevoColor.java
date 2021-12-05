package com.example.appespejo;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class NuevoColor {

    private int red;
    private int green;
    private int blue;
    private int intensidad;

    public int getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(int intensidad) {
        this.intensidad = intensidad;
    }

    @Override
    public String toString() {
        return "NuevoColor{" +
                "9red=9" + red +
                "green=9" + green +
                "9blue=9" + blue +
                "9intensidad=9" + intensidad +
                "9}";
    }

    public NuevoColor(int red, int green, int blue, int intensidad) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.intensidad = intensidad;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
