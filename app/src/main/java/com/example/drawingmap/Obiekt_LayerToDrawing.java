package com.example.drawingmap;

public class Obiekt_LayerToDrawing {
   private String Mslink;
    private String Latlong ;
    private String Kolor;
    private String Podparam;
    private String Param;
    private String Angle;
    private String Type;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Obiekt_LayerToDrawing(String type) {
        Type = type;
    }

    public String getMslink() {
        return Mslink;
    }

    public void setMslink(String mslink) {
        Mslink = mslink;
    }

    public String getLatlong() {
        return Latlong;
    }

    public void setLatlong(String latlong) {
        Latlong = latlong;
    }

    public String getKolor() {
        return Kolor;
    }

    public void setKolor(String kolor) {
        Kolor = kolor;
    }

    public String getPodparam() {
        return Podparam;
    }

    public void setPodparam(String podparam) {
        Podparam = podparam;
    }

    public String getParam() {
        return Param;
    }

    public void setParam(String param) {
        Param = param;
    }

    public String getAngle() {
        return Angle;
    }

    public void setAngle(String angle) {
        Angle = angle;
    }

    public Obiekt_LayerToDrawing(String mslink, String latlong, String kolor, String podparam, String param, String angle,String type) {
        Mslink = mslink;
        Latlong = latlong;
        Kolor = kolor;
        Podparam = podparam;
        Param = param;
        Angle = angle;
        Type  = type;
    }
}
