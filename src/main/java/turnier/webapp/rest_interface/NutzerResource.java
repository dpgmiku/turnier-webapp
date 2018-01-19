package turnier.webapp.rest_interface;

import turnier.webapp.domain.Nutzer;

/**
 * Daten über Nutzer vom Turnier-Webapp. Brauchbar für Data Transfer Object.
 */
public class NutzerResource {

	/** Einzigartige ID-Nummer vom Nutzer */
	public Long id;

	/** Der Vorname vom Nutzer */
	public String vorname;

	/** Der Name vom Nutzer. */
	public String name;

	/** Der einzigartige Nutzername vom Nutzer */
	public String nutzername;

	/** Das Passwort vom Nutzer. */
	public String passwort;

	/** Die einzigartige Email-Adresse vom Nutzer */
	public String email;
	/** Anzahl der gewonnenen Spielen der Nutzer */
	public int gewonneneSpiele;
	/** Anzahl der verlorenen Spielen der Nutzer */
	public int verloreneSpiele;
	/** Anzahl der gewonnen Turniere der Nutzer */
	public int gewonneneTurniere;

	/** erforderlich für Jackson */
	public NutzerResource() {
	}

	/**
	 * kreirt NutzerResource mit den Daten aus übergebenen Nutzerentität.
	 * 
	 * @param entity
	 *            nutzer Objekt
	 */
	public NutzerResource(final Nutzer entity) {
		this.id = entity.getId();
		this.vorname = entity.getVorname();
		this.name = entity.getName();
		this.nutzername = entity.getNutzername();
		this.email = entity.getEmail();
		this.passwort = entity.getPasswort();
		this.gewonneneSpiele = entity.getGewonneneSpiele();
		this.verloreneSpiele = entity.getVerloreneSpiele();
		this.gewonneneTurniere = entity.getGewonneneTurniere();
	}

	@Override
	public String toString() {
		return String.format(
				"Nutzer{id=%d, name='%s', vorname='%s', nutzername='%s', passwort='%s', email='%s', gewonneneSpiele='%d', verloreneSpiele='%d', gewonneneTurniere='%d'}",
				id, name, vorname, nutzername, passwort, email, gewonneneSpiele, verloreneSpiele, gewonneneTurniere);
	}

}
