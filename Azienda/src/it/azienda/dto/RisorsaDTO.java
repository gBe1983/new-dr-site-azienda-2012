package it.azienda.dto;

public class RisorsaDTO {

	//dati anagrafici risorsa
	private int idRisorsa;
	private String cognome;
	private String nome;
	private String dataNascita;
	private String luogoNascita;
	private String sesso;
	private String codiceFiscale;
	private String email;
	private String telefono;
	private String cellulare;
	private String fax;
	
	//residenza
	private String indirizzo;
	private String citta;
	private String provincia;
	private String cap;
	private String nazione;
	private String servizioMilitare;
	
	//altri dati
	private String patente;
	private String costo;
	private boolean occupato;
	private String tipoContratto;
	private String figuraProfessionale;
	private String seniority;

	private boolean visible;
	private boolean flaCreazioneCurriculum;
	private boolean cv_visibile;
	
	public String getCellulare() {
		return cellulare;
	}
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
	public boolean isCv_visibile() {
		return cv_visibile;
	}
	public void setCv_visibile(boolean cv_visibile) {
		this.cv_visibile = cv_visibile;
	}
	public boolean isFlaCreazioneCurriculum() {
		return flaCreazioneCurriculum;
	}
	public void setFlaCreazioneCurriculum(boolean flaCreazioneCurriculum) {
		this.flaCreazioneCurriculum = flaCreazioneCurriculum;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isOccupato() {
		return occupato;
	}
	public void setOccupato(boolean occupato) {
		this.occupato = occupato;
	}
	public int getIdRisorsa() {
		return idRisorsa;
	}
	public void setIdRisorsa(int idRisorsa) {
		this.idRisorsa = idRisorsa;
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
	public String getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(String dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getLuogoNascita() {
		return luogoNascita;
	}
	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}
	public String getSesso() {
		return sesso;
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getCitta() {
		return citta;
	}
	public void setCitta(String citta) {
		this.citta = citta;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getCap() {
		return cap;
	}
	public void setCap(String cap) {
		this.cap = cap;
	}
	public String getNazione() {
		return nazione;
	}
	public void setNazione(String nazione) {
		this.nazione = nazione;
	}
	public String getServizioMilitare() {
		return servizioMilitare;
	}
	public void setServizioMilitare(String servizioMilitare) {
		this.servizioMilitare = servizioMilitare;
	}
	public String getPatente() {
		return patente;
	}
	public void setPatente(String patente) {
		this.patente = patente;
	}
	public String getCosto() {
		return costo;
	}
	public void setCosto(String costo) {
		this.costo = costo;
	}
	public String getTipoContratto() {
		return tipoContratto;
	}
	public void setTipoContratto(String tipoContratto) {
		this.tipoContratto = tipoContratto;
	}
	public String getFiguraProfessionale() {
		return figuraProfessionale;
	}
	public void setFiguraProfessionale(String figuraProfessionale) {
		this.figuraProfessionale = figuraProfessionale;
	}
	public String getSeniority() {
		return seniority;
	}
	public void setSeniority(String seniority) {
		this.seniority = seniority;
	}
}
