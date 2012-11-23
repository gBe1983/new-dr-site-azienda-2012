package it.azienda.dto;

public class PlanningDTO {

	private String cognome;
	private String nome;
	private String ragione_sociale;
	private String codice_commessa;
	private int numero_ore;
	private String descrizione_commessa;
	
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
