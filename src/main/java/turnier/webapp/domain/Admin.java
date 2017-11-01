package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class Admin {
	
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

}
