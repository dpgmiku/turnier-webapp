package turnier.webapp.domain;

import javax.persistence.Entity;

import turnier.webapp.domain.base.EntityBase;

@Entity
public class Gast extends EntityBase<Gast> {

	private String spielerName;

	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private Gast() {
	};

	public Gast(String spielerName) {
		this.spielerName = spielerName;
	}

	public Teilnehmer nutzerKontoErstellen(String name, String vorname, String nutzername, String passwort,
			String email) {

		return null;
	}

	public Gast anTurnierAnmelden(String spielername, Turnier turnier) {

		return null;
	}

	public SpielerStatistik[] StatistikenAnsehen() {

		return null;
	}

	/** Spielername {0} ist zu kurz (kleiner 3 Zeichen) oder zu lang (groesser 255 Zeichen */
	@SuppressWarnings("serial")
	public static class SpielerNameNotAllowedExc extends multex.Exc {
	}

	/** Turnier {0} existiert nicht */
	@SuppressWarnings("serial")
	public static class TurnierGibtEsNichtExc extends multex.Exc {
	}

	/** Spielername {0} gibt es schon */
	@SuppressWarnings("serial")
	public static class SpielernameAlreadyTakenExc extends multex.Exc {
	}

}
