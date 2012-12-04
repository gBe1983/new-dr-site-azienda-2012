package it.azienda.dto;

public class Associaz_Risor_Comm {

	private int id_associazione;
	private int id_risorsa;
	private int id_commessa;
	private String dataInizio;
	private String dataFine;
	private int totaleImporto;
	private String al;
	private boolean attiva;
	
	private String descrizioneCliente;
	private String descrizioneRisorsa;
	
	public Associaz_Risor_Comm() {
		// TODO Auto-generated constructor stub
	}
	
	public Associaz_Risor_Comm(String descrizioneCliente, String descrizioneRisorsa) {
		// TODO Auto-generated constructor stub
		this.descrizioneCliente = descrizioneCliente;
		this.descrizioneRisorsa = descrizioneRisorsa;
	}

	public String getAl() {
		return al;
	}
	public void setAl(String al) {
		this.al = al;
	}
	public boolean isAttiva() {
		return attiva;
	}
	public void setAttiva(boolean attiva) {
		this.attiva = attiva;
	}
	public int getTotaleImporto() {
		return totaleImporto;
	}
	public void setTotaleImporto(int totaleImporto) {
		this.totaleImporto = totaleImporto;
	}
	public String getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}
	public String getDataFine() {
		return dataFine;
	}
	public void setDataFine(String dataFine) {
		this.dataFine = dataFine;
	}
	public int getId_associazione() {
		return id_associazione;
	}
	public void setId_associazione(int id_associazione) {
		this.id_associazione = id_associazione;
	}
	public int getId_risorsa() {
		return id_risorsa;
	}
	public void setId_risorsa(int id_risorsa) {
		this.id_risorsa = id_risorsa;
	}
	public int getId_commessa() {
		return id_commessa;
	}
	public void setId_commessa(int id_commessa) {
		this.id_commessa = id_commessa;
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
	
}
