package com.example.drawingmap;

public class Obiekt_ParamandPodparam {
    private String ParamId;
    private String Param;
    private String PodparamId;
    private String Podparam;

    public String getParamId() {
        return ParamId;
    }

    public void setParamId(String paramId) {
        ParamId = paramId;
    }

    public String getParam() {
        return Param;
    }

    public void setParam(String param) {
        Param = param;
    }

    public String getPodparamId() {
        return PodparamId;
    }

    public void setPodparamId(String podparamId) {
        PodparamId = podparamId;
    }

    public String getPodparam() {
        return Podparam;
    }

    public void setPodparam(String podparam) {
        Podparam = podparam;
    }

    public Obiekt_ParamandPodparam(String paramId, String param, String podparamId, String podparam) {
        ParamId = paramId;
        Param = param;
        PodparamId = podparamId;
        Podparam = podparam;
    }
}
