package turnier.webapp.domain;

import javax.persistence.Entity;
import turnier.webapp.domain.base.EntityBase;
import static multex.MultexUtil.create;

/**
 * TurnierBracketentität mit zwei Teilnehmernutzernamen, zwei Ergebnissenfelder,
 * einem Gewinner und einem Verlierer.
 */
@Entity
public class TurnierBracket extends EntityBase<TurnierBracket> {

	/** Name vom ersten Nutzer */
	private String nutzername1;
	/** Name vom zweiten Nutzer */
	private String nutzername2;
	/** Ergebnis vom ersten Nutzer */
	private int ergebnis1;
	/** Ergebnis vom zweiten Nutzer */
	private int ergebnis2;
	/** Name vom Gewinner */
	private String gewinner;
	/** Name vom Verlierer */
	private String verlierer;

	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private TurnierBracket() {
	};

	/**
	 * /** Konstruktor für TurnierBracket Objekt
	 * 
	 * @param nutzername1
	 *            Name vom ersten Nutzer
	 * @param nutzername2
	 *            Name vom zweiten Nutzer
	 */

	public TurnierBracket(final String nutzername1, final String nutzername2) {
		this.nutzername1 = nutzername1;
		this.nutzername2 = nutzername2;
		this.ergebnis1 = 0;
		this.ergebnis2 = 0;
		this.gewinner = "";
		this.verlierer = "";
	}

	/**
	 * vergleicht beide Ergebnisse und entscheidet, wer gewonnen und verloren hat
	 * 
	 * @param ergebnis1
	 *            Ergebnis vom ersten Nutzer
	 * @param ergebnis2
	 *            Ergebnis vom zweiten Nutzer
	 * @throws ErgebnisSchonDaExc
	 *             Diese TurnierRunde wurde schon entschieden
	 */
	public void setGewinner(final int ergebnis1, final int ergebnis2) throws ErgebnisSchonDaExc {
		if (!(gewinner.equals("")))
			throw create(TurnierBracket.ErgebnisSchonDaExc.class, gewinner, verlierer);
		this.ergebnis1 = ergebnis1;
		this.ergebnis2 = ergebnis2;
		if (ergebnis1 > ergebnis2) {
			gewinner = nutzername1;
			verlierer = nutzername2;
		} else {
			gewinner = nutzername2;
			verlierer = nutzername1;
		}

	}

	// getter und setter, selfexplanatory
	public String getGewinner() {

		return gewinner;
	}

	public String getVerlierer() {

		return verlierer;
	}

	public int getErgebnis1() {

		return ergebnis1;
	}

	public int getErgebnis2() {

		return ergebnis2;
	}

	public void setGewinner(final String gewinner) {

		this.gewinner = gewinner;
	}

	public void setVerlierer(final String verlierer) {

		this.verlierer = verlierer;
	}

	public String getNutzername1() {
		return nutzername1;
	}

	public void setNutzername1(final String nutzername1) {
		this.nutzername1 = nutzername1;
	}

	public String getNutzername2() {
		return nutzername2;
	}

	public void setNutzername2(final String nutzername2) {
		this.nutzername2 = nutzername2;
	}

	@Override
	public String toString() {
		return String.format(
				"TurnierBracket{id='%d', nutzername1='%s', nutzername2='%s', ergebnis1='%d', ergebnis2='%d', gewinner='%s', verlierer='%s'}",
				getId(), nutzername1, nutzername2, ergebnis1, ergebnis2, gewinner, verlierer);

	}

	// Exception
	/**
	 * Nutzer mit dem Nutzername {0} hat schon gegen {1} gewonnen
	 */
	@SuppressWarnings("serial")
	public static class ErgebnisSchonDaExc extends multex.Exc {
	}

}
