function openFinestra(valore,lastMovimento,area,azione){

	if(azione == "anteprima"){
		document.anteprima.parametro.value = valore;
		document.anteprima.lastMovimento.value = lastMovimento;
		if(area != ''){
			var input = document.createElement("input");				
			input.setAttribute("type", "hidden");
			input.setAttribute("name", "area");
			input.setAttribute("value", area);
			document.getElementById("anteprimaForm").appendChild(input);
		}
		
		
		$('#anteprima').dialog({
			modal: true,
			autoOpen: true,
			height: 250,
			width: 450,
			position: [450,150],
			show: {
				effect: "blind",
				duration: 1000
			},
			hide: {
				effect: "explode",
				duration: 1000
			}
		});
		return false;
	}else{
		
		document.pdf.parametro.value = valore;
		document.pdf.lastMovimento.value = lastMovimento;
		document.pdf.area.value = area;
		
		$('#finestra').dialog({
			modal: true,
			autoOpen: true,
			height: 550,
			width: 450,
			position: [500,80],
			show: {
				effect: "blind",
				duration: 1000
			},
			hide: {
				effect: "explode",
				duration: 1000
			}
		});
		return false;
	}
}

/*
 * function openFinestraAnteprima(valore,lastMovimento,area){
	
	alert("sono dentro");
	
	document.anteprima.parametro.value = valore;
	document.anteprima.lastMovimento.value = lastMovimento;
	document.anteprima.area.value = area;
	
	alert(document.anteprima.parametro.value);
	
	$('#anteprima').dialog({
		modal: true,
		autoOpen: true,
		height: 250,
		width: 450,
		position: [450,150]
	});
	return false;
}

function openFinestraEsporta(valore,lastMovimento,area){
	
	document.pdf.parametro.value = valore;
	document.pdf.lastMovimento.value = lastMovimento;
	document.pdf.area.value = area;
	
	$('#finestra').dialog({
		modal: true,
		autoOpen: true,
		height: 550,
		width: 450,
		position: [500,80]
	});
	return false;
}*/

function sceltaRisorsaCurriculum(){
	
	var valore = document.sceltaRisorsa.parametro.value;
	return valore;
}

function closeWindows(){
	
	if(controlloInvioEmailConAllegato()){
		$("#finestra").dialog("close");
	}else{
		return false;
	}
}

function closeWindowsAnteprima(){
	$("#anteprima").dialog("close");
}

function closeFinestra(){
	$("#finestra").dialog("close");
	return false;
}

function closeFinestraAnteprima(){
	$("#anteprima").dialog("close");
	return false;
}

function controlloInvioEmailConAllegato(){
	
	if(document.getElementById("modulo").style.display == "block"){
		
		if(document.pdf.destinatario.value == ""){
			alert("Inserire un valore nel campo \"Destinatario\"");
			return false;
		}
		
		if(document.pdf.oggetto.value == ""){
			alert("Inserire un valore nel campo \"Oggetto\"");
			return false;
		}
		
		if(document.pdf.corpo.value == "" || document.pdf.corpo.value == "Inserisci Testo"){
			alert("Inserire un valore nel campo \"Corpo\"");
			return false;
		}
	}
	
	return true;
	
}


function visualizzazione(valore){
	
	alert(valore);
	if(valore == "europeo"){
		$(".formato").css("display","none");
		/*document.getElementById("formato").style.display = "none";*/
	}else if(valore == "aziendale"){
		alert("sono dentro");
		$(".formato").css("display","block");
		/*document.getElementById("formato").style.display = "block";*/
	}
}

function invioEmail(valore){
	if(valore == "si"){
		document.getElementById("modulo").style.display = "block";
	}else if(valore == "no"){
		document.getElementById("modulo").style.display = "none";
	}
}


/*
 * tramite questo metodo controllo le scelte che vengono effettuate
 * nella pagina visualizzaCurriculum.jsp
 */

function controlloSceltaVisualizzaCurriculum(){
	
	var formCheckbox = document.getElementById("curriculum");
	var scelta = document.sceltaCurriculum.scelta.value;
	
	var contatoreSelezioneCheckBox = 0;
	var valoriCheckBox = "";
	var nomeCheckBox = "";
	
	for (var i=0;i< formCheckbox.elements.length;i++){
	      if(formCheckbox.elements[i].checked){
	    	 nomeCheckBox += "parametro"+contatoreSelezioneCheckBox + ",";
	    	 valoriCheckBox += formCheckbox.elements[i].value + ",";
	    	 contatoreSelezioneCheckBox++;
	      }
	}
	
	
	if(scelta ==  ""){
		alert("Effettuare almeno una scelta.");
		return false;
	}else{
		if(scelta == "aggiungi"){
			if(contatoreSelezioneCheckBox > 0){
				alert("Per questa tipo di operazione non è necessario selezionare un Curriculum.");
				return false;
			}else{
				document.sceltaCurriculum.azione.value="selezionaRisorsa";
			}
		}else if(scelta == "modifica"){
			if(contatoreSelezioneCheckBox == 0){
				alert("Selezionare un Curriculum Vitae.");
				return false;
			}else{
				if(contatoreSelezioneCheckBox > 1){
					alert("Selezionare un solo Curriculum");
					return false;
				}else{
					document.sceltaCurriculum.azione.value="caricamentoCv";
					var nomiCampi = nomeCheckBox.split(",");
					var valoriCampi = valoriCheckBox.split(",");
					
					var input = document.createElement("input");				
					input.setAttribute("type", "hidden");
					input.setAttribute("name", nomiCampi[0]);
					input.setAttribute("value", valoriCampi[0]);
					document.getElementById("sceltaCurriculum").appendChild(input);
				}
			}
		}else if(scelta == "anteprima") {
			if(contatoreSelezioneCheckBox == 0){
				alert("Selezionare un Curriculum Vitae.");
				return false;
			}else{
				if(contatoreSelezioneCheckBox > 1){
					alert("Per questo di operazione è possibile selezionare un solo Curriculum");
					return false;
				}else{
					
					document.sceltaCurriculum.azione.value="anteprimaGlobale";
					var nomiCampiAnteprima = nomeCheckBox.split(",");
					var valoriCampiAnteprima = valoriCheckBox.split(",");
					
					openFinestra(valoriCampiAnteprima[0],'visualizzaCurriculum','all','anteprima');
					return false;
					
					/*
					
					
					var inputAnteprima = document.createElement("input");				
					inputAnteprima.setAttribute("type", "hidden");
					inputAnteprima.setAttribute("name", nomiCampiAnteprima[0]);
					inputAnteprima.setAttribute("value", valoriCampiAnteprima[0]);
					document.getElementById("sceltaCurriculum").appendChild(inputAnteprima);
					*/
				}
			}
		}else if(scelta == "elimina"){
			if(contatoreSelezioneCheckBox == 0){
				alert("Selezionare un Curriculum Vitae.");
				return false;
			}else{
				if(confirm("Sei sicuro di voler eliminare i Curriculum Vitae?")){
					document.sceltaCurriculum.azione.value="eliminazioneGlobale";
					
					var nomiCampiElimina = nomeCheckBox.split(",");
					var valoriCampiElimina = valoriCheckBox.split(",");
					
					//in questo punto carico i campi hidden per quanti sono i campi selezionati
					for(var x = 0; x < nomiCampiElimina.length; x++){
						var inputElimina = document.createElement("input");				
						inputElimina.setAttribute("type", "hidden");
						inputElimina.setAttribute("name", nomiCampiElimina[x]);
						inputElimina.setAttribute("value", valoriCampiElimina[x]);
						document.getElementById("sceltaCurriculum").appendChild(inputElimina);
					}
					
					//in questo punto carico i campi hidden per l'indice
					var inputIndice = document.createElement("input");				
					inputIndice.setAttribute("type", "hidden");
					inputIndice.setAttribute("name", "indice");
					inputIndice.setAttribute("value", contatoreSelezioneCheckBox);
					document.getElementById("sceltaCurriculum").appendChild(inputIndice);
				}
			}
		}
	}

	return true;
}


function controlloSceltaIntestazione(){
	
	/*
	 * recupero i valori del menu a tendina e l'id per effettuare l'operazione
	 */
	
	var scelta = document.intestazione.scelta.value;
	
	if(scelta == ""){
		alert("Selezionare un'azione");
		return false;
	}
	
	/*
	 * carico il valore recuperato nel campo nascosto
	 */
	
	document.intestazione.azione.value = scelta;
	
	return true;
}

function controlloSceltaEsperienze(){
	
	var scelta = document.sceltaEsperienza.scelta.value;
	var parametroId = document.sceltaEsperienza.parametro.value;
	var numeroSelectExp = document.getElementById("esperienze");
	
	var contatoreSelezioneExp = 0;
	var valoriEsperienza = "";
	var nomeCampoEsperienza = "";
	
	
	for (var i=0;i< numeroSelectExp.elements.length;i++){
	      if(numeroSelectExp.elements[i].checked){
	    	 nomeCampoEsperienza += "parametro"+contatoreSelezioneExp + ",";
	    	 valoriEsperienza += numeroSelectExp.elements[i].value + ",";
	    	 contatoreSelezioneExp++;
	      }
	}
	
	if(scelta == ""){
		alert("Selezionare un'azione")
		return false;
	}else if (scelta == "aggiungiEsperienza") {
		if(contatoreSelezioneExp > 0){
			alert("Per questa tipo di azione non necessita la selezione di esperienza.");
			return false;
		}else{
			window.location.href = "./index.jsp?azione=creaCv&sezione=esperienza&parametroId="+parametroId;
			return false;
		}
	}else if(scelta == "modificaEsperienza"){
		if(contatoreSelezioneExp == 0){
			alert("Per questa tipo di azione selezionare una Esperienza.");
			return false;
		}else if(contatoreSelezioneExp > 1){
			alert("Per questa tipo di azione è possibile selezionare una sola Esperienza.");
			return false;
		}else{
			document.sceltaEsperienza.azione.value="modificaEsperienza";
			var nomiCampi = nomeCampoEsperienza.split(",");
			var valoriCampi = valoriEsperienza.split(",");
			
			var input = document.createElement("input");				
			input.setAttribute("type", "hidden");
			input.setAttribute("name", nomiCampi[0]);
			input.setAttribute("value", valoriCampi[0]);
			document.getElementById("sceltaEsperienza").appendChild(input);
		}
	}else if(scelta == "anteprimaEsperienza"){
		if(contatoreSelezioneExp == 0){
			alert("Selezionare almeno una Esperienza.");
			return false;
		}else{
			document.sceltaEsperienza.azione.value="anteprimaEsperienza";
			
			var nomiCampiExp = nomeCampoEsperienza.split(",");
			var valoriCampiExp = valoriEsperienza.split(",");
			
			//in questo punto carico i campi hidden per quanti sono i campi selezionati
			for(var x = 0; x < nomiCampiExp.length; x++){
				var inputExp = document.createElement("input");				
				inputExp.setAttribute("type", "hidden");
				inputExp.setAttribute("name", nomiCampiExp[x]);
				inputExp.setAttribute("value", valoriCampiExp[x]);
				document.getElementById("sceltaEsperienza").appendChild(inputExp);
			}
			
			//in questo punto carico i campi hidden per l'indice
			var inputIndice = document.createElement("input");				
			inputIndice.setAttribute("type", "hidden");
			inputIndice.setAttribute("name", "indice");
			inputIndice.setAttribute("value", contatoreSelezioneExp);
			document.getElementById("sceltaEsperienza").appendChild(inputIndice);
		}
		
	}else if(scelta == "eliminaEsperienza"){
		if(contatoreSelezioneExp == 0){
			alert("Selezionare almeno una Esperienza.");
			return false;
		}else{
			if(confirm("Sei sicuro di voler eliminare l'Esperienze?")){
				document.sceltaEsperienza.azione.value="eliminaEsperienza";
				
				var nomiCampiElimina = nomeCampoEsperienza.split(",");
				var valoriCampiElimina = valoriEsperienza.split(",");
				
				
				//in questo punto carico i campi hidden per quanti sono i campi selezionati
				for(var y = 0; y < nomiCampiElimina.length; y++){
					var inputElimina = document.createElement("input");				
					inputElimina.setAttribute("type", "hidden");
					inputElimina.setAttribute("name", nomiCampiElimina[y]);
					inputElimina.setAttribute("value", valoriCampiElimina[y]);
					document.getElementById("sceltaEsperienza").appendChild(inputElimina);
				}
							
				//in questo punto carico i campi hidden per l'indice
				var inputIndex = document.createElement("input");				
				inputIndex.setAttribute("type", "hidden");
				inputIndex.setAttribute("name", "indice");
				inputIndex.setAttribute("value", contatoreSelezioneExp);
				document.getElementById("sceltaEsperienza").appendChild(inputIndex);
			}
		}
	}
	
	return true;
}


function controlloSceltaDettaglio(){
	
	var scelta = document.sceltaDettaglio.scelta.value;
	var parametro = document.sceltaDettaglio.parametro.value;
	var parametroId = document.sceltaDettaglio.parametroId.value;
	
	if(scelta == ""){
		alert("Selezionare un'azione")
		return false;
	}else if(scelta == "aggiungiDettaglio"){
		document.sceltaDettaglio.azione.value = scelta;
		window.location.href = "./index.jsp?azione=creaCv&sezione=dettaglio&parametroId="+parametro;
		return false;
	}else if(scelta == "modificaDettaglio"){
		if(parametroId == 0){
			alert("Non ci sono dettagli per questa risorsa.");
			return false;
		}else{
			document.sceltaDettaglio.azione.value = scelta;
		}
	}else if(scelta == "anteprimaDettaglio"){
		if(parametroId == 0){
			alert("Non ci sono dettagli per questa risorsa.");
			return false;
		}else{
			document.sceltaDettaglio.azione.value = scelta;
		}
	}else if(scelta == "eliminaDettaglio"){
		if(parametroId == 0){
			alert("Non ci sono dettagli per questa risorsa.");
			return false;
		}else{
			document.sceltaDettaglio.azione.value = scelta;
		}
	}
	
	return true;
}


/**
 * effettuo il controllo sui campi dell'inserimento esperienza
 * @return
 */
function controlloCampiInserimentoExp(){
	
	var annoInizio = document.inserisciEsperienza.annoInizio.value;
	var meseInizio = document.inserisciEsperienza.meseInizio.value;
	var annoFine = document.inserisciEsperienza.annoFine.value;
	var meseFine = document.inserisciEsperienza.meseFine.value;
	var azienda = document.inserisciEsperienza.azienda.value;
	var luogo = document.inserisciEsperienza.luogo.value;
	var descrizione = document.inserisciEsperienza.descrizione.value;
	
	var dateInizio = new Date(annoInizio,meseInizio,0).getTime();
	var dateFine = new Date(annoFine,meseFine,0).getTime();
	
	if(dateInizio > dateFine){
		alert("Data Inizio minore della Data Fine. Valorizzare correttamente le date.");
		return false;
	}
	
	if(azienda == ""){
		alert("Valorizzare il campo \"Azienda\".");
		return false;
	}
	
	if(luogo == ""){
		alert("Valorizzare il campo \"Luogo\".");
		return false;
	}
	
	if(descrizione == ""){
		alert("Valorizzare il campo \"Descrizione\".");
		return false;
	}
	
	return true;
}
