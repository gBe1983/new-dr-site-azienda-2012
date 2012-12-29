package it.azienda.dto;

public class TipologiaCommessa {

	private int id_tipologia;
	private String descrizione;

	public TipologiaCommessa(int id_tipologia, String descrizione) {
		super();
		this.id_tipologia = id_tipologia;
		this.descrizione = descrizione;
	}

	public int getId_tipologia() {
		return id_tipologia;
	}
	public void setId_tipologia(int id_tipologia) {
		this.id_tipologia = id_tipologia;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}