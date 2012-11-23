package it.azienda.dto;

public class Dettaglio_Cv_DTO {

	private int id_dettaglio;
	private String capacita_professionali;
	private String competenze_tecniche;
	private String lingue_Straniere;
	private String istruzione;
	private String formazione;
	private String interessi;
	private int id_risorsa;
	private boolean visible;
	
	public int getId_dettaglio() {
		return id_dettaglio;
	}
	public void setId_dettaglio(int id_dettaglio) {
		this.id_dettaglio = id_dettaglio;
	}
	public String getCompetenze_tecniche() {
		return competenze_tecniche;
	}
	public void setCompetenze_tecniche(String competenze_tecniche) {
		this.competenze_tecniche = competenze_tecniche;
	}
	public int getId_risorsa() {
		return id_risorsa;
	}
	public void setId_risorsa(int id_risorsa) {
		this.id_risorsa = id_risorsa;
	}
	public String getCapacita_professionali() {
		return capacita_professionali;
	}
	public void setCapacita_professionali(String capacita_professionali) {
		this.capacita_professionali = capacita_professionali;
	}
	public String getLingue_Straniere() {
		return lingue_Straniere;
	}
	public void setLingue_Straniere(String lingue_Straniere) {
		this.lingue_Straniere = lingue_Straniere;
	}
	public String getIstruzione() {
		return istruzione;
	}
	public void setIstruzione(String istruzione) {
		this.istruzione = istruzione;
	}
	public String getFormazione() {
		return formazione;
	}
	public void setFormazione(String formazione) {
		this.formazione = formazione;
	}
	public String getInteressi() {
		return interessi;
	}
	public void setInteressi(String interessi) {
		this.interessi = interessi;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
}
