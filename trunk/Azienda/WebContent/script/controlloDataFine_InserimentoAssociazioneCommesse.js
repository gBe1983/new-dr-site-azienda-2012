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

function controlloDataCommessa(parametro,data){
	
	if(data.length < 10 || data.length > 10){
		alert("La lunghezza della data � errata. La lunghezza predefinita � di 10 caratteri");
		$("#dataFine").css("background","#ff0505");
		document.getElementById("inserisciAssociazione").setAttribute("disabled","disabled");
	}else{
		var espressione = /^(0[1-9]|[12][0-9]|3[01])[- \/.](0[1-9]|1[012])[- \/.](19|20)\d\d$/;
		if (!espressione.test(data))
		{
			alert("Formato della \"Data Nascita\" � errato. Il formato corretto � gg-mm-yyyy");
			$("#dataFine").css("background","#ff0505");
			document.getElementById("inserisciAssociazione").setAttribute("disabled","disabled");
		}else{
			var url="./GestioneCommessa?azione=controlloDataCommessa&parametro=" + parametro + "&data=" + data;
			   xmlHttp.open("GET", url , true);
			   xmlHttp.send(null);
			   setTimeout('stateChangeInserimento()',1000);
		}
	}
}
	
function stateChangeInserimento() {
	
	if(xmlHttp.readyState == 4) {
		//Stato OK
		if (xmlHttp.status == 200) {
			var resp = xmlHttp.responseText;
			
			
			//recupero il name dell'oggetto della select Risorsa
			
				var valori = resp.split(",");
				
				var data = valori[0];
				var esito = valori[1];
			
				if(esito == "true") {
					$("#dataFine").css("background","#2cb633");
					$("#inserisciAssociazione").removeAttr("disabled");
				}else{
					document.getElementById("inserisciAssociazione").setAttribute("disabled","disabled");
					$("#dataFine").css("background","#ff0505");
					alert("La data fine � maggiore della data fine della commessa " + data);
				}
		} else {
			alert(xmlHttp.responseText);
		}
	}
}
	