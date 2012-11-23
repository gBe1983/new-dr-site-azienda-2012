var xmlHttp = getXmlHttpObject();

function getXmlHttpObject() {
	var xmlHttp = null;
	try {
		// Firefox, Opera 8.0+, Safari
		xmlHttp = new XMLHttpRequest();
	} catch (e) {
		// Internet Explorer
		try {
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
	return xmlHttp;
}

function valorizzazioneTrattativeRisorsa() {
	var url = "./GestioneRisorse?azione=valorizzazioneRisorsa&parametro="
			+ document.visualizzaTrattative.codice.value;
	xmlHttp.open("GET", url, true);
	xmlHttp.onreadystatechange = valorizzazioneRisorse();
	xmlHttp.send(null);
}

function valorizzazioneRisorse() {

	if (xmlHttp.readyState == 4) {
		//Stato OK
		if (xmlHttp.status == 200) {
			var resp = xmlHttp.responseText;
			
			//recupero il name dell'oggetto della select Risorsa
			var select = document.getElementById("idRisorsa");
			
			//recupero la lunghezza della select
			var numb = select.options.length;
			
			// tramite un ciclo for elimino tutte le option presenti nella select
			for ( var i = 0; i < numb; i++) {
				select.remove(0);
			}
			addOption(select, 0, "-- Seleziona la Risorsa --");
			
			var valori = resp.split(";");
			
			var lunghezzaValori = valori.length;
			
			for(var x=0; x < lunghezzaValori-1; x++){
				var val = valori[x].split(',');
				
				addOption(select, val[0], val[1] + " " + val[2]);
			}
		} else {
			alert(xmlHttp.responseText);
		}
	}
}

function addOption(select, value, text) {
	//Aggiunge un elemento <option> ad una lista <select>
		var option = document.createElement("option");
		option.value = value,
		option.text = text;
		try {
			select.add(option, null);
		} catch(e) {
			//Per Internet Explorer
			select.add(option);
		}
}