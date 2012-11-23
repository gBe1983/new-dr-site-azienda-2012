package it.azienda.dto;

public class EsperienzeDTO {

	private int idEsperienze;
	private String periodo;
	private String azienda;
	private String luogo;
	private String descrizione;
	private int id_risorsa;
	private boolean visibile;
	
	public int getIdEsperienze() {
		return idEsperienze;
	}
	public void setIdEsperienze(int idEsperienze) {
		this.idEsperienze = idEsperienze;
	}
	public boolean isVisibile() {
		return visibile;
	}
	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getAzienda() {
		return azienda;
	}
	public void setAzienda(String azienda) {
		this.azienda = azienda;
	}
	public String getLuogo() {
		return luogo;
	}
	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public int getId_risorsa() {
		return id_risorsa;
	}
	public void setId_risorsa(int id_risorsa) {
		this.id_risorsa = id_risorsa;
	}
	
	
  
}
