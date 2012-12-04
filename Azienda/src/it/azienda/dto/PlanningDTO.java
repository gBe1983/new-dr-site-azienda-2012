package it.azienda.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class PlanningDTO {

	private String cognome;
	private String nome;
	private String ragione_sociale;
	private String codice_commessa;
	private int numero_ore;
	private String descrizione_commessa;

	private Calendar data;
	private double numeroOre;
	private double straordinari;
	
	private String descrizioneRisorsa;
	ArrayList<PlanningDTO> listaGiornate = new ArrayList<PlanningDTO>();

	public PlanningDTO() {}

	public PlanningDTO(	Date data,
								double numeroOre,
								double straordinari,
								String descrizione_commessa){
		this.data=Calendar.getInstance();
		this.data.setTime(data);
		this.numeroOre=numeroOre;
		this.straordinari=straordinari;
		this.descrizione_commessa=descrizione_commessa;
	}

	/**
	 * @return the data
	 */
	public Calendar getData() {
		return data;
	}
	/**
	 * @return the numeroOre
	 */
	public double getNumeroOre() {
		return numeroOre;
	}
	/**
	 * @return the straordinari
	 */
	public double getStraordinari() {
		return straordinari;
	}
	
	public ArrayList<PlanningDTO> getListaGiornate() {
		return listaGiornate;
	}

	public void setListaGiornate(ArrayList<PlanningDTO> listaGiornate) {
		this.listaGiornate = listaGiornate;
	}
	
	public String getDescrizioneRisorsa() {
		return descrizioneRisorsa;
	}

	public void setDescrizioneRisorsa(String descrizioneRisorsa) {
		this.descrizioneRisorsa = descrizioneRisorsa;
	}
	
	public String getDescrizione_commessa() {
		return descrizione_commessa;
	}
	public void setDescrizione_commessa(String descrizione_commessa) {
		this.descrizione_commessa = descrizione_commessa;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getRagione_sociale() {
		return ragione_sociale;
	}
	public void setRagione_sociale(String ragione_sociale) {
		this.ragione_sociale = ragione_sociale;
	}
	public String getCodice_commessa() {
		return codice_commessa;
	}
	public void setCodice_commessa(String codice_commessa) {
		this.codice_commessa = codice_commessa;
	}
	public int getNumero_ore() {
		return numero_ore;
	}
	public void setNumero_ore(int numero_ore) {
		this.numero_ore = numero_ore;
	}
}