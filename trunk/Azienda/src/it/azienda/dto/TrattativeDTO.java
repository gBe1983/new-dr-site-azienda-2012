package it.azienda.dto;

public class TrattativeDTO {

	private int idTrattative;
	private String id_cliente;
	private int id_risorsa;
	private String contatto;
	private String data;
	private String oggetto;
	private String esito;
	private boolean trattativa_chiusa;
	private String id_tipologiaTrattative;
	private String note;
	
	private String codiceCommessa;
	private String descrizioneRisorsa;
	private String descrizioneAzienda;
	private String descrizioneCliente;

	public TrattativeDTO(){}

	/**
	 * @param idTrattative
	 * @param id_cliente
	 * @param id_risorsa
	 * @param data
	 * @param oggetto
	 * @param esito
	 * @param descrizioneRisorsa
	 * @param descrizioneCliente
	 */
	public TrattativeDTO(int idTrattative, String id_cliente, int id_risorsa,
			String data, String oggetto, String esito, String note,
			String descrizioneRisorsa, String descrizioneCliente) {
		super();
		this.idTrattative = idTrattative;
		this.id_cliente = id_cliente;
		this.id_risorsa = id_risorsa;
		this.data = data;
		this.oggetto = oggetto;
		this.esito = esito;
		this.note = note;
		this.descrizioneRisorsa = descrizioneRisorsa;
		this.descrizioneCliente = descrizioneCliente;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getId_tipologiaTrattative() {
		return id_tipologiaTrattative;
	}
	public void setId_tipologiaTrattative(String id_tipologiaTrattative) {
		this.id_tipologiaTrattative = id_tipologiaTrattative;
	}
	public String getCodiceCommessa() {
		return codiceCommessa;
	}
	public void setCodiceCommessa(String codiceCommessa) {
		this.codiceCommessa = codiceCommessa;
	}
	public String getDescrizioneCliente() {
		return descrizioneCliente;
	}
	public void setDescrizioneCliente(String descrizioneCliente) {
		this.descrizioneCliente = descrizioneCliente;
	}
	public String getDescrizioneRisorsa() {
		return descrizioneRisorsa;
	}
	public void setDescrizioneRisorsa(String descrizioneRisorsa) {
		this.descrizioneRisorsa = descrizioneRisorsa;
	}
	public String getDescrizioneAzienda() {
		return descrizioneAzienda;
	}
	public void setDescrizioneAzienda(String descrizioneAzienda) {
		this.descrizioneAzienda = descrizioneAzienda;
	}
	public int getIdTrattative() {
		return idTrattative;
	}
	public void setIdTrattative(int idTrattative) {
		this.idTrattative = idTrattative;
	}
	public String getId_cliente() {
		return id_cliente;
	}
	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}
	public int getId_risorsa() {
		return id_risorsa;
	}
	public void setId_risorsa(int id_risorsa) {
		this.id_risorsa = id_risorsa;
	}
	public String getContatto() {
		return contatto;
	}
	public void setContatto(String contatto) {
		this.contatto = contatto;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getOggetto() {
		return oggetto;
	}
	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public boolean isTrattativa_chiusa() {
		return trattativa_chiusa;
	}
	public void setTrattativa_chiusa(boolean trattativa_chiusa) {
		this.trattativa_chiusa = trattativa_chiusa;
	}
}