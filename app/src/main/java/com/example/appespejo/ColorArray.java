package com.example.appespejo;

import java.util.List;

public class ColorArray {

    List<NuevoColor> color;

    public List<NuevoColor> getColor() {
        return color;
    }


    public ColorArray(List<NuevoColor> color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ColorArray{" +
                "color=" + color +
                '}';
    }

    public void setColor(List<NuevoColor> color) {
        this.color = color;
    }
}
