package it.azienda.dao;

import it.azienda.dto.RisorsaDTO;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RisorseDAO extends BaseDao{
	private MyLogger log;

	public RisorseDAO(Connection connessione) {
		super(connessione);
		log=new MyLogger(this.getClass());
	}

	public List<RisorsaDTO> getRisorse(){
		final String metodo="getRisorse";
		log.start(metodo);
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder sql = new StringBuilder("SELECT ");
		sql	.append("id_risorsa,cognome,nome ")
				.append("FROM tbl_risorse ")
				.append("ORDER BY cognome");
		log.debug(metodo,"sql:"+sql.toString());
		List<RisorsaDTO>ris=new ArrayList<RisorsaDTO>();
		try {
			ps = connessione.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()){
				ris.add(new RisorsaDTO(rs.getInt("id_risorsa"),rs.getString("cognome"),rs.getString("nome")));
			}
		} catch (SQLException e) {
			log.error(metodo, "select tbl_planning,tbl_associaz_risor_comm,tbl_commesse for risorsa:", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return ris;
	}

	public RisorsaDTO getRisorsa(int idRisorsa){
		final String metodo="getRisorsa";
		log.start(metodo);
		PreparedStatement ps=null;
		ResultSet rs=null;
		RisorsaDTO risorsa = null;
		StringBuilder sql = new StringBuilder("SELECT ");
		sql	.append("id_risorsa,cognome,nome,data_nascita,luogo_nascita,sesso,cod_fiscale,mail,telefono,")
				.append("cellulare,fax,indirizzo,citta,provincia,cap,nazione,servizio_militare,patente,costo,occupato,")
				.append("tipo_contratto,figura_professionale,seniority,visible,flag_creazione_cv,cv_visibile ")
				.append("FROM tbl_risorse WHERE id_risorsa=?");
		log.debug(metodo,"sql:"+sql.toString());
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			if(rs.next()){
				risorsa = new RisorsaDTO(	rs.getInt("id_risorsa"),
														rs.getString("cognome"),
														rs.getString("nome"),
														rs.getString("data_nascita"),
														rs.getString("luogo_nascita"),
														rs.getString("sesso"),
														rs.getString("cod_fiscale"),
														rs.getString("mail"),
														rs.getString("telefono"),
														rs.getString("cellulare"),
														rs.getString("fax"),
														rs.getString("indirizzo"),
														rs.getString("citta"),
														rs.getString("provincia"),
														rs.getString("cap"),
														rs.getString("nazione"),
														rs.getString("servizio_militare"),
														rs.getString("patente"),
														rs.getString("costo"),
														rs.getBoolean("occupato"),
														rs.getString("tipo_contratto"),
														rs.getString("figura_professionale"),
														rs.getString("seniority"),
														rs.getBoolean("visible"),
														rs.getBoolean("flag_creazione_cv"),
														rs.getBoolean("cv_visibile"));
			}
		} catch (SQLException e) {
			log.error(metodo, "select tbl_risorse for risorsa:"+idRisorsa, e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return risorsa;
	}
}