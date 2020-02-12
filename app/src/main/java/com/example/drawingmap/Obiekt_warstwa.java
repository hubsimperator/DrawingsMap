package com.example.drawingmap;

public class Obiekt_warstwa {
    private String layerName;
    private String layerId;

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public Obiekt_warstwa(String layerName, String layerId) {
        this.layerName = layerName;
        this.layerId = layerId;
    }
}
