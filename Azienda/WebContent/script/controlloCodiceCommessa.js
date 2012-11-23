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

function controlloCodiceCommessa(parametro){
	if(parametro != ""){
		var url="./GestioneCommessa?azione=controlloCodiceCommessa&codiceCommessa=" + parametro;
		xmlHttp.open("GET", url , true);
		xmlHttp.send(null);
		setTimeout('stateChange()',1000);
	}else{
		alert("Valorizzare il campo Codice Commessa");
	}
}
	
function stateChange() {
	
	if(xmlHttp.readyState == 4) {
		//Stato OK
		if (xmlHttp.status == 200) {
			var resp = xmlHttp.responseText;
			if(resp == "true") {
				alert("Codice Commessa già presente");
				$("#codiceCommessa").css("background","#ff6666");
				$("#inserisciTrattativa").attr("disabled","disabled");
			}else{
				$("#codiceCommessa").css("background","#4ed955");
				$("#inserisciTrattativa").removeAttr("disabled");
			}
		} else {
			alert(xmlHttp.responseText);
		}
	}
}
	