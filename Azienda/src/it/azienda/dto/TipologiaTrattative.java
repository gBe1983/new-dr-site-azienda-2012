package it.azienda.dto;

public class TipologiaTrattative {
	private int idTrattattive;
	private String descrizione;

	/**
	 * @param idTrattattive
	 * @param descrizione
	 */
	public TipologiaTrattative(int idTrattattive, String descrizione) {
		this.idTrattattive = idTrattattive;
		this.descrizione = descrizione;
	}

	public int getIdTrattattive() {
		return idTrattattive;
	}
	public void setIdTrattattive(int idTrattattive) {
		this.idTrattattive = idTrattattive;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}