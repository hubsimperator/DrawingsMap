package com.example.drawingmap;

public class Obiekt {
	private Long nr;
	private String parametr;
	private String podparametr;
	private String parametr_id;
	private String podparametr_id;

	public void setParametr(String parametr) {
		this.parametr = parametr;
	}
	public String getParametr() {
		return parametr;
	}
	public void setParametr_id(String parametr_id) {
		this.parametr_id = parametr_id;
	}

	public String getParametr_id() {
		return parametr_id;
	}
	public void setPodparametr(String podparametr) {
		this.podparametr = podparametr;
	}
	public String getPodparametr() {
		return podparametr;
	}
	public String getPodparametr_id() {
		return podparametr_id;
	}
	public void setPodparametr_id(String podparametr_id) {
		this.podparametr_id = podparametr_id;
	}
	public Long getNr() {
		return nr;
	}
	public void setNr(Long nr) {
		this.nr = nr;
	}
}
