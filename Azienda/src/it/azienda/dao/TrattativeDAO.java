package it.azienda.dao;

import it.azienda.dto.TipologiaTrattative;
import it.azienda.dto.TrattativeDTO;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TrattativeDAO extends BaseDao {
	private MyLogger log;

	public TrattativeDAO(Connection connessione) {
		super(connessione);
		log=new MyLogger(this.getClass());
	}

	public ArrayList<TrattativeDTO>ricercaTrattative(String codiceCliente, int idRisorsa, int idTrattative, String esito){
		final String metodo="ricercaTrattative";
		log.start(metodo);
		ArrayList<TrattativeDTO> listaTrattativa = new ArrayList<TrattativeDTO>();
		StringBuilder sql = new StringBuilder("SELECT id_cliente,id_trattativa,ragione_sociale,");
		sql	.append("id_risorsa,Risorsa,data,oggetto,Esito_trattativa ")
				.append("FROM v_trattative_per_cliente_dettaglio ");
		PreparedStatement ps=null;
		ResultSet rs=null;
		if(codiceCliente != null && idRisorsa != 0 && esito == null){// Codice != "" e idRisorsa != "" e esito == ""
			sql.append("WHERE id_cliente=? AND id_risorsa=?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setString(1, codiceCliente);
				ps.setInt(2, idRisorsa);
			} catch (SQLException e) {
				log.error(metodo, "prepareStatement 1", e);
			}
		}else if(codiceCliente == null && idRisorsa != 0 && esito == null) {//Codice == "" e idRisorsa != "" e esito == ""
			sql.append("WHERE id_risorsa=?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setInt(1, idRisorsa);
			} catch (SQLException e) {
				log.error(metodo, "prepareStatement 2", e);
			}
		}else if(codiceCliente != null && idRisorsa == 0 && esito == null){//Codice != "" e idRisorsa == "" e esito == ""
			sql.append("WHERE id_cliente = ?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setString(1, codiceCliente);
			} catch (SQLException e) {
				log.error(metodo, "prepareStatement 3", e);
			}
		}else if(codiceCliente != null && idRisorsa != 0 && esito != null){//Codice != "" e idRisorsa != "" e esito != ""
			sql.append("WHERE id_cliente=? AND id_risorsa=? AND esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
					ps.setInt(2, idRisorsa);
					ps.setString(3, esito);
				} catch (SQLException e) {
					log.error(metodo, "prepareStatement 4", e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
					ps.setInt(2, idRisorsa);
				} catch (SQLException e) {
					log.error(metodo, "prepareStatement 5", e);
				}
			}
		}else if(codiceCliente == null && idRisorsa != 0 && esito != null) {//Codice == "" e idRisorsa !="" e esito !=""
			sql.append("WHERE id_risorsa=? AND esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setInt(1, idRisorsa);
					ps.setString(2, esito);
				} catch (SQLException e) {
					log.error(metodo, "prepareStatement 6", e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setInt(1, idRisorsa);
				} catch (SQLException e) {
					log.error(metodo, "prepareStatement 7", e);
				}
			}
		}else if(codiceCliente != null && idRisorsa == 0 && esito != null){//Codice !="" e idRisorsa == "" e esito !="" 
			sql.append("WHERE id_cliente=? AND esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
					ps.setString(2, esito);
				} catch (SQLException e) {
					log.error(metodo, "prepareStatement 8", e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
				} catch (SQLException e) {
					log.error(metodo, "prepareStatement 9", e);
				}
			}
		}else if(codiceCliente == null && idRisorsa == 0 && esito != null){//Codice == "" e idRisorsa == "" e esito !=""
			sql.append("WHERE esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1,esito);
				} catch (SQLException e) {
					log.error(metodo, "prepareStatement 10", e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
				} catch (SQLException e) {
					log.error(metodo, "prepareStatement 11", e);
				}
			}
		}else if(idTrattative != 0){//questa tipo di ricerca viene effettuata al momento di quanto l'utente effettua il dettaglio della trattattiva
			sql.append("WHERE id_trattativa=?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setInt(1, idTrattative);
			} catch (SQLException e) {
				log.error(metodo, "prepareStatement 12", e); 
			}
		}else{//questa tipo di ricerca viene effettuata al momento di quanto l'utente effettua il dettaglio della trattattiva
			try {
				ps = connessione.prepareStatement(sql.toString());
			} catch (SQLException e) {
				log.error(metodo,"prepareStatement 13",e);
			}
		}
		log.debug(metodo, sql.toString());
		try {
			rs = ps.executeQuery();
			while(rs.next()){
				listaTrattativa.add(
					new TrattativeDTO(
						rs.getInt("id_trattativa"),
						rs.getString("id_cliente"),
						rs.getInt("id_risorsa"),
						rs.getString("data"),
						rs.getString("oggetto"),
						rs.getString("Esito_trattativa"),
						rs.getString("Risorsa"),
						rs.getString("ragione_sociale")));
			}
		} catch (SQLException e) {
			log.error(metodo, "executeQuery", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return listaTrattativa;
	}

	/**
	 * con questo metodo effetuo l'inserimento della trattativa nella tabella tbl_trattative
	 * @param trattativa
	 * @return
	 */
	public String inserimentoTrattative(TrattativeDTO trattativa){
		final String metodo="inserimentoTrattative";
		log.start(metodo);
		String sql = "INSERT INTO tbl_trattative(id_cliente,id_risorsa,contatto,data,oggetto,esito,id_tipologia_trattattiva)VALUES(?,?,?,?,?,?,?)";
		log.debug(metodo,sql);
		int esitoInserimentoTrattative = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, trattativa.getId_cliente());
			ps.setInt(2, trattativa.getId_risorsa());
			ps.setString(3, trattativa.getContatto());
			ps.setString(4, trattativa.getData());
			ps.setString(5, trattativa.getOggetto());
			ps.setString(6, trattativa.getEsito());
			ps.setString(7, trattativa.getId_tipologiaTrattative());
			esitoInserimentoTrattative = ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "INSERT INTO tbl_trattative for id_risorsa:"+trattativa.getId_risorsa(), e);
			return "Siamo spiacenti l'inserimento della trattativa non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
			log.end(metodo);
		}
		return (esitoInserimentoTrattative == 1)?
			"ok":
			"Siamo spiacenti l'inserimento della trattativa non è avvenuta con successo. Contattare l'amministrazione.";
	}

	/**
	 * con questo metodo effettuo l'aggiornamento delle trattattive che l'utente ha deciso di chiudere
	 * @param idTrattativa
	 * @param idAzienda
	 */
	public void chiusuraTrattativa(int idTrattativa, int idAzienda){
		final String metodo="chiusuraTrattativa";
		log.start(metodo);
		String sql = "UPDATE tbl_trattative SET esito='chiusa',trattativa_chiusa=0 WHERE id_trattative=? AND id_azienda=?";
		log.debug(metodo,sql);
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idTrattativa);
			ps.setInt(2, idAzienda);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "UPDATE tbl_trattative for idTrattativa:"+idTrattativa+" and idAzienda:"+idAzienda, e);
		}finally{
			close(ps);
			log.end(metodo);
		}
	}

	/**
	 * tramite questo metodo effettuo la ricerca del singola trattativa per poi visualizzare i relativi dati nella pagina "Aggiungi Trattative"
	 * @param idTrattativa
	 * @return
	 */
	public TrattativeDTO aggiornaSingolaTrattativa(int idTrattativa){
		final String metodo="aggiornaSingolaTrattativa";
		log.start(metodo);
		StringBuilder sql = new StringBuilder("SELECT cliente.ragione_sociale,");
		sql	.append("trattative.id_trattativa,trattative.id_cliente,trattative.id_risorsa,trattative.contatto,")
				.append("trattative.data,trattative.oggetto,trattative.esito,trattative.id_tipologia_trattattiva ")
				.append("FROM tbl_trattative AS trattative, tbl_clienti AS cliente ")
				.append("WHERE cliente.id_cliente=trattative.id_cliente AND trattative.id_trattativa=?");
		log.debug(metodo,sql.toString());
		TrattativeDTO trattativa = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setInt(1, idTrattativa);
			rs = ps.executeQuery();
			if(rs.next()){
				trattativa = new TrattativeDTO();
				trattativa.setDescrizioneCliente(rs.getString("ragione_sociale"));
				trattativa.setIdTrattative(rs.getInt("id_trattativa"));
				trattativa.setId_cliente(rs.getString("id_cliente"));
				trattativa.setId_risorsa(rs.getInt("id_risorsa"));
				trattativa.setContatto(rs.getString("contatto"));
				trattativa.setData(rs.getString("data"));
				trattativa.setOggetto(rs.getString("oggetto"));
				trattativa.setEsito(rs.getString("esito"));
				trattativa.setId_tipologiaTrattative(rs.getString("id_tipologia_trattattiva"));
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT tbl_trattative,tbl_clienti for idTrattativa:"+idTrattativa, e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return trattativa;
	}

	/**
	 * tramite questo metodo effettuo la modifica della trattativa
	 * @param trattativa
	 * @return
	 */
	public String modificaTrattativa(TrattativeDTO trattativa){
		final String metodo="modificaTrattativa";
		log.start(metodo);
		StringBuilder sql = new StringBuilder("UPDATE tbl_trattative ");
		sql	.append("SET id_cliente=?,id_risorsa=?,contatto=?,data=?,oggetto=?,esito=?,id_tipologia_trattattiva=? ")
				.append("WHERE id_trattativa=?");
		log.debug(metodo,sql.toString());
		int esitoModificaTrattativa = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setString(1, trattativa.getId_cliente());
			ps.setInt(2, trattativa.getId_risorsa());
			ps.setString(3, trattativa.getContatto());
			ps.setString(4, trattativa.getData());
			ps.setString(5, trattativa.getOggetto());
			ps.setString(6, trattativa.getEsito());
			ps.setString(7, trattativa.getId_tipologiaTrattative());
			ps.setInt(8, trattativa.getIdTrattative());
			esitoModificaTrattativa = ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "UPDATE tbl_trattative for trattativa:"+trattativa.getIdTrattative(), e);
			return "Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps);
			log.end(metodo);
		}
		return (esitoModificaTrattativa == 1)?
			"ok":
			"Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";
	}

	public String ricercaCodiceCommessa(int idTrattative){
		final String metodo="ricercaCodiceCommessa";
		log.start(metodo);
		String sql = "SELECT codice_commessa FROM tbl_commesse WHERE id_trattativa=?";
		log.debug(metodo,sql);
		String codiceCommessa = "";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idTrattative);
			rs = ps.executeQuery();
			if(rs.next()){
				codiceCommessa = rs.getString("codice_commessa");
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT tbl_commesse for idTrattative:"+idTrattative, e);
			return "Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return codiceCommessa;
	}

	public String ricercaDescrizioneRisorsa(int id_risorsa){
		final String metodo="ricercaDescrizioneRisorsa";
		log.start(metodo);
		StringBuilder sql = new StringBuilder("SELECT cognome,nome ");
		sql	.append("FROM tbl_trattative AS trattative, tbl_risorse AS risorse ")
				.append("WHERE trattative.id_risorsa=risorse.id_risorsa AND trattative.id_risorsa=?");
		log.debug(metodo,sql.toString());
		String descrizioneRisorsa = "";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setInt(1, id_risorsa);
			rs = ps.executeQuery();
			if(rs.next()){
				descrizioneRisorsa = rs.getString("cognome") + " " + rs.getString("nome");
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT tbl_trattative,tbl_risorse for id_risorsa:"+id_risorsa, e);
			return "Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";//?????????
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return descrizioneRisorsa;
	}

	public ArrayList<TipologiaTrattative>caricamentoTipologie(){
		final String metodo="caricamentoTipologie";
		log.start(metodo);
		String sql = "SELECT id_tipologia_trattativa,descrizione FROM tbl_tipologie_trattative";
		log.debug(metodo,sql);
		ArrayList<TipologiaTrattative> listaTrattattive = new ArrayList<TipologiaTrattative>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				listaTrattattive.add(
					new TipologiaTrattative(
						rs.getInt("id_tipologia_trattativa"),
						rs.getString("descrizione")));
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT tbl_tipologie_trattative", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return listaTrattattive;
	}

	public int caricamentoIdTrattativa(){
		final String metodo="caricamentoIdTrattativa";
		log.start(metodo);
		String sql = "SELECT MAX(id_trattativa)id_trattativa_max FROM tbl_trattative";
		log.debug(metodo,sql);
		int idTrattative = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				idTrattative = rs.getInt("id_trattativa_max");
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT MAX(id_trattativa) tbl_trattative", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return idTrattative;
	}
}