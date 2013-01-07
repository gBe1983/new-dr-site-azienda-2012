package it.azienda.dto;

import java.util.ArrayList;

public class CurriculumDTO {

	private int id_risorsa;
	private String nominativo;
	private boolean informazioniPersonali;
	private boolean esperienze;
	private boolean dettaglio;
	
	private RisorsaDTO risorsa = new RisorsaDTO();
	private ArrayList<EsperienzeDTO> listaEsperienze = new ArrayList<EsperienzeDTO>();
	private Dettaglio_Cv_DTO listaDettaglio = new Dettaglio_Cv_DTO();
	
	public RisorsaDTO getRisorsa() {
		return risorsa;
	}
	public void setRisorsa(RisorsaDTO risorsa) {
		this.risorsa = risorsa;
	}
	public ArrayList<EsperienzeDTO> getListaEsperienze() {
		return listaEsperienze;
	}
	public void setListaEsperienze(ArrayList<EsperienzeDTO> listaEsperienze) {
		this.listaEsperienze = listaEsperienze;
	}
	public Dettaglio_Cv_DTO getListaDettaglio() {
		return listaDettaglio;
	}
	public void setListaDettaglio(Dettaglio_Cv_DTO listaDettaglio) {
		this.listaDettaglio = listaDettaglio;
	}
	public int getId_risorsa() {
		return id_risorsa;
	}
	public void setId_risorsa(int id_risorsa) {
		this.id_risorsa = id_risorsa;
	}
	public String getNominativo() {
		return nominativo;
	}
	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}
	public boolean isInformazioniPersonali() {
		return informazioniPersonali;
	}
	public void setInformazioniPersonali(boolean informazioniPersonali) {
		this.informazioniPersonali = informazioniPersonali;
	}
	public boolean isEsperienze() {
		return esperienze;
	}
	public void setEsperienze(boolean esperienze) {
		this.esperienze = esperienze;
	}
	public boolean isDettaglio() {
		return dettaglio;
	}
	public void setDettaglio(boolean dettaglio) {
		this.dettaglio = dettaglio;
	}
	

}
