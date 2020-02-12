package com.example.drawingmap;

public class Obiekt_SelectedLayer {
    private String Parametr;
    private String Podparametr;

    public String getParametr() {
        return Parametr;
    }

    public void setParametr(String parametr) {
        Parametr = parametr;
    }

    public String getPodparametr() {
        return Podparametr;
    }

    public void setPodparametr(String podparametr) {
        Podparametr = podparametr;
    }

    public Obiekt_SelectedLayer(String parametr, String podparametr) {
        Parametr = parametr;
        Podparametr = podparametr;
    }
}
