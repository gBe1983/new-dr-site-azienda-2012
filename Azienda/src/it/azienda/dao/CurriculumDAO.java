package it.azienda.dao;

import it.azienda.dto.Dettaglio_Cv_DTO;
import it.azienda.dto.EsperienzeDTO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CurriculumDAO {
	
	PreparedStatement ps = null;
	
	
	//tramite questo metodo effettuiamo l'abilitazione del flag curriculum 
	public void creazioneFlagCreazioneCurriculum(int idRisorsa, Connection conn){
		
		String sql = "update tbl_risorse set flag_creazione_cv = ? where id_risorsa = ?";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setBoolean(1, true);
			ps.setInt(2, idRisorsa);
			ps.executeUpdate();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	//effettuiamo l'inserimento delle esperienza nella tabella "Tbl_Esperienze_Professionali_Cv"
	public void inserimentoEsperienze(EsperienzeDTO esperienze, Connection conn){
		
		String sql = "insert into tbl_esperienze_professionali_cv(periodo,azienda,luogo,descrizione,id_risorsa) values (?,?,?,?,?)";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, esperienze.getPeriodo());
			ps.setString(2, esperienze.getAzienda());
			ps.setString(3, esperienze.getLuogo());
			ps.setString(4, esperienze.getDescrizione());
			ps.setInt(5, esperienze.getId_risorsa());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void inserimentoDettaglio(Dettaglio_Cv_DTO dettaglio, Connection conn){
		
		String sql = "insert into tbl_dettaglio_cv(capacita_professionali,competenze_tecniche,lingue_straniere,istruzione,formazione,interessi,id_risorsa) values (?,?,?,?,?,?,?)";
		
		int esitoInserimentoDettaglio = 0; 
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, dettaglio.getCapacita_professionali());
			ps.setString(2, dettaglio.getCompetenze_tecniche());
			ps.setString(3, dettaglio.getLingue_Straniere());
			ps.setString(4, dettaglio.getIstruzione());
			ps.setString(5, dettaglio.getFormazione());
			ps.setString(6, dettaglio.getInteressi());
			ps.setInt(7, dettaglio.getId_risorsa());
			esitoInserimentoDettaglio = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	
	//caricamento dei dati del curriculum
	public ArrayList caricamentoCurriculum(int idRisorsa,Connection conn) throws IOException{
		
		ArrayList curriculum = new ArrayList();
		RisorsaDAO uDAO = new RisorsaDAO();
		
		curriculum.add(uDAO.caricamentoProfiloRisorsa(idRisorsa, conn));
		curriculum = caricamentoEsperienze(idRisorsa, curriculum, conn);
		curriculum = caricamentoDettaglio(idRisorsa, curriculum, conn);
		
		return curriculum;
	}
	
	public ArrayList caricamentoEsperienze(int idRisorsa, ArrayList curriculum, Connection conn) throws IOException{
		
		String sql = "select * from tbl_esperienze_professionali_cv where id_risorsa = ? and visibile = true";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				EsperienzeDTO esperienze = new EsperienzeDTO();
				esperienze.setIdEsperienze(rs.getInt(1));
				esperienze.setPeriodo(rs.getString(2));
				esperienze.setAzienda(rs.getString(3));
				esperienze.setLuogo(rs.getString(4));
				esperienze.setDescrizione(rs.getString(5));
				esperienze.setId_risorsa(Integer.parseInt(rs.getString(6)));
				esperienze.setVisibile(rs.getBoolean(7));
				
				curriculum.add(esperienze);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return curriculum;
	}
	
	public ArrayList caricamentoDettaglio(int idRisorsa, ArrayList curriculum, Connection conn) throws IOException{
		
		String sql = "select * from tbl_dettaglio_cv where id_risorsa = ? and visible = true";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Dettaglio_Cv_DTO dettaglio = new Dettaglio_Cv_DTO();
				dettaglio.setId_dettaglio(rs.getInt(1));
				dettaglio.setCapacita_professionali(rs.getString(2));
				dettaglio.setCompetenze_tecniche(rs.getString(3));
				dettaglio.setLingue_Straniere(rs.getString(4));
				dettaglio.setIstruzione(rs.getString(5));
				dettaglio.setFormazione(rs.getString(6));
				dettaglio.setInteressi(rs.getString(7));
				dettaglio.setId_risorsa(rs.getInt(8));
				
				curriculum.add(dettaglio);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return curriculum;
	}
	
	//tramite questo messaggio effettuo l'aggiornamento della singola Esperinza
	public String aggiornamentoEsperienza(EsperienzeDTO esperienza, Connection conn) throws IOException{
			
			String sql = "update tbl_esperienze_professionali_cv set periodo = ?, azienda = ?, luogo = ?, descrizione = ?, id_risorsa = ? where id_esperienza_professionale = ?";
			int esitoModificaEsperienza = 0;	
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, esperienza.getPeriodo());
				ps.setString(2, esperienza.getAzienda());
				ps.setString(3, esperienza.getLuogo());
				ps.setString(4, esperienza.getDescrizione());
				ps.setInt(5, esperienza.getId_risorsa());
				ps.setInt(6, esperienza.getIdEsperienze());
				esitoModificaEsperienza = ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Siamo spiacenti la modifica della singola esperienza non è avvenuta con successo. Contattare l'amministrazione.";
			}
			
			if(esitoModificaEsperienza == 1){
				return "ok";
			}else{
				return "Siamo spiacenti la modifica della singola esperienza non è avvenuta con successo. Contattare l'amministrazione.";
			}
			
	}
	
	//tramite questo messaggio effettuo l'aggiornamento della singola Esperinza
	public String aggiornamentoDettaglio(Dettaglio_Cv_DTO dettaglio, Connection conn) throws IOException{
			
			String sql = "update tbl_dettaglio_cv set capacita_professionali = ?, competenze_tecniche = ?, lingue_straniere = ?, istruzione = ?, formazione = ?, interessi = ? where id_risorsa = ? and id_dettaglio = ?";
			int esitoModificaDettaglio = 0;	
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, dettaglio.getCapacita_professionali());
				ps.setString(2, dettaglio.getCompetenze_tecniche());
				ps.setString(3, dettaglio.getLingue_Straniere());
				ps.setString(4, dettaglio.getIstruzione());
				ps.setString(5, dettaglio.getFormazione());
				ps.setString(6, dettaglio.getInteressi());
				ps.setInt(7, dettaglio.getId_risorsa());
				ps.setInt(8, dettaglio.getId_dettaglio());
				esitoModificaDettaglio = ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Siamo spiacenti la modifica del dettaglio curriculum non è avvenuta con successo. Contattare l'amministrazione.";
			}
			
			if(esitoModificaDettaglio == 1){
				return "ok";
			}else{
				return "Siamo spiacenti la modifica del dettaglio curriculum non è avvenuta con successo. Contattare l'amministrazione.";
			}
			
	}
	
	//tramite questo messaggio effettuo l'eliminazione della singola Esperienza
	public String eliminazioneEsperienza(int idEsperienza, Connection conn) throws IOException{
		
		String sql = "update tbl_esperienze_professionali_cv set visibile = ? where id_esperienza_professionale = ?";
		int esitoEliminazioneEsperienza = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, idEsperienza);
			esitoEliminazioneEsperienza = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti l'eliminazione della singola esperienza non è avvenuta con successo. Contattare l'amministrazione.";
		}
		
		if(esitoEliminazioneEsperienza == 1){
			return "ok";
		}else{
			return "Siamo spiacenti l'eliminazione della singola esperienza non è avvenuta con successo. Contattare l'amministrazione.";
		}
		
	}
}
