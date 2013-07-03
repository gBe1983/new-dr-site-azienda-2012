package it.azienda.dao;

import it.azienda.dto.TipologiaTrattative;
import it.azienda.dto.TrattativeDTO;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;

public class TrattativeDAO extends BaseDao {
	private Logger log;

	public TrattativeDAO(Connection connessione) {
		super(connessione);
		log= Logger.getLogger(TrattativeDAO.class);
	}

	public ArrayList<TrattativeDTO>ricercaTrattative(String codiceCliente, String idRisorsa, int idTrattative, String esito, String anno){
		
		log.info("metodo: ricercaTrattative");
		
		ArrayList<TrattativeDTO> listaTrattativa = new ArrayList<TrattativeDTO>();
		
		StringBuilder sql = new StringBuilder("SELECT id_cliente,id_trattativa,ragione_sociale,");
		sql	.append("id_risorsa,Risorsa,data,oggetto,Esito_trattativa,note ")
				.append("FROM v_trattative_per_cliente_dettaglio ");
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		/*
		 * faccio questo procedimento in quanto nel richiamo del
		 * metodo i valori posso essere a null, pertanto casto i valori 
		 * a NULL in ""
		 */
		
		if(codiceCliente == null){
			codiceCliente = "";
		}
		if(idRisorsa == null || idRisorsa.equals("0")){
			idRisorsa = "";
		}
		if(esito == null){
			esito = "";
		}
		if(anno == null || anno.equals("0")){
			anno = "";
		}
		
		if(codiceCliente != "" && idRisorsa == "" && esito == "" && anno == ""){//Codice != "" e idRisorsa == "" e esito == "" e anno == ""
			sql.append("WHERE id_cliente = ?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setString(1, codiceCliente);
			} catch (SQLException e) {
				log.error("errore sql: "+ e);
			}
		}else if(codiceCliente == "" && idRisorsa != "" && esito == "" && anno == "") {//Codice == "" e idRisorsa != "" e esito == "" e anno == ""
			sql.append("WHERE id_risorsa=?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setInt(1, Integer.parseInt(idRisorsa));
			} catch (SQLException e) {
				log.error("errore sql: "+ e);
			}
		}else if(codiceCliente == "" && idRisorsa == "" && esito != "" && anno == ""){//Codice == "" e idRisorsa == "" e esito !="" e anno == "" 
			sql.append("WHERE esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1,esito);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}
		}else if(codiceCliente == "" && idRisorsa == "" && esito == "" && anno != "") {//Codice == "" e idRisorsa == "" e esito == "" e anno != ""
			sql.append("WHERE data like ?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setString(1, "%"+anno);
			} catch (SQLException e) {
				log.error("errore sql: " + e);
			}
		}else if(codiceCliente != "" && idRisorsa != "" && esito == "" && anno == ""){// Codice != "" e idRisorsa != "" e esito == "" e anno == ""
			sql.append("WHERE id_cliente=? AND id_risorsa=?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setString(1, codiceCliente);
				ps.setInt(2, Integer.parseInt(idRisorsa));
			} catch (SQLException e) {
				log.error("errore sql: "+ e);
			}
		}else if(codiceCliente != "" && idRisorsa == "" && esito != "" && anno == ""){//Codice !="" e idRisorsa == "" e esito !="" 
			sql.append("WHERE id_cliente=? AND esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
					ps.setString(2, esito);
				} catch (SQLException e) {
					log.error("errore sql: " + e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}
		}else if(codiceCliente != "" && idRisorsa == "" && esito == "" && anno != ""){// Codice != "" e idRisorsa == "" e esito == "" e anno != ""
			sql.append("WHERE id_cliente=? AND data like ?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setString(1, codiceCliente);
				ps.setString(2, "%"+anno);
			} catch (SQLException e) {
				log.error("errore sql: " + e);
			}
		}else if(codiceCliente == "" && idRisorsa != "" && esito != "" && anno == "") {//Codice == "" e idRisorsa !="" e esito !="" e anno == ""
			sql.append("WHERE id_risorsa=? AND esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setInt(1, Integer.parseInt(idRisorsa));
					ps.setString(2, esito);
				} catch (SQLException e) {
					log.error("errore sql: " + e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setInt(1, Integer.parseInt(idRisorsa));
				} catch (SQLException e) {
					log.error("errore sql: " + e);
				}
			}
		}else if(codiceCliente == "" && idRisorsa != "" && esito == "" && anno != "") {//Codice == "" e idRisorsa !="" e esito == "" e anno != ""
			sql.append("WHERE id_risorsa=? AND data like ?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setInt(1, Integer.parseInt(idRisorsa));
				ps.setString(2, "%"+anno);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("errore sql: " + e);
			}
		}else if(codiceCliente == "" && idRisorsa == "" && esito != "" && anno != "") {//Codice == "" e idRisorsa == "" e esito != "" e anno != ""
			sql.append("WHERE data like ? AND esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, "%"+anno);
					ps.setString(2, esito);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, "%"+anno);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}
		}else if(codiceCliente != "" && idRisorsa != "" && esito != "" && anno == ""){//Codice != "" e idRisorsa != "" e esito != "" e anno == 0
			sql.append("WHERE id_cliente=? AND id_risorsa=? AND esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
					ps.setInt(2, Integer.parseInt(idRisorsa));
					ps.setString(3, esito);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
					ps.setInt(2, Integer.parseInt(idRisorsa));
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}
		}else if(codiceCliente != "" && idRisorsa != "" && esito == "" && anno != ""){//Codice != "" e idRisorsa != "" e esito == "" e anno != 0
			sql.append("WHERE id_cliente=? AND id_risorsa=? AND data like ?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setString(1, codiceCliente);
				ps.setInt(2, Integer.parseInt(idRisorsa));
				ps.setString(3, "%"+anno);
			} catch (SQLException e) {
				log.error("errore sql: "+ e);
			}
			
		}else if(codiceCliente == "" && idRisorsa != "" && esito != "" && anno != ""){//Codice == "" e idRisorsa != "" e esito != "" e anno != 0
			sql.append("WHERE id_cliente=? AND id_risorsa=? AND data like ? AND esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setInt(1, Integer.parseInt(idRisorsa));
					ps.setString(2, "%"+anno);
					ps.setString(3, esito);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setInt(1, Integer.parseInt(idRisorsa));
					ps.setString(2, "%"+anno);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}
		}else if(codiceCliente != "" && idRisorsa == "" && esito != "" && anno != ""){//Codice != "" e idRisorsa == "" e esito != "" e anno != 0
			sql.append("WHERE id_cliente=? AND data like ? AND data like ? AND esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
					ps.setString(2, "%"+anno);
					ps.setString(3, esito);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
					ps.setString(2, "%"+anno);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}
		}else if(codiceCliente != "" && idRisorsa != "" && esito != "" && anno != ""){//Codice != "" e idRisorsa != "" e esito != "" e anno != 0
			sql.append("WHERE id_cliente=? AND id_risorsa = ? AND data like ? AND data like ? AND esito");
			if(esito.equals("aperta")||esito.equals("persa")){
				sql.append("=?");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
					ps.setInt(2, Integer.parseInt(idRisorsa));
					ps.setString(3, "%"+anno);
					ps.setString(4, esito);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}else if(esito.equals("commessaPresa")){
				sql.append(" NOT IN('aperta','persa')");
				try {
					ps = connessione.prepareStatement(sql.toString());
					ps.setString(1, codiceCliente);
					ps.setInt(2, Integer.parseInt(idRisorsa));
					ps.setString(3, "%"+anno);
				} catch (SQLException e) {
					log.error("errore sql: "+ e);
				}
			}
		}else if(idTrattative != 0){//questa tipo di ricerca viene effettuata al momento di quanto l'utente effettua il dettaglio della trattattiva
			sql.append("WHERE id_trattativa=?");
			try {
				ps = connessione.prepareStatement(sql.toString());
				ps.setInt(1, idTrattative);
			} catch (SQLException e) {
				log.error("errore sql: "+ e); 
			}
		}else{//questa tipo di ricerca viene effettuata al momento di quanto l'utente effettua il dettaglio della trattattiva
			try {
				sql.append(" where data like ?");
				ps = connessione.prepareStatement(sql.toString());
				ps.setString(1, "%"+Calendar.getInstance().get(Calendar.YEAR));
			} catch (SQLException e) {
				log.error("errore sql: "+ e);
			}
		}
		
		log.info("sql: " + sql.toString());
		
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
						rs.getString("note"),
						rs.getString("Risorsa"),
						rs.getString("ragione_sociale")));
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		return listaTrattativa;
	}

	/**
	 * con questo metodo effetuo l'inserimento della trattativa nella tabella tbl_trattative
	 * @param trattativa
	 * @return
	 */
	public String inserimentoTrattative(TrattativeDTO trattativa){
		
		log.info("metodo: inserimentoTrattative");
		String sql = "INSERT INTO tbl_trattative(id_cliente,id_risorsa,contatto,data,oggetto,esito,id_tipologia_trattattiva,note)VALUES(?,?,?,?,?,?,?,?)";
		
		log.info("sql: INSERT INTO tbl_trattative(id_cliente,id_risorsa,contatto,data,oggetto,esito,id_tipologia_trattattiva,note)VALUES("+trattativa.getId_cliente()+","+trattativa.getId_risorsa()+","+trattativa.getContatto()+","+trattativa.getData()+","+trattativa.getOggetto()+","+trattativa.getEsito()+","+trattativa.getId_tipologiaTrattative()+","+trattativa.getNote()+")");
		
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
			ps.setString(8, trattativa.getNote());
			esitoInserimentoTrattative = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
			return "Siamo spiacenti l'inserimento della trattativa non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
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

		log.info("metodo: chiusuraTrattativa");
		String sql = "UPDATE tbl_trattative SET esito='chiusa',trattativa_chiusa=0 WHERE id_trattative=? AND id_azienda=?";
		
		log.info("sql: UPDATE tbl_trattative SET esito='chiusa',trattativa_chiusa=0 WHERE id_trattative="+idTrattativa+" AND id_azienda="+idAzienda);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idTrattativa);
			ps.setInt(2, idAzienda);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
	}

	/**
	 * tramite questo metodo effettuo la ricerca del singola trattativa per poi visualizzare i relativi dati nella pagina "Aggiungi Trattative"
	 * @param idTrattativa
	 * @return
	 */
	public TrattativeDTO aggiornaSingolaTrattativa(int idTrattativa){
		
		log.info("aggiornaSingolaTrattativa");
		
		StringBuilder sql = new StringBuilder("SELECT cliente.ragione_sociale,");
		sql	.append("trattative.id_trattativa,trattative.id_cliente,trattative.id_risorsa,trattative.contatto,")
				.append("trattative.data,trattative.oggetto,trattative.esito,trattative.id_tipologia_trattattiva,trattative.note ")
				.append("FROM tbl_trattative AS trattative, tbl_clienti AS cliente ")
				.append("WHERE cliente.id_cliente=trattative.id_cliente AND trattative.id_trattativa=?");
		
		log.info("SELECT cliente.ragione_sociale,trattative.id_trattativa,trattative.id_cliente,trattative.id_risorsa,trattative.contatto," +
				"trattative.data,trattative.oggetto,trattative.esito,trattative.id_tipologia_trattattiva,trattative.note " +
				"FROM tbl_trattative AS trattative, tbl_clienti AS cliente WHERE cliente.id_cliente=trattative.id_cliente AND trattative.id_trattativa="+idTrattativa);
		
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
				trattativa.setNote(rs.getString("note"));
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return trattativa;
	}

	/**
	 * tramite questo metodo effettuo la modifica della trattativa
	 * @param trattativa
	 * @return
	 */
	public String modificaTrattativa(TrattativeDTO trattativa){
		
		log.info("metodo: modificaTrattativa");
		
		StringBuilder sql = new StringBuilder("UPDATE tbl_trattative ");
		sql	.append("SET id_cliente=?,id_risorsa=?,contatto=?,data=?,oggetto=?,esito=?,id_tipologia_trattattiva=?,note=? ")
				.append("WHERE id_trattativa=?");
		
		log.info("sql: UPDATE tbl_trattative SET id_cliente="+trattativa.getId_cliente()+",id_risorsa="+trattativa.getId_risorsa()+"," +
				"contatto="+trattativa.getContatto()+",data="+trattativa.getData()+",oggetto="+trattativa.getOggetto()+",esito="+trattativa.getEsito()+"," +
				"id_tipologia_trattattiva="+trattativa.getId_tipologiaTrattative()+",note="+trattativa.getNote()+" WHERE id_trattativa="+trattativa.getIdTrattative());
		
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
			ps.setString(8, trattativa.getNote());
			ps.setInt(9, trattativa.getIdTrattative());
			esitoModificaTrattativa = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: " + e);
			return "Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
		return (esitoModificaTrattativa == 1)?
			"ok":
			"Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";
	}

	public String ricercaCodiceCommessa(int idTrattative){
		
		log.info("metodo: ricercaCodiceCommessa");
		
		String sql = "SELECT codice_commessa FROM tbl_commesse WHERE id_trattativa=?";
		
		log.info("SELECT codice_commessa FROM tbl_commesse WHERE id_trattativa="+idTrattative);
		
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
			log.error("errore sql: "+ e);
			return "Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps,rs);
		}
		return codiceCommessa;
	}

	public String ricercaDescrizioneRisorsa(int id_risorsa){
		
		log.info("metodo: ricercaDescrizioneRisorsa");
		
		StringBuilder sql = new StringBuilder("SELECT cognome,nome ");
		sql	.append("FROM tbl_trattative AS trattative, tbl_risorse AS risorse ")
				.append("WHERE trattative.id_risorsa=risorse.id_risorsa AND trattative.id_risorsa=?");
		
		log.info("sql: "+sql.toString());
		
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
			log.error("errore sql: " + e);
			return "Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";//?????????
		}finally{
			close(ps,rs);
		}
		return descrizioneRisorsa;
	}

	public ArrayList<TipologiaTrattative>caricamentoTipologie(){
		
		log.info("metodo: caricamentoTipologie");
		
		String sql = "SELECT id_tipologia_trattativa,descrizione FROM tbl_tipologie_trattative";
		
		log.debug("sql: "+sql);
		
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
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		return listaTrattattive;
	}

	public int caricamentoIdTrattativa(){
		
		log.info("metodo: caricamentoIdTrattativa");
		
		String sql = "SELECT MAX(id_trattativa)id_trattativa_max FROM tbl_trattative";
		
		log.info("sql: " + sql);
		
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
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		return idTrattative;
	}
}