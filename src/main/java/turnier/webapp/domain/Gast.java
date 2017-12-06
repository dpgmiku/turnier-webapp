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

public SpielerStatistik[] StatistikenAnsehen() {
	
	return null;
}

/** Spielername {0} ist zu kurz (<3 Zeichen) oder zu lang (>255 Zeichen*/
@SuppressWarnings("serial")
public static class SpielerNameNotAllowedExc extends multex.Exc {}

/** Turnier {0} existiert nicht*/
@SuppressWarnings("serial")
public static class TurnierGibtEsNichtExc extends multex.Exc {}

/** Spielername {0} gibt es schon*/
@SuppressWarnings("serial")
public static class SpielernameAlreadyTakenExc extends multex.Exc {}


}
