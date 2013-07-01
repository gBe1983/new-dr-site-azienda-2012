package it.azienda.servlet;

import it.azienda.connessione.Connessione;
import it.azienda.dao.RisorsaDAO;
import it.azienda.dto.RisorsaDTO;
import it.azienda.schedulatore.Schedulatore;
import it.mail.Email;
import it.util.log.MyLogger;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Servlet implementation class StartScheduler
 */
public class StartScheduler extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private MyLogger log;
//TODO PER FAR FUNZIONARE QUESTO SCHEDULATORE OCCORRE DECIDERE OGNI QUANTO TEMPO DEVE GIRARE,OPPURE SE FARE UNA PAGINA DOVE CLICCANDO INVIA LA MAIL DI REMAINDER gBe
	public StartScheduler() {
		super();
		log=new MyLogger(this.getClass());
	}

	@Override
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    	
		try {
			JobDetail job = JobBuilder.newJob(Schedulatore.class)
		    .withIdentity("testJob")
		    .build();
		
			// specify the running period of the job
			Trigger trigger = TriggerBuilder.newTrigger()
			      .withSchedule( 
			    		  CronScheduleBuilder.cronSchedule("59 * * * * ?"))
			    		  .build();  
			
			//schedule the job
			SchedulerFactory schFactory = new StdSchedulerFactory();
			Scheduler sch = schFactory.getScheduler();
			sch.start();	    	
			sch.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="doGet";
		log.start(metodo);
		//startScheduler();
		log.end(metodo);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="doPost";
		log.start(metodo);
		//startScheduler();
		log.end(metodo);
	}
}
