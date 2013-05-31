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
	private double ferie;
	private double mutua;
	private double permessi;
	private double permessiNonRetribuiti;
	
	private String descrizioneRisorsa;
	ArrayList<PlanningDTO> listaGiornate = new ArrayList<PlanningDTO>();

	public PlanningDTO() {}

	public PlanningDTO(	Date data,
								double numeroOre,
								double straordinari,
								double ferie,
								double permessi,
								double mutua,
								double permessiNonRetribuiti,
								String descrizione_commessa){
		this.data=Calendar.getInstance();
		this.data.setTime(data);
		this.numeroOre=numeroOre;
		this.straordinari=straordinari;
		this.ferie = ferie;
		this.mutua = mutua;
		this.permessi = permessi;
		this.permessiNonRetribuiti = permessiNonRetribuiti;
		this.descrizione_commessa=descrizione_commessa;
	}

	public double getFerie() {
		return ferie;
	}

	public void setFerie(double ferie) {
		this.ferie = ferie;
	}

	public double getMutua() {
		return mutua;
	}

	public void setMutua(double mutua) {
		this.mutua = mutua;
	}

	public double getPermessi() {
		return permessi;
	}

	public void setPermessi(double permessi) {
		this.permessi = permessi;
	}

	public double getPermessiNonRetribuiti() {
		return permessiNonRetribuiti;
	}

	public void setPermessiNonRetribuiti(double permessiNonRetribuiti) {
		this.permessiNonRetribuiti = permessiNonRetribuiti;
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
	
	public void setData(Calendar data) {
		this.data = data;
	}

	public void setNumeroOre(double numeroOre) {
		this.numeroOre = numeroOre;
	}

	public void setStraordinari(double straordinari) {
		this.straordinari = straordinari;
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