package it.azienda.dao;

import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.TipologiaCommessa;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CommesseDAO extends BaseDao {
	private Logger log;

	public CommesseDAO(Connection connessione) {
		super(connessione);
		log= Logger.getLogger(CommesseDAO.class);
	}

	//mi serve per castare le varie date_inizio e date_fine delle varie commesse
	SimpleDateFormat formattaDataWeb = new SimpleDateFormat("dd-MM-yyyy");
	
	//mi serve per formattare le varie date_inizio e date_fine nel formato del DB
	SimpleDateFormat formattaDataServer = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * inserimento della commessa
	 * @param commessa
	 * @param tipologia
	 * @return
	 */
	public String inserimentoCommessa(CommessaDTO commessa, String tipologia){
		
		log.info("metodo: inserimentoCommessa");
		
		int esitoInserimentoCommessa = 0;
		
		String sql = "";
		
		/*
		 * questo controllo lo effettuo per capire con quale tipologia 
		 * si effettua l'inserimento della commessa.
		 * Pertanto se la tipologia ha come valore diverso da 4 
		 * effettuo la query normale altrimenti quella relativa alla commessa.
		 */
		PreparedStatement ps=null;
		if(!tipologia.equals("4")){
			sql = "insert into tbl_commesse (id_cliente,data_comm,oggetto_comm,descrizione,sede_lavoro,data_inizio,data_fine,importo,importo_lettere,al,pagamento,note,attiva,codice_commessa,totale_ore,stato,id_tipologia_commessa,id_trattativa) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			log.info("sql: insert into tbl_commesse (id_cliente,data_comm,oggetto_comm,descrizione,sede_lavoro,data_inizio,data_fine," +
					"importo,importo_lettere,al,pagamento,note,attiva,codice_commessa,totale_ore,stato,id_tipologia_commessa,id_trattativa) values ("
					+commessa.getId_cliente()+","+commessa.getData_offerta()+","+commessa.getOggetto_offerta()+","+commessa.getDescrizione()+","+commessa.getSede_lavoro()+","
					+commessa.getData_inizio()+","+commessa.getData_fine()+","+commessa.getImporto()+","+commessa.getImporto_lettere()+","+commessa.getAl()+","
					+commessa.getPagamento()+","+commessa.getNote()+","+commessa.getStato()+","+commessa.getCodiceCommessa()+","
					+commessa.getTotaleOre()+","+commessa.getStato()+","+commessa.getTipologia()+","+commessa.getId_trattative()+")");

			
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, commessa.getId_cliente());
				ps.setString(2, commessa.getData_offerta());
				ps.setString(3, commessa.getOggetto_offerta());
				ps.setString(4, commessa.getDescrizione());
				ps.setString(5, commessa.getSede_lavoro());
				ps.setString(6, commessa.getData_inizio());
				ps.setString(7, commessa.getData_fine());
				ps.setDouble(8, commessa.getImporto());
				ps.setString(9, commessa.getImporto_lettere());
				ps.setString(10, commessa.getAl());
				ps.setString(11,commessa.getPagamento());
				ps.setString(12,commessa.getNote());
				if(commessa.getStato().equals("aperta")){
					ps.setBoolean(13, true);
				}else{
					ps.setBoolean(13, false);
				}
				ps.setString(14, commessa.getCodiceCommessa());
				ps.setInt(15, commessa.getTotaleOre());
				ps.setString(16, commessa.getStato());
				ps.setString(17, commessa.getTipologia());
				ps.setInt(18, commessa.getId_trattative());
			} catch (SQLException e) {
				log.error("errore sql: " + e);
			}
		}else{
			//recupero l'anno corrente che mi serve per ricavare l'anno corrente
			Calendar calendario = Calendar.getInstance();
			
			sql = "insert into tbl_commesse(descrizione,data_Inizio,data_Fine,codice_commessa,note,id_tipologia_commessa,id_trattativa) values (?,?,?,?,?,?,?)";
			
			log.info("sql: insert into tbl_commesse(descrizione,data_Inizio,data_Fine,codice_commessa,note,id_tipologia_commessa,id_trattativa) values ("+commessa.getDescrizione()+","+calendario.get(Calendar.YEAR)+"-01-01,"+calendario.get(Calendar.YEAR)+"-12-31"+","+commessa.getCodiceCommessa()+","+commessa.getNote()+","+commessa.getTipologia()+","+0);
			
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, commessa.getDescrizione());
				ps.setString(2, calendario.get(Calendar.YEAR)+"-01-01");
				ps.setString(3, calendario.get(Calendar.YEAR)+"-12-31");
				ps.setString(4, commessa.getCodiceCommessa());
				ps.setString(5, commessa.getNote());
				ps.setString(6, commessa.getTipologia());
				ps.setInt(7, 0);
			} catch (SQLException e) {
				log.error("errore sql: " + e);
			}
			
		}

		try {
			esitoInserimentoCommessa = ps.executeUpdate();
			if(esitoInserimentoCommessa == 1 && tipologia.equals("4")){
				sql = "insert into tbl_altro (id_commessa,flag_ferie,flag_mutua,flag_permessi,flag_permessiNonRetribuiti) values (?,?,?,?,?)";
				
				log.info("sql: insert into tbl_altro (id_commessa,flag_ferie,flag_mutua,flag_permessi,flag_permessiNonRetribuiti) values ("+selectIdCommessa()+","+commessa.isFlag_ferie()+","
						  +commessa.isFlag_mutua()+","+commessa.isFlag_permessi()+","+commessa.isFlag_permessiNonRetribuite());
				
				try {
					ps = connessione.prepareStatement(sql);
					ps.setInt(1, selectIdCommessa());
					ps.setBoolean(2, commessa.isFlag_ferie());
					ps.setBoolean(3, commessa.isFlag_mutua());
					ps.setBoolean(4, commessa.isFlag_permessi());
					ps.setBoolean(5, commessa.isFlag_permessiNonRetribuite());
					ps.executeUpdate();
				} catch (SQLException e) {
					log.error("errore sql: " + e);
				}
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
			return "Siamo spiacenti l'inserimento della commessa non è avvenuta correttamente. Contattare l'amministratore.";
		}finally{
			close(ps);
		}
		return (esitoInserimentoCommessa == 1)?
			"ok":
			"Siamo spiacenti l'inserimento della commessa non è avvenuta correttamente. Contattare l'amministratore.";
	}

	/**
	 * recupero dell'idCommessa al momento dell'inserimento di una nuova Commessa
	 * @return
	 */
	public int selectIdCommessa(){
		
		log.info("metodo: selectIdCommessa");
		String sql = "select max(id_commessa) from tbl_commesse";
		
		int idCommessa = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				idCommessa = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		
		return idCommessa;
	}

	/**
	 * restituisce le tipologie di commessa possibili, leggendo da tbl_tipologiacommessa
	 * @return
	 */
	public ArrayList<TipologiaCommessa>caricamentoTipologiaCommessa(){
		
		log.info("metodo: caricamentoTipologiaCommessa");
		
		String sql = "select id_tipologia_commessa,descrizione from tbl_tipologie_commesse";
		
		log.info("sql: select id_tipologia_commessa,descrizione from tbl_tipologie_commesse");
		
		ArrayList<TipologiaCommessa>tipologie = new ArrayList<TipologiaCommessa>();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				tipologie.add(new TipologiaCommessa(rs.getInt("id_tipologia_commessa"),rs.getString("descrizione")));
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return tipologie;
	}

	/*
	 * con questo metodo effettuo l'inserimento dell'associazione tra la commessa il cliente e la risorsa
	 * nella tabella Tbl_AssCommessaClienteRisorsa
	 */
	
	public String inserimentoAssCommessa(Associaz_Risor_Comm asscommessa){
		
		log.info("metodo: inserimentoAssCommessa");
		
		String sql = "insert into tbl_associaz_risor_comm (id_risorsa,id_commessa,data_inizio,data_fine,importo,al,attiva) values (?,?,?,?,?,?,?)";
		
		log.info("sql: insert into tbl_associaz_risor_comm (id_risorsa,id_commessa,data_inizio,data_fine,importo,al,attiva) values ("+asscommessa.getId_risorsa()+","+asscommessa.getId_commessa()+","
				+asscommessa.getDataInizio()+","+asscommessa.getDataFine()+","+asscommessa.getTotaleImporto()+","+asscommessa.getAl()+","+asscommessa.isAttiva());
		
		
		int esitoInserimentoCommessaClienteRisorsa = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, asscommessa.getId_risorsa());
			ps.setInt(2, asscommessa.getId_commessa());
			ps.setString(3, asscommessa.getDataInizio());
			ps.setString(4, asscommessa.getDataFine());
			ps.setDouble(5, asscommessa.getTotaleImporto());
			ps.setString(6, asscommessa.getAl());
			ps.setBoolean(7, asscommessa.isAttiva());
			esitoInserimentoCommessaClienteRisorsa = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
			return "Siamo spiacenti l'associazione tra cliente e risorsa non è avvenuta correttamente. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoInserimentoCommessaClienteRisorsa == 1){
			return "ok";
		}else{
			return "Siamo spiacenti l'inserimento della commessa non è avvenuta correttamente. Contattare l'amministratore.";
		}
	}

	/**
	 * @return next cod commessa esterna
	 */
	public String creazioneCodiceCommessaEsterna(){
		log.info("metodo: creazioneCodiceCommessaEsterna");
		return creazioneCodiceCommessa("CCE");
	}

	/**
	 * @return next cod commessa interna
	 */
	public String creazioneCodiceCommessaInterna(){
		log.info("metodo: creazioneCodiceCommessaInterna");
		return creazioneCodiceCommessa("CCIN");
	}

	/**
	 * @param codiceCommessa
	 * @return next cod commessa
	 */
	private String creazioneCodiceCommessa(String codiceCommessa){
		
		log.info("metodo: creazioneCodiceCommessaEsterna");
		
		int numberPartSize=4;
		int year = Calendar.getInstance().get(Calendar.YEAR)-2000;
		
		StringBuilder sql = new StringBuilder("SELECT MAX(codice_commessa)maxCodCommessa ");
		sql	.append(" FROM tbl_commesse ")
			.append(" WHERE codice_commessa like ?");
		
		log.info("sql: SELECT MAX(codice_commessa)maxCodCommessa FROM tbl_commesse WHERE codice_commessa like "+codiceCommessa+year+"%");
		
		int codCommessa = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setString(1, codiceCommessa+year+"%");
			rs = ps.executeQuery();
			if(rs.next()){
				String maxCodCommessa = rs.getString("maxCodCommessa");
				if(maxCodCommessa != null){
					codCommessa = Integer.parseInt(maxCodCommessa.substring(maxCodCommessa.length()-numberPartSize));
				}
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		codCommessa++;
		return codiceCommessa + year + StringUtils.leftPad(codCommessa+"", numberPartSize, '0');
	}

	/*
	 * tramite questo metodo effettuo il caricamento di tutte le commesse
	 * create nella tabella tbl_commessa a seconda di come 
	 * viene effettuata la ricerca.
	 */
	public ArrayList caricamentoCommesse(CommessaDTO commessa, int anno){
		
		log.info("metodo: caricamentoCommesse");
		
		ArrayList listaCommesse = new ArrayList();
		
		//effettuo questo tipo di controllo a monte per verificare che tipo di ricerca si sta effettuando
		String sql = "select commessa.* from tbl_commesse as commessa";
		
		
		boolean codiceCommessa = false;
		boolean codiceCliente = false;
		boolean stato = false;
		boolean tipologia = false;
		
		
		if(commessa.getCodiceCommessa() != null && !commessa.getCodiceCommessa().equals("")){
			sql += " where codice_commessa like ?";
			codiceCommessa = true;
		}else{
			if(anno == 0){
				Calendar calendar = Calendar.getInstance();
				int annoCorrente = calendar.get(Calendar.YEAR) - 2000;
				sql += " where (commessa.codice_commessa like 'CCE"+annoCorrente+"%' or commessa.codice_commessa like 'CCIN"+annoCorrente+"%')";
			}else{
				sql += " where (commessa.codice_commessa like 'CCE"+anno+"%' or commessa.codice_commessa like 'CCIN"+anno+"%')";
			}
		}
		
		if(commessa.getId_cliente() != null && !commessa.getId_cliente().equals("")){
			sql += "and id_cliente = ? ";
			codiceCliente = true;
		}
		
		if(commessa.getStato() != null && !commessa.getStato().equals("")){
				sql += " and stato = ? ";
				stato = true;
		}
		
		if(commessa.getTipologia() != null && !commessa.getTipologia().equals("")){
			sql += " and id_tipologia_commessa = ? ";
			tipologia = true;
		}
		
		
		sql += " order by id_commessa";
		
		log.info("sql: " + sql);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			
			int contatore = 1;
			if(codiceCommessa){
				ps.setString(contatore, "%"+commessa.getCodiceCommessa()+"%");
				contatore++;
			}
			
			if(codiceCliente){
				ps.setString(contatore, commessa.getId_cliente());
				contatore++;
			}
			
			if(stato){
				ps.setString(contatore, commessa.getStato());
				contatore++;
			}
			
			if(tipologia){
				ps.setString(contatore, commessa.getTipologia());
			}
			
			rs = ps.executeQuery();
			while(rs.next()){
				CommessaDTO commessaTrovata = new CommessaDTO();
				commessaTrovata.setId_commessa(rs.getInt(1));
				if(rs.getString(2) != null && !rs.getString(2).equals("")){
					commessaTrovata.setId_cliente(rs.getString(2));
					commessaTrovata.setDescrizioneCliente(caricamentoDescrizioneCliente(rs.getString(2)));
				}else{
					commessaTrovata.setId_cliente(rs.getString(2));
					commessaTrovata.setDescrizioneCliente("");
				}
				commessaTrovata.setData_offerta(rs.getString(3));
				commessaTrovata.setOggetto_offerta(rs.getString(4));
				commessaTrovata.setDescrizione(rs.getString(5));
				commessaTrovata.setSede_lavoro(rs.getString(6));
				if(rs.getString(7) != null){
					commessaTrovata.setData_inizio(formattaDataWeb.format(formattaDataServer.parse(rs.getString(7))));
				}else{
					commessaTrovata.setData_inizio("");
				}
				if(rs.getString(8) != null){
					commessaTrovata.setData_fine(formattaDataWeb.format(formattaDataServer.parse(rs.getString(8))));
				}else{
					commessaTrovata.setData_fine("");
				}
				commessaTrovata.setImporto(rs.getDouble(9));
				commessaTrovata.setImporto_lettere(rs.getString(10));
				commessaTrovata.setPagamento(rs.getString(12));
				commessaTrovata.setNote(rs.getString(13));
				commessaTrovata.setAttiva(rs.getBoolean(14));
				commessaTrovata.setCodiceCommessa(rs.getString(15));
				commessaTrovata.setTotaleOre(rs.getInt(16));
				commessaTrovata.setStato(rs.getString(17));
				commessaTrovata.setTipologia(rs.getString(18));
				commessaTrovata.setId_trattative(rs.getInt(19));
				
				listaCommesse.add(commessaTrovata);
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("errore di conversione: " + e);
		}finally{
			close(ps,rs);
		}
		return listaCommesse;
	}
	
	/*
	 * Tramite questo metodo effettuo la ricerca di ragione sociale
	 * relazionata alla commessa
	 */
	
	private String caricamentoDescrizioneCliente(String codiceCliente){
		
		log.info("metodo caricamentoDescrizioneCliente");
		
		String sql = "select ragione_sociale from tbl_clienti where id_cliente = ?";
		
		log.info("sql: select ragione_sociale from tbl_clienti where id_cliente = "+codiceCliente);
		
		String ragioneSociale = "";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, codiceCliente);
			rs = ps.executeQuery();
			while(rs.next()){
				ragioneSociale = rs.getString(1);
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		
		return ragioneSociale;
	}
	
	/*
	 * Tramite questo metodo effettuo la ricerca del nome e cognome
	 * relazionata alla commessa
	 */
	
	private String caricamentoDescrizioneRisorsa(int idRisorsa){
		
		log.info("metodo: caricamentoDescrizioneRisorsa");
		
		String sql = "select cognome,nome from tbl_risorse where id_risorsa = ?";
		
		log.info("sql: select cognome,nome from tbl_risorse where id_risorsa = "+idRisorsa);
		
		String anagrafica = "";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				anagrafica = rs.getString(1) + " " + rs.getString(2);
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		
		return anagrafica;
	}
	
	/*
	 * tramite questo metodo effettuo il caricamento della commessa che l'utente
	 * vuole modificate nella tabella tbl_commessa
	 */
	public CommessaDTO aggiornoCommessa(int idCommessa){
		
		log.info("metodo: aggiornoCommessa");
		
		CommessaDTO commessa = null;
		
		String sql = "select * from tbl_commesse where id_commessa = ?";
		
		log.info("sql: select * from tbl_commesse where id_commessa = "+idCommessa);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			rs = ps.executeQuery();
			if(rs.next()){
				commessa = new CommessaDTO();
				commessa.setId_commessa(rs.getInt(1));
				
				if(rs.getString(2) != null){
					commessa.setId_cliente(rs.getString(2));
					commessa.setDescrizioneCliente(caricamentoDescrizioneCliente(rs.getString(2)));
				}else{
					commessa.setId_cliente(rs.getString(2));
					commessa.setDescrizioneCliente("");
				}
				commessa.setData_offerta(rs.getString(3));
				commessa.setOggetto_offerta(rs.getString(4));
				commessa.setDescrizione(rs.getString(5));
				commessa.setSede_lavoro(rs.getString(6));
				if(rs.getString(7) != null){
					commessa.setData_inizio(formattaDataWeb.format(formattaDataServer.parse(rs.getString(7))));
				}else{
					commessa.setData_inizio("");
				}
				if(rs.getString(8) != null){
					commessa.setData_fine(formattaDataWeb.format(formattaDataServer.parse(rs.getString(8))));
				}else{
					commessa.setData_fine("");
				}
				commessa.setImporto(rs.getDouble(9));
				commessa.setImporto_lettere(rs.getString(10));
				commessa.setAl(rs.getString(11));
				commessa.setPagamento(rs.getString(12));
				commessa.setNote(rs.getString(13));
				commessa.setAttiva(rs.getBoolean(14));
				commessa.setCodiceCommessa(rs.getString(15));
				commessa.setTotaleOre(rs.getInt(16));
				commessa.setStato(rs.getString(17));
				commessa.setTipologia(rs.getString(18));
				commessa.setId_trattative(rs.getInt(19));
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("errore di conversione: " + e);
		}finally{
			close(ps,rs);
		}
		return commessa;
	}
	
	public CommessaDTO ricercaCommessa(int idCommessa,String tipologia){
		
		log.info("metodo: aggiornoCommessa");
		
		CommessaDTO commessa = null;
		String sql = "";
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		if(!tipologia.equals("4")){
			sql = "select * from tbl_commesse where id_commessa = ?";
			
			log.info("sql: select * from tbl_commesse where id_commessa = "+idCommessa);
			
			try {
				ps = connessione.prepareStatement(sql);
				ps.setInt(1, idCommessa);
				rs = ps.executeQuery();
				if(rs.next()){
					commessa = new CommessaDTO();
					commessa.setId_commessa(rs.getInt(1));
					
					if(rs.getString(2) != null){
						commessa.setId_cliente(rs.getString(2));
						commessa.setDescrizioneCliente(caricamentoDescrizioneCliente(rs.getString(2)));
					}else{
						commessa.setId_cliente(rs.getString(2));
						commessa.setDescrizioneCliente("");
					}
					commessa.setData_offerta(rs.getString(3));
					commessa.setOggetto_offerta(rs.getString(4));
					commessa.setDescrizione(rs.getString(5));
					commessa.setSede_lavoro(rs.getString(6));
					if(rs.getString(7) != null){
						commessa.setData_inizio(formattaDataWeb.format(formattaDataServer.parse(rs.getString(7))));
					}else{
						commessa.setData_inizio("");
					}
					if(rs.getString(8) != null){
						commessa.setData_fine(formattaDataWeb.format(formattaDataServer.parse(rs.getString(8))));
					}else{
						commessa.setData_fine("");
					}
					commessa.setImporto(rs.getDouble(9));
					commessa.setImporto_lettere(rs.getString(10));
					commessa.setAl(rs.getString(11));
					commessa.setPagamento(rs.getString(12));
					commessa.setNote(rs.getString(13));
					commessa.setAttiva(rs.getBoolean(14));
					commessa.setCodiceCommessa(rs.getString(15));
					commessa.setTotaleOre(rs.getInt(16));
					commessa.setStato(rs.getString(17));
					commessa.setTipologia(rs.getString(18));
					commessa.setId_trattative(rs.getInt(19));
				}
			} catch (SQLException e) {
				log.error("errore sql: "+ e);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				log.error("errore di conversione: " + e);
			}finally{
				close(ps,rs);
			}
		}else{
			sql = "select commessa.codice_commessa, commessa.descrizione, commessa.data_inizio,commessa.data_fine, commessa.note, altro.* from tbl_altro as altro, tbl_commesse as commessa where commessa.id_commessa = ? and commessa.id_tipologia_commessa = 4 and commessa.id_commessa = altro.id_commessa";
			
			log.info("sql: select commessa.codice_commessa, commessa.descrizione, commessa.data_inizio,commessa.data_fine, commessa.note, altro.* from tbl_altro as altro, tbl_commesse as commessa where commessa.id_commessa = "+idCommessa+" and commessa.id_tipologia_commessa = 4 and commessa.id_commessa = altro.id_commessa");
			
			try {
				ps = connessione.prepareStatement(sql);
				ps.setInt(1, idCommessa);
				rs = ps.executeQuery();
				if(rs.next()){
					commessa = new CommessaDTO();
					commessa.setCodiceCommessa(rs.getString(1));
					commessa.setDescrizione(rs.getString(2));
					commessa.setData_inizio(formattaDataWeb.format(formattaDataServer.parse(rs.getString(3))));
					commessa.setData_fine(formattaDataWeb.format(formattaDataServer.parse(rs.getString(4))));
					commessa.setNote(rs.getString(5));
					commessa.setTipologia(tipologia);
					commessa.setId_commessa(rs.getInt(7));
					commessa.setFlag_ferie(rs.getBoolean(8));
					commessa.setFlag_mutua(rs.getBoolean(9));
					commessa.setFlag_permessi(rs.getBoolean(10));
					commessa.setFlag_permessiNonRetribuite(rs.getBoolean(11));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("errore sql: "+e);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				log.error("errore di conversione: " + e);
			}
		}
		return commessa;
	}
	
	/*
	 * tramite questo metodo effettuo la modifica della commessa.
	 */
	
	public String modificaCommessa(CommessaDTO commessa){
		
		log.info("metodo: modificaCommessa");
		
		int esitoModificaCommessa = 0;
		PreparedStatement ps=null;
		
		String sql = "";
		if(!commessa.getTipologia().equals("4")){
			sql = "update tbl_commesse set id_cliente = ?, data_comm = ?, oggetto_comm = ?, descrizione = ?, sede_lavoro = ?, data_inizio = ?, data_fine = ?, importo = ?, importo_lettere = ?, al = ?, pagamento = ?, note = ?, attiva = ?, codice_commessa = ?, totale_ore = ?, stato = ?, id_tipologia_commessa = ? where id_commessa = ?";
		
			log.info("update tbl_commesse set id_cliente = "+commessa.getId_cliente()+", data_comm = "+commessa.getData_offerta()+", oggetto_comm = "+commessa.getOggetto_offerta()+",descrizione = "+commessa.getDescrizione()+", sede_lavoro = "+commessa.getSede_lavoro()+", data_inizio = "+commessa.getData_inizio()+"," +
					"data_fine = "+commessa.getData_fine()+", importo = "+commessa.getImporto()+", importo_lettere = "+commessa.getImporto_lettere()+"," +
					"al = "+commessa.getAl()+", pagamento = "+commessa.getPagamento()+", note = "+commessa.getNote()+", attiva = true, codice_commessa = "+commessa.getCodiceCommessa()+"," +
					"totale_ore = "+commessa.getTotaleOre()+", stato = "+commessa.getStato()+", id_tipologia_commessa = "+commessa.getTipologia()+" where id_commessa = "+commessa.getId_commessa());
			
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, commessa.getId_cliente());
				ps.setString(2, commessa.getData_offerta());
				ps.setString(3, commessa.getOggetto_offerta());
				ps.setString(4, commessa.getDescrizione());
				ps.setString(5, commessa.getSede_lavoro());
				ps.setString(6, commessa.getData_inizio());
				ps.setString(7, commessa.getData_fine());
				ps.setDouble(8, commessa.getImporto());
				ps.setString(9, commessa.getImporto_lettere());
				ps.setString(10, commessa.getAl());
				ps.setString(11, commessa.getPagamento());
				ps.setString(12, commessa.getNote());
				ps.setBoolean(13, true);
				ps.setString(14, commessa.getCodiceCommessa());
				ps.setInt(15, commessa.getTotaleOre());
				ps.setString(16, commessa.getStato());
				ps.setString(17, commessa.getTipologia());
				ps.setInt(18, commessa.getId_commessa());
				esitoModificaCommessa = ps.executeUpdate();
			} catch (SQLException e) {
				log.error("errore sql: "+ e);
				return "Siamo spiacenti l'inserimento della commessa non è avvenuta correttamente. Contattare l'amministratore.";
			}finally{
				close(ps);
			}
		}else{
			
			sql = "update tbl_commesse as commessa, tbl_altro as altro set commessa.descrizione = ?, commessa.note = ?, " +
					" altro.flag_ferie = ?,altro.flag_mutua = ?, altro.flag_permessi = ?, altro.flag_permessiNonRetribuiti = ? " +
					" where altro.id_commessa = ? and altro.id_commessa = commessa.id_commessa";
			
			log.info("sql: update tbl_commesse as commessa, tbl_altro as altro set commessa.descrizione = "+commessa.getDescrizione()+", " +
					"commessa.note = "+commessa.getNote()+", altro.flag_ferie = "+commessa.isFlag_ferie()+"," +
					"altro.flag_mutua = "+commessa.isFlag_mutua()+", altro.flag_permessi = "+commessa.isFlag_permessi()+", altro.flag_permessiNonRetribuiti = "+commessa.isFlag_permessiNonRetribuite()+"" +
					"where altro.id_commessa = ? and altro.id_commessa = commessa.id_commessa");
			
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, commessa.getDescrizione());
				ps.setString(2, commessa.getNote());
				ps.setBoolean(3, commessa.isFlag_ferie());
				ps.setBoolean(4, commessa.isFlag_mutua());
				ps.setBoolean(5, commessa.isFlag_permessi());
				ps.setBoolean(6, commessa.isFlag_permessiNonRetribuite());
				ps.setInt(9, commessa.getId_commessa());
				esitoModificaCommessa = ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.info("errore sql: " + e);
			}
			
		}
		
		if(esitoModificaCommessa >= 1){
			return "ok";
		}else{
			return "Siamo spiacenti l'inserimento della commessa non è avvenuta correttamente. Contattare l'amministratore.";
		}
	}
	
	/*
	 * questo metodo serve per estrapolare i dati relativi alle associazioni
	 * delle risorse alle commesse 
	 */
	public ArrayList caricamentoRisorseCommessa(int idCommessa, String tipologia){
		
		log.info("metodo: caricamentoRisorseCommessa");
		
		String sql = "";
		
		if(tipologia.equals("1") || tipologia.equals("2")){
			sql = "select asscommessa.*, risorsa.cognome, risorsa.nome, cliente.ragione_sociale from tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorsa,tbl_commesse as commessa,tbl_clienti as cliente where asscommessa.id_risorsa = risorsa.id_risorsa and asscommessa.id_commessa = commessa.id_commessa and commessa.id_cliente = cliente.id_cliente and asscommessa.id_commessa = ?";
		}else{
			sql = "select asscommessa.*, risorsa.cognome, risorsa.nome from tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorsa,tbl_commesse as commessa where asscommessa.id_risorsa = risorsa.id_risorsa and asscommessa.id_commessa = commessa.id_commessa and asscommessa.id_commessa = ?";
		}
		
		log.info("sql: "+sql);
		
		ArrayList listaRisorse = new ArrayList();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			rs = ps.executeQuery();
			while(rs.next()){
				Associaz_Risor_Comm assCommessa = new Associaz_Risor_Comm();
				assCommessa.setId_associazione(rs.getInt(1));
				assCommessa.setId_risorsa(rs.getInt(2));
				assCommessa.setId_commessa(rs.getInt(3));
				if(rs.getString(4) != null){
					assCommessa.setDataInizio(formattaDataWeb.format(formattaDataServer.parse(rs.getString(4))));
				}
				if(rs.getString(5) != null){
					assCommessa.setDataFine(formattaDataWeb.format(formattaDataServer.parse(rs.getString(5))));
				}
				assCommessa.setTotaleImporto(rs.getDouble(6));
				assCommessa.setAttiva(rs.getBoolean(8));
				assCommessa.setDescrizioneRisorsa(rs.getString(9) + " " + rs.getString(10));
				if(tipologia.equals("1") || tipologia.equals("2")){
					assCommessa.setDescrizioneCliente(rs.getString(11));
				}
				listaRisorse.add(assCommessa);
			}
		} catch (SQLException e) {
			log.error("errore sql"+ e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("errore nella conversione: "+e);
		}finally{
			close(ps,rs);
		}
		
		return listaRisorse;
	}
	
	/*
	 * tramite questo metodo recupero tutte le risorse
	 * associate a una commessa
	 */
	
	public ArrayList risorseAssociate(int idCommessa){
		
		log.info("metodo: risorseAssociate");
		
		String sql = "select id_risorsa from tbl_associaz_risor_comm where id_commessa = ? and attiva = true group by id_risorsa";
		
		log.info("sql: select id_risorsa from tbl_associaz_risor_comm where id_commessa = "+idCommessa+" and attiva = true group by id_risorsa");
		
		ArrayList risorseAssociate = new ArrayList();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			rs = ps.executeQuery();
			while(rs.next()){
				risorseAssociate.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		
		return risorseAssociate;
	}
	
	/*
	 * con questo metodo verifico che il codice della commessa al momento dell'inserimento
	 * non sia gia presente nella tabella Tbl_Commessa
	 */
	
	public boolean controlloCodiceCommessa(String codiceCommessa){
		
		log.info("metodo: controlloCodiceCommessa");
		
		boolean commessa = false;
		
		String sql = "select id_commessa from tbl_commessa where codiceCommessa = ?";
		
		log.info("sql: select id_commessa from tbl_commessa where codiceCommessa = "+codiceCommessa);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, codiceCommessa);
			rs = ps.executeQuery();
			if(rs.next()){
				commessa = true;
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		
		return commessa;
	}
	
	/*
	 * tramite questo metodo effettuo la disociazione della risorsa a
	 * una commessa
	 */
	
	public String dissociazioniRisorsa(int idRisorsa, int idCommessa){
		
		log.info("metodo: dissociazioniRisorsa");
		Date data = new Date();
		String dataOdierna = formattaDataServer.format(data);
		
		String sql = "update tbl_associaz_risor_comm set data_fine = ?, attiva = ? where id_risorsa = ? and id_commessa = ?";
		
		log.info("sql: update tbl_associaz_risor_comm set data_fine = "+dataOdierna+", attiva = false where id_risorsa = "+idRisorsa+"and id_commessa = "+idCommessa);
		
		int esitoQuery = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, dataOdierna);
			ps.setBoolean(2, false);
			ps.setInt(3, idRisorsa);
			ps.setInt(4, idCommessa);
			esitoQuery = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
			return "Siamo spiacente la dissociazione della risorsa alla commmessa non è avvenuto con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoQuery == 1){
			return "ok";
		}else{
			return "Siamo spiacente la dissociazione della risorsa alla commmessa non è avvenuto con successo. Contattare l'amministrazione.";
		}
	}

	/*
	 * effettuo l'eliminazione concreta dell'associazione tra la risorsa e commessa di 
	 * con tipologia 4 cioè "Altro".
	 */
	
	public String elimina_Associazione_Risorsa_con_Commessa_Altro(int id_associazione){
		
		log.info("metodo: elimina_Associazione_Risorsa_con_Commessa_Altro");
		
		String sql = "delete from tbl_associaz_risor_comm where id_associazione = ?";
		
		log.info("sql: delete from tbl_associaz_risor_comm where id_associazione = "+id_associazione);
		
		int esitoQuery = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_associazione);
			esitoQuery = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
			return "Siamo spiacente la dissociazione della risorsa alla commmessa non è avvenuto con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoQuery == 1){
			return "ok";
		}else{
			return "Siamo spiacente la dissociazione della risorsa alla commmessa non è avvenuto con successo. Contattare l'amministrazione.";
		}
	}

	public String chiudiCommessa_Con_Data(String dataChiusura,int idCommessa){
		
		log.info("metodo: chiudiCommessa_Con_Data");
		
		int esitoChiusuraCommessa = 0;
		
		String sql = "update tbl_commesse set data_fine = ?, stato = 'chiusa', attiva = false where id_commessa = ?";
		
		log.info("sql: update tbl_commesse set data_fine = "+dataChiusura+", stato = 'chiusa', attiva = false where id_commessa = "+idCommessa);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, dataChiusura);
			ps.setInt(2, idCommessa);
			esitoChiusuraCommessa = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
			return "Siamo spiacenti la chiusura della commessa non è avvenuta correttamente. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
		
		if(esitoChiusuraCommessa == 1){
			return "ok";
		}else{
			return "Siamo spiacenti la chiusura della commessa non è avvenuta correttamente. Contattare l'amministrazione";
		}
	}

	/*
	 * con questo metodo effettuo la chiusura dell'associazione della singola commessa alla risorsa
	 */
	public String chiudi_Associaz_Risors_Comm_Con_Data(int id_associazione){
		
		log.info("metodo: chiudi_Associaz_Risors_Comm_Con_Data");
		
		Date data = new Date();
		String dataOdierna = formattaDataServer.format(data);
		
		int esitoChiusura = 0;
		String sql = "update tbl_associaz_risor_comm set data_fine = ?, attiva = false where id_associazione = ?";
		
		log.info("sql: update tbl_associaz_risor_comm set data_fine = "+dataOdierna+", attiva = false where id_associazione = "+id_associazione);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, dataOdierna);
			ps.setInt(2, id_associazione);
			esitoChiusura = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
		if(esitoChiusura == 1){
			return "ok";
		}else{
			return "Siamo spiacenti la chiusura delle associazioni tra risorsa e commessa è fallita. Contattare l'amministrazione.";
		}
		
	}

	/*
	 * con questo metodo effettuo la chiusura dell'associazione della risorsa alla commessa
	 */
	public void chiudi_Associaz_Risors_Comm_Data_Fine_Antecedente(String data, int id_associazione){
		
		log.info("metodo: chiudi_Associaz_Risors_Comm_Data_Fine_Antecedente");
		
		String sql = "update tbl_associaz_risor_comm set data_fine = ?, attiva = ? where id_associazione = ?";
		
		log.info("sql: update tbl_associaz_risor_comm set data_fine = "+data+", attiva = 0 where id_associazione = "+id_associazione);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, data);
			ps.setInt(2, 0);
			ps.setInt(3, id_associazione);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
	}
	
	/*
	 * con questo metodo effettuo il caricamento della nuova data fine 
	 * dell'associazione della risorsa alla commessa.
	 */
	public void associaz_Risors_Comm_Data_Fine_Posticipata(String data, int id_associazione){
		
		log.info("metodo: chiudi_Associaz_Risors_Comm_Data_Fine_Posticipata");
		
		String sql = "update tbl_associaz_risor_comm set data_fine = ? where id_associazione = ?";
		
		log.info("update tbl_associaz_risor_comm set data_fine = "+data+" where id_associazione = "+id_associazione);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, data);
			ps.setInt(2, id_associazione);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
	}
	
	/*
	 * tramite questo metodo effettuo il recupero 
	 * delle descrizione delle risorse associate alle commesse con 
	 * tipologia 1, cioè commesse singole
	 */
	
	public String descrizioneRisorsa(int idCommessa){
		log.info("metodo: descrizioneRisorsa");
		
		String sql = "select risorse.cognome,risorse.nome from tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorse where asscommessa.id_risorsa = risorse.id_risorsa and asscommessa.id_commessa = ?";
		
		log.info("select risorse.cognome,risorse.nome from tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorse where asscommessa.id_risorsa = risorse.id_risorsa and asscommessa.id_commessa = "+idCommessa);
		
		String nomeCognome = "";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			rs = ps.executeQuery();
			while(rs.next()){
				nomeCognome = rs.getString(1) + " " + rs.getString(2);
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		
		return nomeCognome;
	}
	
	public ArrayList<String> descrizioneRisorse(int idCommessa){
		
		log.info("metodo: descrizioneRisorse");
		
		String sql = "select risorse.cognome,risorse.nome from tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorse where asscommessa.id_risorsa = risorse.id_risorsa and asscommessa.id_commessa = ?";
		
		log.info("select risorse.cognome,risorse.nome from tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorse where asscommessa.id_risorsa = risorse.id_risorsa and asscommessa.id_commessa = "+idCommessa);
		
		ArrayList<String> listaRisorse = new ArrayList<String>();
		
		String nomeCognome = "";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			rs = ps.executeQuery();
			while(rs.next()){
				nomeCognome = rs.getString(1) + " " + rs.getString(2);
				listaRisorse.add(nomeCognome);
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		
		return listaRisorse;
	}

	/*
	 * tramite questo metodo effettuo il caricamento del calendario della risorsa
	 */
	
	public void caricamentoCalendario(long differenzaGiorni, Calendar giornoIniziale, int idAssociazione){
		
		log.info("metodo: caricamentoCalendario");
		
		String sql = "insert into tbl_planning(data,id_associazione) values (?,?)";
		
		log.info("sql: insert into tbl_planning(data,id_associazione) values ("+formattaDataServer.format(giornoIniziale.getTime())+","+idAssociazione+")");
		
		while(differenzaGiorni >= 0){
				Calendar calendario = Calendar.getInstance();
				//calendario.setTime(date)
				
				int esito = 0;
				PreparedStatement ps=null;
				try {
					ps = connessione.prepareStatement(sql);
					ps.setString(1, formattaDataServer.format(giornoIniziale.getTime()));
					ps.setInt(2, idAssociazione);
					
					esito = ps.executeUpdate();
				} catch (SQLException e) {
					log.error("errore sql"+ e);
				}finally{
					close(ps);
				}
				
				if(esito == 1){
					giornoIniziale.add(Calendar.DATE, 1);
					differenzaGiorni--;
				}
			
		}
		
	}

	/*
	 * recupero l'idAssociazione che mi serve per inserirlo poi nella tabella Tbl_Planning
	 */
	public int caricamentoIdAssociazione(){
		
		log.info("metodo: caricamentoIdAssociazione");
		
		String sql = "select max(id_associazione) from tbl_associaz_risor_comm";
		
		log.info("sql: "+sql);
		
		int idAssociazione = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				idAssociazione = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		return idAssociazione;
	}
	/*
	 * questo metodo mi permette di caricare tutte le commesse con le loro date fine
	 * lo utilizzo principalmente nella sezione "chiudiCommesse" per caricare tutte le 
	 * commesse con id_commessa e data_fine
	 */
	
	public ArrayList caricamentoCommesse(Connection conn){
		
		log.info("metodo: caricamentoCommesse");
		
		String sql = "select id_commessa,data_fine from tbl_commesse";
		
		log.info("sql: "+sql);
		
		ArrayList listaCommesse = new ArrayList();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				CommessaDTO commessa = new CommessaDTO();
				commessa.setId_commessa(rs.getInt(1));
				commessa.setData_fine(rs.getString(2));
				listaCommesse.add(commessa);
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		
		return listaCommesse;
	}

	/*
	 * tramite questo metodo mi carico tutte le associazioni che ci sono tra la risorsa
	 * e la commessa.
	 */
	public ArrayList caricamento_Tutte_Associazione_Risorsa_Commessa(int idCommessa){
		
		log.info("metodo: caricamento_Tutte_Associazione_Risorsa_Commessa");
		
		String sql = "select asscommessa.id_associazione,asscommessa.id_risorsa,asscommessa.id_commessa,asscommessa.data_inizio,asscommessa.data_fine from tbl_associaz_risor_comm asscommessa, tbl_commesse as commessa where asscommessa.id_commessa = commessa.id_commessa and asscommessa.id_commessa = ? and asscommessa.attiva = true and commessa.id_tipologia_commessa <> 4";
		
		log.info("sql: select asscommessa.id_associazione,asscommessa.id_risorsa,asscommessa.id_commessa,asscommessa.data_inizio,asscommessa.data_fine from tbl_associaz_risor_comm asscommessa, tbl_commesse as commessa where asscommessa.id_commessa = commessa.id_commessa and asscommessa.id_commessa = "+idCommessa+" and asscommessa.attiva = true and commessa.id_tipologia_commessa <> 4");
		
		ArrayList lista_Associazioni_Risorsa_Commessa = new ArrayList();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			rs = ps.executeQuery();
			while(rs.next()){
				Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
				asscommessa.setId_associazione(rs.getInt(1));
				asscommessa.setId_risorsa(rs.getInt(2));
				asscommessa.setId_commessa(rs.getInt(3));
				asscommessa.setDataInizio(rs.getString(4));
				asscommessa.setDataFine(rs.getString(5));
				lista_Associazioni_Risorsa_Commessa.add(asscommessa);
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		
		return lista_Associazioni_Risorsa_Commessa;
		
	}

	/*
	 * tramite questo metodo mi carico la singola associazione che cè tra la risorsa
	 * e la commessa.
	 */
	public Associaz_Risor_Comm caricamento_Singole_Associazione_Risorsa_Commessa(int id_associazione){
		
		log.info("metodo: caricamento_Singole_Associazione_Risorsa_Commessa");
		
		String sql = "select * from tbl_associaz_risor_comm where id_associazione = ?";
		
		log.info("select * from tbl_associaz_risor_comm where id_associazione = "+id_associazione);
		
		Associaz_Risor_Comm asscommessa = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_associazione);
			rs = ps.executeQuery();
			if(rs.next()){
				asscommessa = new Associaz_Risor_Comm();
				asscommessa.setId_associazione(rs.getInt(1));
				asscommessa.setId_risorsa(rs.getInt(2));
				asscommessa.setId_commessa(rs.getInt(3));
				asscommessa.setDataInizio(formattaDataWeb.format(formattaDataServer.parse(rs.getString(4))));
				asscommessa.setDataFine(formattaDataWeb.format(formattaDataServer.parse(rs.getString(5))));
				asscommessa.setTotaleImporto(rs.getDouble(6));
				asscommessa.setAl(rs.getString(7));
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("errore di conversione: "+e);
		}finally{
			close(ps,rs);
		}
		
		return asscommessa;
		
	}

	public String aggiornaCalendarioChiusuraSingolo(String data,int id_associazione){
		
		log.info("metodo: aggiornaCalendarioChiusuraSingolo");
		
		String sql = "update tbl_planning set num_ore=0,attivo = false where data = ? and id_associazione = ?";
		
		log.info("update tbl_planning set num_ore=0,attivo = false where data = "+data+" and id_associazione = "+id_associazione);
		
		int esito = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, data);
			ps.setInt(2, id_associazione);
			esito = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
		if(esito == 1){
			return "corretto";
		}else{
			return "fallita";
		}
	}
	
	/*
	 * tramite questo metodo mi recupero i giorni prenseti nel mese sceltos
	 */
	
	public int calcoloGiorni(int mese) {
		
		log.info("metodo: calcoloGiorni");
		
		// istanzio l'oggetto Calendar
		Calendar calendario = Calendar.getInstance();

		// dichiaro una variabile che conterrà i giorni
		int giorni = 0;

		int anno = calendario.get(Calendar.YEAR);

		if (mese == 2 && anno % 100 == 0 && anno % 400 == 0) {
			giorni = 29;
		} else if (mese == 2 && anno % 100 != 0 && anno % 4 == 0) {
			giorni = 29;
		} else {
			giorni = 28;
		}
		if (mese == 4 || mese == 6 || mese == 9 || mese == 11) {
			giorni = 30;
		}
		if (mese == 1 || mese == 3 || mese == 5 || mese == 7 || mese == 8
				|| mese == 10 || mese == 12) {
			giorni = 31;
		}

		return giorni;
	}

	/*
	 * tramite questo metodo effettuo la chiusura delle giornate del mese scelto
	 * sulla tabella Tbl_Planning
	 */
	
	public void chiusuraMensilita(String data){
		
		log.info("metodo: chiusuraMensilita");
		
		String sql = "update tbl_planning set attivo = 0 where data = ?";
		
		log.info("update tbl_planning set attivo = 0 where data = "+data);
		
		int esito = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, data);
			esito = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
		if(esito == 1){
			log.info("la data " + data + " è stata chiusa");
		}else{
			log.warn("la data " + data + " non è presente nel DataBase");
		}
	}

	public ArrayList controlloChiusuraMensilitàCommessa(String data){
		
		log.info("metodo: controlloChiusuraMensilitàCommessa");
		
		String sql = "select id_associazione,id_risorsa,id_commessa from tbl_associaz_risor_comm where data_fine = ?";
		
		log.info("select id_associazione,id_risorsa,id_commessa from tbl_associaz_risor_comm where data_fine = "+data);
		
		ArrayList commesseDaChiudere = new ArrayList();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, data);
			rs = ps.executeQuery();
			while(rs.next()){
				Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
				asscommessa.setId_associazione(rs.getInt(1));
				asscommessa.setId_risorsa(rs.getInt(2));
				asscommessa.setId_commessa(rs.getInt(3));
				commesseDaChiudere.add(asscommessa);
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		return commesseDaChiudere;
	}

	public String chiudi_Associaz_Risors_Comm_Senza_Data(int id_associazione){
		
		log.info("metodo: chiudi_Associaz_Risors_Comm_Senza_Data");
		
		int esitoChiusura = 0;
		
		String sql = "update tbl_associaz_risor_comm set attiva = false where id_associazione = ?";
		
		log.info("sql: update tbl_associaz_risor_comm set attiva = false where id_associazione = "+id_associazione);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_associazione);
			esitoChiusura = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
		if(esitoChiusura == 1){
			return "ok";
		}else{
			return "Siamo spiacenti la chiusura delle associazioni tra risorsa e commessa è fallita. Contattare l'amministrazione.";
		}
		
	}

	public String chiudiCommessa_Senza_Data(int idCommessa){
		
		log.info("metodo: chiudiCommessa_Senza_Data");
		int esitoChiusuraCommessa = 0;
		
		String sql = "update tbl_commesse set stato = 'chiusa',attiva = false where id_commessa = ?";
		
		log.info("update tbl_commesse set stato = 'chiusa',attiva = false where id_commessa = "+idCommessa);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			esitoChiusuraCommessa = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql:"+ e);
			return "Siamo spiacenti la chiusura della commessa non è avvenuta correttamente. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
		
		if(esitoChiusuraCommessa == 1){
			return "ok";
		}else{
			return "Siamo spiacenti la chiusura della commessa non è avvenuta correttamente. Contattare l'amministrazione";
		}
	}

	public String controlloDataCommessa(int idCommessa, String data){
		
		log.info("metodo: controlloDataCommessa");
		//mi serve per castare le varie date_inizio e date_fine delle varie commesse
		SimpleDateFormat formattaDataWeb = new SimpleDateFormat("dd-MM-yyyy");
		
		//mi serve per formattare le varie date_inizio e date_fine nel formato del DB
		SimpleDateFormat formattaDataServer = new SimpleDateFormat("yyyy-MM-dd");
		
		String esitoChiusuraCommessa = "";
		
		Calendar dataInizio = Calendar.getInstance();
		Calendar dataFine = Calendar.getInstance();
		
		
		Calendar dataCommessa = Calendar.getInstance();
		try {
			dataCommessa.setTime(formattaDataServer.parse(formattaDataServer.format(formattaDataWeb.parse(data))));
		} catch (ParseException e) {
			log.error("errore di conversione: "+ e);
		}
		
		String sql = "select data_inizio,data_fine from tbl_commesse where id_commessa = ?";
		
		log.info("sql: select data_inizio,data_fine from tbl_commesse where id_commessa = "+idCommessa);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			rs = ps.executeQuery();
			while(rs.next()){
				dataInizio.setTime(formattaDataServer.parse(rs.getString(1)));
				dataFine.setTime(formattaDataServer.parse(rs.getString(2)));
				esitoChiusuraCommessa += formattaDataServer.format(formattaDataServer.parse(rs.getString(2)));
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		} catch (ParseException e) {
			log.error("errore di conversione: "+ e);
		}finally{
			close(ps,rs);
		}
		
		if(dataCommessa.getTime().after(dataFine.getTime()) || dataCommessa.getTime().before(dataInizio.getTime())){
			return esitoChiusuraCommessa += ",false";
		}else{
			return esitoChiusuraCommessa += ",true";
		}
		
	}

	public String controlloDataInizio_Associazione(int idCommessa, String data){
		
		log.info("metodo: controlloDataInizio_Associazione");
		String esitoChiusuraCommessa = "";
		
		Calendar dataInizio = Calendar.getInstance();
		Calendar dataFine = Calendar.getInstance();
		
		Calendar dataCommessa = Calendar.getInstance();
		try {
			dataCommessa.setTime(formattaDataServer.parse(formattaDataServer.format(formattaDataWeb.parse(data))));
		} catch (ParseException e) {
			log.error("errore di conversione: "+ e);
		}
		
		String sql = "select data_inizio,data_fine from tbl_commesse where id_commessa = ?";
		
		log.info("select data_inizio,data_fine from tbl_commesse where id_commessa = "+idCommessa);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			rs = ps.executeQuery();
			while(rs.next()){
				dataInizio.setTime(formattaDataServer.parse(rs.getString(1)));
				dataFine.setTime(formattaDataServer.parse(rs.getString(2)));
				esitoChiusuraCommessa += formattaDataServer.format(formattaDataServer.parse(rs.getString(1)));
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		} catch (ParseException e) {
			log.error("errore di conversione: "+ e);
		}finally{
			close(ps,rs);
		}
		
		if(dataCommessa.getTime().before(dataInizio.getTime()) || dataCommessa.getTime().after(dataFine.getTime())){
			return esitoChiusuraCommessa += ",false";
		}else{
			return esitoChiusuraCommessa += ",true";
		}
	}

	/*
	 * tramite questo metodo effettuo il caricamento di tutte le commesse 
	 * di tipologia 4 (cioè "Altro" nella scelta "Aggiungi Commessa") 
	 * verifico quelle che sono state già create.
	 */
	
	public CommessaDTO caricamentoCommesseTipologiaAltro(){
		
		log.info("metodo: caricamentoCommesseTipologiaAltro");
		
		Calendar calendario = Calendar.getInstance();
		
		String sql = "select count(*) as numeroCommessa from tbl_commesse as commessa where id_tipologia_commessa = 4 and commessa.data_inizio like ?";
		
		log.info("sql: select count(*) as numeroCommessa from tbl_commesse as commessa where id_tipologia_commessa = 4 and commessa.data_inizio like "+calendario.get(Calendar.YEAR)+"%");
		
		CommessaDTO commessa = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1,calendario.get(Calendar.YEAR)+"%");
			rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getInt("numeroCommessa") != 0){
					commessa = new CommessaDTO();
				}
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		return commessa;
	}
	
	public int estrazione_tipologia_commessa(int id_commessa){
		
		log.info("metodo: estrazione_tipologia_commessa");
		String sql = "select tipologia.id_tipologia_commessa " +
				"from tbl_tipologie_commesse as tipologia, tbl_commesse as commessa " +
				"where commessa.id_tipologia_commessa = tipologia.id_tipologia_commessa and commessa.id_commessa = ?";
		
		log.info("sql: select tipologia.id_tipologia_commessa " +
				"from tbl_tipologie_commesse as tipologia, tbl_commesse as commessa " +
				"where commessa.id_tipologia_commessa = tipologia.id_tipologia_commessa and commessa.id_commessa = "+id_commessa);
		
		int tipologia = 0;
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_commessa);
			rs = ps.executeQuery();
			while(rs.next()){
				tipologia = rs.getInt("id_tipologia_commessa");
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		return tipologia;
	}
}