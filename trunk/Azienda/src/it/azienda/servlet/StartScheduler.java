package it.azienda.servlet;

import it.azienda.dao.RisorsaDAO;
import it.azienda.dto.RisorsaDTO;
import it.sauronsoftware.cron4j.Scheduler;
import it.util.log.MyLogger;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	public void init(ServletConfig config) throws ServletException {
		final String metodo="init";
		log.start(metodo);
		super.init(config);
		try {
			//startScheduler();
		} catch (Exception e) {
			log.error(metodo, "schedulatore remainder consuntivo fallito", e);
		}finally{
			log.end(metodo);
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

	private void startScheduler(){
		final String metodo="startScheduler";
		log.start(metodo);
		final RisorsaDAO risorsa = new RisorsaDAO(conn.getConnection());
		Scheduler s = new Scheduler();
		// Schedula un task, che sarà eseguito ogni minuto.
		s.schedule("44 11 08 * *", new Runnable() {
			public void run() {
				ArrayList<RisorsaDTO>listaEmail = risorsa.invioEmail();
				StringBuilder testoEmail;
				for (RisorsaDTO risorsa : listaEmail) {
					log.debug(metodo, "nome " + risorsa.getNome());
					log.debug(metodo, "cognome " + risorsa.getCognome());
					testoEmail = new StringBuilder("<html>");
					testoEmail	.append("<head><title>Remainder consuntivazione mensile</title></head>")
								.append("<body>Gentile ")
								.append(risorsa.getCognome())
								.append(" ")
								.append(risorsa.getNome())
								.append(",<br><p>Ti ricordiamo che entro fine del mese bisogna compilare il Time Report.<br>")
								.append("Per effettuare la login cliccare su questo <a href=\"")
								.append(prop.getProperty("siteUrl"))
								.append("/index.php?azione=login\"> Login </a>.")
								.append("<br><br><br/><p>Cordiali Saluti <br>Roberto Camarca</p></body></html>");
					try {
						mail.sendMail(risorsa.getEmail(),"Remainder consuntivazione mensile", testoEmail.toString());
					} catch (Exception e) {
						log.error(metodo, "invio mail Remainder fallita", e);
					}
				}
			}
		});
		s.start();
		log.end(metodo);
	}
}
