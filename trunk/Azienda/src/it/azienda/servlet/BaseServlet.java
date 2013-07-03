package it.azienda.servlet;

import it.azienda.connessione.Connessione;
import it.mail.Email;
import it.util.log.MyLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = -8632980348704639397L;
	protected Connessione conn;
	protected Email mail;
	protected Properties prop;
	private Logger log;

	public BaseServlet() {
		log = Logger.getLogger(BaseServlet.class);
	}

	public void init(ServletConfig config) throws ServletException {
	
		log.info("-------------------------------------------------------------------------------");
		log.info("metodo: init");
		
		super.init(config);
		ServletContext servletContext = getServletContext();
		conn = new Connessione(servletContext);
		mail = new Email(servletContext);
		initProperties(servletContext);
		
	}

	private void initProperties(ServletContext servletContext) throws ServletException {
		
		log.info("-------------------------------------------------------------------------------");
		log.info("metodo: initProperties");
		
		prop=new Properties();
		prop.setProperty("siteUrl", servletContext.getInitParameter("siteUrl"));
	}

	protected void sessioneScaduta(HttpServletResponse response){
		
		log.info("-------------------------------------------------------------------------------");
		log.info("metodo: sessioneScaduta");
		
		response.setContentType("text/html");
		try {
			PrintWriter out = response.getWriter();
			out.print("<html><head></head><body><script type=\"text/javascript\">" +
					"alert(\"La sessione è scaduta. Rieffettuare la login\");" +
					"var url = window.location.href;" +
					"var variabiliUrl = url.split(\"/\");" +
					"for(a=0; a < variabiliUrl.length; a++){" +
					"		if(a == 2){" +
					"			var localVariabili = variabiliUrl[a].replace(\":\",\".\").split(\".\");" +
					"			for(x=0; x < localVariabili.length; x++){" +
					"				if(localVariabili[x] == \"localhost\"){" +
					"					window.location = \"http://localhost/dr\";" +
					"				}if(localVariabili[x] == \"cvonline\"){" +
					"					window.location.href = \"http://cvonline.tv\";" +
					"				}if(localVariabili[x] == \"drconsulting\"){" +
					"					window.location.href= \"http://drconsulting.tv\";" +
					"				}" +
					"			}" +
					"		}else{" +
					"			continue;" +
					"		}" +
					"}" +
					"</script></body></html>");
			out.flush();
		} catch (IOException e) {
			log.error("errore: " + e);
		}
	}

	protected void clearSession(HttpSession session) {
		
		log.info("metodo: clearSession");
		
		Enumeration<String> attrNames = session.getAttributeNames();
		String valoriSessione;
		while (attrNames.hasMoreElements()){
				valoriSessione = (String) attrNames.nextElement();
				session.removeAttribute(valoriSessione);
				log.debug("valori in sessione: "+ valoriSessione);
		}
	}
}