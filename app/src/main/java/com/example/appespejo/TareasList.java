package com.example.appespejo;

public class TareasList {

    public String tarea;

    public TareasList(String tarea) {
        this.tarea = tarea;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    @Override
    public String toString() {
        return "TareasList{" +
                "tarea='" + tarea + '\'' +
                '}';
    }
}
