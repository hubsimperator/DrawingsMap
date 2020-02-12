package com.example.drawingmap;

public class Obiekt_Adres {
	private Long nr;
	private String ulica;
	private String numer;
	private String latitude;
	private String longitude;

	public void setUlica(String ulica) {
		this.ulica = ulica;
	}
	public String getUlica() {
		return ulica;
	}

	public void setNumer(String numer) {
		this.numer = numer;
	}
	public String getNumer() {
		return numer;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public Long getNr() {
		return nr;
	}
	public void setNr(Long nr) {
		this.nr = nr;
	}
}
