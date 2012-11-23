package it.azienda.dto;

/*
 * corrisponde alla tabella Tbl_Utente;
 */

public class UtenteDTO {
	
	private int id_utente;
	private String username;
	private String password;
	private int id_azienda;
	private String data_registrazione;
	private String data_login;
	private boolean utente_visible;
	private int id_risorsa;
	private String descrizioneRisorsa;
	private String email;
	
	public String getDescrizioneRisorsa() {
		return descrizioneRisorsa;
	}
	public void setDescrizioneRisorsa(String descrizioneRisorsa) {
		this.descrizioneRisorsa = descrizioneRisorsa;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public int getId_utente() {
		return id_utente;
	}
	public void setId_utente(int id_utente) {
		this.id_utente = id_utente;
	}
	public boolean isUtente_visible() {
		return utente_visible;
	}
	public void setUtente_visible(boolean utente_visible) {
		this.utente_visible = utente_visible;
	}
	public int getId_risorsa() {
		return id_risorsa;
	}
	public void setId_risorsa(int id_risorsa) {
		this.id_risorsa = id_risorsa;
	}
	public String getData_registrazione() {
		return data_registrazione;
	}
	public void setData_registrazione(String data_registrazione) {
		this.data_registrazione = data_registrazione;
	}
	public String getData_login() {
		return data_login;
	}
	public void setData_login(String data_login) {
		this.data_login = data_login;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId_azienda() {
		return id_azienda;
	}
	public void setId_azienda(int id_azienda) {
		this.id_azienda = id_azienda;
	}
	  
	
}
