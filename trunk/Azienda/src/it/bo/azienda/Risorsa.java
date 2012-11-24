package it.bo.azienda;

import it.azienda.dto.PlanningDTO;
import it.azienda.dto.RisorsaDTO;
import it.util.log.MyLogger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Risorsa implements Serializable{
	private MyLogger log;
	private RisorsaDTO risorsaDTO;

	private HashMap<String,List<PlanningDTO>>commesse;

	public Risorsa(RisorsaDTO risorsaDTO){
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		this.risorsaDTO=risorsaDTO;
		commesse=new HashMap<String,List<PlanningDTO>>();
		log.end(metodo);
	}

	/**
	 * @return the risorsaDTO
	 */
	public RisorsaDTO getRisorsaDTO() {
		return risorsaDTO;
	}

	/**
	 * @return the commesse
	 */
	public HashMap<String, List<PlanningDTO>> getCommesse() {
		return commesse;
	}

	/**
	 * 1.se la settimana non contiene la commessa passata, viene creata la lista associata.
	 * 2.aggiungo la commessa nella lista associata
	 * @param p
	 */
	public void addPlanningDTO(PlanningDTO p){
		final String metodo="addPlanningDTO";
		log.start(metodo);
		if(!commesse.containsKey(p.getDescrizione_commessa())){
			commesse.put(p.getDescrizione_commessa(),new ArrayList<PlanningDTO>());
		}
		commesse.get(p.getDescrizione_commessa()).add(p);
		log.end(metodo);
	}
}