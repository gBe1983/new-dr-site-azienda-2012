package it.azienda.dao;

import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.PlanningDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReportDAO extends BaseDao {

	public ReportDAO(Connection connessione) {
		super(connessione);
	}

	//mi serve per castare le varie date_inizio e date_fine delle varie commesse
	SimpleDateFormat formattaDataWeb = new SimpleDateFormat("dd-MM-yyyy");
	
	//mi serve per formattare le varie date_inizio e date_fine nel formato del DB
	SimpleDateFormat formattaDataServer = new SimpleDateFormat("yyyy-MM-dd");
	
	public ArrayList caricamentoCommessa(){
		 
		String sql = "select id_commessa,descrizione,codice_commessa from tbl_commesse";
		
		ArrayList listaCommesse = new ArrayList();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				CommessaDTO commessa = new CommessaDTO();
				commessa.setId_commessa(rs.getInt(1));
				commessa.setDescrizione(rs.getString(2));
				commessa.setCodiceCommessa(rs.getString(3));
				listaCommesse.add(commessa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return listaCommesse;
		
	}
	
	public ArrayList caricamentoCommesseCliente(String id_cliente){
		
		String sql = "select id_commessa from tbl_commesse where id_cliente = ?";
		
		ArrayList listaCommesse = new ArrayList();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, id_cliente);
			rs = ps.executeQuery();
			while(rs.next()){
				CommessaDTO commessa = new CommessaDTO();
				commessa.setId_commessa(rs.getInt(1));
				listaCommesse.add(commessa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return listaCommesse;
	}
	
	
	/*
	 * tramite questo metodo effettuo la somma delle ore di ogni commessa
	 */
	
	public PlanningDTO caricamentoPlanning(int id_associazione,String dataInizio, String dataFine){
		
		String sql = "";
		
		String codiceCliente = controlloCodiceCliente(id_associazione);
		if(codiceCliente != null){
		
			sql = "select sum(num_ore), risorsa.cognome, risorsa.nome, cliente.ragione_sociale, commessa.codice_commessa, commessa.descrizione" +
                  " from tbl_planning as planning, tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorsa, tbl_commesse as commessa, tbl_clienti as cliente" +
                  " where planning.id_associazione = ?" +
                  " and planning.id_associazione =  asscommessa.id_associazione" +
				  " and asscommessa.id_risorsa =  risorsa.id_risorsa" +
				  " and asscommessa.id_commessa = commessa.id_commessa" +
				  " and commessa.id_cliente = cliente.id_cliente" +
				  " and planning.data between ? and ?";
		}else{
			sql = "select if(sum(num_ore) is null,0,sum(num_ore)) as numero_ore, risorsa.cognome, risorsa.nome, commessa.codice_commessa, commessa.descrizione" +
			      " from tbl_planning as planning, tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorsa, tbl_commesse as commessa" +
			      "	where planning.id_associazione = ?" +
			      "	and planning.id_associazione =  asscommessa.id_associazione" +
			      "	and asscommessa.id_risorsa =  risorsa.id_risorsa " +
			      " and asscommessa.id_commessa = commessa.id_commessa" +
			      " and planning.data between ? and ?";
		}
		
		PlanningDTO planning = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_associazione);
			ps.setString(2, dataInizio);
			ps.setString(3, dataFine);
			rs = ps.executeQuery();
			while(rs.next()){
				planning = new PlanningDTO();
				planning.setNumero_ore(rs.getInt(1));
				planning.setCognome(rs.getString(2));
				planning.setNome(rs.getString(3));
				if(codiceCliente != null){
					planning.setRagione_sociale(rs.getString(4));
					planning.setCodice_commessa(rs.getString(5));
					planning.setDescrizione_commessa(rs.getString(6));
				}else{
					planning.setRagione_sociale("");
					planning.setCodice_commessa(rs.getString(4));
					planning.setDescrizione_commessa(rs.getString(5));
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return planning;
	}
	
	/*
	 * tramite questo metodo effettuo il caricamento delle associazione legate
	 * ad una commessa o ad una risorsa.
	 */
	
	public ArrayList caricamentoAssociazioniCommessaRisorsa(ArrayList listaCommesse, int id_commessa, int id_risorsa, String dataInizio, String dataFine){
		
		if(id_commessa != 0 || id_risorsa != 0){
			String sql = "select id_associazione,id_commessa,id_risorsa from tbl_associaz_risor_comm";
			
			boolean commessa = false;
			boolean risorsa = false;
			
			if(id_commessa != 0){
				sql += " where id_commessa = ? ";
				commessa = true;
			}
			if(id_risorsa != 0 && !commessa){
				sql += " where id_risorsa = ? ";
				risorsa = true;
			}else if(id_risorsa != 0){
				sql += " and id_risorsa = ?";
				risorsa = true;
			}
			
			if(commessa || risorsa){
				sql += "and data_inizio <= ? and data_fine >= ?";
			}
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps = connessione.prepareStatement(sql);
				if(commessa){
					ps.setInt(1, id_commessa);
				}
				if(commessa && risorsa){
					ps.setInt(2, id_risorsa);
				}else if(risorsa){
					ps.setInt(1, id_risorsa);
				}
				
				if(commessa && !risorsa){
					ps.setString(2, dataInizio);
					ps.setString(3, dataFine);
				}else if(!commessa && risorsa){
					ps.setString(2, dataInizio);
					ps.setString(3, dataFine);
				}else if(commessa && risorsa){
					ps.setString(3, dataInizio);
					ps.setString(4, dataFine);
				}
				System.out.println(sql);
				rs = ps.executeQuery();
				while(rs.next()){
					Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
					asscommessa.setId_associazione(rs.getInt(1));
					asscommessa.setId_commessa(rs.getInt(2));
					asscommessa.setId_risorsa(rs.getInt(3));
					listaCommesse.add(asscommessa);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				close(ps,rs);
			}
		}else{
			String sql = "select id_associazione,id_commessa,id_risorsa from tbl_associaz_risor_comm where data_inizio <= ? and data_fine >= ?";
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, formattaDataServer.format(formattaDataWeb.parse(dataInizio)));
				ps.setString(2, formattaDataServer.format(formattaDataWeb.parse(dataFine)));
				rs = ps.executeQuery();
				while(rs.next()){
					Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
					asscommessa.setId_associazione(rs.getInt(1));
					asscommessa.setId_commessa(rs.getInt(2));
					asscommessa.setId_risorsa(rs.getInt(3));
					listaCommesse.add(asscommessa);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				close(ps,rs);
			}
			
			
		}
		
		
		return listaCommesse;
	}
	
	/*
	 * tramite questo metodo verifico se è presente il codice cliente
	 */
	
	private String controlloCodiceCliente(int associazione){
		
		String sql = "select commessa.id_cliente from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_associazione = ? and asscommessa.id_commessa = commessa.id_commessa";
		
		String codiceCliente = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, associazione);
			rs = ps.executeQuery();
			if(rs.next()){
				codiceCliente = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return codiceCliente;
	}
	
	
	
	
}
