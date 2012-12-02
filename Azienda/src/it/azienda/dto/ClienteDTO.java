package it.azienda.dto;

/**
 * questo classe corrisponde alla tabella "Tbl_Cliente" 
 * @author Dr
 *
 */
public class ClienteDTO {
	private String id_cliente;
	private String ragioneSociale;
	private String indirizzo;
	private String cap;
	private String citta;
	private String provincia;
	private String pIva;
	private String referente;
	private String telefono;
	private String cellulare;
	private String fax;
	private String email;
	private String sito;
	private boolean attivo;
	private String codFiscale;
	private int id_banca;

	public ClienteDTO() {}

	/**
	 * @param id_cliente
	 * @param ragioneSociale
	 */
	public ClienteDTO(String id_cliente, String ragioneSociale) {
		this.id_cliente = id_cliente;
		this.ragioneSociale = ragioneSociale;
	}

	/**
	 * @param id_cliente
	 * @param ragioneSociale
	 * @param indirizzo
	 * @param cap
	 * @param citta
	 * @param provincia
	 * @param pIva
	 * @param referente
	 * @param telefono
	 * @param cellulare
	 * @param fax
	 * @param email
	 * @param sito
	 * @param attivo
	 * @param codFiscale
	 * @param id_banca
	 */
	public ClienteDTO(String id_cliente, String ragioneSociale,
			String indirizzo, String cap, String citta, String provincia,
			String pIva, String referente, String telefono, String cellulare,
			String fax, String email, String sito, boolean attivo,
			String codFiscale, int id_banca) {
		this(id_cliente, ragioneSociale);
		this.indirizzo = indirizzo;
		this.cap = cap;
		this.citta = citta;
		this.provincia = provincia;
		this.pIva = pIva;
		this.referente = referente;
		this.telefono = telefono;
		this.cellulare = cellulare;
		this.fax = fax;
		this.email = email;
		this.sito = sito;
		this.attivo = attivo;
		this.codFiscale = codFiscale;
		this.id_banca = id_banca;
	}

	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
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
	public String getId_cliente() {
		return id_cliente;
	}
	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getPIva() {
		return pIva;
	}
	public void setPIva(String iva) {
		pIva = iva;
	}
	public String getReferente() {
		return referente;
	}
	public void setReferente(String referente) {
		this.referente = referente;
	}
	public String getCellulare() {
		return cellulare;
	}
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSito() {
		return sito;
	}
	public void setSito(String sito) {
		this.sito = sito;
	}
	public boolean isAttivo() {
		return attivo;
	}
	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}
	public String getCodFiscale() {
		return codFiscale;
	}
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}
	public int getId_banca() {
		return id_banca;
	}
	public void setId_banca(int id_banca) {
		this.id_banca = id_banca;
	}
}