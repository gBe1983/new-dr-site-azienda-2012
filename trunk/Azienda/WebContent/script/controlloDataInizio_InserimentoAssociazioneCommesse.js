var xmlHttp = getXmlHttpObject();

function getXmlHttpObject(){
	  var xmlHttp=null;
  try{
	// Firefox, Opera 8.0+, Safari
	xmlHttp=new XMLHttpRequest();
  }	catch (e){
	// Internet Explorer
	  try{
		  xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
	  }catch (e){
		  xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
  }
  return xmlHttp;
}

function controlloDataInizio_Associazione(parametro,data){
	
	if(data.length < 10 || data.length > 10){
		alert("La lunghezza della data è errata. La lunghezza predefinita è di 10 caratteri");
		$("#dataInizio").css("background","#ff0505");
	}else{
		var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
		if (!espressione.test(data))
		{
			alert("Formato della \"Data Nascita\" è errato. Il formato corretto è gg-mm-yyyy");
			$("#dataInizio").css("background","#ff0505");
		}else{
			var url="./GestioneCommessa?azione=controlloDataInizioAssociazione&parametro=" + parametro + "&data=" + data;
			xmlHttp.open("GET", url , true);
			xmlHttp.send(null);
			setTimeout('stateChangeInserimentoDataInizio()',1000);
		}
	}
		
}
	
function stateChangeInserimentoDataInizio() {
	
	if(xmlHttp.readyState == 4) {
		//Stato OK
		if (xmlHttp.status == 200) {
			var resp = xmlHttp.responseText;
			
			
			//recupero il name dell'oggetto della select Risorsa
			
				var valori = resp.split(",");
				
				var data = valori[0];
				var esito = valori[1];
			
				if(esito == "true") {
					$("#dataInizio").css("background","#2cb633");
				}else{
					$("#dataInizio").css("background","#ff0505");
					alert("La data Inizio è antecedente alla data Inizio della commessa " + data);
				}
		} else {
			alert(xmlHttp.responseText);
		}
	}
}
	