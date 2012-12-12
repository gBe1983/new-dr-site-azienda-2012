package it.azienda.dao;

import it.azienda.dto.ClienteDTO;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends BaseDao {
	private MyLogger log;

	public ClienteDAO(Connection connessione) {
		super(connessione);
		log=new MyLogger(this.getClass());
	}

	/**
	 * tramite questo metodo effettuo l'inserimento del Cliente
	 * @param cliente
	 * @return
	 */
	public String inserimentoCliente(ClienteDTO cliente){
		final String metodo="";
		log.start(metodo);
		StringBuilder sql = new StringBuilder("insert into tbl_clienti");
		sql	.append("(id_cliente,ragione_sociale,indirizzo,cap,citta,provincia,p_iva,referente,telefono,")
				.append("cellulare,fax,email,sito,cod_Fiscale)")
				.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		log.debug(metodo, sql.toString());
		int esitoInserimentoCliente = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setString(1, cliente.getId_cliente());
			ps.setString(2, cliente.getRagioneSociale());
			ps.setString(3, cliente.getIndirizzo());
			ps.setString(4, cliente.getCap());
			ps.setString(5, cliente.getCitta());
			ps.setString(6, cliente.getProvincia());
			ps.setString(7, cliente.getPIva());
			ps.setString(8, cliente.getReferente());
			ps.setString(9, cliente.getTelefono());
			ps.setString(10, cliente.getCellulare());
			ps.setString(11, cliente.getFax());
			ps.setString(12, cliente.getEmail());
			ps.setString(13, cliente.getSito());
			ps.setString(14, cliente.getCodFiscale());
			esitoInserimentoCliente = ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "", e);
			return "Siamo spiacenti ma l'inserimento del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
			log.end(metodo);
		}
		return (esitoInserimentoCliente == 1)?
			"ok":
			"Siamo spiacenti ma l'inserimento del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
	}

	/**
	 * tramite questo metodo effettuo la modifica del Cliente
	 * @param cliente
	 * @return
	 */
	public String modificaCliente(ClienteDTO cliente){
		final String metodo="modificaCliente";
		log.start(metodo);
		StringBuilder sql = new StringBuilder("UPDATE tbl_clienti ");
		sql	.append("SET ragione_sociale=?,indirizzo=?,cap=?,citta=?,provincia=?,p_iva=?,")
				.append("referente=?,telefono=?,cellulare=?,fax=?,email=?,sito=?,cod_fiscale=? ")
				.append("WHERE id_cliente=?");
		log.debug(metodo, sql.toString());
		int esitoModificaCliente = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setString(1, cliente.getRagioneSociale());
			ps.setString(2, cliente.getIndirizzo());
			ps.setString(3, cliente.getCap());
			ps.setString(4, cliente.getCitta());
			ps.setString(5, cliente.getProvincia());
			ps.setString(6, cliente.getPIva());
			ps.setString(7, cliente.getReferente());
			ps.setString(8, cliente.getTelefono());
			ps.setString(9, cliente.getCellulare());
			ps.setString(10, cliente.getFax());
			ps.setString(11, cliente.getEmail());
			ps.setString(12, cliente.getSito());
			ps.setString(13, cliente.getCodFiscale());
			ps.setString(14, cliente.getId_cliente());
			esitoModificaCliente = ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "UPDATE tbl_clienti", e);
			return "Siamo spiacenti ma la modifica del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
			log.end(metodo);
		}
		return(esitoModificaCliente == 1)?
			"ok":
			"Siamo spiacenti ma la modifica del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
	}

	/**
	 * con questo metodo carico tutti i nomitivi di tutti i clienti
	 * @return
	 */
	public List<String>caricamentoNominativiCliente(){
		final String metodo="caricamentoNominativiCliente";
		log.start(metodo);
		List<String>listaNominativi = new ArrayList<String>();
		String sql = "SELECT ragione_sociale FROM tbl_clienti WHERE attivo=1 ORDER BY ragione_sociale ASC";
		log.debug(metodo, sql.toString());
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				listaNominativi.add(rs.getString("ragione_sociale"));
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT tbl_clienti attivo=1", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return listaNominativi;
	}

	/**
	 * con questo metodo carico tutti i nomitivi di tutti i clienti
	 * @return
	 */
	public List<String>caricamentoNominativiClienteDisabilitati(){
		final String metodo="caricamentoNominativiClienteDisabilitati";
		log.start(metodo);
		List<String>listaNominativi = new ArrayList<String>();
		String sql = "SELECT ragione_sociale FROM tbl_clienti WHERE attivo=0";
		log.debug(metodo, sql.toString());
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				listaNominativi.add(rs.getString("ragione_sociale"));
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT tbl_clienti attivo=0", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return listaNominativi;
	}

	/**
	 * con questo metodo effettuo il caricamento del profilo del Cliente attraverso due tipologie che sono:
	 * - Nominativo: che avviene quando effettuo la "Ricerca Cliente" dove mi viene passato il nominativo del Cliente
	 * - CodiceCliente: a fronte della visualizzazione, quando effettuiamo "Modifica Cliente" ricerco il cliente per il suo "CodiceCliente"
	 * @param codiceCliente
	 * @param nominativo
	 * @return
	 */
	public ClienteDTO caricamentoCliente(String codiceCliente, String nominativo){
		final String metodo="caricamentoCliente";
		log.start(metodo);
		StringBuilder sql = new StringBuilder("SELECT id_cliente,ragione_sociale,indirizzo,cap,citta,");
		sql	.append("provincia,p_iva,referente,telefono,cellulare,fax,email,")
				.append("sito,attivo,cod_fiscale,id_conto_corrente ")
				.append(" FROM tbl_clienti WHERE ")
				.append(((codiceCliente == null)?" ragione_sociale":"id_cliente"))
				.append("=?");
		log.debug(metodo, sql.toString());
		PreparedStatement ps=null;
		ResultSet rs=null;
		ClienteDTO cliente = null;
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setString(1,((codiceCliente == null)?nominativo:codiceCliente));
			rs = ps.executeQuery();
			if(rs.next()){
				cliente =
					new ClienteDTO(
						rs.getString("id_cliente"),
						rs.getString("ragione_sociale"),
						rs.getString("indirizzo"),
						rs.getString("cap"),
						rs.getString("citta"),
						rs.getString("provincia"),
						rs.getString("p_iva"),
						rs.getString("referente"),
						rs.getString("telefono"),
						rs.getString("cellulare"),
						rs.getString("fax"),
						rs.getString("email"),
						rs.getString("sito"),
						rs.getBoolean("attivo"),
						rs.getString("cod_fiscale"),
						rs.getInt("id_conto_corrente"));
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT tbl_clienti for codiceCliente:"+codiceCliente+" nominativo:"+nominativo, e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return cliente;
	}

	/**
	 * con questo metodo estrapolo tutti i Clienti legati all'azienda
	 * @return
	 */
	public List<ClienteDTO>caricamentoClienti(){
		final String metodo="caricamentoClienti";
		log.start(metodo);
		List<ClienteDTO>listaClienti = new ArrayList<ClienteDTO>();
		String sql = "SELECT id_cliente,ragione_sociale FROM tbl_clienti WHERE attivo=true ORDER BY ragione_sociale ASC";
		log.debug(metodo, sql.toString());
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				listaClienti.add(
					new ClienteDTO(
						rs.getString("id_cliente"),
						rs.getString("ragione_sociale")));
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT tbl_clienti attivo=true", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return listaClienti;
	}

	/**
	 * con questo metodo effettuo l'eliminazione logica del Cliente che l'Utente ha deciso di eliminare.
	 * @param codiceCliente
	 * @return
	 */
	public String disabilitaCliente(String codiceCliente){
		final String metodo="disabilitaCliente";
		log.start(metodo);
		String sql = "UPDATE tbl_clienti SET attivo=0 WHERE id_cliente=?";
		log.debug(metodo, sql.toString());
		int esitoEliminazioneCliente = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, codiceCliente);
			esitoEliminazioneCliente = ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "UPDATE tbl_clienti for codiceCliente:"+codiceCliente, e);
			return "Siamo spiacenti ma l'eliminazione del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
			log.end(metodo);
		}
		return (esitoEliminazioneCliente != 0)?
			"ok":
			"Siamo spiacenti ma l'eliminazione del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
	}

	public boolean controlloCodiceCliente(String codiceCliente){
		final String metodo="controlloCodiceCliente";
		log.start(metodo);
		String sql = "SELECT ragioneSociale FROM tbl_cliente WHERE codiceCliente=?";
		log.debug(metodo, sql.toString());
		boolean esitoControlloCodiceCliente = false;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, codiceCliente);
			rs = ps.executeQuery();
			if(rs.next()){
				esitoControlloCodiceCliente = true;
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT tbl_cliente for codiceCliente:"+codiceCliente, e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return esitoControlloCodiceCliente;
	}

	/**
	 * tramite questo metodo effettuo il caricamento dinamico del codice cliente al momento dell'inserimento del cliente
	 * @return
	 */
	public String creazioneCodiceCliente(){
		final String metodo="creazioneCodiceCliente";
		log.start(metodo);
		String sql = "SELECT MAX(id_cliente)max_id_cliente FROM tbl_clienti ORDER BY id_cliente";
		log.debug(metodo, sql.toString());
		int codCliente = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getString(1) != null){
					codCliente = Integer.parseInt(rs.getString("max_id_cliente").substring(3, 5));
				}
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT MAX(id_cliente) tbl_clienti", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		codCliente++;
		String codiceCliente = "CL";
		if(codCliente < 10){
			codiceCliente += "00" + String.valueOf(codCliente);
		}else if(codCliente >= 10 && codCliente < 100){
			codiceCliente += "0" + String.valueOf(codCliente);
		}else if(codCliente >= 100){
			codiceCliente += String.valueOf(codCliente);
		}
		return codiceCliente;
	}

	public List<String>caricamentoNominativiClienteDisabilitato(int idAzienda){
		final String metodo="caricamentoNominativiClienteDisabilitato";
		log.start(metodo);
		List<String>listaNominativi = new ArrayList<String>();
		String sql = "SELECT ragioneSociale FROM tbl_cliente WHERE id_azienda=? and attivo=0";
		log.debug(metodo, sql.toString());
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idAzienda);
			rs = ps.executeQuery();
			while(rs.next()){
				listaNominativi.add(rs.getString("ragioneSociale"));
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT tbl_cliente for idAzienda:"+idAzienda, e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return listaNominativi;
	}

	/**
	 * con questo metodo effettuo l'eliminazione logica del Cliente che l'Utente ha deciso di eliminare.
	 * @param codiceCliente
	 * @return
	 */
	public String abilitaCliente(String codiceCliente){
		final String metodo="abilitaCliente";
		log.start(metodo);
		String sql = "UPDATE tbl_clienti SET attivo=1 WHERE id_cliente=?";
		log.debug(metodo, sql.toString());
		int esitoAbilitazioneCliente = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, codiceCliente);
			esitoAbilitazioneCliente = ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "UPDATE tbl_clienti for id_cliente:"+codiceCliente, e);
			return "Siamo spiacenti ma l'abilitazione del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
			log.end(metodo);
		}
		return (esitoAbilitazioneCliente != 0)?
			"ok":
			"Siamo spiacenti ma l'abilitazione del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
	}
	
	public String caricamentoNominativo(String id_cliente){
		final String metodo="abilitaCliente";
		log.start(metodo);
		String sql = "Select ragione_sociale from tbl_clienti WHERE id_cliente=?";
		log.debug(metodo, sql.toString());
		
		String nominativo = null;
		
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, id_cliente);
			rs = ps.executeQuery();
			while(rs.next()){
				nominativo = rs.getString(1);
			}
		} catch (SQLException e) {
			log.error(metodo, "UPDATE tbl_clienti for id_cliente:"+id_cliente, e);
		}finally{
			close(ps);
			log.end(metodo);
		}
		
		return nominativo;
	}
}