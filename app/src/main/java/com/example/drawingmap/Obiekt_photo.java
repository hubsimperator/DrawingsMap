package com.example.drawingmap;

public class Obiekt_photo {
	private Long nr;
	private String photo_id;
	private String photo;

	public void setphoto_id(String photo_id) {
		this.photo_id = photo_id;
	}
	public String getphoto_id() {
		return photo_id;
	}

	public void setphoto(String photo) {
		this.photo = photo;
	}
	public String getphoto() {
		return photo;
	}

	public Long getNr() {
		return nr;
	}
	public void setNr(Long nr) {
		this.nr = nr;
	}
}
