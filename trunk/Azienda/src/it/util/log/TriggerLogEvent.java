/**
 * 
 */
package it.util.log;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * @author Dr
 *
 */
public class TriggerLogEvent implements TriggeringEventEvaluator{
	/* (non-Javadoc)
	 * @see org.apache.log4j.spi.TriggeringEventEvaluator#isTriggeringEvent(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	public boolean isTriggeringEvent(LoggingEvent event) {
		return event.getLevel().isGreaterOrEqual(Level.ERROR);
	}
}