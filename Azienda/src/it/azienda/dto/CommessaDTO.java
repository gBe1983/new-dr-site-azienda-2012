package it.azienda.dto;

import java.util.ArrayList;


public class CommessaDTO {

	private int id_commessa;
	private String id_cliente;
	private String data_offerta;
	private String oggetto_offerta;
	private String descrizione;
	private String sede_lavoro;
	private String data_inizio;
	private String data_fine;
	private double importo;
	private String importo_lettere;
	private String al;
	private String pagamento;
	private String note;
	private boolean attiva;
	private String codiceCommessa;
	private int totaleOre;
	private String stato;
	private String tipologia;
	private int id_trattative;
	
	//mi serve per visualizzare il cliente nella sezione delle commesse
	private String descrizioneCliente;
	private String descrizioneRisorsa;
	
	private ArrayList<String> listaRisorse = new ArrayList<String>();
	
	
	public ArrayList<String> getListaRisorse() {
		return listaRisorse;
	}
	public void setListaRisorse(ArrayList<String> listaRisorse) {
		this.listaRisorse = listaRisorse;
	}
	public boolean isAttiva() {
		return attiva;
	}
	public void setAttiva(boolean attiva) {
		this.attiva = attiva;
	}
	public String getAl() {
		return al;
	}
	public void setAl(String al) {
		this.al = al;
	}
	public int getId_trattative() {
		return id_trattative;
	}
	public void setId_trattative(int id_trattative) {
		this.id_trattative = id_trattative;
	}
	public String getDescrizioneRisorsa() {
		return descrizioneRisorsa;
	}
	public void setDescrizioneRisorsa(String descrizioneRisorsa) {
		this.descrizioneRisorsa = descrizioneRisorsa;
	}
	public String getTipologia() {
		return tipologia;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	public String getCodiceCommessa() {
		return codiceCommessa;
	}
	public void setCodiceCommessa(String codiceCommessa) {
		this.codiceCommessa = codiceCommessa;
	}
	public int getTotaleOre() {
		return totaleOre;
	}
	public void setTotaleOre(int totaleOre) {
		this.totaleOre = totaleOre;
	}
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	public int getId_commessa() {
		return id_commessa;
	}
	public void setId_commessa(int id_commessa) {
		this.id_commessa = id_commessa;
	}
	public String getId_cliente() {
		return id_cliente;
	}
	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}
	public String getData_offerta() {
		return data_offerta;
	}
	public void setData_offerta(String data_offerta) {
		this.data_offerta = data_offerta;
	}
	public String getOggetto_offerta() {
		return oggetto_offerta;
	}
	public void setOggetto_offerta(String oggetto_offerta) {
		this.oggetto_offerta = oggetto_offerta;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getSede_lavoro() {
		return sede_lavoro;
	}
	public void setSede_lavoro(String sede_lavoro) {
		this.sede_lavoro = sede_lavoro;
	}
	public String getData_inizio() {
		return data_inizio;
	}
	public void setData_inizio(String data_inizio) {
		this.data_inizio = data_inizio;
	}
	public String getData_fine() {
		return data_fine;
	}
	public void setData_fine(String data_fine) {
		this.data_fine = data_fine;
	}
	public double getImporto() {
		return importo;
	}
	public void setImporto(double importo) {
		this.importo = importo;
	}
	public String getImporto_lettere() {
		return importo_lettere;
	}
	public void setImporto_lettere(String importo_lettere) {
		this.importo_lettere = importo_lettere;
	}
	public String getPagamento() {
		return pagamento;
	}
	public void setPagamento(String pagamento) {
		this.pagamento = pagamento;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getDescrizioneCliente() {
		return descrizioneCliente;
	}
	public void setDescrizioneCliente(String descrizioneCliente) {
		this.descrizioneCliente = descrizioneCliente;
	}
	
	
}
