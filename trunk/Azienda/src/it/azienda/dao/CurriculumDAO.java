package it.azienda.dao;

import it.azienda.dto.Dettaglio_Cv_DTO;
import it.azienda.dto.EsperienzeDTO;
import it.util.log.MyLogger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CurriculumDAO extends BaseDao {
	private MyLogger log;

	public CurriculumDAO(Connection connessione) {
		super(connessione);
		log = new MyLogger(this.getClass());
	}

	/**
	 * tramite questo metodo effettuiamo l'abilitazione del flag curriculum
	 * @param idRisorsa
	 */
	public void creazioneFlagCreazioneCurriculum(int idRisorsa) {
		final String metodo = "creazioneFlagCreazioneCurriculum";
		log.start(metodo);
		String sql = "update tbl_risorse set flag_creazione_cv = ? where id_risorsa = ?";
		log.debug(metodo, sql);
		PreparedStatement ps = null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, true);
			ps.setInt(2, idRisorsa);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "update tbl_risorse", e);
		} finally {
			close(ps);
			log.end(metodo);
		}

	}

	/**
	 * effettuiamo l'inserimento delle esperienza nella tabella "Tbl_Esperienze_Professionali_Cv"
	 * @param esperienze
	 */
	public void inserimentoEsperienze(EsperienzeDTO esperienze) {
		final String metodo = "inserimentoEsperienze";
		log.start(metodo);
		String sql = "insert into tbl_esperienze_professionali_cv(periodo,azienda,luogo,descrizione,id_risorsa) values (?,?,?,?,?)";
		log.debug(metodo, sql);
		PreparedStatement ps = null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, esperienze.getPeriodo());
			ps.setString(2, esperienze.getAzienda());
			ps.setString(3, esperienze.getLuogo());
			ps.setString(4, esperienze.getDescrizione());
			ps.setInt(5, esperienze.getId_risorsa());
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "insert tbl_esperienze_professionali_cv", e);
		} finally {
			close(ps);
			log.end(metodo);
		}
	}

	public void inserimentoDettaglio(Dettaglio_Cv_DTO dettaglio) {
		final String metodo = "inserimentoDettaglio";
		log.start(metodo);
		String sql = "insert into tbl_dettaglio_cv(capacita_professionali,competenze_tecniche,lingue_straniere,istruzione,formazione,interessi,id_risorsa) values (?,?,?,?,?,?,?)";
		log.debug(metodo, sql);
		int esitoInserimentoDettaglio = 0;
		PreparedStatement ps = null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, dettaglio.getCapacita_professionali());
			ps.setString(2, dettaglio.getCompetenze_tecniche());
			ps.setString(3, dettaglio.getLingue_Straniere());
			ps.setString(4, dettaglio.getIstruzione());
			ps.setString(5, dettaglio.getFormazione());
			ps.setString(6, dettaglio.getInteressi());
			ps.setInt(7, dettaglio.getId_risorsa());
			esitoInserimentoDettaglio = ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "insert tbl_dettaglio_cv", e);
		} finally {
			close(ps);
			log.debug(metodo, "esitoInserimentoDettaglio:"+esitoInserimentoDettaglio);
			log.end(metodo);
		}
	}

	/**
	 * caricamento dei dati del curriculum
	 * @param idRisorsa
	 * @return
	 * @throws IOException
	 */
	public ArrayList caricamentoCurriculum(int idRisorsa) throws IOException {
		ArrayList curriculum = new ArrayList();
		RisorsaDAO uDAO = new RisorsaDAO(connessione);// TODO QUI' C'E' DA FARE ORDINE!
		curriculum.add(uDAO.caricamentoProfiloRisorsa(idRisorsa));
		curriculum = caricamentoEsperienze(idRisorsa, curriculum);
		curriculum = caricamentoDettaglio(idRisorsa, curriculum);
		return curriculum;
	}

	public ArrayList caricamentoEsperienze(int idRisorsa, ArrayList curriculum)
			throws IOException {
		final String metodo = "caricamentoEsperienze";
		log.start(metodo);
		String sql = "select * from tbl_esperienze_professionali_cv where id_risorsa = ? and visibile = true";
		log.debug(metodo, sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while (rs.next()) {
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
			log.error(metodo, "select tbl_esperienze_professionali_cv", e);
		} finally {
			close(ps, rs);
			log.end(metodo);
		}
		return curriculum;
	}

	public ArrayList caricamentoDettaglio(int idRisorsa, ArrayList curriculum)
			throws IOException {
		final String metodo = "caricamentoDettaglio";
		log.start(metodo);
		String sql = "select * from tbl_dettaglio_cv where id_risorsa = ? and visible = true";
		log.debug(metodo, sql);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while (rs.next()) {
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
			log.error(metodo, "select tbl_dettaglio_cv", e);
		} finally {
			close(ps, rs);
			log.end(metodo);
		}
		return curriculum;
	}

	/**
	 * tramite questo messaggio effettuo l'aggiornamento della singola Esperinza
	 * @param esperienza
	 * @return
	 * @throws IOException
	 */
	public String aggiornamentoEsperienza(EsperienzeDTO esperienza)
			throws IOException {
		final String metodo = "aggiornamentoEsperienza";
		log.start(metodo);
		String sql = "update tbl_esperienze_professionali_cv set periodo = ?, azienda = ?, luogo = ?, descrizione = ?, id_risorsa = ? where id_esperienza_professionale = ?";
		log.debug(metodo, sql);
		int esitoModificaEsperienza = 0;
		PreparedStatement ps = null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, esperienza.getPeriodo());
			ps.setString(2, esperienza.getAzienda());
			ps.setString(3, esperienza.getLuogo());
			ps.setString(4, esperienza.getDescrizione());
			ps.setInt(5, esperienza.getId_risorsa());
			ps.setInt(6, esperienza.getIdEsperienze());
			esitoModificaEsperienza = ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "update tbl_esperienze_professionali_cv", e);
			return "Siamo spiacenti la modifica della singola esperienza non è avvenuta con successo. Contattare l'amministrazione.";
		} finally {
			close(ps);
			log.end(metodo);
		}
		return (esitoModificaEsperienza == 1)?
				"ok":
				"Siamo spiacenti la modifica della singola esperienza non è avvenuta con successo. Contattare l'amministrazione.";
	}

	/**
	 * tramite questo messaggio effettuo l'aggiornamento della singola Esperinza
	 * @param dettaglio
	 * @return
	 * @throws IOException
	 */
	public String aggiornamentoDettaglio(Dettaglio_Cv_DTO dettaglio)
			throws IOException {
		final String metodo = "aggiornamentoDettaglio";
		log.start(metodo);
		String sql = "update tbl_dettaglio_cv set capacita_professionali = ?, competenze_tecniche = ?, lingue_straniere = ?, istruzione = ?, formazione = ?, interessi = ? where id_risorsa = ? and id_dettaglio = ?";
		log.debug(metodo, sql);
		int esitoModificaDettaglio = 0;
		PreparedStatement ps = null;
		try {
			ps = connessione.prepareStatement(sql);
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
			log.error(metodo, "update tbl_dettaglio_cv", e);
			return "Siamo spiacenti la modifica del dettaglio curriculum non è avvenuta con successo. Contattare l'amministrazione.";
		} finally {
			close(ps);
			log.end(metodo);
		}
		return (esitoModificaDettaglio == 1)?
				"ok":
				"Siamo spiacenti la modifica del dettaglio curriculum non è avvenuta con successo. Contattare l'amministrazione.";
	}

	/**
	 * tramite questo messaggio effettuo l'eliminazione della singola Esperienza
	 * @param idEsperienza
	 * @return
	 * @throws IOException
	 */
	public String eliminazioneEsperienza(int idEsperienza) throws IOException {
		final String metodo = "eliminazioneEsperienza";
		log.start(metodo);
		String sql = "update tbl_esperienze_professionali_cv set visibile = ? where id_esperienza_professionale = ?";
		log.debug(metodo, sql);
		int esitoEliminazioneEsperienza = 0;
		PreparedStatement ps = null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, idEsperienza);
			esitoEliminazioneEsperienza = ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "update tbl_esperienze_professionali_cv", e);
			return "Siamo spiacenti l'eliminazione della singola esperienza non è avvenuta con successo. Contattare l'amministrazione.";
		} finally {
			close(ps);
			log.end(metodo);
		}
		return (esitoEliminazioneEsperienza == 1)?
				"ok":
				"Siamo spiacenti l'eliminazione della singola esperienza non è avvenuta con successo. Contattare l'amministrazione.";
	}
}