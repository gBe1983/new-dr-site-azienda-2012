package it.azienda.servlet;

import it.azienda.dao.ClienteDAO;
import it.azienda.dto.ClienteDTO;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class GestioneCliente
 */
public class GestioneCliente extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private Logger log;

	/**
	 * 
	 */
	public GestioneCliente() {
		super();
		log = Logger.getLogger(GestioneCliente.class);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		log.info("metodo: doGet");
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		log.info("metodo: doPost");
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		log.info("metodo: processRequest");
		HttpSession sessione = request.getSession();
		// creo l'istanza della classe ClienteDAO
		ClienteDAO cDAO = new ClienteDAO(conn.getConnection());

		// recupero il parametro "azione"
		if (sessione.getAttribute("utenteLoggato") != null) {
			String azione = request.getParameter("azione");

			// verifico che valore ha assunto il parametro "Azione"
			if (azione.equals("inserimentoCliente")|| azione.equals("modificaCliente")) {
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				// creo l'istanza dell'oggetto ClienteDTO
				ClienteDTO cliente = new ClienteDTO();

				// recupero i valori che vengono passati dal form
				cliente.setId_cliente(request.getParameter("codiceCliente"));
				cliente.setRagioneSociale(request.getParameter("ragioneSociale"));
				cliente.setIndirizzo(request.getParameter("indirizzo"));
				cliente.setCap(request.getParameter("cap"));
				cliente.setCitta(request.getParameter("citta"));
				cliente.setProvincia(request.getParameter("provincia"));
				cliente.setPIva(request.getParameter("pIva"));
				cliente.setReferente(request.getParameter("referente"));
				cliente.setTelefono(request.getParameter("telefono"));
				cliente.setCellulare(request.getParameter("cellulare"));
				cliente.setFax(request.getParameter("fax"));
				cliente.setEmail(request.getParameter("email"));
				cliente.setSito(request.getParameter("sito"));
				cliente.setCodFiscale(request.getParameter("codFiscale"));
				/*
				 * verifico che tipo di azione sta compiendo l'utente in modo
				 * che a seconda di come è valorizzata la variabile "azione"
				 * compio una query piuttosto che un altra.
				 */
				if (azione.equals("inserimentoCliente")) {
					String messaggio = cDAO.inserimentoCliente(cliente);
					if (messaggio.equals("ok")) {
						request.setAttribute("messaggio",
								"Inserimento del Cliente avvenuta con successo");
						
						log.info("url: /index.jsp?azione=messaggio");
						
						getServletContext()
							.getRequestDispatcher("/index.jsp?azione=messaggio")
								.forward(request, response);
					} else {
						
						log.info("url: /index.jsp?azione=messaggio");
						
						request.setAttribute("messaggio", messaggio);
						getServletContext()
							.getRequestDispatcher("/index.jsp?azione=messaggio")
								.forward(request, response);
					}
				} else {
					String messaggio = cDAO.modificaCliente(cliente);
					if (messaggio.equals("ok")) {
						request.setAttribute("messaggio",
								"La modifica del Cliente avvenuta con successo");
						
						log.info("url: /index.jsp?azione=messaggio");
						
						getServletContext()
							.getRequestDispatcher("/index.jsp?azione=messaggio")
								.forward(request, response);
					} else {
						request.setAttribute("messaggio", messaggio);
						
						log.info("url: /index.jsp?azione=messaggio");
						
						getServletContext()
							.getRequestDispatcher("/index.jsp?azione=messaggio")
								.forward(request, response);
					}
				}
				
			} else if (azione.equals("caricamentoNominativiCliente")) {//in questa sezione effettuo il caricamento di tutti i clienti che sono stati caricati nella tabella "Tbl_Cliente"
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				request.setAttribute("nominativi",cDAO.caricamentoNominativiCliente());
				
				log.info("url: /index.jsp?azione=visualizzaNominativi&dispositiva=cliente");
				
				getServletContext()
					.getRequestDispatcher(	"/index.jsp?azione=visualizzaNominativi&dispositiva=cliente")
						.forward(request, response);
				
			} else if (azione.equals("ricercaCliente")) {
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				/*
				 * la ricerca cliente può avvenire da diversi canali che sono
				 * Modifica Cliente, Dettaglio Cliente, Ricerca Cliente
				 */
				
				request.setAttribute("cliente", cDAO.caricamentoCliente(request.getParameter("codice"), request.getParameter("nominativo")));
				
				/*
				 * filtro per questo parametro perchè la richiesta avvenendo da più canali 
				 * non sempre tale parametro viene passato in modalita "Modifica Cliente"
				 */
				if (request.getParameter("nominativo") != null) {
					
					log.info("url: /index.jsp?azione=visualizzaCliente&dispositiva=cliente");
					
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=visualizzaCliente&dispositiva=cliente")
							.forward(request, response);
				} else {
					
					log.info("url: /index.jsp?azione=aggiornaCliente&dispositiva=cliente");
					
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=aggiornaCliente&dispositiva=cliente")
							.forward(request, response);
				}
				
			} else if (azione.equals("disabilitaCliente")) {
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				//in questa sezione effettuo l'eliminazione logica del singolo Cliente che l'Utente ha deciso di eliminare
				String messaggio = cDAO.disabilitaCliente(request.getParameter("codice"));
				if (messaggio.equals("ok")) {
					request.setAttribute("messaggio","Il Cliente è stato diabilitato con successo");
					
					log.info("url: /index.jsp?azione=messaggio");
					
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				} else {
					request.setAttribute("messaggio", messaggio);
					
					log.info("url: /index.jsp?azione=messaggio");
					
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				}
				
			} else if (azione.equals("controlloCodiceCliente")) {
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				boolean controlloCodiceCliente = cDAO.controlloCodiceCliente(request.getParameter("codiceCliente"));
				PrintWriter out;
				try {
					out = response.getWriter();
					out.print(controlloCodiceCliente);
					out.flush();
				} catch (IOException e) {
					log.error("eccezione"+ e);
				}
				
			} else if (azione.equals("caricamentoNominativiClienteDisabilitati")) {//in questa sezione effettuo il caricamento di tutti i clienti che sono stati caricati nella tabella "Tbl_Cliente"
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				request.setAttribute("nominativi",cDAO.caricamentoNominativiClienteDisabilitati());
				
				log.info("url: /index.jsp?azione=visualizzaNominativi&dispositiva=cliente&tipo=disabilitato");
				
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=visualizzaNominativi&dispositiva=cliente&tipo=disabilitato")
						.forward(request, response);
			
			} else if (azione.equals("abilitazioneCliente")) {
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				String messaggio = cDAO.abilitaCliente(request.getParameter("codice"));
				if (messaggio.equals("ok")) {
					request.setAttribute("messaggio","L'abilitazione del Cliente avvenuta con successo");
				
					log.info("url: /index.jsp?azione=messaggio");
					
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				} else {
					request.setAttribute("messaggio", messaggio);
					
					log.info("url: /index.jsp?azione=messaggio");
					
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				}
			}
		} else {
			sessioneScaduta(response);
		}
	}
}