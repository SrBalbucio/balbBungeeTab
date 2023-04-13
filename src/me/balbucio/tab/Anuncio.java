package me.balbucio.tab;

public class Anuncio {
	
	private String anuncio;
	private String owner;
	
	public Anuncio(String name, String anuncio) {
		this.anuncio = anuncio;
		this.owner = name;
	}

	public String getAnuncio() {
		return anuncio;
	}

	public void setAnuncio(String anuncio) {
		this.anuncio = anuncio;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	
}
