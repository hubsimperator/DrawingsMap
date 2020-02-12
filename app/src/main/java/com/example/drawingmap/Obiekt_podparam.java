package com.example.drawingmap;

public class Obiekt_podparam {
    private String RootId;
    private String RootName;
    private String PodparamId;
    private String PodparamName;



    public String getRootId() {
        return RootId;
    }

    public void setRootId(String rootId) {
        RootId = rootId;
    }

    public String getRootName() {
        return RootName;
    }

    public void setRootName(String rootName) {
        RootName = rootName;
    }

    public String getPodparamId() {
        return PodparamId;
    }

    public void setPodparamId(String podparamId) {
        PodparamId = podparamId;
    }

    public String getPodparamName() {
        return PodparamName;
    }

    public void setPodparamName(String podparamName) {
        PodparamName = podparamName;
    }

    public Obiekt_podparam(String podparamId, String podparamName,String rootId,String rootName) {
        PodparamId = podparamId;
        PodparamName = podparamName;
        RootId=rootId;
        RootName=rootName;
    }
}
