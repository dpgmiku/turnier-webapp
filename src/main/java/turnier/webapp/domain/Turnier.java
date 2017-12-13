package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Turnier extends EntityBase<Turnier> {
	private String name;
	private String adresse;
	private LocalDate datum;
	private LocalTime uhrzeit;
	@ManyToOne
	private Nutzer organisator;
	private int maxTeilnehmer;
	private TurnierStatus turnierStatus;
	private ArrayList<Nutzer> teilnehmer;
	@ManyToOne
	private TurnierBracket turnierbaum;

	@Autowired
	private transient TurnierRepository turnierRepository;

	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	public Turnier() {
	};

	public Turnier(String name, String adresse, LocalDate datum, LocalTime uhrzeit, Nutzer organisator,
			int maxTeilnehmer) {
		this.name = name;
		this.adresse = adresse;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.organisator = organisator;
		this.maxTeilnehmer = maxTeilnehmer;
		this.teilnehmer = new ArrayList<>();
		this.turnierbaum = null;
		setTurnierStatus(TurnierStatus.OFFEN);
	}

	// package Sichtbarkeit
	void fuegeTeilnehmerHinzu(Nutzer teilnehmer) {
		
		if (!(turnierStatus == TurnierStatus.OFFEN)) {
			throw create(FuegeTeilnehmerNichtZugelassenExc.class, teilnehmer.getNutzername(), this.name,
					turnierStatus.toString());

		}
//		if istVoll( ) {
//			
//		throw create(IstVollExc)	
//		}
		this.teilnehmer.add(teilnehmer);


		turnierRepository.save(this);

	

	}
	// TODO private methode istVoll()
	// private istVoll();

	void entferneTeilnehmerAusDemTurnier(Nutzer teilnehmer) {
		if (turnierStatus == TurnierStatus.BEENDET || turnierStatus == TurnierStatus.GESTARTET) {
			throw create(EntferneTeilnehmerNichtZugelassenExc.class, teilnehmer.getNutzername(), this.name,
					turnierStatus.toString());
		}

		this.teilnehmer.remove(teilnehmer);
		turnierRepository.save(this);
	}

	public Nutzer teilnehmerSuchen(String nutzername) {
		for (Nutzer nutzer : teilnehmer) {
			if (nutzer.getNutzername().equals(nutzername))

				return nutzer;
		}
		return null;

	}

	public TurnierBracket kreireTurnierbaum(Teilnehmer[] teilnehmer) {

		return null;
	}

	public void starteTurnier() {

	}

	public TurnierErgebnisse beendeTurnier(TurnierBracket turnierbaum) {
		turnierStatus = TurnierStatus.BEENDET;
		return null;

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

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	public LocalTime getUhrzeit() {
		return uhrzeit;
	}

	public void setUhrzeit(LocalTime uhrzeit) {
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

	public ArrayList<Nutzer> getTeilnehmer() {
		return teilnehmer;
	}

	public void setTeilnehmer(ArrayList<Nutzer> teilnehmer) {
		this.teilnehmer = teilnehmer;
	}

	public TurnierBracket getTurnierbaum() {
		return turnierbaum;
	}

	public void setTurnierbaum(TurnierBracket turnierbaum) {
		this.turnierbaum = turnierbaum;
	}

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
	 * Nutzer{0} könnte nicht mehr aus dem Turnier {1} entfernt werden, denn {2}
	 * 
	 */
	@SuppressWarnings("serial")
	public static class EntferneTeilnehmerNichtZugelassenExc extends multex.Exc {
	}

}
