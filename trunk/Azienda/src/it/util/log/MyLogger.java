package it.util.log;

import it.util.format.StringFormat;

import org.apache.log4j.Logger;

public class MyLogger {
	private Logger log;
	private final static String START="START";
	private final static String END="END";

	public MyLogger(@SuppressWarnings("rawtypes") Class clazz) {
		log = Logger.getLogger(clazz);
	}

	public void fatal(String metodo, String message){
		log.fatal(StringFormat.formatMessage(metodo, message));
	}
	public void fatal(String metodo, String message, Exception e){
		log.fatal(StringFormat.formatMessage(metodo, message),e);
	}

	public void error(String metodo, String message){
		log.error(StringFormat.formatMessage(metodo, message));
	}
	public void error(String metodo, String message, Exception e){
		log.error(StringFormat.formatMessage(metodo, message),e);
	}

	public void warn(String metodo, String message){
		log.warn(StringFormat.formatMessage(metodo, message));
	}
	public void warn(String metodo, String message, Exception e){
		log.warn(StringFormat.formatMessage(metodo, message),e);
	}

	public void info(String metodo, String message){
		log.info(StringFormat.formatMessage(metodo, message));
	}
	public void info(String metodo, String message, Exception e){
		log.info(StringFormat.formatMessage(metodo, message),e);
	}

	public void debug(String metodo, String message){
		log.debug(StringFormat.formatMessage(metodo, message));
	}
	public void debug(String metodo, String message, Exception e){
		log.debug(StringFormat.formatMessage(metodo, message),e);
	}

	public void trace(String metodo, String message){
		log.trace(StringFormat.formatMessage(metodo, message));
	}
	public void trace(String metodo, String message, Exception e){
		log.trace(StringFormat.formatMessage(metodo, message),e);
	}

	public void start(String metodo){
		log.trace(StringFormat.formatMessage(metodo,START));
	}
	public void end(String metodo){
		log.trace(StringFormat.formatMessage(metodo,END));
	}
}