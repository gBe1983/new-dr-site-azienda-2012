package it.azienda.dao;

import it.azienda.dto.RisorsaDTO;
import it.azienda.dto.UtenteDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RisorsaDAO {

	PreparedStatement ps = null;
	
	/*
	 * con questo metodo effettuo l'inserimento della risorsa 
	 * nella tabella tbl_risorse
	 */
	
	public String inserimentoRisorsa(RisorsaDTO risorsa, Connection conn){
		
		String messaggio = "";
		
		int esitoInserimentoRisorsa = 0;
		
		String sql = "insert into tbl_risorse (cognome,nome,data_nascita,luogo_nascita,sesso,cod_fiscale,mail,telefono,cellulare,fax,indirizzo,citta,provincia,cap,nazione,servizio_militare,patente,costo,occupato,tipo_contratto,figura_professionale,seniority) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			ps = conn.prepareStatement(sql);
			
			//dati anagrafici
			ps.setString(1, risorsa.getCognome());
			ps.setString(2, risorsa.getNome());
			ps.setString(3, risorsa.getDataNascita());
			ps.setString(4, risorsa.getLuogoNascita());
			ps.setString(5, risorsa.getSesso());
			ps.setString(6, risorsa.getCodiceFiscale());
			ps.setString(7, risorsa.getEmail());
			ps.setString(8, risorsa.getTelefono());
			ps.setString(9, risorsa.getCellulare());
			ps.setString(10, risorsa.getFax());
			
			//residenza
			ps.setString(11, risorsa.getIndirizzo());
			ps.setString(12, risorsa.getCitta());
			ps.setString(13, risorsa.getProvincia());
			ps.setString(14, risorsa.getCap());
			ps.setString(15, risorsa.getNazione());
			ps.setString(16, risorsa.getServizioMilitare());
			
			//altri dati
			ps.setString(17, risorsa.getPatente());
			ps.setString(18, risorsa.getCosto());
			ps.setBoolean(19, risorsa.isOccupato());
			ps.setString(20, risorsa.getTipoContratto());
			ps.setString(21, risorsa.getFiguraProfessionale());
			ps.setString(22, risorsa.getSeniority());
			
			esitoInserimentoRisorsa = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti l'inserimento della risorsa non è avvenuta con successo. Contattare l'amministrazione";
		}
			
		if(esitoInserimentoRisorsa == 1){
			messaggio = "ok";
		}else{
			messaggio = "Siamo spiacenti l'inserimento della risorsa non è avvenuta con successo. Contattare l'amministrazione";
		}
		
		return messaggio;
	}

	/*
	 * con questo metodo effettuo la ricerca a seconda dei paramatri
	 * inseriti dall'utente
	 */
	public ArrayList ricercaRisorse(RisorsaDTO risorsa,Connection conn){
		
		ArrayList listaRisorse = new ArrayList();
		
		boolean cognome = false;
		boolean nome = false;
		boolean costo = false;
		boolean figuraProfessionale = false;
		boolean seniority = false;
		int contatori = 1;
		
		String sql = "select * from tbl_risorse ";
		
		if(risorsa.getCognome() != null && !risorsa.getCognome().equals("")){
			sql += "where cognome like ?";
			cognome = true;
			contatori++;
		}
		
		if(risorsa.getNome() != null && !risorsa.getNome().equals("")){
			if(!cognome){
				sql += "where nome like ?";
				nome = true;
				contatori++;
			}else{
				sql += " and nome like ?";
				nome = true;
				contatori++;
			}
		}
		
		if(risorsa.getCosto() != null && !risorsa.getCosto().equals("")){
			if(!cognome && !nome){
				sql += "where costo = ?";
				costo = true;
				contatori++;
			}else{
				sql += " and costo = ?";
				costo = true;
				contatori++;
			}
		}
		
		if(risorsa.getFiguraProfessionale() != null && !risorsa.getFiguraProfessionale().equals("")){
			if(!cognome && !nome && !costo){
				sql += "where figura_professionale like ?";
				figuraProfessionale = true;
				contatori++;
			}else{
				sql += " and figura_professionale like ?";
				figuraProfessionale = true;
				contatori++;
			}
		}
		
		if(risorsa.getSeniority() != null && !risorsa.getSeniority().equals("")){
			if(!cognome && !nome && !costo && !figuraProfessionale){
				sql += "where seniority = ?";
				seniority = true;
				contatori++;
			}else{
				sql += " and seniority = ?";
				seniority = true;
				contatori++;
			}
		}
		
		if(contatori == 1){
			sql += " where visible = true order by cognome ASC";
		}else{
			sql += " and visible = true order by cognome ASC";
		}
		System.out.println(sql);
		
		try {
			ps = conn.prepareStatement(sql);
			if(cognome){
				ps.setString(1, "%"+risorsa.getCognome().trim()+"%");
			}
			
			if(cognome && nome){
				ps.setString(2,	"%"+risorsa.getNome().trim()+"%");
			}else if(!cognome && nome){
				ps.setString(1, "%"+risorsa.getNome().trim()+"%");
			}
			
			if(cognome && nome && costo){
				ps.setString(3, risorsa.getCosto().trim());
			}else if(!cognome && nome && costo || cognome && !nome && costo){
				ps.setString(2, risorsa.getCosto().trim());
			}else if(!cognome && !nome && costo){
				ps.setString(1, risorsa.getCosto().trim());
			}
			
			if(cognome && nome && costo && figuraProfessionale){
				ps.setString(4, "%"+risorsa.getFiguraProfessionale().trim()+"%");
			}else if(!cognome && nome && costo && figuraProfessionale 
					|| cognome && !nome && costo && figuraProfessionale 
					|| cognome && nome && !costo && figuraProfessionale
			){
				ps.setString(3, "%"+risorsa.getFiguraProfessionale().trim()+"%");
			}else if(!cognome && !nome && costo && figuraProfessionale 
					|| !cognome && nome && !costo && figuraProfessionale 
					|| cognome && !nome && !costo && figuraProfessionale){
				ps.setString(2, "%"+risorsa.getFiguraProfessionale().trim()+"%");
			}else if(!cognome && !nome && !costo && figuraProfessionale){
				ps.setString(1, "%"+risorsa.getFiguraProfessionale().trim()+"%");
			}
			
			if(cognome && nome && costo && figuraProfessionale && seniority){
				ps.setString(5, risorsa.getSeniority());
			}else if(!cognome && nome && costo && figuraProfessionale && seniority
					|| cognome && !nome && costo && figuraProfessionale && seniority
					|| cognome && nome && !costo && figuraProfessionale && seniority
					|| cognome && nome && costo && !figuraProfessionale && seniority
			){
				ps.setString(4, risorsa.getSeniority());
			}else if(!cognome && !nome && costo && figuraProfessionale && seniority
					|| !cognome && 	nome && !costo && figuraProfessionale && seniority
					|| !cognome &&  nome &&  costo && !figuraProfessionale && seniority
					|| 	cognome && !nome && !costo && figuraProfessionale && seniority
					|| 	cognome && !nome &&  costo && !figuraProfessionale && seniority
					|| 	cognome &&  nome && !costo && !figuraProfessionale && seniority){
				ps.setString(3, risorsa.getSeniority());
			}else if(cognome && !nome && !costo && !figuraProfessionale && seniority
					|| !cognome &&   nome && !costo && !figuraProfessionale && seniority
					|| !cognome && !nome &&   costo && !figuraProfessionale && seniority
					|| !cognome &&  !nome && !costo && figuraProfessionale && seniority
			){
				ps.setString(2, risorsa.getSeniority());
			}else if(!cognome && !nome && !costo && !figuraProfessionale && seniority){
				ps.setString(1, risorsa.getSeniority());
			}
			
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				RisorsaDTO risorseTrovate = new RisorsaDTO();
				risorseTrovate.setIdRisorsa(rs.getInt(1));
				risorseTrovate.setCognome(rs.getString(2));
				risorseTrovate.setNome(rs.getString(3));
				risorseTrovate.setDataNascita(rs.getString(4));
				risorseTrovate.setLuogoNascita(rs.getString(5));
				risorseTrovate.setSesso(rs.getString(6));
				risorseTrovate.setCodiceFiscale(rs.getString(7));
				risorseTrovate.setEmail(rs.getString(8));
				risorseTrovate.setTelefono(rs.getString(9));
				risorseTrovate.setCellulare(rs.getString(10));
				risorseTrovate.setFax(rs.getString(11));
				risorseTrovate.setIndirizzo(rs.getString(12));
				risorseTrovate.setCitta(rs.getString(13));
				risorseTrovate.setProvincia(rs.getString(14));
				risorseTrovate.setCap(rs.getString(15));
				risorseTrovate.setNazione(rs.getString(16));
				risorseTrovate.setServizioMilitare(rs.getString(17));
				risorseTrovate.setPatente(rs.getString(18));
				risorseTrovate.setCosto(rs.getString(19));
				risorseTrovate.setOccupato(rs.getBoolean(20));
				risorseTrovate.setTipoContratto(rs.getString(21));
				risorseTrovate.setFiguraProfessionale(rs.getString(22));
				risorseTrovate.setSeniority(rs.getString(23));
				risorseTrovate.setVisible(rs.getBoolean(24));
				risorseTrovate.setFlaCreazioneCurriculum(rs.getBoolean(25));
				
				listaRisorse.add(risorseTrovate);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaRisorse;
		
	}
	
	/*
	 * questo metodo serve per il profilo della risorsa
	 */
	
	public RisorsaDTO caricamentoProfiloRisorsa(int idRisorsa, Connection conn){
		
		RisorsaDTO risorsa = null;
		
		String sql = "select * from tbl_risorse where id_risorsa = ?";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				risorsa = new RisorsaDTO();
				risorsa.setIdRisorsa(rs.getInt(1));
				risorsa.setCognome(rs.getString(2));
				risorsa.setNome(rs.getString(3));
				risorsa.setDataNascita(rs.getString(4));
				risorsa.setLuogoNascita(rs.getString(5));
				risorsa.setSesso(rs.getString(6));
				risorsa.setCodiceFiscale(rs.getString(7));
				risorsa.setEmail(rs.getString(8));
				risorsa.setTelefono(rs.getString(9));
				risorsa.setCellulare(rs.getString(10));
				risorsa.setFax(rs.getString(11));
				risorsa.setIndirizzo(rs.getString(12));
				risorsa.setCitta(rs.getString(13));
				risorsa.setProvincia(rs.getString(14));
				risorsa.setCap(rs.getString(15));
				risorsa.setNazione(rs.getString(16));
				risorsa.setServizioMilitare(rs.getString(17));
				risorsa.setPatente(rs.getString(18));
				risorsa.setCosto(rs.getString(19));
				risorsa.setOccupato(rs.getBoolean(20));
				risorsa.setTipoContratto(rs.getString(21));
				risorsa.setFiguraProfessionale(rs.getString(22));
				risorsa.setSeniority(rs.getString(23));
				risorsa.setVisible(rs.getBoolean(24));
				risorsa.setFlaCreazioneCurriculum(rs.getBoolean(25));
				risorsa.setCv_visibile(rs.getBoolean(26));
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return risorsa;
	}
	
	public String modificaRisorsa(RisorsaDTO risorsa, Connection conn){
		
		String messaggio = "";
		
		int esitoModificaRisorsa = 0;
		
		String sql = "update tbl_risorse set cognome = ?, nome = ?, data_nascita = ?, luogo_nascita = ?, sesso = ?, cod_fiscale = ?, mail = ?, telefono = ?, cellulare = ?, fax = ?, indirizzo = ?, citta = ?, provincia = ?, cap = ?, nazione = ?, servizio_militare = ?, patente = ?, costo = ?, occupato = ?, tipo_contratto = ?, figura_professionale = ?, seniority = ? where id_risorsa = ?";
		
		try {
			ps = conn.prepareStatement(sql);
			
			//dati anagrafici
			ps.setString(1, risorsa.getCognome());
			ps.setString(2, risorsa.getNome());
			ps.setString(3, risorsa.getDataNascita());
			ps.setString(4, risorsa.getLuogoNascita());
			ps.setString(5, risorsa.getSesso());
			ps.setString(6, risorsa.getCodiceFiscale());
			ps.setString(7, risorsa.getEmail());
			ps.setString(8, risorsa.getTelefono());
			ps.setString(9, risorsa.getCellulare());
			ps.setString(10, risorsa.getFax());
			
			//residenza
			ps.setString(11, risorsa.getIndirizzo());
			ps.setString(12, risorsa.getCitta());
			ps.setString(13, risorsa.getProvincia());
			ps.setString(14, risorsa.getCap());
			ps.setString(15, risorsa.getNazione());
			ps.setString(16, risorsa.getServizioMilitare());
			
			//altri dati
			ps.setString(17, risorsa.getPatente());
			ps.setString(18, risorsa.getCosto());
			ps.setBoolean(19, risorsa.isOccupato());
			ps.setString(20, risorsa.getTipoContratto());
			ps.setString(21, risorsa.getFiguraProfessionale());
			ps.setString(22, risorsa.getSeniority());
			
			ps.setInt(23, risorsa.getIdRisorsa());
			
			esitoModificaRisorsa = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti la modifica della risorsa non è avvenuta con successo. Contattare l'amministrazione";
		}
			
		if(esitoModificaRisorsa == 1){
			messaggio = "Modifica Risorsa avvenuta con successo";
		}else{
			messaggio = "Siamo spiacenti la modifica della risorsa non è avvenuta con successo. Contattare l'amministrazione";
		}
		
		return messaggio;
	}
	
	public String eliminaRisorsa(int idRisorsa, Connection conn){
		
		String messaggio = "";
		
		int esitoEliminaRisorsa = 0;
		
		String sql = "update tbl_risorse set visible = ? where id_risorsa = ?";
		
		try {
			ps = conn.prepareStatement(sql);
			
			ps.setBoolean(1, false);
			ps.setInt(2, idRisorsa);
			
			esitoEliminaRisorsa = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti la risorsa non è stata disibilitata con successo. Contattare l'amministrazione";
		}
			
		if(esitoEliminaRisorsa == 1){
			messaggio = "La Risorsa è stata disabilitata con successo";
		}else{
			messaggio = "Siamo spiacenti l'eliminazione della risorsa non è avvenuta con successo. Contattare l'amministrazione";
		}
		
		return messaggio;
	}
	
	public ArrayList elencoRisorse(Connection conn){
		
		ArrayList listaRisorse = new ArrayList();
		
		String sql = "select id_risorsa, nome, cognome, costo, figura_professionale, seniority from tbl_risorse where visible = true order by cognome ASC";
		
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				RisorsaDTO risorsa = new RisorsaDTO();
				risorsa.setIdRisorsa(rs.getInt(1));
				risorsa.setNome(rs.getString(2));
				risorsa.setCognome(rs.getString(3));
				risorsa.setCosto(rs.getString(4));
				risorsa.setFiguraProfessionale(rs.getString(5));
				risorsa.setSeniority(rs.getString(6));
				listaRisorse.add(risorsa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
		return listaRisorse;
	}
	
	/*
	 * questo metodo viene richiamato, tramite funzione javascript, per valorizzare dinamicamente
	 * le risorse associate a un cliente al momento della ricerca delle trattattive.
	 */
	
	public String elencoTrattativeRisorse(String codiceCliente, Connection conn){
		
		String valori = "";
		
		String sql = "select risorse.id_risorsa, risorse.cognome, risorse.nome from tbl_risorse as risorse, tbl_trattative as trattative where trattative.id_risorsa = risorse.id_risorsa and trattative.id_cliente = ? and risorse.visible = true group by id_risorsa order by cognome ASC";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, codiceCliente);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				valori = valori + rs.getInt(1) + "," + rs.getString(2) + "," + rs.getString(3) + ";";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
		return valori;
	}
	
	
	public UtenteDTO caricamentoCredenziali(int idRisorsa, Connection conn){
		
		String sql = "select risorsa.cognome, risorsa.nome, risorsa.mail, utenti.id_utente, utenti.username, utenti.password from tbl_utenti as utenti, tbl_risorse as risorsa where utenti.id_risorsa = risorsa.id_risorsa and utenti.id_risorsa = ?";
		
		UtenteDTO utente = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				utente = new UtenteDTO();
				utente.setDescrizioneRisorsa(rs.getString(1) + " " + rs.getString(2));
				utente.setEmail(rs.getString(3));
				utente.setId_utente(rs.getInt(4));
				utente.setUsername(rs.getString(5));
				utente.setPassword(rs.getString(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return utente;
	}
	
	public String modificaCredenziali(String username,String password,int idUtente, Connection conn){
		
		String sql = "update tbl_utenti set username = ?, password = ? where id_utente = ?";
		
		int esitoModifica = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, MD5(password));
			ps.setInt(3, idUtente);
			esitoModifica = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti la modifica delle credenziali della risorsa non sono avvenute con successo. Contattare l'amministrazione.";
		}
		
		if(esitoModifica == 1){
			return "ok";
		}else{
			return "Siamo spiacenti la modifica delle credenziali della risorsa non sono avvenute con successo. Contattare l'amministrazione.";
		}
		
	}
	
	/*
	 * questo metodo serve per criptare le password
	 */
	
	public String MD5(String md5) {
		
		try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(md5.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	        }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    	
	    }
	    return null;
	}
	
	public ArrayList invioEmail(Connection conn){
		
		String sql = "select * from utente";
		
		ArrayList listaUtenti = new ArrayList();
		
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				RisorsaDTO utente = new RisorsaDTO();
				utente.setIdRisorsa(rs.getInt(1));
				utente.setNome(rs.getString(2));
				utente.setCognome(rs.getString(3));
				utente.setEmail(rs.getString(4));
				listaUtenti.add(utente);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaUtenti;
	}
}
