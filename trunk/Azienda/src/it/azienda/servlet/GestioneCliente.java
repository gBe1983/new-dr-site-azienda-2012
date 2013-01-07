package it.azienda.servlet;

import it.azienda.dao.ClienteDAO;
import it.azienda.dto.ClienteDTO;
import it.util.log.MyLogger;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GestioneCliente
 */
public class GestioneCliente extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private MyLogger log;

	/**
	 * 
	 */
	public GestioneCliente() {
		super();
		log = new MyLogger(this.getClass());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		final String metodo = "doGet";
		log.start(metodo);
		processRequest(request, response);
		log.end(metodo);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		final String metodo = "doPost";
		log.start(metodo);
		processRequest(request, response);
		log.end(metodo);
	}

	private void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		final String metodo = "processRequest";
		log.start(metodo);
		HttpSession sessione = request.getSession();
		// creo l'istanza della classe ClienteDAO
		ClienteDAO cDAO = new ClienteDAO(conn.getConnection());

		// recupero il parametro "azione"
		if (sessione.getAttribute("utenteLoggato") != null) {
			String azione = request.getParameter("azione");

			// verifico che valore ha assunto il parametro "Azione"
			if (azione.equals("inserimentoCliente")|| azione.equals("modificaCliente")) {

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
						getServletContext()
							.getRequestDispatcher("/index.jsp?azione=messaggio")
								.forward(request, response);
					} else {
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
						getServletContext()
							.getRequestDispatcher("/index.jsp?azione=messaggio")
								.forward(request, response);
					} else {
						request.setAttribute("messaggio", messaggio);
						getServletContext()
							.getRequestDispatcher("/index.jsp?azione=messaggio")
								.forward(request, response);
					}
				}
			} else if (azione.equals("caricamentoNominativiCliente")) {//in questa sezione effettuo il caricamento di tutti i clienti che sono stati caricati nella tabella "Tbl_Cliente"
				request.setAttribute("nominativi",cDAO.caricamentoNominativiCliente());
				getServletContext()
					.getRequestDispatcher(	"/index.jsp?azione=visualizzaNominativi&dispositiva=cliente")
						.forward(request, response);
			} else if (azione.equals("ricercaCliente")) {
				request.setAttribute("cliente", cDAO.caricamentoCliente(request.getParameter("codice"), request.getParameter("nominativo")));
				if (request.getParameter("nominativo") != null) {
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=visualizzaCliente&dispositiva=cliente")
							.forward(request, response);
				} else {
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=aggiornaCliente&dispositiva=cliente")
							.forward(request, response);
				}
			} else if (azione.equals("disabilitaCliente")) {//in questa sezione effettuo l'eliminazione logica del singolo Cliente che l'Utente ha deciso di eliminare
				String messaggio = cDAO.disabilitaCliente(request.getParameter("codice"));
				if (messaggio.equals("ok")) {
					request.setAttribute("messaggio","L'eliminazione del Cliente avvenuta con successo");
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				} else {
					request.setAttribute("messaggio", messaggio);
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				}
			} else if (azione.equals("controlloCodiceCliente")) {
				boolean controlloCodiceCliente = cDAO.controlloCodiceCliente(request.getParameter("codiceCliente"));
				PrintWriter out;
				try {
					out = response.getWriter();
					out.print(controlloCodiceCliente);
					out.flush();
				} catch (IOException e) {
					log.error(metodo, "controlloCodiceCliente", e);
				}
			} else if (azione.equals("caricamentoNominativiClienteDisabilitati")) {//in questa sezione effettuo il caricamento di tutti i clienti che sono stati caricati nella tabella "Tbl_Cliente"
				request.setAttribute("nominativi",cDAO.caricamentoNominativiClienteDisabilitati());
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=visualizzaNominativi&dispositiva=cliente&tipo=disabilitato")
						.forward(request, response);
			} else if (azione.equals("abilitazioneCliente")) {
				String messaggio = cDAO.abilitaCliente(request.getParameter("codice"));
				if (messaggio.equals("ok")) {
					request.setAttribute("messaggio","L'abilitazione del Cliente avvenuta con successo");
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				} else {
					request.setAttribute("messaggio", messaggio);
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				}
			}
		} else {
			sessioneScaduta(response);
		}
		log.end(metodo);
	}
}