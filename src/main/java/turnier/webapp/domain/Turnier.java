package turnier.webapp.domain;

import javax.persistence.Entity;
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
	private Nutzer organisator;
	private int maxTeilnehmer;
	private TurnierStatus turnierStatus;
    @OneToMany
	@JoinColumn(name = "jc_teilnehmer")
	private List<Nutzer> teilnehmer;
	//@ManyToOne
	//private TurnierBracket turnierbaum;



	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private Turnier() {
	};

	public Turnier(String name, String adresse, String datum, String uhrzeit, Nutzer organisator,
			int maxTeilnehmer) {
		this.name = name;
		this.adresse = adresse;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.organisator = organisator;
		this.maxTeilnehmer = maxTeilnehmer;
		this.teilnehmer = new ArrayList<>();
	//	this.turnierbaum = null;
		setTurnierStatus(TurnierStatus.OFFEN);
	}

	// package Sichtbarkeit
	public void fuegeTeilnehmerHinzu(Nutzer teilnehmer) {
		
		if (!(turnierStatus == TurnierStatus.OFFEN)) {
			throw create(FuegeTeilnehmerNichtZugelassenExc.class, teilnehmer.getNutzername(), this.name,
					turnierStatus.toString());

		}
		if (istVoll()) {
			
		throw create(IstVollExc.class, this.teilnehmer.size(), maxTeilnehmer);	
		}
		this.teilnehmer.add(teilnehmer);
	}
	
private Boolean istVoll() {
	
	return (teilnehmer.size() >= maxTeilnehmer);
}
	void entferneTeilnehmerAusDemTurnier(Nutzer teilnehmer) {
		if (turnierStatus == TurnierStatus.BEENDET || turnierStatus == TurnierStatus.GESTARTET) {
			throw create(EntferneTeilnehmerNichtZugelassenExc.class, teilnehmer.getNutzername(), this.name,
					turnierStatus.toString());
		}
		this.teilnehmer.remove(teilnehmer);
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

	
	@Override
	public String toString() {
		return String.format("Turnier{id=%d, name='%s', adresse='%s', datum='%s', uhrzeit='%s', organisator='%s, maxTeilnehmer=%d, turnierstatus='%s', teilnehmer='%s'}",
				getId(), name, name, adresse, datum.toString(), uhrzeit.toString(), organisator.toString(), maxTeilnehmer, turnierStatus.toString(), teilnehmer.toString());
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

	//public TurnierBracket getTurnierbaum() {
	//	return turnierbaum;
	//}

	//public void setTurnierbaum(TurnierBracket turnierbaum) {
	//	this.turnierbaum = turnierbaum;
	//}

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
	 * Nutzer{0} könnte nicht mehr aus dem Turnier {1} entfernt werden, denn {2}
	 * 
	 */
	@SuppressWarnings("serial")
	public static class EntferneTeilnehmerNichtZugelassenExc extends multex.Exc {
	}

}
