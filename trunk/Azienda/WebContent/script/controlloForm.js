
/*
 * questa variabile è stata creata con lo scopo di effettaure in seguito
 * dei controlli sul suo valore in modo da effettuare in maniera
 * corretta il tutto.
 */

var tipologiaTrattativa = "";
var inserimentoCommessaTrattativa = false;

function controlloModificaAzienda(){
	
	//gestione dati aziendali
	
	var ragione_sociale = document.azienda.ragioneSociale.value;
	var email = document.azienda.email.value;
	var pIva = document.azienda.pIva.value;
	
	//gestione area legale
	var indirizzoLegale = document.azienda.indirizzoLegale.value;
	var cittaLegale = document.azienda.cittaLegale.value;
	var provinciaLegale = document.azienda.provinciaLegale.value;
	var capLegale = document.azienda.capLegale.value;
	var nazioneLegale = document.azienda.nazioneLegale.value;
	
	
	if(ragione_sociale == "" || ragione_sociale == null){
		alert("Valorizzare correttamente il campo \"Ragione Sociale\"");
		return false;
	}
	if(email == "" || email == null){
		alert("Valorizzare correttamente il campo \"Email\"");
		return false;
	}
	if(pIva == "" || pIva == null){
		alert("Valorizzare correttamente il campo \"Partita Iva\"");
		return false;
	}else{
		if(isNaN(pIva)){
			alert("Valorizzare il campo \"Partita Iva\" solo con valori numerici");
			return false;
		}
	}
	
	if(indirizzoLegale == "" || indirizzoLegale == null){
		alert("Valorizzare correttamente il campo \"Indirizzo Legale\"");
		return false;
	}
	if(cittaLegale == "" || cittaLegale == null){
		alert("Valorizzare correttamente il campo \"Citta Legale\"");
		return false;
	}
	if(provinciaLegale == "" || provinciaLegale == null){
		alert("Valorizzare correttamente il campo \"Provincia Legale\"");
		return false;
	}
	if(capLegale == "" || capLegale == null){
		alert("Valorizzare correttamente il campo \"Cap Legale\"");
		return false;
	}else{
		if(isNaN(capLegale)){
			alert("Valorizzare il campo \"Cap Legale\" solo con valori numerici");
			return false;
		}
	}
	if(nazioneLegale == "" || nazioneLegale == null){
		alert("Valorizzare correttamente il campo \"Nazione Legale\"");
		return false;
	}
	
	return true;

}

/*
* con questa funzione controllo tutte i campi relativi al cliente
* sia in fase d'inserimento che in fase di modifica. 
*/

function controlloInserisciModificaCliente(){
	
	var ragione_sociale = document.cliente.ragioneSociale.value;
	var pIva = document.cliente.pIva.value;
	var email = document.cliente.email.value;
	
	if(ragione_sociale == "" || ragione_sociale == null){
		alert("Valorizzare correttamente il campo \"Ragione Sociale\"");
		return false;
	}
	if(pIva == "" || pIva == null || pIva < 11){
		alert("Valorizzare correttamente il campo \"Partita Iva\"");
		return false;
	}else{
		if(isNaN(pIva)){
			alert("Valorizzare il campo \"Partita Iva\" solo con valori numerici");
			return false;
		}
	}
	if(email == "" || email == null){
		alert("Valorizzare correttamente il campo \"Email\"");
		return false;
	}
	
	return true;
}

/*
* con questa funzione controllo tutte i campi relativi della risorsa
* sia in fase d'inserimento che in fase di modifica. 
*/

function controlloInserisciModificaRisorsa(){
	
	var cognome = document.risorsa.cognome.value;
	var nome = document.risorsa.nome.value;
	var dataNascita = document.risorsa.dataNascita.value;
	var luogoNascita = document.risorsa.luogoNascita.value;
	var email = document.risorsa.mail.value;
	var cellulare = document.risorsa.cellulare.value;
	
	var username = "";
	if(document.risorsa.username != undefined){
		username = document.risorsa.username.value;
	}

	var password = "";
	if(document.risorsa.password != undefined){
		password = document.risorsa.password.value;
	}

	if(cognome == "" || cognome == null){
		alert("Valorizzare correttamente il campo \"Cognome\"");
		return false;
	}
	if(nome == "" || nome == null){
		alert("Valorizzare correttamente il campo \"Nome\"");
		return false;
	}
	if(dataNascita == "" || dataNascita == null){
		alert("Valorizzare correttamente il campo \"Data Nascita\"");
		return false;
	}else{
		if(dataNascita.length < 10 || dataNascita.length > 10){
			alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
			return false;
		}else{
			var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
			if (!espressione.test(dataNascita))
			{
				alert("Formato della \"Data Nascita\" è errato. Il formato corretto è gg-mm-yyyy");
				return false;
			}
		}
	}
	if(luogoNascita == "" || luogoNascita == null){
		alert("Valorizzare correttamente il campo \"Luogo Nascita\"");
		return false;
	}
	if(codiceFiscale.length < 16){
		alert("La lunghezza del campo \"Codice Fiscale\" non è corretta");
		return false;
	}
	if(email == "" || email == null){
		alert("Valorizzare correttamente il campo \"Email\"");
		return false;
	}
	if(cellulare == "" || cellulare == null){
		alert("Valorizzare correttamente il campo \"Cellulare\"");
		return false;
	}else{
		if(isNaN(cellulare)){
			alert("Valorizzare il campo \"Cellulare\" solo con valori numerici");
			return false;
		}
	}
	if(username == "" || username == null){
		alert("Valorizzare correttamente il campo \"Username\"");
		return false;
	}
	if(password == "" || password == null){
		alert("Valorizzare correttamente il campo \"Password\"");
		return false;
	}
	
	return true;
}

/*
* con questa funzione controllo tutte le tipologie di commesse
* in fase d'inserimento. 
*/

function controlloInserisciCommessa(tipologia){
	
	if(tipologia == "1"){
		
		/*
		 * commessa esterna singola
		 * 
		 * commessaEsternaSingola_codice
		 * commessaEsternaSingola_idRisorsa
		 * commessaEsternaSingola_dataOfferta
		 * commessaEsternaSingola_oggettoOfferta
		 * commessaEsternaSingola_descrizione
		 * commessaEsternaSingola_dataInizio
		 * commessaEsternaSingola_dataFine
		 * commessaEsternaSingola_importo
		 * commessaEsternaSingola_ore
		*/
		
		var codice_CommessaSingola = "";
		var risorsa_CommessaSingola = "";
		var dataOfferta_CommessaSingola = "";
		var oggettoOfferta_CommessaSingola = "";
		var descrizione_CommessaSingola = "";
		var dataInizio_CommessaSingola = "";
		var dataFine_CommessaSingola = "";
		var importo_CommessaSingola = "";
		var ore_CommessaSingola = "";
		
		if(document.commessa.commessaEsternaSingola_codice != undefined){
			codice_CommessaSingola = document.commessa.commessaEsternaSingola_codice.value;
			if(codice_CommessaSingola == null || codice_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Cliente\"");
				return false;
			}
		}
		
		if(document.commessa.commessaEsternaSingola_idRisorsa != undefined){
			risorsa_CommessaSingola = document.commessa.commessaEsternaSingola_idRisorsa.value;
			if(risorsa_CommessaSingola == null || risorsa_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Risorsa\"");
				return false;
			}
		}
		
		if(document.commessa.commessaEsternaSingola_dataOfferta != undefined){
			dataOfferta_CommessaSingola = document.commessa.commessaEsternaSingola_dataOfferta.value;
			if(dataOfferta_CommessaSingola == null || dataOfferta_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Data Offerta\"");
				return false;
			}else{
				if(dataOfferta_CommessaSingola.length < 10 || dataOfferta_CommessaSingola.length > 10){
					alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
					return false;
				}else{
					var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
					if (!espressione.test(dataOfferta_CommessaSingola))
					{
						alert("Formato della \"Data Offerta\" è errato. Il formato corretto è gg-mm-yyyy");
						return false;
					}
				}
			}
		}
		
		if(document.commessa.commessaEsternaSingola_oggettoOfferta != undefined){
			oggettoOfferta_CommessaSingola = document.commessa.commessaEsternaSingola_oggettoOfferta.value;
			if(oggettoOfferta_CommessaSingola == null || oggettoOfferta_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Oggetto Offerta\"");
				return false;
			}
		}
		
		if(document.commessa.commessaEsternaSingola_descrizione != undefined){
			descrizione_CommessaSingola = document.commessa.commessaEsternaSingola_descrizione.value;
			if(descrizione_CommessaSingola == null || descrizione_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Descrizione\"");
				return false;
			}
		}
		
		if(document.commessa.commessaEsternaSingola_dataInizio != undefined){
			dataInizio_CommessaSingola = document.commessa.commessaEsternaSingola_dataInizio.value;
			if(dataInizio_CommessaSingola == null || dataInizio_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Data Inizio\"");
				return false;
			}else{
				if(dataInizio_CommessaSingola.length < 10 || dataInizio_CommessaSingola.length > 10){
					alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
					return false;
				}else{
					var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
					if (!espressione.test(dataInizio_CommessaSingola))
					{
						alert("Formato della \"Data Inizio\" è errato. Il formato corretto è gg-mm-yyyy");
						return false;
					}
				}
			}
		}
		
		if(document.commessa.commessaEsternaSingola_dataFine != undefined){
			dataFine_CommessaSingola = document.commessa.commessaEsternaSingola_dataFine.value;
			if(dataFine_CommessaSingola == null || dataFine_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Data Fine\"");
				return false;
			}else{
				if(dataFine_CommessaSingola.length < 10 || dataFine_CommessaSingola.length > 10){
					alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
					return false;
				}else{
					var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
					if (!espressione.test(dataFine_CommessaSingola))
					{
						alert("Formato della \"Data Fine\" è errato. Il formato corretto è gg-mm-yyyy");
						return false;
					}
				}
			}
		}
		
		var split_DataInizio_CommessaSingola = dataInizio_CommessaSingola.split("-");
		var split_DataFine_CommessaSingola = dataFine_CommessaSingola.split("-");

		var dateInizio_CommessaSingola = new Date(split_DataInizio_CommessaSingola[2],split_DataInizio_CommessaSingola[1],split_DataInizio_CommessaSingola[0]).getTime();
		var dateFine_CommessaSingola = new Date(split_DataFine_CommessaSingola[2],split_DataFine_CommessaSingola[1],split_DataFine_CommessaSingola[0]).getTime();
		
		if(dateInizio_CommessaSingola > dateFine_CommessaSingola){
			alert("Data Inizio maggiore della Data Fine");
			return false;
		}
		
		if(document.commessa.commessaEsternaSingola_importo != undefined){
			importo_CommessaSingola = document.commessa.commessaEsternaSingola_importo.value;
			if(importo_CommessaSingola == null || importo_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Importo\"");
				return false;
			}else{
				if(isNaN(importo_CommessaSingola)){
					alert("Valorizzare il campo \"Importo\" solo con valori numerici");
					return false;
				}
			}
		}
		
		if(document.commessa.commessaEsternaSingola_ore != undefined){
			ore_CommessaSingola = document.commessa.commessaEsternaSingola_ore.value;
			if(isNaN(ore_CommessaSingola)){
				alert("Valorizzare il campo \"Ore\" solo con valori numerici");
				return false;
			}
		}
		
	}else if(tipologia == "2"){
	
		/*
		 * commessa esterna multipla
		 * 
		 * codice
		 * dataOfferta
		 * Offerta
		 * dataInizio
		 * dataFine
		 * importo
		 * ore
		*/
		
		var codice = "";
		var dataOfferta = "";
		var oggettoOfferta = "";
		var descrizione = "";
		var dataInizio = "";
		var dataFine = "";
		var importo = "";
		var ore = "";
		
		if(document.commessa.codice != undefined){
			codice = document.commessa.codice.value;
			if(codice == null || codice == ""){
				alert("Valorizzare correttamente il campo \"Cliente\"");
				return false;
			}
		}
		
		if(document.commessa.dataOfferta != undefined){
			dataOfferta = document.commessa.dataOfferta.value;
			if(dataOfferta == null || dataOfferta == ""){
				alert("Valorizzare correttamente il campo \"Data Offerta\"");
				return false;
			}else{
				if(dataOfferta.length < 10 || dataOfferta.length > 10){
					alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
					return false;
				}else{
					var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
					if (!espressione.test(dataOfferta))
					{
						alert("Formato della \"Data Offerta\" è errato. Il formato corretto è gg-mm-yyyy");
						return false;
					}
				}
			}
		}
		
		if(document.commessa.oggettoOfferta != undefined){
			oggettoOfferta = document.commessa.oggettoOfferta.value;
			if(oggettoOfferta == null || oggettoOfferta == ""){
				alert("Valorizzare correttamente il campo \"Oggetto Offerta\"");
				return false;
			}
		}
		
		if(document.commessa.descrizione != undefined){
			descrizione = document.commessa.descrizione.value;
			if(descrizione == null || descrizione == ""){
				alert("Valorizzare correttamente il campo \"Descrizione\"");
				return false;
			}
		}
		
		if(document.commessa.dataInizio != undefined){
			dataInizio = document.commessa.dataInizio.value;
			if(dataInizio == null || dataInizio == ""){
				alert("Valorizzare correttamente il campo \"Data Inizio\"");
				return false;
			}else{
				if(dataInizio.length < 10 || dataInizio.length > 10){
					alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
					return false;
				}else{
					var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
					if (!espressione.test(dataInizio))
					{
						alert("Formato della \"Data Inizio\" è errato. Il formato corretto è gg-mm-yyyy");
						return false;
					}
				}
			}
		}
		
		if(document.commessa.dataFine != undefined){
			dataFine = document.commessa.dataFine.value;
			if(dataFine == null || dataFine == ""){
				alert("Valorizzare correttamente il campo \"Data Fine\"");
				return false;
			}else{
				if(dataFine.length < 10 || dataFine.length > 10){
					alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
					return false;
				}else{
					var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
					if (!espressione.test(dataFine))
					{
						alert("Formato della \"Data Fine\" è errato. Il formato corretto è gg-mm-yyyy");
						return false;
					}
				}
			}
		}
		
		var split_DataInizio = dataInizio.split("-");
		var split_DataFine = dataFine.split("-");

		var dateInizio = new Date(split_DataInizio[2],split_DataInizio[1],split_DataInizio[0]).getTime();
		var dateFine = new Date(split_DataFine[2],split_DataFine[1],split_DataFine[0]).getTime();
		
		if(dateInizio > dateFine){
			alert("Data Inizio maggiore della Data Fine");
			return false;
		}
		
		
		if(document.commessa.importo != undefined){
			importo = document.commessa.importo.value;
			if(importo == null || importo == ""){
				alert("Valorizzare correttamente il campo \"Importo\"");
				return false;
			}else{
				if(isNaN(importo)){
					alert("Valorizzare il campo \"Importo\" solo con valori numerici");
					return false;
				}
			}
		}
		
		if(document.commessa.ore != undefined){
			ore = document.commessa.ore.value;
			if(isNaN(ore)){
				alert("Valorizzare il campo \"Ore\" solo con valori numerici");
				return false;
			}
		}
		
	}else if(tipologia == "3"){
		
			/*
			 * * commessa interna
			 * 
			 * commessaInterna_dataOfferta
			 * commessaInterna_oggettoOfferta
			 * commessaInterna_dataInizio
			 * commessaInterna_dataFine
			 * commessaInterna_importo
			 * commessaInterna_ore
			 */
			
			var dataOfferta_CommessaInterna = "";
			var oggettoOfferta_CommessaInterna = "";
			var descrizione_CommessaInterna = "";
			var dataInizio_CommessaInterna = "";
			var dataFine_CommessaInterna = "";
			var importo_CommessaInterna = "";
			var ore_CommessaInterna = "";
			
			if(document.commessa.commessaInterna_dataOfferta != undefined){
				dataOfferta_CommessaInterna = document.commessa.commessaInterna_dataOfferta.value;
				if(dataOfferta_CommessaInterna == null || dataOfferta_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Data Offerta\"");
					return false;
				}else{
					if(dataOfferta_CommessaInterna.length < 10 || dataOfferta_CommessaInterna.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataOfferta_CommessaInterna))
						{
							alert("Formato della \"Data Offerta\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.commessa.commessaInterna_oggettoOfferta != undefined){
				oggettoOfferta_CommessaInterna = document.commessa.commessaInterna_oggettoOfferta.value;
				if(oggettoOfferta_CommessaInterna == null || oggettoOfferta_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Oggetto Offerta\"");
					return false;
				}
			}
			
			if(document.commessa.commessaInterna_descrizione != undefined){
				descrizione_CommessaInterna = document.commessa.commessaInterna_descrizione.value;
				if(descrizione_CommessaInterna == null || descrizione_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Descrizione\"");
					return false;
				}
			}
			
			if(document.commessa.commessaInterna_dataInizio != undefined){
				dataInizio_CommessaInterna = document.commessa.commessaInterna_dataInizio.value;
				if(dataInizio_CommessaInterna == null || dataInizio_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Data Inizio\"");
					return false;
				}else{
					if(dataInizio_CommessaInterna.length < 10 || dataInizio_CommessaInterna.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataInizio_CommessaInterna))
						{
							alert("Formato della \"Data Inizio\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.commessa.commessaInterna_dataFine != undefined){
				dataFine_CommessaInterna = document.commessa.commessaInterna_dataFine.value;
				if(dataFine_CommessaInterna == null || dataFine_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Data Fine\"");
					return false;
				}else{
					if(dataFine_CommessaInterna.length < 10 || dataFine_CommessaInterna.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataFine_CommessaInterna))
						{
							alert("Formato della \"Data Fine\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			var split_DataInizio_CommessaInterna = dataInizio_CommessaInterna.split("-");
			var split_DataFine_CommessaInterna = dataFine_CommessaInterna.split("-");

			var dateInizio_CommessaInterna = new Date(split_DataInizio_CommessaInterna[2],split_DataInizio_CommessaInterna[1],split_DataInizio_CommessaInterna[0]).getTime();
			var dateFine_CommessaInterna = new Date(split_DataFine_CommessaInterna[2],split_DataFine_CommessaInterna[1],split_DataFine_CommessaInterna[0]).getTime();
			
			if(dateInizio_CommessaInterna > dateFine_CommessaInterna){
				alert("Data Inizio maggiore della Data Fine");
				return false;
			}
			
			if(document.commessa.commessaInterna_importo != undefined){
				importo_CommessaInterna = document.commessa.commessaInterna_importo.value;
				if(importo_CommessaInterna == null || importo_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Importo\"");
					return false;
				}else{
					if(isNaN(importo_CommessaInterna)){
						alert("Valorizzare il campo \"Importo\" solo con valori numerici");
						return false;
					}
				}
			}
			
			if(document.commessa.commessaInterna_ore != undefined){
				ore_CommessaInterna = document.commessa.commessaInterna_ore.value;
				if(isNaN(ore_CommessaInterna)){
					alert("Valorizzare il campo \"Ore\" solo con valori numerici");
					return false;
				}
			}
			
	}else if(tipologia == 4){
		
		/*
		 * altro
		 * 
		 * altro_descrizione
		 */
		
	  tipologia_Commessa = document.commessa.altro_descrizione;
	  var controlloSelezione = false;
	  for(var i=0;i<tipologia_Commessa.length;i++){
	    if(tipologia_Commessa[i].checked){
	    	controlloSelezione = true;
	    }
	  }
	  if(!controlloSelezione){
		  alert("Selezionare una \"Tipologia Commessa\"");
		  return controlloSelezione;
	  }else{
		  return controlloSelezione;
	  }
	}
	
	return true;
}

/*
 * con questa funzione controllo tutte le tipologie di commesse
 * in fase di modifica. 
 */ 

function controlloModificaCommessa(tipologia){
	
	
	
	if(tipologia == "1"){
		
		/*
		 * commessa esterna singola
		 * 
		 * commessaEsternaSingola_codice
		 * commessaEsternaSingola_idRisorsa
		 * commessaEsternaSingola_dataOfferta
		 * commessaEsternaSingola_oggettoOfferta
		 * commessaEsternaSingola_dataInizio
		 * commessaEsternaSingola_dataFine
		 * commessaEsternaSingola_importo
		 * commessaEsternaSingola_ore
		*/
		
		var dataOfferta_CommessaSingola = "";
		var oggettoOfferta_CommessaSingola = "";
		var descrizione_CommessaSingola = "";
		var dataFine_CommessaSingola = "";
		var importo_CommessaSingola = "";
		var ore_CommessaSingola = "";
		
		if(document.inserisciCommessaMultipla.commessaEsternaSingola_dataOfferta != undefined){
			dataOfferta_CommessaSingola = document.inserisciCommessaMultipla.commessaEsternaSingola_dataOfferta.value;
			if(dataOfferta_CommessaSingola == null || dataOfferta_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Data Offerta\"");
				return false;
			}else{
				if(dataOfferta_CommessaSingola.length < 10 || dataOfferta_CommessaSingola.length > 10){
					alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
					return false;
				}else{
					var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
					if (!espressione.test(dataOfferta_CommessaSingola))
					{
						alert("Formato della \"Data Offerta\" è errato. Il formato corretto è gg-mm-yyyy");
						return false;
					}
				}
			}
		}
		
		if(document.inserisciCommessaMultipla.commessaEsternaSingola_oggettoOfferta != undefined){
			oggettoOfferta_CommessaSingola = document.inserisciCommessaMultipla.commessaEsternaSingola_oggettoOfferta.value;
			if(oggettoOfferta_CommessaSingola == null || oggettoOfferta_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Oggetto Offerta\"");
				return false;
			}
		}
		
		if(document.inserisciCommessaMultipla.commessaEsternaSingola_descrizione != undefined){
			descrizione_CommessaSingola = document.inserisciCommessaMultipla.commessaEsternaSingola_descrizione.value;
			if(descrizione_CommessaSingola == null || descrizione_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Descrizione\"");
				return false;
			}
		}
	
		
		if(document.inserisciCommessaMultipla.commessaEsternaSingola_dataFine != undefined){
			dataFine_CommessaSingola = document.inserisciCommessaMultipla.commessaEsternaSingola_dataFine.value;
			if(dataFine_CommessaSingola == null || dataFine_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Data Fine\"");
				return false;
			}else{
				if(dataFine_CommessaSingola.length < 10 || dataFine_CommessaSingola.length > 10){
					alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
					return false;
				}else{
					var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
					if (!espressione.test(dataFine_CommessaSingola))
					{
						alert("Formato della \"Data Fine\" è errato. Il formato corretto è gg-mm-yyyy");
						return false;
					}
				}
			}
		}
		
		dataInizio_CommessaSingola = document.inserisciCommessaMultipla.commessaEsternaSingola_dataInizio.value;
		var split_DataInizio = dataInizio_CommessaSingola.split("-");
		var split_DataFine = dataFine_CommessaSingola.split("-");

		var dateInizio = new Date(split_DataInizio[0],split_DataInizio[1],split_DataInizio[2]).getTime();
		var dateFine = new Date(split_DataFine[2],split_DataFine[1],split_DataFine[0]).getTime();
		
		if(dateInizio > dateFine){
			alert("Data Inizio maggiore della Data Fine");
			return false;
		}
		
		if(document.inserisciCommessaMultipla.commessaEsternaSingola_importo != undefined){
			importo_CommessaSingola = document.inserisciCommessaMultipla.commessaEsternaSingola_importo.value;
			if(importo_CommessaSingola == null || importo_CommessaSingola == ""){
				alert("Valorizzare correttamente il campo \"Importo\"");
				return false;
			}else{
				if(isNaN(importo_CommessaSingola)){
					alert("Valorizzare il campo \"Importo\" solo con valori numerici");
					return false;
				}
			}
		}
		
		if(document.inserisciCommessaMultipla.commessaEsternaSingola_ore != undefined){
			ore_CommessaSingola = document.inserisciCommessaMultipla.commessaEsternaSingola_ore.value;
			if(isNaN(ore_CommessaSingola)){
				alert("Valorizzare il campo \"Ore\" solo con valori numerici");
				return false;
			}
		}
		
	}else if(tipologia == "2"){
	
		/*
		 * commessa esterna multipla
		 * 
		 * dataOfferta
		 * Offerta
		 * descrizione
		 * dataInizio
		 * dataFine
		 * importo
		 * ore
		*/
		
		var dataOfferta = "";
		var oggettoOfferta = "";
		var descrizione = "";
		var dataInizio = "";
		var dataFine = "";
		var importo = "";
		var ore = "";
		
		if(document.inserisciCommessaMultipla.dataOfferta != undefined){
			dataOfferta = document.inserisciCommessaMultipla.dataOfferta.value;
			if(dataOfferta == null || dataOfferta == ""){
				alert("Valorizzare correttamente il campo \"Data Offerta\"");
				return false;
			}else{
				if(dataOfferta.length < 10 || dataOfferta.length > 10){
					alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
					return false;
				}else{
					var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
					if (!espressione.test(dataOfferta))
					{
						alert("Formato della \"Data Offerta\" è errato. Il formato corretto è gg-mm-yyyy");
						return false;
					}
				}
			}
		}
		
		if(document.inserisciCommessaMultipla.oggettoOfferta != undefined){
			oggettoOfferta = document.inserisciCommessaMultipla.oggettoOfferta.value;
			if(oggettoOfferta == null || oggettoOfferta == ""){
				alert("Valorizzare correttamente il campo \"Oggetto Offerta\"");
				return false;
			}
		}
		
		if(document.inserisciCommessaMultipla.descrizione != undefined){
			descrizione = document.inserisciCommessaMultipla.descrizione.value;
			if(descrizione == null || descrizione == ""){
				alert("Valorizzare correttamente il campo \"Descrizione\"");
				return false;
			}
		}
		
		
		if(document.inserisciCommessaMultipla.dataFine != undefined){
			dataFine = document.inserisciCommessaMultipla.dataFine.value;
			if(dataFine == null || dataFine == ""){
				alert("Valorizzare correttamente il campo \"Data Fine\"");
				return false;
			}else{
				if(dataFine.length < 10 || dataFine.length > 10){
					alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
					return false;
				}else{
					var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
					if (!espressione.test(dataFine))
					{
						alert("Formato della \"Data Fine\" è errato. Il formato corretto è gg-mm-yyyy");
						return false;
					}
				}
			}
		}
		
		dataInizio = document.inserisciCommessaMultipla.dataInizio.value;
		var split_DataInizio = dataInizio.split("-");
		var split_DataFine = dataFine.split("-");

		var dateInizio = new Date(split_DataInizio[0],split_DataInizio[1],split_DataInizio[2]).getTime();
		var dateFine = new Date(split_DataFine[2],split_DataFine[1],split_DataFine[0]).getTime();
		
		if(dateInizio > dateFine){
			alert("Data Inizio maggiore della Data Fine");
			return false;
		}
		
		
		
		if(document.inserisciCommessaMultipla.importo != undefined){
			importo = document.inserisciCommessaMultipla.importo.value;
			if(importo == null || importo == ""){
				alert("Valorizzare correttamente il campo \"Importo\"");
				return false;
			}else{
				if(isNaN(importo)){
					alert("Valorizzare il campo \"Importo\" solo con valori numerici");
					return false;
				}
			}
		}
		
		if(document.inserisciCommessaMultipla.ore != undefined){
			ore = document.inserisciCommessaMultipla.ore.value;
			if(isNaN(ore)){
				alert("Valorizzare il campo \"Ore\" solo con valori numerici");
				return false;
			}
		}
	}else if(tipologia == "3"){
		
			/*
			 *	Commessa Interna
			 * 
			 * 
			 * commessaInterna_dataOfferta
			 * commessaInterna_oggettoOfferta
			 * commessaInterna_dataFine
			 * commessaInterna_importo
			 * commessaInterna_ore
			 */
			
			var dataOfferta_CommessaInterna = "";
			var oggettoOfferta_CommessaInterna = "";
			var descrizione_CommessaInterna = "";
			var dataInizio_CommessaInterna = "";
			var dataFine_CommessaInterna = "";			
			var importo_CommessaInterna = "";
			var ore_CommessaInterna = "";
			
			if(document.inserisciCommessaMultipla.commessaInterna_dataOfferta != undefined){
				dataOfferta_CommessaInterna = document.inserisciCommessaMultipla.commessaInterna_dataOfferta.value;
				if(dataOfferta_CommessaInterna == null || dataOfferta_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Data Offerta\"");
					return false;
				}else{
					if(dataOfferta_CommessaInterna.length < 10 || dataOfferta_CommessaInterna.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataOfferta_CommessaInterna))
						{
							alert("Formato della \"Data Offerta\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.inserisciCommessaMultipla.commessaInterna_oggettoOfferta != undefined){
				oggettoOfferta_CommessaInterna = document.inserisciCommessaMultipla.commessaInterna_oggettoOfferta.value;
				if(oggettoOfferta_CommessaInterna == null || oggettoOfferta_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Oggetto Offerta\"");
					return false;
				}
			}
			
			if(document.inserisciCommessaMultipla.commessaInterna_descrizione != undefined){
				descrizione_CommessaInterna = document.inserisciCommessaMultipla.commessaInterna_descrizione.value;
				if(descrizione_CommessaInterna == null || descrizione_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Descrizione\"");
					return false;
				}
			}
			
			if(document.inserisciCommessaMultipla.commessaInterna_dataFine != undefined){
				dataFine_CommessaInterna = document.inserisciCommessaMultipla.commessaInterna_dataFine.value;
				if(dataFine_CommessaInterna == null || dataFine_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Data Fine\"");
					return false;
				}else{
					if(dataFine_CommessaInterna.length < 10 || dataFine_CommessaInterna.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataFine_CommessaInterna))
						{
							alert("Formato della \"Data Fine\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			dataInizio_CommessaInterna = document.inserisciCommessaMultipla.commessaInterna_dataInizio.value;
			var split_DataInizio_CommessaInterna = dataInizio_CommessaInterna.split("-");
			var split_DataFine_CommessaInterna = dataFine_CommessaInterna.split("-");

			var dateInizio_CommessaInterna = new Date(split_DataInizio_CommessaInterna[0],split_DataInizio_CommessaInterna[1],split_DataInizio_CommessaInterna[2]).getTime();
			var dateFine_CommessaInterna = new Date(split_DataFine_CommessaInterna[2],split_DataFine_CommessaInterna[1],split_DataFine_CommessaInterna[0]).getTime();
			
			if(dateInizio_CommessaInterna > dateFine_CommessaInterna){
				alert("Data Inizio maggiore della Data Fine");
				return false;
			}
			
			if(document.inserisciCommessaMultipla.commessaInterna_importo != undefined){
				importo_CommessaInterna = document.inserisciCommessaMultipla.commessaInterna_importo.value;
				if(importo_CommessaInterna == null || importo_CommessaInterna == ""){
					alert("Valorizzare correttamente il campo \"Importo\"");
					return false;
				}else{
					if(isNaN(importo_CommessaInterna)){
						alert("Valorizzare il campo \"Importo\" solo con valori numerici");
						return false;
					}
				}
			}
			
			if(document.inserisciCommessaMultipla.commessaInterna_ore != undefined){
				ore_CommessaInterna = document.inserisciCommessaMultipla.commessaInterna_ore.value;
				if(isNaN(ore_CommessaInterna)){
					alert("Valorizzare il campo \"Ore\" solo con valori numerici");
					return false;
				}
			}
			
	}else if(tipologia == 4){
		
		/*
		 * altro_descrizione
		 */
		
	  ff = document.commessa.altro_descrizione;
	  for(var i=0;i<ff.length;i++){
	    if(ff[i].checked){
	    	return true;
	    }else{
	    	alert("Selezionare almeno una tipologia di Commessa");
	    	return false;
	    }
	  }
	}
	
	return true;
}


function controlloRicercaCliente(){
	
	var valoreCliente = document.ricercaCliente.nominativo.value;
	
	if(valoreCliente == ""){
		alert("Il valore del cliente non è valorizzato");
		return false;
	}
	
	return true;
}

function controlloPassword(){
	
	vecchiaPasswordDigitata = document.vecchiaPassword.vecchiaPassword.value;
	nuovaPassword = document.vecchiaPassword.nuovaPassword.value;
	confermaNuovaPassword = document.vecchiaPassword.confermaNuovaPassword.value;
	
	
	if(nuovaPassword != confermaNuovaPassword){
		alert("Digitare correttamente la nuova Password");
		return false;
	}
	
	return true;
	
}



/*
 * questo metodo viene richiamato nella sezione trattativa, al momento della modifica che dell'inserimento
 * e viene richiamato quando la trattativa viene associata ad una commessa.
 */

function commessa(tipoOperazione){
	if(tipoOperazione == "inserisci"){
		
		var inserisci = "";
		
		//verifico quale select è stata valorizzata con l'attributo "Nuova Commessa"
		
		if(document.inserisciTrattative.trattattivaSingola_esito.value == "nuova commessa"){
			inserisci = document.inserisciTrattative.trattattivaSingola_esito.value;
			inserimentoCommessaTrattativa = true;
		}else if(document.inserisciTrattative.esito.value == "nuova commessa"){
			inserisci = document.inserisciTrattative.esito.value;
			inserimentoCommessaTrattativa = true;
		}
		
		if(inserisci == "nuova commessa"){
			document.getElementById("creazioneCommessa").style.display = "block";
		}else{
			document.getElementById("creazioneCommessa").style.display = "none";
			inserimentoCommessaTrattativa = false;
		}
	}else{
		var modifica = "";
		//verifico quale select è stata valorizzata con l'attributo "Nuova Commessa"
		
		if(document.modificaTrattativa.trattattivaSingola_esito != null){
			if(document.modificaTrattativa.trattattivaSingola_esito.value == "nuova commessa"){
				modifica = document.modificaTrattativa.trattattivaSingola_esito.value;
				inserimentoCommessaTrattativa = true;
			}
		}
		
		if(document.modificaTrattativa.esito != null){
			if(document.modificaTrattativa.esito.value == "nuova commessa"){
				modifica = document.modificaTrattativa.esito.value;
				inserimentoCommessaTrattativa = true;
			}
		}
		
		if(modifica == "nuova commessa"){
			document.getElementById("creazioneCommessa").style.display = "block";
		}else{
			document.getElementById("creazioneCommessa").style.display = "none";
			inserimentoCommessaTrattativa = false;
		}
	}
}

/*
 * con questa azzero i campo esito presente nella sezione trattativa al
 * momento dell'inserimento
 */


function azzeramentoCampoEsito(){
	if(document.inserisciTrattative.trattattivaSingola_esito.value == "aperta" 
		|| document.inserisciTrattative.trattattivaSingola_esito.value == "persa" 
		&& document.inserisciTrattative.esito.value == "nuova Commessa"){
		document.inserisciTrattative.esito.value = "aperta";
		inserimentoCommessaTrattativa = false;
	}
	if(document.inserisciTrattative.esito.value == "aperta" 
		|| document.inserisciTrattative.esito.value == "persa" 
		&& document.inserisciTrattative.trattattivaSingola_esito.value == "nuova Commessa"){
		document.inserisciTrattative.trattattivaSingola_esito.value = "aperta";
		inserimentoCommessaTrattativa = false;
	}
}
 
 /*
  * tramite questo metodo effettuo il controllo sulle rispettive tipologie di trattative 
  * al momento dell'inserimento.
  */
 
 function controlloInserimentoTrattativa(){
		
		/* 
		 * variabili della trattativa
		 */
		var cliente = "";
		var risorsa = "";
		var data = "";
		var oggetto = "";
		
		/*
		 * variabile della commessa
		 */
		
		var dataOfferta = "";
		var oggettoOfferta = "";
		var descrizione = "";
		var dataInizio = "";
		var dataFine = "";
		var importo = "";
		var ore = "";
		
		/*
		 * qua inizia la trattattiva singolas
		 */
		if(tipologiaTrattativa == "trattattivaSingola"){
			if(document.inserisciTrattative.trattattivaSingola_codice != undefined){
				cliente = document.inserisciTrattative.trattattivaSingola_codice.value;
				if(cliente == null || cliente == ""){
					alert("Valorizzare il campo \"Cliente\"");
					return false;
				}
			}
			
			if(document.inserisciTrattative.trattattivaSingola_risorsa != undefined){
				risorsa = document.inserisciTrattative.trattattivaSingola_risorsa.value;
				if(risorsa == null || risorsa == ""){
					alert("Valorizzare il campo \"Risorsa\"");
					return false;
				}
			}
			
			if(document.inserisciTrattative.trattattivaSingola_data != undefined){
				data = document.inserisciTrattative.trattattivaSingola_data.value;
				if(data == null || data == ""){
					alert("Valorizzare il campo \"Data\"");
					return false;
				}else{
					if(data.length < 10 || data.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(data))
						{
							alert("Formato della \"Data\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.inserisciTrattative.trattattivaSingola_oggetto != undefined){
				oggetto = document.inserisciTrattative.trattattivaSingola_oggetto.value;
				if(oggetto == null || oggetto == ""){
					alert("Valorizzare il campo \"Oggetto\"");
					return false;
				}
			}
		}
		
		/*
		 * qua inizia la trattativa multipla
		 */
		if(tipologiaTrattativa == "trattattivaMultipla"){
			if(document.inserisciTrattative.codice != undefined){
				cliente = document.inserisciTrattative.codice.value;
				if(cliente == null || cliente == ""){
					alert("Valorizzare il campo \"Cliente\"");
					return false;
				}
			}
			
			if(document.inserisciTrattative.data != undefined){
				data = document.inserisciTrattative.data.value;
				if(data == null || data == ""){
					alert("Valorizzare il campo \"Data\"");
					return false;
				}else{
					if(data.length < 10 || data.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(data))
						{
							alert("Formato della \"Data\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.inserisciTrattative.oggetto != undefined){
				oggetto = document.inserisciTrattative.oggetto.value;
				if(oggetto == null || oggetto == ""){
					alert("Valorizzare il campo \"Oggetto\"");
					return false;
				}
			}
		}
		 
		//sezione commessa
		
		if(inserimentoCommessaTrattativa){
			if(document.inserisciTrattative.dataOfferta != undefined){
				dataOfferta = document.inserisciTrattative.dataOfferta.value;
				if(dataOfferta == null || dataOfferta == ""){
					alert("Valorizzare il campo \"Data Offerta\"");
					return false;
				}else{
					if(dataOfferta.length < 10 || dataOfferta.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataOfferta))
						{
							alert("Formato della \"Data Offerta\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.inserisciTrattative.oggettoOfferta != undefined){
				oggettoOfferta = document.inserisciTrattative.oggettoOfferta.value;
				if(oggettoOfferta == null || oggettoOfferta == ""){
					alert("Valorizzare il campo \"Oggetto Offerta\"");
					return false;
				}
			}
			
			if(document.inserisciTrattative.descrizione != undefined){
				descrizione = document.inserisciTrattative.descrizione.value;
				if(descrizione == null || descrizione == ""){
					alert("Valorizzare il campo \"Descrizione\"");
					return false;
				}
			}
			
			if(document.inserisciTrattative.dataInizio != undefined){
				dataInizio = document.inserisciTrattative.dataInizio.value;
				if(dataInizio == null || dataInizio == ""){
					alert("Valorizzare il campo \"Data Inizio\"");
					return false;
				}else{
					if(dataInizio.length < 10 || dataInizio.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataInizio))
						{
							alert("Formato della \"Data Inizio\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.inserisciTrattative.dataFine != undefined){
				dataFine = document.inserisciTrattative.dataFine.value;
				if(dataFine == null || dataFine == ""){
					alert("Valorizzare il campo \"Data Fine\"");
					return false;
				}else{
					if(dataFine.length < 10 || dataFine.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataFine))
						{
							alert("Formato della \"Data Fine\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.inserisciTrattative.importo != undefined){
				importo = document.inserisciTrattative.importo.value;
				if(importo == null || importo == ""){
					alert("Valorizzare il campo \"Importo\"");
					return false;
				}else{
					if(isNaN(importo)){
						alert("Formato del campo \"Importo\" errato. Inserire solo valori numerici.");
						return false;
					}
				}
			}
			
			if(document.inserisciTrattative.ore != undefined){
					ore = document.inserisciTrattative.ore.value;
					if(isNaN(ore)){
						alert("Formato del campo \"Ore\" errato. Inserire solo valori numerici.");
						return false;
					}
			}
			
		}
		
	return true;
	
}
  
  
 /*
 * tramite questo metodo effettuo il controllo sulle rispettive tipologie di trattative 
 * al momento della modifica.
 */

function controlloModificaTrattativa(tipologia){
		
		/* 
		 * variabili della trattativa
		 */
		 
		var cliente = "";
		var risorsa = "";
		var data = "";
		var oggetto = "";
		
		/*
		 * variabile della commessa
		 */
		
		var dataOfferta = "";
		var oggettoOfferta = "";
		var descrizione = "";
		var dataInizio = "";
		var dataFine = "";
		var importo = "";
		var ore = "";
		
		/*
		 * qua inizia la trattattiva singolas
		 */
		if(tipologia == "1"){
			if(document.modificaTrattativa.trattattivaSingola_codice != undefined){
				cliente = document.modificaTrattativa.trattattivaSingola_codice.value;
				if(cliente == null || cliente == ""){
					alert("Valorizzare il campo \"Cliente\"");
					return false;
				}
			}
			
			if(document.modificaTrattativa.trattattivaSingola_risorsa != undefined){
				risorsa = document.modificaTrattativa.trattattivaSingola_risorsa.value;
				if(risorsa == null || risorsa == ""){
					alert("Valorizzare il campo \"Risorsa\"");
					return false;
				}
			}
			
			if(document.modificaTrattativa.trattattivaSingola_data != undefined){
				data = document.modificaTrattativa.trattattivaSingola_data.value;
				if(data == null || data == ""){
					alert("Valorizzare il campo \"Data\"");
					return false;
				}
			}
			
			if(document.modificaTrattativa.trattattivaSingola_oggetto != undefined){
				oggetto = document.modificaTrattativa.trattattivaSingola_oggetto.value;
				if(oggetto == null || oggetto == ""){
					alert("Valorizzare il campo \"Oggetto\"");
					return false;
				}
			}
		}
		
		/*
		 * qua inizia la trattativa multipla
		 */
		if(tipologia == "2"){
			if(document.modificaTrattativa.codice != undefined){
				cliente = document.modificaTrattativa.codice.value;
				if(cliente == null || cliente == ""){
					alert("Valorizzare il campo \"Cliente\"");
					return false;
				}
			}
			
			if(document.modificaTrattativa.data != undefined){
				data = document.modificaTrattativa.data.value;
				if(data == null || data == ""){
					alert("Valorizzare il campo \"Data\"");
					return false;
				}
			}
			
			if(document.modificaTrattativa.oggetto != undefined){
				oggetto = document.modificaTrattativa.oggetto.value;
				if(oggetto == null || oggetto == ""){
					alert("Valorizzare il campo \"Oggetto\"");
					return false;
				}
			}
		}
		if(inserimentoCommessaTrattativa){
			if(document.modificaTrattativa.dataOfferta != undefined){
				dataOfferta = document.modificaTrattativa.dataOfferta.value;
				if(dataOfferta == null || dataOfferta == ""){
					alert("Valorizzare il campo \"Data Offerta\"");
					return false;
				}else{
					if(dataOfferta.length < 10 || dataOfferta.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataOfferta))
						{
							alert("Formato della \"Data Offerta\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.modificaTrattativa.oggettoOfferta != undefined){
				oggettoOfferta = document.modificaTrattativa.oggettoOfferta.value;
				if(oggettoOfferta == null || oggettoOfferta == ""){
					alert("Valorizzare il campo \"Oggetto Offerta\"");
					return false;
				}
			}
			
			if(document.modificaTrattativa.descrizione != undefined){
				descrizione = document.modificaTrattativa.descrizione.value;
				if(descrizione == null || descrizione == ""){
					alert("Valorizzare il campo \"Descrizione\"");
					return false;
				}
			}
			
			if(document.modificaTrattativa.dataInizio != undefined){
				dataInizio = document.modificaTrattativa.dataInizio.value;
				if(dataInizio == null || dataInizio == ""){
					alert("Valorizzare il campo \"Data Inizio\"");
					return false;
				}else{
					if(dataInizio.length < 10 || dataInizio.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataInizio))
						{
							alert("Formato della \"Data Inizio\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.modificaTrattativa.dataFine != undefined){
				dataFine = document.modificaTrattativa.dataFine.value;
				if(dataFine == null || dataFine == ""){
					alert("Valorizzare il campo \"Data Fine\"");
					return false;
				}else{
					if(dataFine.length < 10 || dataFine.length > 10){
						alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
						return false;
					}else{
						var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
						if (!espressione.test(dataFine))
						{
							alert("Formato della \"Data Fine\" è errato. Il formato corretto è gg-mm-yyyy");
							return false;
						}
					}
				}
			}
			
			if(document.modificaTrattativa.importo != undefined){
				importo = document.modificaTrattativa.importo.value;
				if(importo == null || importo == ""){
					alert("Valorizzare il campo \"Importo\"");
					return false;
				}else{
					if(isNaN(importo)){
						alert("Formato del campo \"Importo\" errato. Inserire solo valori numerici.");
						return false;
					}
				}
			}
			
			if(document.modificaTrattativa.ore != undefined){
				ore = document.modificaTrattativa.ore.value;
				if(isNaN(ore)){
					alert("Formato del campo \"Ore\" errato. Inserire solo valori numerici.");
					return false;
				}
			}
		}
		
	return true;
	
}
 
 
 
/*
 * con questa funzione premetto di visualizzare una trattattiva piuttosto che un altra.
 * Tutto questo avviente nella sezione Trattattive.
 */ 
 
function sceltaTrattattiva(valore){
	
	
	if(valore == "1"){
		document.getElementById("trattattivaMultipla").style.display = "none";
		document.getElementById("trattattivaSingola").style.display = "block";
		document.getElementById("bottoni").style.display = "block";
		if(document.getElementById("creazioneCommessa").style.display == "block"){
			document.getElementById("creazioneCommessa").style.display = "none";
		}
		tipologiaTrattativa = "trattattivaSingola";
		azzeramentoCampoEsito();
	}else if(valore == "2"){
		document.getElementById("trattattivaMultipla").style.display = "block";
		document.getElementById("trattattivaSingola").style.display = "none";
		document.getElementById("bottoni").style.display = "block";
		if(document.getElementById("creazioneCommessa").style.display == "block"){
			document.getElementById("creazioneCommessa").style.display = "none";
		}
		tipologiaTrattativa = "trattattivaMultipla";
		azzeramentoCampoEsito();
	}else{
		document.getElementById("trattattivaMultipla").style.display = "none";
		document.getElementById("trattattivaSingola").style.display = "none";
		document.getElementById("bottoni").style.display = "none";
		if(document.getElementById("creazioneCommessa").style.display == "block"){
			document.getElementById("creazioneCommessa").style.display = "none";
		}
		tipologiaTrattativa = "";
	}
	
}


function modificaTrattamentoCommessa(){
	var inserisci = document.inserisciTrattative.esito.value;
	if(inserisci == "nuovaCommessa"){
		document.getElementById("nuovaCommessa").style.display = "block";
	}else{
		if(document.getElementById("nuovaCommessa").style.display == "block"){
			document.getElementById("nuovaCommessa").style.display = "none";
		}
	}
}


/* 
 * questa funzione viene richiamata quando l'utente cerca di inserire una commessa. 
 */
function tipologia(tipologiaScelta){
	
	if(tipologiaScelta == "0"){
		document.getElementById("commessaEsternaMultipla").style.display = "none";
		document.getElementById("commessaEsternaSingola").style.display = "none";
		document.getElementById("commessaInterna").style.display = "none";
		document.getElementById("altro").style.display = "none";
	}else if(tipologiaScelta == "1"){
		document.getElementById("commessaEsternaSingola").style.display = "block";
		document.getElementById("commessaEsternaMultipla").style.display = "none";
		document.getElementById("commessaInterna").style.display = "none";
		document.getElementById("altro").style.display = "none";
	}else if(tipologiaScelta == "2"){
		document.getElementById("commessaEsternaMultipla").style.display = "block";
		document.getElementById("commessaEsternaSingola").style.display = "none";
		document.getElementById("commessaInterna").style.display = "none";
		document.getElementById("altro").style.display = "none";
	}else if(tipologiaScelta == "3"){
		document.getElementById("commessaInterna").style.display = "block";
		document.getElementById("commessaEsternaMultipla").style.display = "none";
		document.getElementById("commessaEsternaSingola").style.display = "none";
		document.getElementById("altro").style.display = "none";
	}else if(tipologiaScelta == "4"){
		document.getElementById("altro").style.display = "block";
		document.getElementById("commessaEsternaMultipla").style.display = "none";
		document.getElementById("commessaEsternaSingola").style.display = "none";
		document.getElementById("commessaInterna").style.display = "none";
	}
	
}
 
function cancellaCampoTextArea(campoForm,valore){
	
	/*
	 * in questa sezione cancello la textarea presente in Trattative 
	 * al momento dell'inserimento di essa.
	 */
	if(campoForm == "inserisciTrattativeNote" && document.inserisciTrattative.commessaEsternaSingola_note.value == "Inserisci la descrizione"){
		document.inserisciTrattative.commessaEsternaSingola_note.value = "";
	}
	
	/*
	 * in questa sezione cancello la textarea presente in Trattative 
	 * al momento della modifica di essa.
	 */
	if(campoForm == "modificaTrattativeNote" && document.modificaTrattativa.commessaEsternaSingola_note.value == "Inserisci la descrizione"){
		document.modificaTrattativa.commessaEsternaSingola_note.value = "";
	}
	
	/*
	 * in questa sezione cancello la textarea presente al momento 
	 * dell'inserimento delle varie tipologie di commesse.
	 */
	if(campoForm == "inserisciCommessa"){
		if(valore == "multiple" && document.commessa.note.value == "Inserisci la descrizione"){
			document.commessa.note.value = "";
		}else if(valore == "singola" && document.commessa.commessaEsternaSingola_note.value == "Inserisci la descrizione"){
			document.commessa.commessaEsternaSingola_note.value = "";
		}else if(valore == "interna" && document.commessa.commessaInterna_note.value == "Inserisci la descrizione"){
			document.commessa.commessaInterna_note.value = "";
		}else if(valore == "altro" && document.commessa.altro_note.value == "Inserisci la descrizione" ){
			document.commessa.altro_note.value = "";
		}
	}
	
	if(campoForm == "inserisciCompetenze"){
		if(valore == "capacitaCompetenzeRelazionali" && document.competenze.capacitaCompetenzeRelazionali.value == "Inserisci testo"){
			document.competenze.capacitaCompetenzeRelazionali.value = "";
		}
		if(valore == "capacitaCompetenzeOrganizzative" && document.competenze.capacitaCompetenzeOrganizzative.value == "Inserisci testo"){
			document.competenze.capacitaCompetenzeOrganizzative.value = "";
		}
		if(valore == "capacitaCompetenzeTecniche" && document.competenze.capacitaCompetenzeTecniche.value == "Inserisci testo"){
			document.competenze.capacitaCompetenzeTecniche.value = "";
		}
		if(valore == "altreCapacitaCompetenze" && document.competenze.altreCapacitaCompetenze.value == "Inserisci testo"){
			document.competenze.altreCapacitaCompetenze.value = "";
		}
		if(valore == "ulterioriInformazioni" && document.competenze.ulterioriInformazioni.value == "Inserisci testo"){
			document.competenze.ulterioriInformazioni.value = "";
		}
	}
	
	if(campoForm == "modificaCompetenze"){
		if(valore == "capacitaCompetenzeRelazionali" && document.altreCompetenze.capacitaCompetenzeRelazionali.value == "Inserisci testo"){
			document.altreCompetenze.capacitaCompetenzeRelazionali.value = "";
		}
		if(valore == "capacitaCompetenzeOrganizzative" && document.altreCompetenze.capacitaCompetenzeOrganizzative.value == "Inserisci testo"){
			document.altreCompetenze.capacitaCompetenzeOrganizzative.value = "";
		}
		if(valore == "capacitaCompetenzeTecniche" && document.altreCompetenze.capacitaCompetenzeTecniche.value == "Inserisci testo"){
			document.altreCompetenze.capacitaCompetenzeTecniche.value = "";
		}
		if(valore == "altreCapacitaCompetenze" && document.altreCompetenze.altreCapacitaCompetenze.value == "Inserisci testo"){
			document.altreCompetenze.altreCapacitaCompetenze.value = "";
		}
		if(valore == "ulterioriInformazioni" && document.altreCompetenze.ulterioriInformazioni.value == "Inserisci testo"){
			document.altreCompetenze.ulterioriInformazioni.value = "";
		}
	}
	
	if(campoForm == "modificaInserimentoCompetenze"){
		if(valore == "capacitaCompetenzeRelazionali" && document.modificaInserimentoCompetenze.capacitaCompetenzeRelazionali.value == "Inserisci testo"){
			document.modificaInserimentoCompetenze.capacitaCompetenzeRelazionali.value = "";
		}
		if(valore == "capacitaCompetenzeOrganizzative" && document.modificaInserimentoCompetenze.capacitaCompetenzeOrganizzative.value == "Inserisci testo"){
			document.modificaInserimentoCompetenze.capacitaCompetenzeOrganizzative.value = "";
		}
		if(valore == "capacitaCompetenzeTecniche" && document.modificaInserimentoCompetenze.capacitaCompetenzeTecniche.value == "Inserisci testo"){
			document.modificaInserimentoCompetenze.capacitaCompetenzeTecniche.value = "";
		}
		if(valore == "altreCapacitaCompetenze" && document.modificaInserimentoCompetenze.altreCapacitaCompetenze.value == "Inserisci testo"){
			document.modificaInserimentoCompetenze.altreCapacitaCompetenze.value = "";
		}
		if(valore == "ulterioriInformazioni" && document.modificaInserimentoCompetenze.ulterioriInformazioni.value == "Inserisci testo"){
			document.modificaInserimentoCompetenze.ulterioriInformazioni.value = "";
		}
	}
	
}



function controlloDateReport(){
	
	var dataInizio = document.report.dtDa.value;
	var dataFine = document.report.dtA.value;
	var tipologiaReport = document.report.tipologiaReport.value;
	
	var slipt_DataInizio = dataInizio.split('-');
	var split_DataFine = dataFine.split('-');
	
	var dateInizio = new Date(slipt_DataInizio[2],slipt_DataInizio[1],slipt_DataInizio[0]).getTime();
	var dateFine = new Date(split_DataFine[2],split_DataFine[1],split_DataFine[0]).getTime();
	
	
	if(dataInizio == ""){
		alert("Valorizzare il campo \"Da\"");
		return false;
	}
	if(dataFine == ""){
		alert("Valorizzare il campo \"A\"");
		return false;
	}
	
	if(dateInizio > dateFine){
		alert("Data Inizio maggiore della Data Fine");
		return false;
	}
	
	if(tipologiaReport == ""){
		alert("Valorizzare la tipologia Report");
		return false;
	}
	
	return true;
	

}

function controlloSessioneAttiva(){
	alert("La sessione è scaduta. Rieffettuare la login");
	url = window.location.href;
	var variabiliUrl = url.split("/");
	for(var a=0; a < variabiliUrl.length; a++){
			if(a == 2){
				var localVariabili = "";
				if(variabiliUrl[a].indexOf(":")){
					localVariabili = variabiliUrl[a].split(":");
				}
				
				for(var x=0; x < localVariabili.length; x++){
					if(localVariabili[x] == "localhost"){
						window.location = "http://localhost/dr";
					}if(localVariabili[x] == "cvonline.tv"){
						window.location.href = "http://cvonline.tv";
					}if(localVariabili[x] == "drconsulting.tv"){
						window.location.href= "http://drconsulting.tv";
					}
				}
			}else{
				continue;
			}
	}
}


function controlloDateAssociazioni(){
	
	var dataInizio = document.caricaAssociazione.dataInizio.value;
	var dataFine = document.caricaAssociazione.dataFine.value;
	var importo = document.caricaAssociazione.importo.value;
	
	var split_DataInizio = dataInizio.split("-");
	var split_DataFine = dataFine.split("-");

	var dateInizio = new Date(split_DataInizio[2],split_DataInizio[1],split_DataInizio[0]).getTime();
	var dateFine = new Date(split_DataFine[2],split_DataFine[1],split_DataFine[0]).getTime();
	
	if(dataInizio == null || dataInizio == ""){
		alert("Valorizzare il campo \"Data Inizio\"");
		return false;
	}else{
		if(dataInizio.length < 10 || dataInizio.length > 10){
			alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
			return false;
		}else{
			var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
			if (!espressione.test(dataInizio))
			{
				alert("Formato della \"Data Inizio\" è errato. Il formato corretto è gg-mm-yyyy");
				return false;
			}
		}
	}
	
	if(dataFine == null || dataFine == ""){
		alert("Valorizzare il campo \"Data Fine\"");
		return false;
	}else{
		if(dataFine.length < 10 || dataFine.length > 10){
			alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
			return false;
		}else{
			var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
			if (!espressione.test(dataFine))
			{
				alert("Formato della \"Data Fine\" è errato. Il formato corretto è gg-mm-yyyy");
				return false;
			}
		}
	}
	
	if(importo == null || importo == ""){
		alert("Valorizzare il campo \"Importo\"");
		return false;
	}
	
	if(dateInizio > dateFine){
		alert("Data Inizio maggiore della Data Fine");
		return false;
	}
	
	return true;
}

function controlloDataCommessa(){
	
	var dataInizio = document.modificaDissociazioneRisorsaCommessa.dataInizio.value;
	var dataFine = document.modificaDissociazioneRisorsaCommessa.dataFine.value;
	
	var split_DataInizio = dataInizio.split("-");
	var split_DataFine = dataFine.split("-");

	var dateInizio = new Date(split_DataInizio[0],split_DataInizio[1],split_DataInizio[2]).getTime();
	var dateFine = new Date(split_DataFine[2],split_DataFine[1],split_DataFine[0]).getTime();
	
	if(dataFine == null || dataFine == ""){
		alert("Valorizzare il campo \"Data Fine\"");
		return false;
	}else{
		if(dataFine.length < 10 || dataFine.length > 10){
			alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
			$("#dataFine").css("background","#ff0505");
			document.getElementById("modificaAssociazione").setAttribute("disabled","disabled");
			return false;
		}else{
			var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
			if (!espressione.test(dataFine))
			{
				alert("Formato della \"Data Fine\" è errato. Il formato corretto è gg-mm-yyyy");
				$("#dataFine").css("background","#ff0505");
				document.getElementById("modificaAssociazione").setAttribute("disabled","disabled");
				return false;
			}
		}
	}
	
	if(dateInizio > dateFine){
		alert("Data Inizio maggiore della Data Fine");
		return false;
	}
	
	return true;
}


/*
function controlloValorizzazioneRisorsa(tipologia){
	if(tipologia == "1"){
		var clienteCommessaMultipla = document.commessa.codice.value;
		if(clienteCommessaMultipla == ""){
			alert("valorizzare il campo Cliente");
			return false;
		}
	}else if(tipologia == "2"){
		var clienteCommessaSingola = document.commessa.commessaEsternaSingola_codice.value;
		var risorsaCommessaSingola = document.commessa.commessaEsternaSingola_idRisorsa.value;
		
		if(clienteCommessaSingola == ""){
			alert("valorizzare il campo Cliente");
			return false;
		}
		if(risorsaCommessaSingola == ""){
			alert("valorizzare il campo Risorsa");
			return false;
		}
		
	}
	return true;
}*/
