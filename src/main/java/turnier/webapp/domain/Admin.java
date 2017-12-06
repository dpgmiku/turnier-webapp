package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity sag uns bescheid, dass bei der Klasse JPA sich um eine JPA entity handelt
@Entity
public class Admin {
	
	
	
//JPA will recognize this property as the object ID
@Id
final String id;

public Admin(String id) {
	
	this.id = id;
}
	
public Boolean turnierVerifizieren(Turnier turnier) {
	
	return true;
	
}

public void loescheTurnier(Turnier turnier) {
	
	
}

public void loescheNutzer(Nutzer nutzer) {
	
	
}

/** Turnier {0} existiert nicht*/
@SuppressWarnings("serial")
public static class TurnierGibtEsNichtExc extends multex.Exc {}

/** Nutzer {0} existiert nicht*/
@SuppressWarnings("serial")
public static class NutzerGibtEsNichtExc extends multex.Exc {}

}


