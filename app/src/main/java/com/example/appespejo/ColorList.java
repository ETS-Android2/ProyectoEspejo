package com.example.appespejo;

import java.util.List;

public class ColorList {

    int colorR;
    int colorG;
    int colorB;
    int intencidad;

    @Override
    public String toString() {
        return "ColorList{" +
                "colorR=" + colorR +
                ", colorG=" + colorG +
                ", colorB=" + colorB +
                ", intencidad=" + intencidad +
                '}';
    }

    public int getColorR() {
        return colorR;
    }

    public void setColorR(int colorR) {
        this.colorR = colorR;
    }

    public int getColorG() {
        return colorG;
    }

    public void setColorG(int colorG) {
        this.colorG = colorG;
    }

    public int getColorB() {
        return colorB;
    }

    public void setColorB(int colorB) {
        this.colorB = colorB;
    }

    public int getIntencidad() {
        return intencidad;
    }

    public void setIntencidad(int intencidad) {
        this.intencidad = intencidad;
    }

    public ColorList(int colorR, int colorG, int colorB, int intencidad) {
        this.colorR = colorR;
        this.colorG = colorG;
        this.colorB = colorB;
        this.intencidad = intencidad;
    }
}
