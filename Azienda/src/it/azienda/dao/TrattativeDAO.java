package it.azienda.dao;

import it.azienda.dto.TipologiaTrattative;
import it.azienda.dto.TrattativeDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TrattativeDAO extends BaseDao {
	
	public TrattativeDAO(Connection connessione) {
		super(connessione);
	}

	public ArrayList ricercaTrattative(String codiceCliente, int idRisorsa, int idTrattative, String esito){
		
		/*
		 * in questa effettuo i controlli dei paramtri "idRisorsa", "Codice, esito che sono 
		 *  
		 *  Codice == "" e idRisorsa == "" e esito == ""
		 *  idTrattativa <> ""
		 */
		
		
		ArrayList listaTrattativa = new ArrayList();
		
		String sql = "";

		PreparedStatement ps=null;
		ResultSet rs=null;

		if(codiceCliente != null && idRisorsa != 0 && esito == null){
			
			/* Codice <> "" e idRisorsa <> "" e esito == "" */
			
			sql = "select * from v_trattative_per_cliente_dettaglio where id_cliente = ? and id_risorsa = ?";
			
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, codiceCliente);
				ps.setInt(2, idRisorsa);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(codiceCliente == null && idRisorsa != 0 && esito == null) {
			
			/* Codice == "" e idRisorsa <> "" e esito == "" */
			
			sql = "select * from v_trattative_per_cliente_dettaglio where id_risorsa = ?";
			
			try {
				ps = connessione.prepareStatement(sql);
				ps.setInt(1, idRisorsa);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(codiceCliente != null && idRisorsa == 0 && esito == null){
			
			/* Codice <> "" e idRisorsa == "" e esito == "" */
			
			sql = "select * from v_trattative_per_cliente_dettaglio where id_cliente = ?";
			
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, codiceCliente);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(codiceCliente != null && idRisorsa != 0 && esito != null){
			
			/* Codice <> "" e idRisorsa <> "" e esito <> "" */
			
			if(esito.equals("aperta")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where id_cliente = ? and id_risorsa = ? and esito = 'aperta'";
			
				try {
					ps = connessione.prepareStatement(sql);
					ps.setString(1, codiceCliente);
					ps.setInt(2, idRisorsa);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(esito.equals("persa")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where id_cliente = ? and id_risorsa = ? and esito = 'persa'";
				
				try {
					ps = connessione.prepareStatement(sql);
					ps.setString(1, codiceCliente);
					ps.setInt(2, idRisorsa);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(esito.equals("commessaPresa")){
				
				
				sql = "select * from v_trattative_per_cliente_dettaglio where id_cliente = ? and id_risorsa = ? and esito not in ('aperta','persa')";
				
				try {
					ps = connessione.prepareStatement(sql);
					ps.setString(1, codiceCliente);
					ps.setInt(2, idRisorsa);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if(codiceCliente == null && idRisorsa != 0 && esito != null) {
			
			/* Codice == "" e idRisorsa <> "" e esito <> "" */
			
			
			if(esito.equals("aperta")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where id_risorsa = ? and esito = 'aperta'";
				
				try {
					ps = connessione.prepareStatement(sql);
					ps.setInt(1, idRisorsa);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
			}else if(esito.equals("persa")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where id_risorsa = ? and esito = 'persa'";
				
				try {
					ps = connessione.prepareStatement(sql);
					ps.setInt(1, idRisorsa);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(esito.equals("commessaPresa")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where id_risorsa = ? and esito = not in ('aperta','persa')";
				
				try {
					ps = connessione.prepareStatement(sql);
					ps.setInt(1, idRisorsa);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else if(codiceCliente != null && idRisorsa == 0 && esito != null){
			
			/* Codice <> "" e idRisorsa == "" e esito <> "" */ 
			
			if(esito.equals("aperta")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where id_cliente = ? and esito = 'aperta'";
				
				try {
					ps = connessione.prepareStatement(sql);
					ps.setString(1, codiceCliente);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
			}else if(esito.equals("persa")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where id_cliente = ? and esito = 'persa'";
				
				try {
					ps = connessione.prepareStatement(sql);
					ps.setString(1, codiceCliente);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(esito.equals("commessaPresa")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where id_cliente = ? and esito not in ('aperta','persa')";
				
				try {
					ps = connessione.prepareStatement(sql);
					ps.setString(1, codiceCliente);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else if(codiceCliente == null && idRisorsa == 0 && esito != null){
			
			/* Codice == "" e idRisorsa == "" e esito <> "" */ 
			
			if(esito.equals("aperta")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where esito = 'aperta'";
				
				try {
					ps = connessione.prepareStatement(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
			}else if(esito.equals("persa")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where esito = 'persa'";
				
				try {
					ps = connessione.prepareStatement(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(esito.equals("commessaPresa")){
				
				sql = "select * from v_trattative_per_cliente_dettaglio where esito not in ('aperta','persa')";
				
				try {
					ps = connessione.prepareStatement(sql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}else if(idTrattative != 0){
			/*
			 * questa tipo di ricerca viene effettuata al momento di quanto l'utente effettua il dettaglio
			 * della trattattiva
			 */
			sql = "select * from v_trattative_per_cliente_dettaglio where id_trattativa = ?";
			
			try {
				ps = connessione.prepareStatement(sql);
				ps.setInt(1, idTrattative);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			}
			
		}else{
			
			/*
			 * questa tipo di ricerca viene effettuata al momento di quanto l'utente effettua il dettaglio
			 * della trattattiva
			 */
			sql = "select * from v_trattative_per_cliente_dettaglio";
			
			try {
				ps = connessione.prepareStatement(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		System.out.println(sql);
		
		try {
			rs = ps.executeQuery();
			while(rs.next()){
				TrattativeDTO trattative = new TrattativeDTO();
				trattative.setId_cliente(rs.getString(1));
				trattative.setIdTrattative(rs.getInt(2));
				trattative.setDescrizioneCliente(rs.getString(3));
				trattative.setId_risorsa(rs.getInt(4));
				trattative.setDescrizioneRisorsa(rs.getString(5));
				trattative.setData(rs.getString(6));
				trattative.setOggetto(rs.getString(7));
				trattative.setEsito(rs.getString(9));
				listaTrattativa.add(trattative);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return listaTrattativa;
	}
	
	/*
	 * con questo metodo effetuo l'inserimento della trattativa
	 * nella tabella tbl_trattative
	 */
	
	public String inserimentoTrattative(TrattativeDTO trattativa){
		
		String sql = "insert into tbl_trattative (id_cliente,id_risorsa,contatto,data,oggetto,esito,id_tipologia_trattattiva) values (?,?,?,?,?,?,?)";
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti l'inserimento della trattativa non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoInserimentoTrattative == 1){
			return "ok";
		}else{
			return "Siamo spiacenti l'inserimento della trattativa non è avvenuta con successo. Contattare l'amministrazione.";
		}
		
	}
	
	/*
	 * con questo metodo effettuo l'aggiornamento delle trattattive che l'utente 
	 * ha deciso di chiudere
	 */
	public void chiusuraTrattativa(int idTrattativa, int idAzienda){
		
		String sql = "update tbl_trattative set esito = 'chiusa', trattativa_chiusa = 0 where id_trattative = ? and id_azienda = ?";
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idTrattativa);
			ps.setInt(2, idAzienda);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps);
		}
	}
	
	/*
	 * tramite questo metodo effettuo la ricerca del singola trattativa per poi visualizzare
	 * i relativi dati nella pagina "Aggiungi Trattative"
	 */
	public TrattativeDTO aggiornaSingolaTrattativa(int idTrattativa){
		
		String sql = "select cliente.ragione_sociale,trattative.* from tbl_trattative as trattative, tbl_clienti as cliente where cliente.id_cliente = trattative.id_cliente and trattative.id_trattativa = ?";
		
		TrattativeDTO trattativa = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idTrattativa);
			rs = ps.executeQuery();
			if(rs.next()){
				trattativa = new TrattativeDTO();
				trattativa.setDescrizioneCliente(rs.getString(1));
				trattativa.setIdTrattative(rs.getInt(2));
				trattativa.setId_cliente(rs.getString(3));
				trattativa.setId_risorsa(rs.getInt(4));
				trattativa.setContatto(rs.getString(5));
				trattativa.setData(rs.getString(6));
				trattativa.setOggetto(rs.getString(7));
				trattativa.setEsito(rs.getString(8));
				trattativa.setId_tipologiaTrattative(rs.getString(10));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return trattativa;
	}
	/*
	 * tramite questo metodo effettuo la modifica della trattativa
	 * 
	 */
	public String modificaTrattativa(TrattativeDTO trattativa){
		
		String sql = "update tbl_trattative set id_cliente = ?, id_risorsa = ?, contatto = ?, data = ?, oggetto = ?, esito = ?, id_tipologia_trattattiva = ? where id_trattativa = ?";
		
		int esitoModificaTrattativa = 0;
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
			ps.setInt(8, trattativa.getIdTrattative());
			esitoModificaTrattativa = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
		
		if(esitoModificaTrattativa == 1){
			return "ok";
		}else{
			return "Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";
		}
		
		
	}
	
	public String ricercaCodiceCommessa(int idTrattative){
		
		String sql = "select codice_commessa from tbl_commesse where id_trattativa = ?";
		
		String codiceCommessa = "";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idTrattative);
			rs = ps.executeQuery();
			if(rs.next()){
				codiceCommessa = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps,rs);
		}
		
		return codiceCommessa;
	}
	
	public String ricercaDescrizioneRisorsa(int id_risorsa){
		
		String sql = "select cognome, nome from tbl_trattative as trattative, tbl_risorse as risorse where trattative.id_risorsa = risorse.id_risorsa and trattative.id_risorsa = ?";
		
		String descrizioneRisorsa = "";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_risorsa);
			rs = ps.executeQuery();
			if(rs.next()){
				descrizioneRisorsa = rs.getString(1) + " " + rs.getString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Spiacenti le modifiche relative alla trattative non è avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps,rs);
		}
		
		return descrizioneRisorsa;
	}
	
	public ArrayList caricamentoTipologie(){
		
		String sql = "select * from tbl_tipologie_trattative";
		
		ArrayList listaTrattattive = new ArrayList();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				TipologiaTrattative trattattive = new TipologiaTrattative();
				trattattive.setIdTrattattive(rs.getInt(1));
				trattattive.setDescrizione(rs.getString(2));
				listaTrattattive.add(trattattive);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return listaTrattattive;
	}
	
	public int caricamentoIdTrattativa(){
		
		String sql = "select max(id_trattativa) from tbl_trattative";
		
		int idTrattative = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				idTrattative = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return idTrattative;
	}
	
}
