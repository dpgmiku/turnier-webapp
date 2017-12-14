package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import turnier.webapp.domain.base.EntityBase;

//@Entity sag uns bescheid, dass bei der Klasse JPA sich um eine JPA entity handelt
@Entity
public class Admin extends EntityBase<Admin> {

	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private Admin() {
	};

	public Boolean turnierVerifizieren(Turnier turnier) {

		return true;

	}

	public void loescheTurnier(Turnier turnier) {

	}

	public void loescheNutzer(Nutzer nutzer) {

	}

	/** Turnier {0} existiert nicht */
	@SuppressWarnings("serial")
	public static class TurnierGibtEsNichtExc extends multex.Exc {
	}

	/** Nutzer {0} existiert nicht */
	@SuppressWarnings("serial")
	public static class NutzerGibtEsNichtExc extends multex.Exc {
	}

}
