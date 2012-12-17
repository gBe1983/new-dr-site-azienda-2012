package it.azienda.dao;

import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.ClienteDTO;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.PlanningDTO;
import it.azienda.dto.RisorsaDTO;
import it.bo.azienda.TimeReport;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
		sql	.append("planning.data,planning.num_ore,planning.straordinari,")
			.append("commessa.descrizione,")
			.append("risorse.id_risorsa,risorse.cognome,risorse.nome ")
			.append("FROM tbl_planning planning,tbl_associaz_risor_comm asscommessa,tbl_commesse commessa,tbl_risorse risorse ")
			.append("WHERE planning.id_associazione=asscommessa.id_associazione ")
			.append("AND asscommessa.id_commessa=commessa.id_commessa ")
			.append("AND asscommessa.id_risorsa=risorse.id_risorsa ")
			.append("AND planning.data >=? ")
			.append("AND planning.data <=? ");

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
		
		String sql = "SELECT planning.data,planning.num_ore,planning.straordinari " +
				" FROM tbl_planning planning,tbl_associaz_risor_comm asscommessa,tbl_commesse commessa,tbl_risorse risorse, tbl_clienti cliente " +
				" WHERE planning.id_associazione=asscommessa.id_associazione " +
				" AND asscommessa.id_commessa=commessa.id_commessa " +
				" AND commessa.id_cliente=commessa.id_cliente " +
				" AND asscommessa.id_risorsa=risorse.id_risorsa " +
				" AND planning.data >= ? " +
				" AND planning.data <= ? " +
				" AND asscommessa.id_risorsa = ? " +
				" AND commessa.id_commessa = ? group by data";

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
		
		String sql = "SELECT cliente.ragione_sociale, commessa.descrizione, commessa.id_commessa,risorse.id_risorsa, risorse.nome, risorse.cognome " +
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
				
				sql += " group by descrizione ORDER BY ragione_sociale";
		
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
	
	public ArrayList<PlanningDTO> caricamentoOrePerCliente(Calendar dtDa, Calendar dtA, int mesi, String descrizione){
		
		final String metodo="caricamentoOrePerCliente";
		log.start(metodo);
		
		SimpleDateFormat formatoGiorni = new SimpleDateFormat("dd");
		
		//creo l'array list per caricare le giornate
		ArrayList<PlanningDTO> listaGiornate = new ArrayList<PlanningDTO>();

		//verifico i mesi di differenza che ci sono tra la data inizio e la data fine
		if(mesi == 0){
				String sql = "SELECT commessa.descrizione, sum(planning.num_ore), sum(planning.straordinari)  " +
							 " FROM tbl_planning planning, tbl_associaz_risor_comm asscommessa,tbl_commesse commessa " +
							 " WHERE asscommessa.id_commessa=commessa.id_commessa  " +
							 " AND planning.id_associazione=asscommessa.id_associazione " +
							 " AND planning.data >= ?  AND planning.data <= ? " +
							 " AND commessa.descrizione = ?";
			
			
				PreparedStatement ps=null;
				ResultSet rs=null;
				
				int i = 1;
				try {
					ps = connessione.prepareStatement(sql);
					ps.setString(i++, formattaDataServer.format(dtDa.getTime()));
					ps.setString(i++, formattaDataServer.format(dtA.getTime()));
					ps.setString(i++, descrizione);
					
					rs = ps.executeQuery();
					while(rs.next()){
						PlanningDTO planning = new PlanningDTO();
						planning.setDescrizione_commessa(rs.getString(1));
						planning.setNumeroOre(rs.getDouble(2));
						planning.setStraordinari(rs.getDouble(3));
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
					
						
					String sql = "SELECT commessa.descrizione, sum(planning.num_ore), sum(planning.straordinari)  " +
							" FROM tbl_planning planning, tbl_associaz_risor_comm asscommessa,tbl_commesse commessa " +
							" WHERE asscommessa.id_commessa=commessa.id_commessa  " +
							" AND planning.id_associazione=asscommessa.id_associazione " +
							" AND planning.data >= ?  AND planning.data <= ? " +
							" AND commessa.descrizione = ?";
			
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
						ps.setString(i++, descrizione);
						
						rs = ps.executeQuery();
						while(rs.next()){
							PlanningDTO planning = new PlanningDTO();
							planning.setDescrizione_commessa(rs.getString(1));
							planning.setNumeroOre(rs.getDouble(2));
							planning.setStraordinari(rs.getDouble(3));
							listaGiornate.add(planning);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						log.error(metodo, "", e);
					}finally{
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

}