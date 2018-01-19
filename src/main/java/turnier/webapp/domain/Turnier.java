package turnier.webapp.domain;

import javax.persistence.Entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


import turnier.webapp.domain.base.EntityBase;

import static multex.MultexUtil.create;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Turnierentität mit Name, Adresse, Datum, Uhrzeit, Organisator, maxTeilnehmer,
 * turnierStatus, teilnehmer, turneirBracets Eigenschaften.
 */
@Entity
public class Turnier extends EntityBase<Turnier> {
	/** Name vom Turnier */
	private String name;
	/** Adresse vom Turnier */
	private String adresse;
	/** Datum vom Turnier */
	private String datum;
	/** Uhrzeit vom Turnier */
	private String uhrzeit;
	/** Organisator vom Turnier */
	@ManyToOne
	private Nutzer organisator; // Ein Organisator kann mehrere Turniere besitzen
	/** maximale Anzahl der Teilnehmernutzer */
	private int maxTeilnehmer;
	/** Status vom Turnier */
	private TurnierStatus turnierStatus;
	/** Teilnehmerliste */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	@JoinColumn(name = "jc_teilnehmer")
	private List<Nutzer> teilnehmer;
	/** Turnierbracketliste, wird nach dem Starten vom Turnier gefüllt */
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	@JoinColumn(name = "jc_turnierbracket")
	private List<TurnierBracket> turnierBrackets;

	//
	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private Turnier() {
	};

	/**
	 * Konstruktor für das Turnier
	 * 
	 * @param name
	 *            Name vom Turnier
	 * @param adresse
	 *            Adresse vom Turnier
	 * @param datum
	 *            Datum wann das Turnier stattfinden soll.
	 * @param uhrzeit
	 *            Uhrzeit wann das Turnier stattfindet.
	 * @param organisator
	 *            Der Organisator des Turniers.
	 * @param maxTeilnehmer
	 *            Die Anzahl an erlaubten Teilnehmer
	 */
	public Turnier(String name, String adresse, String datum, String uhrzeit, Nutzer organisator, int maxTeilnehmer) {
		this.name = name;
		this.adresse = adresse;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.organisator = organisator;
		this.maxTeilnehmer = maxTeilnehmer;
		this.teilnehmer = new ArrayList<>();
		this.turnierBrackets = new ArrayList<>();
		// this.turnierbaum = null;
		setTurnierStatus(TurnierStatus.OFFEN);
	}

	/**
	 * Ändert die Eigenschaften vom Turnier
	 * 
	 * @param name
	 *            neuer Turniername
	 * @param adresse
	 *            neue Adresse
	 * @param datum
	 *            neues Datum
	 * @param uhrzeit
	 *            neue Uhrzeit
	 * @param maxTeilnehmer
	 *            neue max. Anzahl der Teilnehmer
	 */

	public void fuerAdminTurnierAendern(final String name, final String adresse, final String datum,
			final String uhrzeit, final int maxTeilnehmer) {
		if (!(turnierStatus == TurnierStatus.OFFEN)) {
			throw create(TuernierAendernNichtZugelassen.class, this.name, this.turnierStatus.toString());
		}
		this.name = name;
		this.adresse = adresse;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.maxTeilnehmer = maxTeilnehmer;

	}

	/**
	 * Fügt ein Teilnehmer dem Turnier hinzu.
	 * 
	 * @param teilnehmer
	 *            Der Teilnehmer der zum Turnier hingefügt wird.
	 * @throws FuegeTeilnehmerNichtZugelassenExc
	 *             TurnierStatus ist nicht mehr auf offen gesetzt
	 * @throws IstVollExc
	 *             max Anzahl der Teilnehmer wurde schon erreicht.
	 */
	public void anTurnierAnmelden(Nutzer teilnehmer) throws FuegeTeilnehmerNichtZugelassenExc, IstVollExc {

		if (!(turnierStatus == TurnierStatus.OFFEN)) {
			throw create(FuegeTeilnehmerNichtZugelassenExc.class, teilnehmer.getNutzername(), this.name,
					turnierStatus.toString());

		}
		if (istVoll()) {

			throw create(IstVollExc.class, this.teilnehmer.size(), maxTeilnehmer);
		}
		this.teilnehmer.add(teilnehmer);
	}

	/**
	 * Prüft ob das Turnier voll ist.
	 * 
	 * @return ein Boolean ob das Turnier voll ist oder nicht.
	 */
	private Boolean istVoll() {

		return (teilnehmer.size() >= maxTeilnehmer);
	}

	/**
	 * Entfernt ein Teilnehmer aus dem Turnier
	 * 
	 * @param teilnehmer
	 *            Der Teilnehmer der aus dem Turnier entfernt wird.
	 * @throws EntferneTeilnehmerNichtZugelassenExc
	 *             TurnierStatus ist nicht mehr auf offen gesetzt
	 */
	public void entferneTeilnehmer(Nutzer teilnehmer) throws EntferneTeilnehmerNichtZugelassenExc {
		if (!(turnierStatus == TurnierStatus.OFFEN)) {
			throw create(EntferneTeilnehmerNichtZugelassenExc.class, teilnehmer.getNutzername(), this.name,
					turnierStatus.toString());
		}
		this.teilnehmer.remove(teilnehmer);
	}

	/**
	 * Sucht und gibt ein Teilnehmer zurück der in diesem Turnier angemeldet ist.
	 * 
	 * @param nutzername
	 *            Der Nutzername des Teilnehmers nachdem gesucht wird.
	 * @return Gibt den, falls gefundenden, Nutzer zurück.
	 * @throws KeinTeilnehmerInDiesemTurnierExc
	 *             Teilnehmernutzer wurde nicht gefunden
	 */
	public Nutzer teilnehmerSuchen(String nutzername) throws KeinTeilnehmerInDiesemTurnierExc {
		for (Nutzer nutzer : teilnehmer) {
			if (nutzer.getNutzername().equals(nutzername))

				return nutzer;
		}
		throw create(KeinTeilnehmerInDiesemTurnierExc.class, nutzername, this.name);
	}

	/**
	 * gibt die Ergebnisse als String zurück, wenn das Turnier schon beendet wurde
	 * 
	 * @return Liste als String mit allen Teilnehmernergebnissen. Der beste Platz
	 *         ist ganz oben
	 * @throws TurnierIstNochtNichtBeendetExc
	 *             Turnier wurde noch nicht beendet.
	 */
	public String getTurnierErgebnisse() throws TurnierIstNochNichtBeendetExc {
		if (!(turnierStatus == TurnierStatus.BEENDET)) {
			throw create(TurnierIstNochNichtBeendetExc.class, this.name, this.turnierStatus);
		}
		int turnierBracketLaenge = turnierBrackets.size();
		String gewinner = turnierBrackets.get(turnierBracketLaenge - 1).getGewinner();
		String verlierer = turnierBrackets.get(turnierBracketLaenge - 1).getVerlierer();
		String returnString = gewinner + "\n" + verlierer + "\n";
		for (int i = turnierBrackets.size() - 2; i >= 0; i--) {
			TurnierBracket turnierPaar = turnierBrackets.get(i);
			String verliererSchleife = turnierPaar.getVerlierer();
			returnString = returnString + verliererSchleife + "\n";
		}
		return returnString;
	}

	/**
	 * Turnierstatus wird auf gestartet gesetzt.
	 */
	public void starteTurnier() {
		turnierStatus = TurnierStatus.GESTARTET;

	}

	/**
	 * Turnierstatus wird auf beendet gesetzt.
	 */
	public void beendeTurnier() {
		turnierStatus = TurnierStatus.BEENDET;

	}

	/**
	 * TurnierBracket wird in der TurnierBracketliste hinzugefuegt
	 * 
	 * @param turnierBracket
	 *            neues TurnierBracket wird in der Liste hinzugefuegt
	 */
	public void turnierBracketHinzufuegen(TurnierBracket turnierBracket) {
		turnierBrackets.add(turnierBracket);

	}

	/**
	 * Teilnehmer wird geschuffelt
	 */
	public void shuffleTeilnehmer() {

		Collections.shuffle(teilnehmer);
	}

	/**
	 * gibt das TurnierBracket an der Stelle zurück
	 * 
	 * @param position
	 *            Stelle
	 * @return turnierBracket an dieser Stelle
	 */
	public TurnierBracket getTurnierBracketAtPos(int position) {
		if (position < 0 || position >= turnierBrackets.size()) {
			throw create(DieseStelleGibtEsNichtExc.class, position, turnierBrackets.size() - 1);
		}
		return turnierBrackets.get(position);
	}

	// getters and setters, selfexplanatory

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getDatum() {
		return datum;
	}

	public List<TurnierBracket> getTurnierBrackets() {
		return turnierBrackets;
	}

	public void setTurnierBrackets(List<TurnierBracket> turnierBrackets) {
		this.turnierBrackets = turnierBrackets;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getUhrzeit() {
		return uhrzeit;
	}

	public void setUhrzeit(String uhrzeit) {
		this.uhrzeit = uhrzeit;
	}

	public Nutzer getOrganisator() {
		return organisator;
	}

	public void setOrganisator(Nutzer organisator) {
		this.organisator = organisator;
	}

	public int getMaxTeilnehmer() {
		return maxTeilnehmer;
	}

	public void setMaxTeilnehmer(int maxTeilnehmer) {
		this.maxTeilnehmer = maxTeilnehmer;
	}

	public List<Nutzer> getTeilnehmer() {
		return teilnehmer;
	}

	public void setTeilnehmer(ArrayList<Nutzer> teilnehmer) {
		this.teilnehmer = teilnehmer;
	}

	public TurnierStatus getTurnierStatus() {
		return turnierStatus;
	}

	public void setTurnierStatus(TurnierStatus turnierStatus) {
		this.turnierStatus = turnierStatus;
	}

	/**
	 * Gibt die Attribute des Turnier als String aus.
	 */
	@Override
	public String toString() {
		return String.format(
				"Turnier{id=%d, name='%s', adresse='%s', datum='%s', uhrzeit='%s', organisator='%s, maxTeilnehmer=%d, turnierstatus='%s', teilnehmer='%s', turnierBrackets='%s'}",
				getId(), name, adresse, datum.toString(), uhrzeit.toString(), organisator.toString(), maxTeilnehmer,
				turnierStatus.toString(), teilnehmer.toString(), turnierBrackets.toString());
	}

	// Exceptions

	/**
	 * Turnier {0} wurde noch nicht beendet. Aktueller Status: {1}.
	 */
	@SuppressWarnings("serial")
	public static class TurnierIstNochNichtBeendetExc extends multex.Exc {
	}

	/**
	 * Diese Stelle {0} gibt es nicht. Mögliche Werte nur zw {0} und {1} möglich!.
	 */
	@SuppressWarnings("serial")
	public static class DieseStelleGibtEsNichtExc extends multex.Exc {
	}

	/**
	 * 
	 * Admin, Du darfst der Turnier {0} nicht mehr ändern, weil er sich gerade im
	 * Status {1} befindet.
	 *
	 */
	@SuppressWarnings("serial")
	public static class TuernierAendernNichtZugelassen extends multex.Exc {
	}

	/**
	 * Nutzer{0} könnte sich nicht mehr im Turnier {1} anmelden, denn {2}
	 * 
	 */
	@SuppressWarnings("serial")
	public static class FuegeTeilnehmerNichtZugelassenExc extends multex.Exc {
	}

	/**
	 * Du kannst sich nicht mehr anmelden. {0} von max {1} sind schon angemeldet.
	 * 
	 */
	@SuppressWarnings("serial")
	public static class IstVollExc extends multex.Exc {
	}

	/**
	 * Der Nutzer {0} nimmt in Turnier {1} nicht teil.
	 * 
	 */
	@SuppressWarnings("serial")
	public static class KeinTeilnehmerInDiesemTurnierExc extends multex.Exc {
	}

	/**
	 * Nutzer{0} könnte nicht mehr aus dem Turnier {1} entfernt werden, denn {2}
	 * 
	 */
	@SuppressWarnings("serial")
	public static class EntferneTeilnehmerNichtZugelassenExc extends multex.Exc {
	}

}