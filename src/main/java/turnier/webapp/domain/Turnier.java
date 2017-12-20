package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.base.EntityBase;
import turnier.webapp.domain.imports.NutzerRepository;
import turnier.webapp.domain.imports.TurnierRepository;

import static multex.MultexUtil.create;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Turnier extends EntityBase<Turnier> {
	private String name;
	private String adresse;
	private String datum;
	private String uhrzeit;
	@ManyToOne
	private Nutzer organisator; // Ein Organisator kann mehrere Turniere besitzen
	private int maxTeilnehmer;
	private TurnierStatus turnierStatus;
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "jc_teilnehmer")
	private List<Nutzer> teilnehmer; // StackOverflow
	// @ManyToOne
	// private TurnierBracket turnierbaum;

	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private Turnier() {
	};

	
	/**
	 * Konstruktor für das Turnier
	 * @param name Name vom Turnier
	 * @param adresse Adresse vom Turnier
	 * @param datum Datum wann das Turnier stattfinden soll.
	 * @param uhrzeit Uhrzeit wann das Turnier stattfindet.
	 * @param organisator Der Organisator des Turniers.
	 * @param maxTeilnehmer Die Anzahl an erlaubten Teilnehmer
	 */
	public Turnier(String name, String adresse, String datum, String uhrzeit, Nutzer organisator, int maxTeilnehmer) {
		this.name = name;
		this.adresse = adresse;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.organisator = organisator;
		this.maxTeilnehmer = maxTeilnehmer;
		this.teilnehmer = new ArrayList<>();
		// this.turnierbaum = null;
		setTurnierStatus(TurnierStatus.OFFEN);
	}

	/**
	 * Fügt ein Teilnehmer dem Turnier hinzu.
	 * @param teilnehmer Der Teilnehmer der zum Turnier hingefügt wird.
	 */
	public void anTurnierAnmelden(Nutzer teilnehmer) {

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
	 * @return ein Boolean ob das Turnier voll ist oder nicht.
	 */
	private Boolean istVoll() {

		return (teilnehmer.size() >= maxTeilnehmer);
	}

	/**
	 * Entfernt ein Teilnehmer aus dem Turnier
	 * @param teilnehmer Der Teilnehmer der aus dem Turnier entfernt wird.
	 */
	public void entferneTeilnehmer(Nutzer teilnehmer) {
		if (turnierStatus == TurnierStatus.BEENDET || turnierStatus == TurnierStatus.GESTARTET) {
			throw create(EntferneTeilnehmerNichtZugelassenExc.class, teilnehmer.getNutzername(), this.name,
					turnierStatus.toString());
		}
		this.teilnehmer.remove(teilnehmer);
	}

	/**
	 * Sucht und gibt ein Teilnehmer zurück der in diesem Turnier angemeldet ist.
	 * @param nutzername Der Nutzername des Teilnehmers nachdem gesucht wird.
	 * @return Gibt den, falls gefundenden, Nutzer zurück.
	 */
	public Nutzer teilnehmerSuchen(String nutzername) {
		for (Nutzer nutzer : teilnehmer) {
			if (nutzer.getNutzername().equals(nutzername))

				return nutzer;
		}
		throw create(KeinTeilnehmerInDiesemTurnierExc.class, nutzername, this.name);
	}

	
	/**
	 * Kreirt ein Turnierbaum 
	 * @param teilnehmer Die teilnehmer die in den Turnierbaum hinzugefügt werden.
	 * @return Gibt den erstellten Turnierbaum zurück.
	 */
	public TurnierBracket kreireTurnierbaum(Teilnehmer[] teilnehmer) {

		return null;
	}

	/**
	 * Startet das Turnier falls es nicht schon gestartet wurde.
	 */
	public void starteTurnier() {
		if (!(isPowerOfTwo(teilnehmer.size()))) {

			throw create(AnzahlTeilnehmerNoPowerOfTwoExc.class, teilnehmer.size());
		}
		turnierStatus = TurnierStatus.GESTARTET;
	}

	/**
	 * Prüft ob die Teilnehmer = 2^x sind. Z.b. 32 oder 64 Teilnehmer. 
	 * @param number Number ist Teilnehmeranzahl
	 * @return Gibt zurück ob die Teilnehmer anzahl = 2^x ist.
	 */
	private boolean isPowerOfTwo(int number) {

		return number >= 2 && ((number & (number - 1)) == 0);
	}


	/**
	 * TODO Später implementieren
	 * @param turnierbaum 
	 * @return
	 */
	public TurnierErgebnisse beendeTurnier(TurnierBracket turnierbaum) {
		if (turnierStatus == TurnierStatus.GESTARTET) {
		turnierStatus = TurnierStatus.BEENDET;
		}
		return null;

	}
	
	/**
	 * Gibt die Attribute des Turnier als String aus.
	 */
	@Override
	public String toString() {
		return String.format("Turnier{id=%d, name='%s', adresse='%s', datum='%s', uhrzeit='%s', organisator='%s, maxTeilnehmer=%d, turnierstatus='%s', teilnehmer='%s'}",
				getId(), name,  adresse, datum.toString(), uhrzeit.toString(), organisator.toString(), maxTeilnehmer, turnierStatus.toString(), teilnehmer.toString());
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

	// public TurnierBracket getTurnierbaum() {
	// return turnierbaum;
	// }

	// public void setTurnierbaum(TurnierBracket turnierbaum) {
	// this.turnierbaum = turnierbaum;
	// }

	public TurnierStatus getTurnierStatus() {
		return turnierStatus;
	}

	public void setTurnierStatus(TurnierStatus turnierStatus) {
		this.turnierStatus = turnierStatus;
	}

	/** Nutzername {0} existiert im Turnier {1} nicht */
	@SuppressWarnings("serial")
	public static class NutzernameNichtImTurnierExc extends multex.Exc {
	}

	/**
	 * Anzahl der Teilnehmer {0} . Es gibe keine Teilnehmer oder nur einen
	 * (mindestens 2 Teilnehmer erforderlich, bitte fügen Sie mehrere Teilnehmer
	 * hinzu
	 */
	@SuppressWarnings("serial")
	public static class ZuWenigTeilnehmerExc extends multex.Exc {
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
	 * Anzahl der Teilnehmer {0} ist kein Power von 2.
	 * 
	 */
	@SuppressWarnings("serial")
	public static class AnzahlTeilnehmerNoPowerOfTwoExc extends multex.Exc {
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