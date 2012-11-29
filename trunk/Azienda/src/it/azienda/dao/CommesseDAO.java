package it.azienda.dao;

import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.TipologiaCommessa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CommesseDAO extends BaseDao {
	
	
	public CommesseDAO(Connection connessione) {
		super(connessione);
	}

	//mi serve per castare le varie date_inizio e date_fine delle varie commesse
	SimpleDateFormat formattaDataWeb = new SimpleDateFormat("dd-MM-yyyy");
	
	//mi serve per formattare le varie date_inizio e date_fine nel formato del DB
	SimpleDateFormat formattaDataServer = new SimpleDateFormat("yyyy-MM-dd");
	
	//con questo metodo effettuo l'inserimento della commessa
	
	public String inserimentoCommessa(CommessaDTO commessa, String tipologia){
		
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
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, commessa.getId_cliente());
				ps.setString(2, commessa.getData_offerta());
				ps.setString(3, commessa.getOggetto_offerta());
				ps.setString(4, commessa.getDescrizione());
				ps.setString(5, commessa.getSede_lavoro());
				ps.setString(6, commessa.getData_inizio());
				ps.setString(7, commessa.getData_fine());
				ps.setInt(8, commessa.getImporto());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			sql = "insert into tbl_commesse(descrizione,codice_commessa,note,id_tipologia_commessa,id_trattativa) values (?,?,?,?,?)"; 
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, commessa.getDescrizione());
				ps.setString(2, commessa.getCodiceCommessa());
				ps.setString(3, commessa.getNote());
				ps.setString(4, commessa.getTipologia());
				ps.setInt(5, 0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		try {
			esitoInserimentoCommessa = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti l'inserimento della commessa non � avvenuta correttamente. Contattare l'amministratore.";
		}finally{
			close(ps);
		}
		
		if(esitoInserimentoCommessa == 1){
			return "ok";
		}else{
			return "Siamo spiacenti l'inserimento della commessa non � avvenuta correttamente. Contattare l'amministratore.";
		}
	}
	
	/*
	 * con questo metodo effettuo il recupero dell'idCommessa al momento dell'inserimento
	 * di una nuova Commessa
	 */
	public int selectIdCommessa(){
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return idCommessa;
	}
	
	/*
	 * tramite questo metodo carico le tipologie di commessa che ci possono
	 * essere. Le descrizioni vengono prese dalla tabella tbl_tipologiacommessa
	 */
	public ArrayList caricamentoTipologiaCommessa(){
		
		String sql = "select * from tbl_tipologie_commesse";
		
		ArrayList tipologie = new ArrayList();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				TipologiaCommessa tipologia = new TipologiaCommessa();
				tipologia.setId_tipologia(rs.getInt(1));
				tipologia.setDescrizione(rs.getString(2));
				tipologie.add(tipologia);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
		String sql = "insert into tbl_associaz_risor_comm (id_risorsa,id_commessa,data_inizio,data_fine,importo,al,attiva) values (?,?,?,?,?,?,?)";
		
		int esitoInserimentoCommessaClienteRisorsa = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, asscommessa.getId_risorsa());
			ps.setInt(2, asscommessa.getId_commessa());
			ps.setString(3, asscommessa.getDataInizio());
			ps.setString(4, asscommessa.getDataFine());
			ps.setInt(5, asscommessa.getTotaleImporto());
			ps.setString(6, asscommessa.getAl());
			ps.setBoolean(7, asscommessa.isAttiva());
			esitoInserimentoCommessaClienteRisorsa = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti l'associazione tra cliente e risorsa non � avvenuta correttamente. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoInserimentoCommessaClienteRisorsa == 1){
			return "ok";
		}else{
			return "Siamo spiacenti l'inserimento della commessa non � avvenuta correttamente. Contattare l'amministratore.";
		}
	}
	
	/*
	 * tramite questo metodo effettuo la creazione automatica del codice Commessa Esterna
	 */
	
	public String creazioneCodiceCommessaEsterna(){
		
		String sql = "select max(codice_commessa) from tbl_commesse where id_tipologia_commessa = 1 or id_tipologia_commessa = 2 group by codice_commessa";
		
		String codiceCommessa = "CCE";
		
		int codCommessa = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				codCommessa = Integer.parseInt(rs.getString(1).substring(4, 6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		codCommessa++;
		if(codCommessa < 10){
			codiceCommessa = codiceCommessa + "00" + String.valueOf(codCommessa);
		}else if(codCommessa >= 10 && codCommessa < 100){
			codiceCommessa = codiceCommessa + "0" + String.valueOf(codCommessa);
		}else if(codCommessa >= 100){
			codiceCommessa = codiceCommessa + String.valueOf(codCommessa);
		}
		
		
		return codiceCommessa;
	}
	
	/*
	 * tramite questo metodo effettuo la creazione automatica del codice Commessa Interna
	 */
	
	public String creazioneCodiceCommessaInterna(){
		
		String sql = "select max(codice_commessa) from tbl_commesse where id_tipologia_commessa = 3 or id_tipologia_commessa = 4 group by codice_commessa";
		
		String codiceCommessa = "CCIN";
		
		int codCommessa = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				codCommessa = Integer.parseInt(rs.getString(1).substring(5, 7));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		codCommessa++;
		if(codCommessa < 10){
			codiceCommessa = codiceCommessa + "00" + String.valueOf(codCommessa);
		}else if(codCommessa >= 10 && codCommessa < 100){
			codiceCommessa = codiceCommessa + "0" + String.valueOf(codCommessa);
		}else if(codCommessa >= 100){
			codiceCommessa = codiceCommessa + String.valueOf(codCommessa);
		}
		
		
		return codiceCommessa;
	}
	
	/*
	 * tramite questo metodo effettuo il caricamento di tutte le commesse
	 * create nella tabella tbl_commessa a seconda di come 
	 * viene effettuata la ricerca.
	 */
	public ArrayList caricamentoCommesse(CommessaDTO commessa){
		
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
		}
		
		if(commessa.getId_cliente() != null && !commessa.getId_cliente().equals("")){
			if(codiceCommessa){
				sql += "and id_cliente = ? ";
				codiceCliente = true;
			}else{
				sql += " where id_cliente = ?";
				codiceCliente = true;
			}
			
		}
		
		if(commessa.getStato() != null && !commessa.getStato().equals("")){
			if(codiceCommessa || codiceCliente){
				sql += " and stato = ? ";
				stato = true;
			}else{
				sql += " where stato = ?";
				stato = true;
			}
		}
		
		if(commessa.getTipologia() != null && !commessa.getTipologia().equals("0")){
			if(codiceCommessa || codiceCliente || stato){
				sql += " and id_tipologia_commessa = ? ";
				tipologia = true;
			}else{
				sql += " where id_tipologia_commessa = ?";
				tipologia = true;
			}
		}
		
		
		sql += " order by id_commessa";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			
			if(codiceCommessa){
				ps.setString(1, "%"+commessa.getCodiceCommessa()+"%");
			}
			
			if(codiceCliente && codiceCommessa){
				ps.setString(2, commessa.getId_cliente());
			}else if(codiceCliente && !codiceCommessa){
				ps.setString(1, commessa.getId_cliente());
			}
			
			if(codiceCommessa && codiceCliente && stato){
				ps.setString(3, commessa.getStato());
			}else if(!codiceCommessa && codiceCliente && stato ||
					  codiceCommessa && !codiceCliente && stato){
				ps.setString(2, commessa.getStato());
			}else if(!codiceCommessa && !codiceCliente && stato){
				ps.setString(1, commessa.getStato());
			}
			
			if(codiceCommessa && codiceCliente && stato && tipologia){
				ps.setString(4, commessa.getTipologia());
			}else if(!codiceCommessa && codiceCliente && stato && tipologia||
					  codiceCommessa && !codiceCliente && stato && tipologia ||
					  codiceCommessa && codiceCliente && !stato && tipologia){
				ps.setString(3, commessa.getTipologia());
			}else if(!codiceCommessa && !codiceCliente &&  stato && tipologia ||
					 !codiceCommessa &&  codiceCliente && !stato && tipologia ||
					  codiceCommessa && !codiceCliente && !stato && tipologia){
				ps.setString(2, commessa.getTipologia());
			}else if(!codiceCommessa && !codiceCliente && !stato && tipologia){
				ps.setString(1, commessa.getTipologia());
			}
			
			System.out.println(sql);
			
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
				commessaTrovata.setData_inizio(rs.getString(7));
				commessaTrovata.setData_fine(rs.getString(8));
				commessaTrovata.setImporto(rs.getInt(9));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		String sql = "select ragione_sociale from tbl_clienti where id_cliente = ?";
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		String sql = "select cognome,nome from tbl_risorse where id_risorsa = ?";
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		CommessaDTO commessa = null;
		String sql = "select * from tbl_commesse where id_commessa = ?";
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
				commessa.setData_inizio(rs.getString(7));
				commessa.setData_fine(rs.getString(8));
				commessa.setImporto(rs.getInt(9));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		return commessa;
	}
	
	/*
	 * tramite questo metodo effettuo la modifica della commessa.
	 */
	
	public String modificaCommessa(CommessaDTO commessa){
		
		String sql = "update tbl_commesse set id_cliente = ?, data_comm = ?, oggetto_comm = ?, descrizione = ?, sede_lavoro = ?, data_inizio = ?, data_fine = ?, importo = ?, importo_lettere = ?, al = ?, pagamento = ?, note = ?, attiva = ?, codice_commessa = ?, totale_ore = ?, stato = ?, id_tipologia_commessa = ? where id_commessa = ?";
		
		int esitoModificaCommessa = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, commessa.getId_cliente());
			ps.setString(2, commessa.getData_offerta());
			ps.setString(3, commessa.getOggetto_offerta());
			ps.setString(4, commessa.getDescrizione());
			ps.setString(5, commessa.getSede_lavoro());
			ps.setString(6, commessa.getData_inizio());
			ps.setString(7, commessa.getData_fine());
			ps.setInt(8, commessa.getImporto());
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti l'inserimento della commessa non � avvenuta correttamente. Contattare l'amministratore.";
		}finally{
			close(ps);
		}
		
		if(esitoModificaCommessa == 1){
			return "ok";
		}else{
			return "Siamo spiacenti l'inserimento della commessa non � avvenuta correttamente. Contattare l'amministratore.";
		}
	}
	
	/*
	 * questo metodo serve per estrapolare i dati relativi alle associazioni
	 * delle risorse alle commesse 
	 */
	public ArrayList caricamentoRisorseCommessa(int idCommessa, String tipologia){
		
		String sql = "";
		
		if(tipologia.equals("1") || tipologia.equals("2")){
			sql = "select asscommessa.*, risorsa.cognome, risorsa.nome, cliente.ragione_sociale from tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorsa,tbl_commesse as commessa,tbl_clienti as cliente where asscommessa.id_risorsa = risorsa.id_risorsa and asscommessa.id_commessa = commessa.id_commessa and commessa.id_cliente = cliente.id_cliente and asscommessa.attiva = true and asscommessa.id_commessa = ?";
		}else{
			sql = "select asscommessa.*, risorsa.cognome, risorsa.nome from tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorsa,tbl_commesse as commessa where asscommessa.id_risorsa = risorsa.id_risorsa and asscommessa.id_commessa = commessa.id_commessa and asscommessa.attiva = true and asscommessa.id_commessa = ?";
		}
			
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
				assCommessa.setDataInizio(rs.getString(4));
				assCommessa.setDataFine(rs.getString(5));
				assCommessa.setTotaleImporto(rs.getInt(6));
				assCommessa.setAttiva(rs.getBoolean(8));
				assCommessa.setDescrizioneRisorsa(rs.getString(9) + " " + rs.getString(10));
				if(tipologia.equals("1") || tipologia.equals("2")){
					assCommessa.setDescrizioneCliente(rs.getString(11));
				}
				listaRisorse.add(assCommessa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		String sql = "select id_risorsa from tbl_associaz_risor_comm where id_commessa = ? and attiva = true group by id_risorsa";
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		boolean commessa = false;
		
		String sql = "select id_commessa from tbl_commessa where codiceCommessa = ?";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		
		Date data = new Date();
		String dataOdierna = formattaDataServer.format(data);
		
		String sql = "update tbl_associaz_risor_comm set data_fine = ?, attiva = ? where id_risorsa = ? and id_commessa = ?";
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacente la dissociazione della risorsa alla commmessa non � avvenuto con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoQuery == 1){
			return "ok";
		}else{
			return "Siamo spiacente la dissociazione della risorsa alla commmessa non � avvenuto con successo. Contattare l'amministrazione.";
		}
	}
	
	/*
	 * effettuo l'eliminazione concreta dell'associazione tra la risorsa e commessa di 
	 * con tipologia 4 cio� "Altro".
	 */
	
	public String elimina_Associazione_Risorsa_con_Commessa_Altro(int idRisorsa, int idCommessa){
		
		String sql = "delete from tbl_associaz_risor_comm where id_risorsa = ? and id_commessa = ?";
		
		int esitoQuery = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			ps.setInt(2, idCommessa);
			esitoQuery = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacente la dissociazione della risorsa alla commmessa non � avvenuto con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoQuery == 1){
			return "ok";
		}else{
			return "Siamo spiacente la dissociazione della risorsa alla commmessa non � avvenuto con successo. Contattare l'amministrazione.";
		}
	}
	
	
	public String chiudiCommessa_Con_Data(int idCommessa){
		
		
		Date data = new Date();
		String dataOdierna = formattaDataServer.format(data);
		
		int esitoChiusuraCommessa = 0;
		
		String sql = "update tbl_commesse set data_fine = ?, stato = 'chiusa', attiva = false where id_commessa = ?";
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, dataOdierna);
			ps.setInt(2, idCommessa);
			esitoChiusuraCommessa = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti la chiusura della commessa non � avvenuta correttamente. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
		
		if(esitoChiusuraCommessa == 1){
			return "ok";
		}else{
			return "Siamo spiacenti la chiusura della commessa non � avvenuta correttamente. Contattare l'amministrazione";
		}
	}
	
	/*
	 * con questo metodo effettuo la chiusura dell'associazione della singola commessa alla risorsa
	 */
	public String chiudi_Associaz_Risors_Comm_Con_Data(int id_associazione){
		
		Date data = new Date();
		String dataOdierna = formattaDataServer.format(data);
		
		int esitoChiusura = 0;
		String sql = "update tbl_associaz_risor_comm set data_fine = ?, attiva = false where id_associazione = ?";
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, dataOdierna);
			ps.setInt(2, id_associazione);
			esitoChiusura = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps);
		}
		
		if(esitoChiusura == 1){
			return "ok";
		}else{
			return "Siamo spiacenti la chiusura delle associazioni tra risorsa e commessa � fallita. Contattare l'amministrazione.";
		}
		
	}
	
	/*
	 * con questo metodo effettuo la chiusura dell'associazione della risorsa alla commessa
	 */
	public void chiudi_Associaz_Risors_Comm_Data_Fine_Antecedente(String data, int id_associazione){
				
		String sql = "update tbl_associaz_risor_comm set data_fine = ?, attiva = ? where id_associazione = ?";
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, data);
			ps.setInt(2, 0);
			ps.setInt(3, id_associazione);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps);
		}
		
	}
	
	/*
	 * con questo metodo effettuo il caricamento della nuova data fine dell'associazione della risorsa alla commessa.
	 */
	public void chiudi_Associaz_Risors_Comm_Data_Fine_Posticipata(String data, int id_associazione){
				
		String sql = "update tbl_associaz_risor_comm set data_fine = ? where id_associazione = ?";
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, data);
			ps.setInt(2, id_associazione);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps);
		}
		
	}
	
	/*
	 * tramite questo metodo effettuo il recupero 
	 * delle descrizione delle risorse associate alle commesse con 
	 * tipologia 1, cio� commesse singole
	 */
	
	public String descrizioneRisorsa(int idCommessa){
		
		String sql = "select risorse.cognome,risorse.nome from tbl_associaz_risor_comm as asscommessa, tbl_risorse as risorse where asscommessa.id_risorsa = risorse.id_risorsa and asscommessa.id_commessa = ?";

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return nomeCognome;
	}
	
	/*
	 * tramite questo metodo effettuo il caricamento del calendario della risorsa
	 */
	
	public void caricamentoCalendario(long differenzaGiorni, Calendar giornoIniziale, int idAssociazione){
		
		
		String sql = "insert into tbl_planning(data,id_associazione) values (?,?)";
		
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
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		
		
		String sql = "select max(id_associazione) from tbl_associaz_risor_comm";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		String sql = "select id_commessa,data_fine from tbl_commesse";
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		String sql = "select asscommessa.id_associazione,asscommessa.id_risorsa,asscommessa.id_commessa,asscommessa.data_inizio,asscommessa.data_fine from tbl_associaz_risor_comm asscommessa, tbl_commesse as commessa where asscommessa.id_commessa = commessa.id_commessa and asscommessa.id_commessa = ? and commessa.id_tipologia_commessa <> 4";
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return lista_Associazioni_Risorsa_Commessa;
		
	}
	
	/*
	 * tramite questo metodo mi carico la singola associazione che c� tra la risorsa
	 * e la commessa.
	 */
	public Associaz_Risor_Comm caricamento_Singole_Associazione_Risorsa_Commessa(int idCommessa,int idRisorsa){
		
		String sql = "select * from tbl_associaz_risor_comm where id_commessa = ? and id_risorsa = ?";
		
		Associaz_Risor_Comm asscommessa = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			ps.setInt(2, idRisorsa);
			rs = ps.executeQuery();
			if(rs.next()){
				asscommessa = new Associaz_Risor_Comm();
				asscommessa.setId_associazione(rs.getInt(1));
				asscommessa.setId_risorsa(rs.getInt(2));
				asscommessa.setId_commessa(rs.getInt(3));
				asscommessa.setDataInizio(rs.getString(4));
				asscommessa.setDataFine(rs.getString(5));
				asscommessa.setTotaleImporto(rs.getInt(6));
				asscommessa.setAl(rs.getString(7));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return asscommessa;
		
	}
	
	public String aggiornaCalendarioChiusuraSingolo(String data,int id_associazione){
		
		String sql = "update tbl_planning set num_ore=0,attivo = false where data = ? and id_associazione = ?";
		int esito = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, data);
			ps.setInt(2, id_associazione);
			esito = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		//istanzio l'oggetto Calendar
	    Calendar calendario = Calendar.getInstance();
	    
	    
	    //dichiaro una variabile che conterr� i giorni
	    int giorni = 0;
	   		    
	    int anno = calendario.get(Calendar.YEAR);
	    
	    if (mese == 2 && anno % 100 == 0 && anno % 400 == 0) {
			giorni = 29;
		} else if (mese == 2 && anno % 100 != 0 && anno % 4 == 0) {
			giorni = 29;
		} else{
			giorni = 28;
		}	
		if (mese == 4 || mese == 6 || mese == 9 || mese == 11) {
			giorni = 30; }
		if (mese == 1 || mese == 3 || mese == 5 || mese == 7 || mese == 8 || mese == 10
				|| mese == 12) {
			giorni = 31; }
	   
	    return giorni; 
	 }
	
	/*
	 * tramite questo metodo effettuo la chiusura delle giornate del mese scelto
	 * sulla tabella Tbl_Planning
	 */
	
	public void chiusuraMensilita(String data){
		
		String sql = "update tbl_planning set attivo = 0 where data = ?";
		
		int esito = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, data);
			esito = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps);
		}
		
		if(esito == 1){
			System.out.println("la data " + data + " � stata chiusa");
		}else{
			System.out.println("la data " + data + " non � presente ne DataBase");
		}
	}
	
	public ArrayList controlloChiusuraMensilit�Commessa(String data){
		
		String sql = "select id_associazione,id_risorsa,id_commessa from tbl_associaz_risor_comm where data_fine = ?";
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return commesseDaChiudere;
		
		
	}
	
	
	public String chiudi_Associaz_Risors_Comm_Senza_Data(int id_associazione){
		
		int esitoChiusura = 0;
		String sql = "update tbl_associaz_risor_comm set attiva = false where id_associazione = ?";
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_associazione);
			esitoChiusura = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps);
		}
		
		if(esitoChiusura == 1){
			return "ok";
		}else{
			return "Siamo spiacenti la chiusura delle associazioni tra risorsa e commessa � fallita. Contattare l'amministrazione.";
		}
		
	}
	
	public String chiudiCommessa_Senza_Data(int idCommessa){
		
		
		int esitoChiusuraCommessa = 0;
		
		String sql = "update tbl_commesse set stato = 'chiusa',attiva = false where id_commessa = ?";
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			esitoChiusuraCommessa = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti la chiusura della commessa non � avvenuta correttamente. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
		
		if(esitoChiusuraCommessa == 1){
			return "ok";
		}else{
			return "Siamo spiacenti la chiusura della commessa non � avvenuta correttamente. Contattare l'amministrazione";
		}
	}
	
	public String controlloDataCommessa(int idCommessa, String data){
		
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
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String sql = "select data_inizio,data_fine from tbl_commesse where id_commessa = ?";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		
		String esitoChiusuraCommessa = "";
		
		Calendar dataInizio = Calendar.getInstance();
		Calendar dataFine = Calendar.getInstance();
		
		Calendar dataCommessa = Calendar.getInstance();
		try {
			dataCommessa.setTime(formattaDataServer.parse(formattaDataServer.format(formattaDataWeb.parse(data))));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String sql = "select data_inizio,data_fine from tbl_commesse where id_commessa = ?";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * di tipologia 4 (cio� "Altro" nella scelta "Aggiungi Commessa") 
	 * verifico quelle che sono state gi� create.
	 */
	
	public ArrayList caricamentoCommesseTipologiaAltro(){
		
		String sql = "select id_commessa,descrizione from tbl_commesse where id_tipologia_commessa = 4 order by descrizione";
		
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
	
	
}