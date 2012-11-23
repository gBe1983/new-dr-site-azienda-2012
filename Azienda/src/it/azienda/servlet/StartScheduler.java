package it.azienda.servlet;

import it.azienda.connessione.Connessione;
import it.azienda.dao.Email;
import it.azienda.dao.RisorsaDAO;
import it.azienda.dto.RisorsaDTO;
import it.sauronsoftware.cron4j.Scheduler;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class StartScheduler
 */
public class StartScheduler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StartScheduler() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    	
    	try {
			processRequest(request,response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//oggetto connessione
		Connessione connessione = new Connessione();
		
		//oggetto Connection
		final Connection conn = connessione.connessione(request.getParameter("connessione"));
		
		System.out.println("sono passato di qui!!!!");
		
		final RisorsaDAO risorsa = new RisorsaDAO();
		
		Scheduler s = new Scheduler();
		// Schedula un task, che sarà eseguito ogni minuto.
		s.schedule("44 11 08 * *", new Runnable() {
			public void run() {
				ArrayList listaEmail = risorsa.invioEmail(conn);
				for(int x = 0; x < listaEmail.size(); x++){
					RisorsaDTO risorsa = (RisorsaDTO) listaEmail.get(x);
					System.out.println("nome " + risorsa.getNome());
					System.out.println("cognome " + risorsa.getCognome());
					Email email = new Email();
					String testoEmail = "<html>"
							+ "<head>"
							+ " <title> Registrazione Account Azienda</title>"
							+ "</head>"
							+ "<body>"
							+ "Gentile " + risorsa.getCognome() + " " + risorsa.getNome() +",<br>"
							+ "<p> Ti ricordiamo che entro il fine del mese bisogna ricordarsi di compilare il Time Report.<br>"
							+ "Per effettuare la login cliccare su questo <a href=\"http://drconsulting.tv/index.php?azione=login\"> Login </a>." 
							+ "<br><br> I dati d'accesso al portale sono:"
							+ "<br>"
							+ "<br><br/> <p>Cordiali Saluti <br>Roberto Camarca</p>"
							+ "</body>" + "</html>";
					try {
						email.sendMail(risorsa.getEmail(),
								"info.drconsulting@gmail.com",
								"Iscrizione Account Aziendale", testoEmail);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		// Avvia lo scheduler.
		s.start();
		
		
	}
}
