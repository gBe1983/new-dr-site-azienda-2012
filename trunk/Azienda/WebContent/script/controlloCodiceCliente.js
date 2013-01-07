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

function controlloCodiceCliente(parametro){
	var url="./GestioneCliente?azione=controlloCodiceCliente&codiceCliente=" + parametro;
	xmlHttp.open("GET", url , true);
	xmlHttp.send(null);
	setTimeout('stateChanged()',1000);
}
	
function stateChanged() {
	
	if(xmlHttp.readyState == 4) {
		//Stato OK
		if (xmlHttp.status == 200) {
			var resp = xmlHttp.responseText;
			if(resp == "true") {
				alert("Codice Cliente già presente");
				$("#codiceCliente").css("background","#ff6666");
				$("#inviaCliente").attr("disabled","disabled");
			}else{
				$("#codiceCliente").css("background","#4ed955");
				$("#inviaCliente").removeAttr("disabled");
			}
		} else {
			alert(xmlHttp.responseText);
		}
	}
}
	