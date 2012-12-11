package it.bo;


import java.util.Calendar;

public class Day{
	private double oreOrdinarie=0;
	private double oreSraordinarie=0;
	private Calendar day;

	
	public Day() {
		
	}

	/**
	 * @param day
	 */
	public Day(Calendar day) {
		this.day = day;
	}

	/**
	 * @return the oreOrdinarie
	 */
	public double getOreOrdinarie() {
		return oreOrdinarie;
	}

	/**
	 * @return the oreSraordinarie
	 */
	public double getOreSraordinarie() {
		return oreSraordinarie;
	}

	/**
	 * @return the day
	 */
	public Calendar getDay() {
		return day;
	}

	/**
	 * @param oreOrdinarie the oreOrdinarie to add
	 */
	public void addOreOrdinarie(double oreOrdinarie) {
		this.oreOrdinarie += oreOrdinarie;
	}

	/**
	 * @param oreSraordinarie the oreSraordinarie to add
	 */
	public void addOreSraordinarie(double oreSraordinarie) {
		this.oreSraordinarie += oreSraordinarie;
	}

	public void resetHours(){
		oreOrdinarie=0;
		oreSraordinarie=0;
	}

	public String getCssStyle(String tipologia) {
		return (day!=null)?((!tipologia.equals("riepilogo"))?(day.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||day.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)?"holiday":"workDay":"riepologo"):"disabledDay";
	}
	
}