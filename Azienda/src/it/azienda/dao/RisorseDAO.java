package it.azienda.dao;

import it.azienda.dto.RisorsaDTO;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class RisorseDAO extends BaseDao{
	private Logger log;

	public RisorseDAO(Connection connessione) {
		super(connessione);
		log= Logger.getLogger(RisorseDAO.class);
	}

	public List<RisorsaDTO> getRisorse(){
		
		log.info("metodo: getRisorse");
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder sql = new StringBuilder("SELECT ");
		sql	.append("id_risorsa,cognome,nome ")
				.append("FROM tbl_risorse ")
				.append("where visible = true ")
				.append("ORDER BY cognome");
		
		log.info("sql:"+sql.toString());
		
		List<RisorsaDTO>ris=new ArrayList<RisorsaDTO>();
		try {
			ps = connessione.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()){
				ris.add(new RisorsaDTO(rs.getInt("id_risorsa"),rs.getString("cognome"),rs.getString("nome")));
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return ris;
	}

	public RisorsaDTO getRisorsa(int idRisorsa){
		
		log.info("metodo: getRisorsa");
		PreparedStatement ps=null;
		ResultSet rs=null;
		RisorsaDTO risorsa = null;
		StringBuilder sql = new StringBuilder("SELECT ");
		sql	.append("id_risorsa,cognome,nome,data_nascita,luogo_nascita,sesso,cod_fiscale,mail,telefono,")
				.append("cellulare,fax,indirizzo,citta,provincia,cap,nazione,servizio_militare,patente,costo,occupato,")
				.append("tipo_contratto,figura_professionale,seniority,visible,flag_creazione_cv,cv_visibile ")
				.append("FROM tbl_risorse WHERE id_risorsa=?");
		log.info("sql: SELECT id_risorsa,cognome,nome,data_nascita,luogo_nascita,sesso,cod_fiscale,mail,telefono," +
				"cellulare,fax,indirizzo,citta,provincia,cap,nazione,servizio_militare,patente,costo,occupato," +
				"tipo_contratto,figura_professionale,seniority,visible,flag_creazione_cv,cv_visibile FROM tbl_risorse WHERE id_risorsa="+idRisorsa);
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
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return risorsa;
	}
}