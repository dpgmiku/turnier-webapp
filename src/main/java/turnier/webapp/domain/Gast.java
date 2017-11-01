package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity

public class Gast {

	@Id
private String spielerName;

public Gast(String spielerName) {
	this.spielerName = spielerName;
}

public Teilnehmer nutzerKontoErstellen(String name, String vorname, String nutzername, String passwort, String email) {
	
return null;	
}

public Gast anTurnierAnmelden(String spielername, Turnier turnier) {
	
	return null;
}


}
