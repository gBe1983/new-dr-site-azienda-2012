package it.azienda.dao;

import it.azienda.dto.ClienteDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClienteDAO extends BaseDao {

	public ClienteDAO(Connection connessione) {
		super(connessione);
	}

	//tramite questo metodo effettuo l'inserimento del Cliente
	public String inserimentoCliente(ClienteDTO cliente){
		
		String sql = "insert into tbl_clienti (id_cliente,ragione_sociale,indirizzo,cap,citta,provincia,p_iva,referente,telefono,cellulare,fax,email,sito,cod_Fiscale) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		int esitoInserimentoCliente = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti ma l'inserimento del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		if(esitoInserimentoCliente == 1){
			return "ok";
		}else{
			return "Siamo spiacenti ma l'inserimento del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}
	}
	
	//tramite questo metodo effettuo la modifica del Cliente
	public String modificaCliente(ClienteDTO cliente){
		
		String sql = "update tbl_clienti set ragione_sociale = ?,indirizzo = ?,cap = ?, citta = ?, provincia = ?, p_iva = ?, referente = ?, telefono = ?, cellulare = ?, fax = ?, email = ?, sito = ?, cod_fiscale = ? where id_cliente = ?";
		
		
		int esitoInserimentoCliente = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
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
			esitoInserimentoCliente = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti ma la modifica del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoInserimentoCliente == 1){
			return "ok";
		}else{
			return "Siamo spiacenti ma la modifica del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}
	}
	
	//con questo metodo carico tutti i nomitivi di tutti i clienti
	public ArrayList caricamentoNominativiCliente(){
		
		ArrayList listaNominativi = new ArrayList();
		
		String sql = "select ragione_sociale from tbl_clienti where attivo = 1 order by ragione_sociale ASC";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				listaNominativi.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return listaNominativi;
	}
	
	//con questo metodo carico tutti i nomitivi di tutti i clienti
	public ArrayList caricamentoNominativiClienteDisabilitati(){
		
		ArrayList listaNominativi = new ArrayList();
		
		String sql = "select ragione_sociale from tbl_clienti where attivo = 0";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				listaNominativi.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return listaNominativi;
	}
	
	
	/*
	 * con questo metodo effettuo il caricamento del profilo del Cliente
	 * attraverso due tipologie che sono:
	 * - Nominativo: che avviene quando effettuo la "Ricerca Cliente" dove mi viene passato 
	 * 				 il nominativo del Cliente
	 * - CodiceCliente: a fronte della visualizzazione, quando effettuiamo "Modifica Cliente" 
	 * 					ricerco il cliente per il suo "CodiceCliente"
	 */
	public ClienteDTO caricamentoCliente(String codiceCliente, String nominativo){
		
		ClienteDTO cliente = null;
		String sql = "";
		PreparedStatement ps=null;
		ResultSet rs=null;
		if(codiceCliente == null){
			sql = "select * from tbl_clienti where ragione_sociale = ?";
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, nominativo);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			sql = "select * from tbl_clienti where id_cliente = ?";
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, codiceCliente);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			rs = ps.executeQuery();
			if(rs.next()){
				cliente = new ClienteDTO();
				cliente.setId_cliente(rs.getString(1));
				cliente.setRagioneSociale(rs.getString(2));
				cliente.setIndirizzo(rs.getString(3));
				cliente.setCap(rs.getString(4));
				cliente.setCitta(rs.getString(5));
				cliente.setProvincia(rs.getString(6));
				cliente.setPIva(rs.getString(7));
				cliente.setReferente(rs.getString(8));
				cliente.setTelefono(rs.getString(9));
				cliente.setCellulare(rs.getString(10));
				cliente.setFax(rs.getString(11));
				cliente.setEmail(rs.getString(12));
				cliente.setSito(rs.getString(13));
				cliente.setAttivo(rs.getBoolean(14));
				cliente.setCodFiscale(rs.getString(15));
				cliente.setId_banca(rs.getInt(16));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return cliente;
	}
	
	
	//con questo metodo estrapolo tutti i Clienti legati all'azienda
	public ArrayList caricamentoClienti(){
		
		ArrayList listaClienti = new ArrayList();
		String sql = "select id_cliente,ragione_sociale from tbl_clienti where attivo = true order by ragione_sociale ASC";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				ClienteDTO cliente = new ClienteDTO();
				cliente.setId_cliente(rs.getString(1));
				cliente.setRagioneSociale(rs.getString(2));
				listaClienti.add(cliente);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return listaClienti;
	}
	
	/*
	 * con questo metodo effettuo l'eliminazione logica
	 * del Cliente che l'Utente ha deciso di eliminare.
	 */
	
	public String disabilitaCliente(String codiceCliente){
		
		String sql = "update tbl_clienti set attivo = 0 where id_cliente = ?";
		
		int esitoEliminazioneCliente = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, codiceCliente);
			esitoEliminazioneCliente = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti ma l'eliminazione del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoEliminazioneCliente != 0){
			return "ok";
		}else{
			return "Siamo spiacenti ma l'eliminazione del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}
	}
	
	public boolean controlloCodiceCliente(String codiceCliente){
		
		String sql = "select * from tbl_cliente where codiceCliente = ?";
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return esitoControlloCodiceCliente;
	}
	
	/*
	 * tramite questo metodo effettuo il caricamento dinamico 
	 * del codice cliente al momento dell'inserimento del cliente
	 */
	public String creazioneCodiceCliente(){
		
		String sql = "select max(id_cliente) from tbl_clienti order by id_cliente";
		
		String codiceCliente = "CL";
		
		int codCliente = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getString(1) != null)
					codCliente = Integer.parseInt(rs.getString(1).substring(3, 5));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		codCliente++;
		if(codCliente < 10){
			codiceCliente = codiceCliente + "00" + String.valueOf(codCliente);
		}else if(codCliente >= 10 && codCliente < 100){
			codiceCliente = codiceCliente + "0" + String.valueOf(codCliente);
		}else if(codCliente >= 100){
			codiceCliente = codiceCliente + String.valueOf(codCliente);
		}
		
		
		return codiceCliente;
	}
	
	public ArrayList caricamentoNominativiClienteDisabilitato(int idAzienda){
		
		ArrayList listaNominativi = new ArrayList();
		
		String sql = "select ragioneSociale from tbl_cliente where id_azienda = ? and attivo = 0";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idAzienda);
			rs = ps.executeQuery();
			while(rs.next()){
				listaNominativi.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		
		return listaNominativi;
	}
	
	/*
	 * con questo metodo effettuo l'eliminazione logica
	 * del Cliente che l'Utente ha deciso di eliminare.
	 */
	
	public String abilitaCliente(String codiceCliente){
		
		String sql = "update tbl_clienti set attivo = 1 where id_cliente = ?";
		
		int esitoEliminazioneCliente = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, codiceCliente);
			esitoEliminazioneCliente = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti ma l'abilitazione del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoEliminazioneCliente != 0){
			return "ok";
		}else{
			return "Siamo spiacenti ma l'abilitazione del Cliente non è avvenuta con successo. Contattare l'amministrazione.";
		}
	}
	
}
