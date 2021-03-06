package turnier.webapp.domain;

import static multex.MultexUtil.create;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Configurable;

import turnier.webapp.domain.base.EntityBase;

/**
 * Nutzerentität mit Name, Vorname, Nutzername, Passwort, Email, gewonnene
 * Turniere, gewonnene Spiele, verlorene Spiele Eigenschaften.
 */
@Configurable
@Entity
public class Nutzer extends EntityBase<Nutzer> {

	/** Name vom Nutzer */
	private String name;
	/** Vorname vom Nutzer */
	private String vorname;
	/** Einzigartiger Nutzername vom Nutzer */
	private String nutzername;
	/** Passwort vom Nutzer */
	private String passwort;
	/** Einzigartiges Email vom Nutzer */
	private String email;
	/** Anzahl der gewonnen Turnieren */
	private int gewonneneTurniere;
	/** Anzahl der gewonnene Spielen */
	private int gewonneneSpiele;
	/** Anzahl der verlorenen Spielen */
	private int verloreneSpiele;

	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private Nutzer() {
	};

	/**
	 * Konstruktor für Nutzer Objekt
	 * 
	 * @param name
	 *            Name vom Nutzer
	 * @param vorname
	 *            Vorname vom Nutzer
	 * @param nutzername
	 *            Einzigartiger Nutzername vom Nutzer
	 * @param passwort
	 *            Passwwort vom Nutzer
	 * @param email
	 *            Einzigartiges Email vom Nutzer
	 */
	public Nutzer(final String name, final String vorname, final String nutzername, final String passwort, final String email) {
		this.setName(name);
		this.setVorname(vorname);
		this.setNutzername(nutzername);
		this.setPasswort(passwort);
		this.setEmail(email);
		this.gewonneneSpiele = 0;
		this.gewonneneTurniere = 0;
		this.verloreneSpiele = 0;

	}

	/**
	 * vergleicht das Passwort mit dem gespeicherten Passwort im DB
	 * 
	 * @param passwortZurVerifizierung
	 *            zum Vergleichen mit dem gespeicherten Passwort im DB
	 * @return passwort erfolgreich verifiziert, gibt wahr zurück, anderfalls false
	 * @throws PasswortIstFalschExc
	 *             Passwort stimmt nicht mit dem gespeicherten Passwort im DB
	 *             überein
	 */
	public Boolean passwortVerifizieren(final String passwortZurVerifizierung) throws PasswortIstFalschExc {
		if (this.passwort.equals(passwortZurVerifizierung)) {
			return true;
		} else
			throw create(PasswortIstFalschExc.class, passwortZurVerifizierung, this.nutzername);
	}

	/**
	 * ändert das Passwort für die Nutzer
	 * 
	 * @param altesPasswort
	 *            altes Passwort
	 * @param neuesPasswort
	 *            neues Passwort
	 */
	public void passwortAendern(final String altesPasswort, final String neuesPasswort) throws PasswortIstFalschExc {
		if ((passwortVerifizieren(altesPasswort))) {
			this.passwort = neuesPasswort;
		}

	}

	/**
	 * Anzahl der gewonnen Turniere wird um 1 erhöht
	 */
	public void hatTurnierGewonnen() {
		this.gewonneneTurniere = gewonneneTurniere + 1;
	}

	/**
	 * Anzahl der gewonnen Spielen wird um 1 erhöht
	 */
	public void hatGewonnen() {
		this.gewonneneSpiele = gewonneneSpiele + 1;
	}

	/**
	 * Anzahl der verlorenen Spielen wird um 1 erhöht
	 */
	public void hatVerloren() {
		this.verloreneSpiele = verloreneSpiele + 1;
	}

	/**
	 * ändert die Email Adresse von dem Nutzer
	 * 
	 * @param neueEmail
	 *            neue Email Addresse
	 * @param passwort
	 *            Passwort zur Verifizierung
	 */
	public void emailAendern(final String neueEmail, final String passwort) throws PasswortIstFalschExc {
		if (passwortVerifizieren(passwort)) {
			this.email = neueEmail;
		}

	}

	/**
	 * ändert die Nutzereigenschanften von dem Nutzer
	 * 
	 * @param name
	 *            neuer Name
	 * @param vorname
	 *            neuer Vorname
	 * @param nutzername
	 *            neuer Nutzername
	 * @param passwort
	 *            neues Passwort
	 * @param email
	 *            neue Email
	 */

	public void fuerAdminNutzerAendern(final String name, final String vorname, final String nutzername,
			final String passwort, final String email) {
		this.name = name;
		this.vorname = vorname;
		this.nutzername = nutzername;
		this.email = email;
		this.passwort = passwort;

	}

	// getter and setter - selfeplenatory

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(final String vorname) {
		this.vorname = vorname;
	}

	public String getNutzername() {
		return nutzername;
	}

	public void setNutzername(final String nutzername) {
		this.nutzername = nutzername;
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(final String passwort) {
		this.passwort = passwort;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public int getGewonneneTurniere() {
		return gewonneneTurniere;
	}

	public int getGewonneneSpiele() {
		return gewonneneSpiele;
	}

	public int getVerloreneSpiele() {
		return verloreneSpiele;
	}

	@Override
	public String toString() {
		return String.format(
				"Nutzer{id=%d, name='%s', vorname='%s', nutzername='%s', passwort='%s', email='%s', gewonneneSpiele='%d', verloreneSpiele='%d', gewonneneTurniere='%d'}",
				getId(), name, vorname, nutzername, passwort, email, gewonneneSpiele, verloreneSpiele,
				gewonneneTurniere);
	}

	// innere Exception Klassen
	/**
	 * Passwort {0} für den Nutzername {1} stimmt nicht mit dem Passwort Attribut
	 * überein.
	 */
	@SuppressWarnings("serial")
	public static class PasswortIstFalschExc extends multex.Exc {
	}

}
