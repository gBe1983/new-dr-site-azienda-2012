package it.azienda.dao;

import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.ClienteDTO;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.PlanningDTO;
import it.azienda.dto.RisorsaDTO;
import it.bo.azienda.TimeReport;
import it.util.log.MyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


public class ReportDAO extends BaseDao {
	private MyLogger log;

	public ReportDAO(Connection connessione) {
		super(connessione);
		log=new MyLogger(this.getClass());
	}

	//mi serve per castare le varie date_inizio e date_fine delle varie commesse
	SimpleDateFormat formattaDataWeb = new SimpleDateFormat("dd-MM-yyyy");
	
	//mi serve per formattare le varie date_inizio e date_fine nel formato del DB
	SimpleDateFormat formattaDataServer = new SimpleDateFormat("yyyy-MM-dd");
	
	public List<CommessaDTO>caricamentoCommessa(){
		final String metodo="caricamentoCommessa";
		log.start(metodo);
		String sql = "select id_commessa,descrizione,codice_commessa from tbl_commesse";
		log.debug(metodo, sql);
		List<CommessaDTO>listaCommesse = new ArrayList<CommessaDTO>();
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
			log.error(metodo, "select tbl_commesse", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return listaCommesse;
	}

	public ArrayList caricamentoCommesseCliente(String id_cliente){
		final String metodo="caricamentoCommesseCliente";
		log.start(metodo);
		String sql = "select id_commessa from tbl_commesse where id_cliente = ?";
		log.debug(metodo, sql);
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
			log.error(metodo, "select tbl_commesse", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return listaCommesse;
	}

	/**
	 * tramite questo metodo effettuo la somma delle ore di ogni commessa
	 * @param id_associazione
	 * @param dataInizio
	 * @param dataFine
	 * @return
	 */
	public PlanningDTO caricamentoPlanning(int id_associazione,String dataInizio, String dataFine){
		final String metodo="caricamentoPlanning";
		log.start(metodo);
		String sql = "";
		String codiceCliente = controlloCodiceCliente(id_associazione);
		if(codiceCliente != null){
			sql =	"select sum(num_ore), risorsa.cognome, risorsa.nome, cliente.ragione_sociale, commessa.codice_commessa, commessa.descrizione" +
					" from tbl_planning as planning, tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorsa, tbl_commesse as commessa, tbl_clienti as cliente" +
					" where planning.id_associazione = ?" +
					" and planning.id_associazione =  asscommessa.id_associazione" +
					" and asscommessa.id_risorsa =  risorsa.id_risorsa" +
					" and asscommessa.id_commessa = commessa.id_commessa" +
					" and commessa.id_cliente = cliente.id_cliente" +
					" and planning.data between ? and ?";
		}else{
			sql =	"select if(sum(num_ore) is null,0,sum(num_ore)) as numero_ore, risorsa.cognome, risorsa.nome, commessa.codice_commessa, commessa.descrizione" +
					" from tbl_planning as planning, tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorsa, tbl_commesse as commessa" +
					"	where planning.id_associazione = ?" +
					"	and planning.id_associazione =  asscommessa.id_associazione" +
					"	and asscommessa.id_risorsa =  risorsa.id_risorsa " +
					" and asscommessa.id_commessa = commessa.id_commessa" +
					" and planning.data between ? and ?";
		}
		log.debug(metodo, sql);
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
			log.error(metodo, "select tbl_planning,tbl_associaz_risor_comm,tbl_risorse,tbl_commesse", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return planning;
	}

	/**
	 * tramite questo metodo effettuo il caricamento delle associazione legate ad una commessa o ad una risorsa.
	 * @param listaCommesse
	 * @param id_commessa
	 * @param id_risorsa
	 * @param dataInizio
	 * @param dataFine
	 * @return
	 */
	public ArrayList caricamentoAssociazioniCommessaRisorsa(ArrayList listaCommesse, int id_commessa, int id_risorsa, String dataInizio, String dataFine){
		final String metodo="caricamentoAssociazioniCommessaRisorsa";
		log.start(metodo);
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
				log.debug(metodo, sql);
				rs = ps.executeQuery();
				while(rs.next()){
					Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
					asscommessa.setId_associazione(rs.getInt(1));
					asscommessa.setId_commessa(rs.getInt(2));
					asscommessa.setId_risorsa(rs.getInt(3));
					listaCommesse.add(asscommessa);
				}
			} catch (SQLException e) {
				log.error(metodo, "id_commessa != 0 || id_risorsa != 0", e);
			}finally{
				close(ps,rs);
				log.end(metodo);
			}
		}else{
			String sql = "select id_associazione,id_commessa,id_risorsa from tbl_associaz_risor_comm where data_inizio <= ? and data_fine >= ?";
			PreparedStatement ps=null;
			ResultSet rs=null;
			log.debug(metodo, sql);
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
				log.error(metodo, "else", e);
			} catch (ParseException e) {
				log.error(metodo, "else", e);
			}finally{
				close(ps,rs);
				log.end(metodo);
			}
		}
		return listaCommesse;
	}

	/**
	 * tramite questo metodo verifico se è presente il codice cliente
	 * @param associazione
	 * @return
	 */
	private String controlloCodiceCliente(int associazione){
		final String metodo="caricamentoPlanning";
		log.start(metodo);
		String sql = "select commessa.id_cliente from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_associazione = ? and asscommessa.id_commessa = commessa.id_commessa";
		log.debug(metodo, sql);
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
			log.error(metodo, "select tbl_associaz_risor_comm,tbl_commesse", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return codiceCliente;
	}

	public TimeReport getTimeReport(Calendar dtDa, Calendar dtA, String idCliente, String idRisorsa, String idCommessa){
		final String metodo="getTimeReport";
		log.start(metodo);
		
		TimeReport tr = new TimeReport(dtDa, dtA, idCliente, idRisorsa, idCommessa);
		StringBuilder sql = new StringBuilder("SELECT ");
		sql	.append("planning.data,planning.num_ore,planning.straordinari,planning.ferie,planning.permessi,planning.mutua,planning.permessiNonRetribuiti, ")
			.append("commessa.descrizione,")
			.append("risorse.id_risorsa,risorse.cognome,risorse.nome ")
			.append("FROM tbl_planning planning,tbl_associaz_risor_comm asscommessa,tbl_commesse commessa,tbl_risorse risorse ")
			.append("WHERE planning.id_associazione=asscommessa.id_associazione ")
			.append("AND asscommessa.id_commessa=commessa.id_commessa ")
			.append("AND asscommessa.id_risorsa=risorse.id_risorsa ")
			.append("AND planning.data >=? ")
			.append("AND planning.data <=? ")
			.append("AND planning.attivo = true ");

		if(isValidFilter(idCliente)){
			sql.append("AND commessa.id_cliente=? ");
		}

		if(isValidFilter(idRisorsa)){
			sql.append("AND risorse.id_risorsa=? ");
		}

		if(isValidFilter(idCommessa)){
			sql.append("AND commessa.id_commessa=? ");
		}

		sql.append("ORDER BY cognome,data");

		PreparedStatement ps=null;
		ResultSet rs=null;
		log.debug(metodo, sql.toString());
		int i=1;
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setString(i++, formattaDataServer.format(dtDa.getTime()));
			ps.setString(i++, formattaDataServer.format(dtA.getTime()));
			if(isValidFilter(idCliente)){
				ps.setString(i++, idCliente);
			}
			if(isValidFilter(idRisorsa)){
				ps.setInt(i++, Integer.parseInt(idRisorsa));
			}
			if(isValidFilter(idCommessa)){
				ps.setInt(i++, Integer.parseInt(idCommessa));
			}
			rs = ps.executeQuery();
			while(rs.next()){
				tr.addPlanningDTO(
					new PlanningDTO(
							rs.getDate("data"),
							rs.getDouble("num_ore"),
							rs.getDouble("straordinari"),
							rs.getDouble("ferie"),
							rs.getDouble("permessi"),
							rs.getDouble("mutua"),
							rs.getDouble("permessiNonRetribuiti"),
							rs.getString("descrizione")),
					new RisorsaDTO(
						rs.getInt("id_risorsa"),
						rs.getString("cognome"),
						rs.getString("nome")));
			}
		} catch (SQLException e) {
			log.error(metodo, "", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return tr;
	}
	
	public ArrayList<PlanningDTO> getTimeReportCommessaPerCliente(Calendar dtDa, Calendar dtA,int idRisorsa,int id_commessa){
		
		ArrayList<PlanningDTO> listaGiornate = new ArrayList<PlanningDTO>();
		
		final String metodo="getTimeReport";
		log.start(metodo);
		
		String sql = "SELECT planning.data,planning.num_ore,planning.straordinari,planning.ferie,planning.permessi,planning.mutua,planning.permessiNonRetribuiti " +
				" FROM tbl_planning planning,tbl_associaz_risor_comm asscommessa,tbl_commesse commessa,tbl_risorse risorse, tbl_clienti cliente " +
				" WHERE planning.id_associazione=asscommessa.id_associazione " +
				" AND asscommessa.id_commessa=commessa.id_commessa " +
				" AND commessa.id_cliente=commessa.id_cliente " +
				" AND asscommessa.id_risorsa=risorse.id_risorsa " +
				" AND planning.data >= ? " +
				" AND planning.data <= ? " +
				" AND asscommessa.id_risorsa = ? " +
				" AND commessa.id_commessa = ? " +
				" AND planning.attivo = true" +
				" group by data";

		PreparedStatement ps=null;
		ResultSet rs=null;
		log.debug(metodo, sql.toString());
		int i=1;
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setString(i++, formattaDataServer.format(dtDa.getTime()));
			ps.setString(i++, formattaDataServer.format(dtA.getTime()));
			ps.setInt(i++, idRisorsa);
			ps.setInt(i++, id_commessa);
			
			
			rs = ps.executeQuery();
			while(rs.next()){
				PlanningDTO planning = new PlanningDTO(
								rs.getDate("data"),
								rs.getDouble("num_ore"),
								rs.getDouble("straordinari"),
								rs.getDouble("ferie"),
								rs.getDouble("permessi"),
								rs.getDouble("mutua"),
								rs.getDouble("permessiNonRetribuiti"),
								null
				);
				
				listaGiornate.add(planning);
			}
		} catch (SQLException e) {
			log.error(metodo, "", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return listaGiornate;
	}
	
	/**
	 * 
	 * @param dataDa
	 * @param dataA
	 * @param id_cliente
	 * @param id_risorsa
	 * @param id_commessa
	 * @return ArrayList<Associaz_Risor_Comm>
	 * 
	 * tramite questo metodo effettuo il caricamento delle associazioni, a seconda di come vengono
	 * valorizzati i parametri id_cliente, id_risorsa, id_commessa.
	 * 
	 */
	
	public ArrayList<Associaz_Risor_Comm> caricamentoAssociazioni(String dataDa, String dataA, String id_cliente, String id_risorsa, String id_commessa){
		
		final String metodo="caricamentoAssociazioni";
		log.start(metodo);
		
		String sql = "SELECT asscommessa.id_associazione,cliente.ragione_sociale, commessa.descrizione, commessa.id_commessa,risorse.id_risorsa, risorse.nome, risorse.cognome " +
				" FROM tbl_planning planning, tbl_associaz_risor_comm asscommessa,tbl_commesse commessa,tbl_risorse risorse, tbl_clienti cliente " +
				" WHERE asscommessa.id_commessa=commessa.id_commessa " +
				" AND planning.id_associazione=asscommessa.id_associazione " +
				" AND commessa.id_cliente=cliente.id_cliente " +
				" AND asscommessa.id_risorsa=risorse.id_risorsa " +
				" AND planning.data >= ? " +
				" AND planning.data <= ? ";
				
				//if(id_cliente != null && id_cliente != "" && id_cliente != "all");
				if(isValidFilter(id_cliente)){
					sql += " and cliente.id_cliente = ?";
				}
				
				//if(id_risorsa != null && id_risorsa != "" && id_risorsa != "all");
				if(isValidFilter(id_risorsa)){
					sql += " and asscommessa.id_risorsa = ?";
				}
				
				//if(id_commessa != null && id_commessa != "" && id_commessa != "all");
				if(isValidFilter(id_commessa)){
					sql += " and asscommessa.id_commessa = ?";
				}
				
				sql += " group by asscommessa.id_associazione ORDER BY ragione_sociale";
		
		ArrayList<Associaz_Risor_Comm> listaAssociazioni = new ArrayList<Associaz_Risor_Comm>();
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		int i = 1;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(i++, dataDa);
			ps.setString(i++, dataA);
			
			if(isValidFilter(id_cliente)){
				ps.setString(i++, id_cliente);
			}
			if(isValidFilter(id_risorsa)){
				ps.setInt(i++, Integer.parseInt(id_risorsa));
			}
			if(isValidFilter(id_commessa)){
				ps.setInt(i++, Integer.parseInt(id_commessa));
			}
			
			rs = ps.executeQuery();
			while(rs.next()){
				Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
				asscommessa.setId_associazione(rs.getInt("id_associazione"));
				asscommessa.setDescrizioneCliente(rs.getString("ragione_sociale"));
				asscommessa.setDescrizioneCommessa(rs.getString("descrizione"));
				asscommessa.setId_commessa(rs.getInt("id_commessa"));
				asscommessa.setId_risorsa(rs.getInt("id_risorsa"));
				asscommessa.setDescrizioneRisorsa(rs.getString("cognome") + " " + rs.getString("nome"));
				listaAssociazioni.add(asscommessa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(metodo, "", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		
		return listaAssociazioni;
		
	}
	
	/**
	 * 
	 * @param dtDa
	 * @param dtA
	 * @param mesi
	 * @param descrizione
	 * @return ArrayList<PlanningDTO>
	 * 
	 * questo metodo mi serve per il calcolo delle ore totali di un determinato mese
	 *  
	 */
	
	public ArrayList<PlanningDTO> caricamentoOrePerCliente(Calendar dtDa, Calendar dtA, int mesi, int id_associazione){
		
		final String metodo="caricamentoOrePerCliente";
		log.start(metodo);
		
		SimpleDateFormat formatoGiorni = new SimpleDateFormat("dd");
		
		//creo l'array list per caricare le giornate
		ArrayList<PlanningDTO> listaGiornate = new ArrayList<PlanningDTO>();

		//verifico i mesi di differenza che ci sono tra la data inizio e la data fine
		if(mesi == 0){
				String sql = "SELECT commessa.descrizione, sum(planning.num_ore), sum(planning.straordinari)," +
							 " sum(ferie), sum(permessi), sum(mutua)  " +
							 " FROM tbl_planning planning, tbl_associaz_risor_comm asscommessa,tbl_commesse commessa " +
							 " WHERE asscommessa.id_commessa=commessa.id_commessa  " +
							 " AND planning.id_associazione=asscommessa.id_associazione " +
							 " AND planning.data >= ?  AND planning.data <= ? " +
							 " AND planning.attivo = true "+
							 " AND asscommessa.id_associazione = ?";
			
			
				PreparedStatement ps=null;
				ResultSet rs=null;
				
				int i = 1;
				try {
					ps = connessione.prepareStatement(sql);
					ps.setString(i++, formattaDataServer.format(dtDa.getTime()));
					ps.setString(i++, formattaDataServer.format(dtA.getTime()));
					ps.setInt(i++, id_associazione);
					
					rs = ps.executeQuery();
					while(rs.next()){
						PlanningDTO planning = new PlanningDTO();
						planning.setDescrizione_commessa(rs.getString(1));
						planning.setNumeroOre(rs.getDouble(2));
						planning.setStraordinari(rs.getDouble(3));
						planning.setFerie(rs.getDouble(4));
						planning.setPermessi(rs.getDouble(5));
						planning.setMutua(rs.getDouble(6));
						listaGiornate.add(planning);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					log.error(metodo, "", e);
				}finally{
					close(ps,rs);
					log.end(metodo);
				}
			}else{
				
				for(int x = 0; x <= mesi; x++){
					
					//calcolo il giorno iniziale
					int giornoDiPartenza = Integer.parseInt(formatoGiorni.format(dtDa.getTime()));
					
					//calcolo i giorni massimi di un mese
					int giorniMassimi = dtDa.getActualMaximum(Calendar.DAY_OF_MONTH);
					
					//effettuo la differenza tra il giorno iniale e il massimo dei giorni
					int differenzaGiorni = giorniMassimi - giornoDiPartenza;
					
					//creo un clone della data
					Calendar dataFine = (Calendar)dtDa.clone();
					dataFine.add(Calendar.DAY_OF_MONTH, differenzaGiorni);
					
						
					String sql = "SELECT commessa.descrizione, sum(planning.num_ore), sum(planning.straordinari)," +
								 " sum(ferie), sum(permessi), sum(mutua)  " +
								 " FROM tbl_planning planning, tbl_associaz_risor_comm asscommessa,tbl_commesse commessa " +
								 " WHERE asscommessa.id_commessa=commessa.id_commessa  " +
								 " AND planning.id_associazione=asscommessa.id_associazione " +
								 " AND planning.data >= ?  AND planning.data <= ? " +
								 " AND planning.attivo = true "+
								 " AND asscommessa.id_associazione = ?";
			
					PreparedStatement ps=null;
					ResultSet rs=null;
					
					int i = 1;
					try {
						ps = connessione.prepareStatement(sql);
						ps.setString(i++, formattaDataServer.format(dtDa.getTime()));
						
						/*
						 * verifico che la data fine che mi sono calcolato non sia maggiore della data
						 * fine impostata nella ricerca
						 */
						if(dataFine.getTime().after(dtA.getTime())){
							ps.setString(i++, formattaDataServer.format(dtA.getTime()));
						}else{
							ps.setString(i++, formattaDataServer.format(dataFine.getTime()));
						}
						ps.setInt(i++, id_associazione);
						
						rs = ps.executeQuery();
						while(rs.next()){
							PlanningDTO planning = new PlanningDTO();
							planning.setDescrizione_commessa(rs.getString(1));
							planning.setNumeroOre(rs.getDouble(2));
							planning.setStraordinari(rs.getDouble(3));
							planning.setFerie(rs.getDouble(4));
							planning.setPermessi(rs.getDouble(5));
							planning.setMutua(rs.getDouble(6));
							listaGiornate.add(planning);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						log.error(metodo, "", e);
					} finally{
						close(ps,rs);
						log.end(metodo);
					}
					//aggiungo i giorni alla data iniziale
					dtDa.add(Calendar.DAY_OF_MONTH, differenzaGiorni+1);
				}
			}
		
		return listaGiornate;
	}
	
	/**
	 * 
	 * @param dataDa
	 * @param dataA
	 * @return ArrayList<ClienteDTO>
	 * 
	 * questo metodo mi serve per ricavarmi in maniera ordinata
	 * tutti i clienti presenti in un determinato periodo
	 */
	
	public ArrayList<ClienteDTO> caricamentoClienti(String dataDa, String dataA){
		final String metodo="caricamentoClienti";
		log.start(metodo);
		
		String sql = "SELECT cliente.id_cliente, cliente.ragione_sociale" +
				" FROM tbl_planning planning, tbl_associaz_risor_comm asscommessa,tbl_commesse commessa, tbl_clienti cliente " +
				" WHERE asscommessa.id_commessa=commessa.id_commessa " +
				" AND planning.id_associazione=asscommessa.id_associazione " +
				" AND commessa.id_cliente=cliente.id_cliente " +
				" AND planning.data >= ? " +
				" AND planning.data <= ? " +
				" group by ragione_sociale ORDER BY ragione_sociale";
		
		ArrayList<ClienteDTO> listaClienti = new ArrayList<ClienteDTO>();
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, dataDa);
			ps.setString(2, dataA);
			rs = ps.executeQuery();
			while(rs.next()){
				ClienteDTO cliente = new ClienteDTO();
				cliente.setId_cliente(rs.getString("id_cliente"));
				cliente.setRagioneSociale(rs.getString("ragione_sociale"));
				listaClienti.add(cliente);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(metodo, "", e);
		}finally{
			close(ps, rs);
			log.end(metodo);
		}
		
		return listaClienti;
		
	}
	
	/**
	 * 
	 * @param dataDa
	 * @param dataA
	 * @return int
	 * 
	 * Questo metodo serve per calcolare la differenza di mesi
	 * che cè tra la dataDa e dataA
	 */
	
	public int differenzaMesi(Calendar dataDa, Calendar dataA){
		final String metodo="caricamentoClienti";
		log.start(metodo);
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyyMM");
		
		String sql = "select period_diff(?,?) as differenzaMesi";
		
		int differenzaMesi = 0;
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, formato.format(dataA.getTime()));
			ps.setString(2, formato.format(dataDa.getTime()));
			rs = ps.executeQuery();
			while(rs.next()){
				differenzaMesi = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error(metodo, "", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		
		return differenzaMesi;
	}

	private boolean isValidFilter(String v){
		return v!=null&&!"".equals(v)&&!"all".equals(v);
	}
	
	public File scaricaReportInExcel(String dataDa, String dataA, ArrayList<RisorsaDTO> elencoRisorse, ArrayList<PlanningDTO> giornate,String url){
		
		SimpleDateFormat formatWeb = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formatMese = new SimpleDateFormat("MMMM yyyy");
		SimpleDateFormat formatGiorno = new SimpleDateFormat("EEE");
		
		Calendar dataInizio = Calendar.getInstance();
		Calendar dataFine = Calendar.getInstance();
		
		try {
			dataInizio.setTime(formatWeb.parse(dataDa));
			dataFine.setTime(formatWeb.parse(dataA));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int maxGiorni = dataInizio.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		
		File file = new File(url+"/REPORT_"+formatMese.format(dataInizio.getTime())+".xls");
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.RED.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		Calendar dtDA = Calendar.getInstance();
		dtDA.setTime(dataInizio.getTime());
		
		
		/*
		 * intestazione
		 */
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setFont(font);
		
		/*
		 * corpo
		 */
		HSSFCellStyle cellStyle2 = workbook.createCellStyle();
		cellStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		/*
		 * contorni
		 */
		HSSFCellStyle cellStyle3 = workbook.createCellStyle();
		cellStyle3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle3.setBorderBottom((short)1);
		cellStyle3.setBorderRight((short)1);
		
		HSSFCellStyle cellStyle4 = workbook.createCellStyle();
		cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle4.setBorderRight((short)1);

		HSSFCellStyle cellStyle5 = workbook.createCellStyle();
		cellStyle5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle5.setBorderBottom((short)1);		
		
		//creo il foglio excel
		HSSFSheet sheet = workbook.createSheet("REPORT_"+formatMese.format(dataInizio.getTime()).toUpperCase());
			
		
		HSSFRow rowGiorni = sheet.createRow(1);
		/*
		 * creo le celle
		 */
		
		HSSFCell cell = rowGiorni.createCell((short)0);
		cell = rowGiorni.createCell((short)1);
		cell = rowGiorni.createCell((short)2);
		cell = rowGiorni.createCell((short)3);
		
		/*
		 * setto lo stile
		 */
		cell.setCellStyle(cellStyle);
		
		/* 
		 * setto i valori 
		 */
		cell.setCellValue("");
		
		for(int x = 1; x <= maxGiorni; x++){
			HSSFCell cella = rowGiorni.createCell((short)(x+3));
			cella.setCellStyle(cellStyle);
			cella.setCellValue(x);
		}
		
		HSSFRow rowIntestazione = sheet.createRow(2);
		
		/*
		 * settare le caratteristi
		 */
		HSSFCell cellaIntestazione = rowIntestazione.createCell((short)0);
		HSSFCell cellaIntestazione1 = rowIntestazione.createCell((short)1);
		HSSFCell cellaIntestazione2 = rowIntestazione.createCell((short)2);
		HSSFCell cellaIntestazione3 = rowIntestazione.createCell((short)3);
		
		cellaIntestazione.setCellStyle(cellStyle5);
		cellaIntestazione1.setCellStyle(cellStyle5);
		cellaIntestazione2.setCellStyle(cellStyle5);
		cellaIntestazione3.setCellStyle(cellStyle5);
		
		/*
		 * settare i valori
		 */
		
		cellaIntestazione.setCellValue("N.");
		cellaIntestazione1.setCellValue("COGNOME NOME");
		cellaIntestazione2.setCellValue("");
		cellaIntestazione3.setCellValue("");
		
		for(int x = 0; x < maxGiorni; x++){
			HSSFCell cella = rowIntestazione.createCell((short)(x+4));
			cella.setCellStyle(cellStyle5);
			
			cella.setCellValue(formatGiorno.format(dataInizio.getTime()).substring(0,1).toUpperCase());
			dataInizio.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		/*
		 * creo le celle
		 */
		
		HSSFCell cellaIntestazione4 = rowIntestazione.createCell((short)(maxGiorni+4));
		HSSFCell cellaIntestazione5 = rowIntestazione.createCell((short)(maxGiorni+5));
		HSSFCell cellaIntestazione6 = rowIntestazione.createCell((short)(maxGiorni+6));
		
		/*
		 * caricamento dei fogli di stile
		 */
		
		cellaIntestazione4.setCellStyle(cellStyle5);
		cellaIntestazione5.setCellStyle(cellStyle5);
		cellaIntestazione6.setCellStyle(cellStyle3);
		/*
		 * caricamento dei contenuti
		 */
		
		cellaIntestazione4.setCellValue("ORD.");
		cellaIntestazione5.setCellValue("STR.");
		cellaIntestazione6.setCellValue("ASS.");
		
		int numeroRighe = 3;
		for( int y = 0; y < elencoRisorse.size(); y++){
			RisorsaDTO risorsa = (RisorsaDTO) elencoRisorse.get(y);
		
			if(controlloPresenzaGiornate(giornate, risorsa.getCognome() + " " + risorsa.getNome())){
				int contatoreOrdinarie = 4;
				int contatoreStraordinarie = 4;
				int contatoreAssenze = 4;
				double totOrd = 0.0;
				double totStraordinario = 0.0;
				double totAssenze = 0.0;
				
				HSSFRow rowOrdinarie = sheet.createRow((numeroRighe+1));
				rowOrdinarie.createCell((short)0).setCellValue(y+1);
				rowOrdinarie.createCell((short)1).setCellValue(risorsa.getCognome() + " " + risorsa.getNome());
				rowOrdinarie.createCell((short)2).setCellValue("");
				
				HSSFCell cellaOrdinante = rowOrdinarie.createCell((short)3);
				cellaOrdinante.setCellStyle(cellStyle2);
				cellaOrdinante.setCellValue("Ord.");
				
				for(int x = 0; x < giornate.size(); x++){
					PlanningDTO planning = (PlanningDTO) giornate.get(x);
					if(planning.getDescrizioneRisorsa().equals(risorsa.getCognome() + " " + risorsa.getNome()) && ((planning.getData().getTime().after(dtDA.getTime())) && planning.getData().getTime().before(dataInizio.getTime()) || planning.getData().getTime().equals(dtDA.getTime()))){
						HSSFCell cella = rowOrdinarie.createCell((short)(contatoreOrdinarie));
						cella.setCellStyle(cellStyle2);
						cella.setCellValue(planning.getNumeroOre());
						totOrd += planning.getNumeroOre();
						contatoreOrdinarie++;
					}
				}
				HSSFCell cellaOrdinante2 = rowOrdinarie.createCell((short)(maxGiorni+4));
				cellaOrdinante2.setCellStyle(cellStyle2);
				cellaOrdinante2.setCellValue(totOrd);
				
				HSSFCell cellaOrdinante3 = rowOrdinarie.createCell((short)(maxGiorni+5));
				cellaOrdinante3.setCellStyle(cellStyle2);
				cellaOrdinante3.setCellValue("");
				
				HSSFCell cellaOrdinante4 = rowOrdinarie.createCell((short)(maxGiorni+6));
				cellaOrdinante4.setCellStyle(cellStyle4);
				cellaOrdinante4.setCellValue("");
				
				HSSFRow rowStraordinario = sheet.createRow((numeroRighe+2));
				rowStraordinario.createCell((short)0).setCellValue("");
				rowStraordinario.createCell((short)1).setCellValue("");
				rowStraordinario.createCell((short)2).setCellValue("");
				
				HSSFCell cellaStraordinario5 = rowStraordinario.createCell((short)3);
				cellaStraordinario5.setCellStyle(cellStyle2);
				cellaStraordinario5.setCellValue("Str.");
				
				for(int x = 0; x < giornate.size(); x++){
					PlanningDTO planning = (PlanningDTO) giornate.get(x);
					if(planning.getDescrizioneRisorsa().equals(risorsa.getCognome() + " " + risorsa.getNome()) && ((planning.getData().getTime().after(dtDA.getTime())) && planning.getData().getTime().before(dataInizio.getTime()) || planning.getData().getTime().equals(dtDA.getTime()))){
						HSSFCell cella = rowStraordinario.createCell((short)(contatoreStraordinarie));
						cella.setCellStyle(cellStyle2);
						cella.setCellValue(planning.getStraordinari());
						totStraordinario += planning.getStraordinari();
						contatoreStraordinarie++;
					}
				}
				HSSFCell cellaStraordinario = rowStraordinario.createCell((short)(maxGiorni+5));
				cellaStraordinario.setCellStyle(cellStyle2);
				cellaStraordinario.setCellValue(totStraordinario);
				
				HSSFCell cellaStraordinario1 = rowStraordinario.createCell((short)(maxGiorni+6));
				cellaStraordinario1.setCellStyle(cellStyle4);
				cellaStraordinario1.setCellValue("");
				
				/*
				 * creo la riga dell'assenze
				 */
				
				HSSFRow rowAssenze = sheet.createRow((numeroRighe+3));
				
				HSSFCell cellaAssenza = rowAssenze.createCell((short)0);
				cellaAssenza.setCellStyle(cellStyle5);
				cellaAssenza.setCellValue("");
				
				HSSFCell cellaAssenza1 = rowAssenze.createCell((short)1);
				cellaAssenza1.setCellStyle(cellStyle5);
				cellaAssenza1.setCellValue("");
				
				HSSFCell cellaAssenza2 = rowAssenze.createCell((short)2);
				cellaAssenza2.setCellStyle(cellStyle5);
				cellaAssenza2.setCellValue("");
				
				HSSFCell cellaAssenza3 = rowAssenze.createCell((short)3);
				cellaAssenza3.setCellStyle(cellStyle5);
				cellaAssenza3.setCellValue("Ass.");
				
				int contatoreGiornate = 4;
				for(int x = 0; x < giornate.size(); x++){
					PlanningDTO planning = (PlanningDTO) giornate.get(x);
					if(planning.getDescrizioneRisorsa().equals(risorsa.getCognome() + " " + risorsa.getNome()) && ((planning.getData().getTime().after(dtDA.getTime())) && planning.getData().getTime().before(dataInizio.getTime()) || planning.getData().getTime().equals(dtDA.getTime()))){
						if(planning.getDescrizioneAssenza() != null){
							HSSFCell cella = rowAssenze.createCell((short)(contatoreAssenze));
							cella.setCellStyle(cellStyle5);
							cella.setCellValue(planning.getDescrizioneAssenza());
						}else{
							HSSFCell cella = rowAssenze.createCell((short)(contatoreAssenze));
							cella.setCellStyle(cellStyle5);
							cella.setCellValue(planning.getAssenze());
						}
						totAssenze += planning.getAssenze();
						contatoreAssenze++;
						contatoreGiornate++;
					}
				}
				
				if(contatoreGiornate < (maxGiorni+4)){
					for(int x = contatoreGiornate; x < (maxGiorni+4); x++){
						HSSFCell cella = rowAssenze.createCell((short)(x));
						cella.setCellStyle(cellStyle5);
						cella.setCellValue("");
					}
				}
				HSSFCell cellaAssenze = rowAssenze.createCell((short)(maxGiorni+4));
				cellaAssenze.setCellStyle(cellStyle5);
				cellaAssenze.setCellValue("");

				
				HSSFCell cellaAssenze1 = rowAssenze.createCell((short)(maxGiorni+5));
				cellaAssenze1.setCellStyle(cellStyle5);
				cellaAssenze1.setCellValue("");
				
				HSSFCell cellaAssenze2 = rowAssenze.createCell((short)(maxGiorni+6));
				cellaAssenze2.setCellStyle(cellStyle3);
				cellaAssenze2.setCellValue(totAssenze);
				
				numeroRighe+=3;
			}
		}
	
		try {
			FileOutputStream outStream = new FileOutputStream(file);
			workbook.write(outStream);
			outStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
		
		return file;
		
	}
	
	private boolean controlloPresenzaGiornate(ArrayList<PlanningDTO> giornate, String risorsa){
		
		for(int x = 0; x < giornate.size(); x++){
			PlanningDTO pl = (PlanningDTO) giornate.get(x);
			if(pl.getDescrizioneRisorsa().equals(risorsa)){
				return true;
			}
		}
		return false;
	}

	public ArrayList<PlanningDTO> caricaGiornatePerExcel(String dataDa, String dataA,String risorsa,String cliente,String commessa){
		
		SimpleDateFormat formatServer = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatWeb = new SimpleDateFormat("dd-MM-yyyy");
		
		final String metodo="caricaGiornatePerExcel";
		log.start(metodo);
		
		PreparedStatement ps = null;
		
		ArrayList<PlanningDTO> listaGiornate = new ArrayList<PlanningDTO>();
		
		String sql = "select risorsa.id_risorsa, risorsa.cognome, risorsa.nome, planning.id_planning,planning.data,sum(planning.num_ore),sum(planning.straordinari),sum(planning.ferie),sum(planning.permessi),sum(planning.mutua),sum(planning.permessiNonRetribuiti) " +
				"from tbl_planning as planning, tbl_associaz_risor_comm as asscomm, tbl_commesse as commessa, tbl_clienti as cliente,tbl_risorse as risorsa " +
				"where planning.data >= ? and planning.data <= ? and planning.id_associazione = asscomm.id_associazione " +
				"and asscomm.id_commessa = commessa.id_commessa and cliente.id_cliente = commessa.id_cliente " +
				"and asscomm.id_risorsa = risorsa.id_risorsa ";
		
		if(!risorsa.equals("")){
			sql += " and asscomm.id_risorsa = ? ";
		}
		
		if(!cliente.equals("")){
			sql += " and cliente.id_cliente = ? ";
		}
		
		if(!commessa.equals("")){
			sql += " and  asscomm.id_commessa = ? ";
		}
		
		sql += "group by risorsa.id_risorsa,planning.data order by risorsa.cognome,planning.data";
		
		int contatore = 1;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(contatore++,formatServer.format(formatWeb.parse(dataDa)));
			ps.setString(contatore++,formatServer.format(formatWeb.parse(dataA)));
			
			if(!risorsa.equals("")){
				ps.setInt(contatore++, Integer.parseInt(risorsa));
			}
			
			if(!cliente.equals("")){
				ps.setString(contatore++, cliente);
			}
			
			if(!commessa.equals("")){
				ps.setInt(contatore++,  Integer.parseInt(commessa));
			}
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				PlanningDTO planning = new PlanningDTO();
				planning.setId_risorsa(rs.getInt(1));
				planning.setDescrizioneRisorsa(rs.getString(2) + " " + rs.getString(3));
				planning.setId_planning(rs.getInt(4));
				
				Calendar data = Calendar.getInstance();
				data.setTime(formatServer.parse(rs.getString(5)));
				
				planning.setData(data);
				planning.setNumeroOre(rs.getDouble(6));
				planning.setStraordinari(rs.getDouble(7));
				if(rs.getDouble(8) != 0.0){
					planning.setAssenze(rs.getDouble(8));
					planning.setDescrizioneAssenza("Fe");
				}else if(rs.getDouble(9) != 0.0){
					planning.setAssenze(rs.getDouble(9));
					planning.setDescrizioneAssenza("Pe");
				}else if(rs.getDouble(10) != 0.0){
					planning.setAssenze(rs.getDouble(10));
					planning.setDescrizioneAssenza("M");
				}else{
					planning.setAssenze(0.0);
				}
				
				listaGiornate.add(planning);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaGiornate;
	}
}