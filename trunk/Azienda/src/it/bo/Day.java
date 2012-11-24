package it.bo;


import java.util.Calendar;

public class Day{
	public static String getCssStyle(Calendar day) {
		return (day!=null)?((day.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||day.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)?"holiday":"workDay"):"disabledDay";
	}
}