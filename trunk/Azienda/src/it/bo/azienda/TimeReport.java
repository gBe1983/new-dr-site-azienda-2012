package it.bo.azienda;


import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.PlanningDTO;
import it.azienda.dto.RisorsaDTO;
import it.bo.Day;
import it.util.log.MyLogger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TimeReport implements Serializable{
	private MyLogger log;
	private String idCliente;
	private String idRisorsa;
	private String idCommessa;
	private Calendar dtDa;
	private Calendar dtA;

	private HashMap<Integer,Risorsa>risorse;
	private List<Integer>risorseKey;
	private List<Day>days;
	
	public TimeReport() {
		// TODO Auto-generated constructor stub
	}
	
	
	public TimeReport(Calendar dtDa, Calendar dtA, String idCliente, String idRisorsa, String idCommessa){
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		Calendar dayFrom = (Calendar)dtDa.clone();
		this.dtDa= (Calendar)dtDa.clone();
		this.dtA = (Calendar)dtA.clone();
		this.idCliente=idCliente; 
		this.idRisorsa=idRisorsa; 
		this.idCommessa=idCommessa;
		days=new ArrayList<Day>();
		while(dayFrom.compareTo(dtA)<=0){
			days.add(new Day((Calendar)dayFrom.clone()));
			dayFrom.add(Calendar.DAY_OF_MONTH,1);
		}
		risorse = new HashMap<Integer,Risorsa>();
		risorseKey= new ArrayList<Integer>();
		log.end(metodo);
	}

	public void setDays(Calendar dtDa, Calendar dtA){
		days=new ArrayList<Day>();
		Calendar dayFrom = (Calendar)dtDa.clone();
		this.dtDa= (Calendar)dtDa.clone();
		this.dtA = (Calendar)dtA.clone();
		
		while(dayFrom.compareTo(dtA)<=0){
			days.add(new Day((Calendar)dayFrom.clone()));
			dayFrom.add(Calendar.DAY_OF_MONTH,1);
		}
		
	}
	
	
	/**
	 * cerco la settimana a cui aggiungere la commessa, una volta trovata la gli passo la commessa da aggiungere
	 * @param p
	 */
	public void addPlanningDTO(PlanningDTO p, RisorsaDTO risorsa){
		final String metodo="addPlanningDTO";
		log.start(metodo);
		if(!risorse.containsKey(risorsa.getIdRisorsa())){
			risorse.put(risorsa.getIdRisorsa(), new Risorsa(risorsa));
			risorseKey.add(risorsa.getIdRisorsa());
		}
		risorse.get(risorsa.getIdRisorsa()).addPlanningDTO(p);
		log.end(metodo);
	}

	/**
	 * @return the idCliente
	 */
	public String getIdCliente() {
		return idCliente;
	}

	/**
	 * @return the idRisorsa
	 */
	public String getIdRisorsa() {
		return idRisorsa;
	}

	/**
	 * @return the idCommessa
	 */
	public String getIdCommessa() {
		return idCommessa;
	}

	/**
	 * @return the risorse
	 */
	public HashMap<Integer, Risorsa> getRisorse() {
		return risorse;
	}

	/**
	 * @return the risorseKey
	 */
	public List<Integer> getRisorseKey() {
		return risorseKey;
	}

	/**
	 * @return the days
	 */
	public List<Day> getDays() {
		return days;
	}

	/**
	 * reset deli totali giornalieri
	 */
	public void resetHours(){
		for (Day d : days) {
			d.resetHours();
		}
	}
}