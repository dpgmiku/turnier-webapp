package turnier.webapp.domain;

import javax.persistence.Entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
import turnier.webapp.domain.imports.TurnierBracketRepository;
import turnier.webapp.domain.imports.TurnierRepository;

import static multex.MultexUtil.create;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	@JoinColumn(name = "jc_teilnehmer")
	private List<Nutzer> teilnehmer;
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
		this.turnierBrackets = new ArrayList<>();
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

//	private void fuegeTurnierbracketHinzu() {
//		final int size = turnierBrackets.size();
//		if (size >= (teilnehmer.size() - 1)) {
//			throw create(AlleBracketsSchonErstelltExc.class, this.name);
//		}
//		for (int i = 0; i <= size; i = i + 2) {
//			TurnierBracket turnierBracket1 = turnierBrackets.get(i);
//			TurnierBracket turnierBracket2 = turnierBrackets.get(i + 1);
//			if (!(turnierBracket1.getGewinner().equals("")) && !(turnierBracket2.getGewinner().equals(""))) {
//				TurnierBracket turnierBracket = new TurnierBracket(turnierBracket1.getGewinner(),
//						turnierBracket2.getGewinner());
//				turnierBrackets.add(turnierBracket);
//			}
//		}
//
//		}
//	


	//@Autowired
//	private transient NutzerRepository nutzerRepository;

//	public void setErgebnisse(TurnierBracket turnierBracket, int ergebnis1, int ergebnis2) {
//		turnierBracket.setGewinner(ergebnis1, ergebnis2);
//		final int size = turnierBrackets.size();
//		
//
//		fuegeTurnierbracketHinzu();
//	}
//	
	public String getTurnierErgebnisse() {
		if (!(turnierStatus == TurnierStatus.BEENDET)) {
		throw create(TurnierIstNochNichtBeendetExc.class, this.name, this.turnierStatus);	
		}
		int turnierBracketLaenge = turnierBrackets.size();
		String gewinner = turnierBrackets.get(turnierBracketLaenge-1).getGewinner();
		String verlierer =turnierBrackets.get(turnierBracketLaenge-1).getVerlierer();
		String returnString = gewinner+ "\n"+verlierer+"\n";
		for(int i = turnierBrackets.size()-2; i>=0; i--) {
	    TurnierBracket turnierPaar = turnierBrackets.get(i);   
	    String verliererSchleife = turnierPaar.getVerlierer();
		returnString = returnString + verliererSchleife + "\n"; 
		}
		return returnString;
		}

	//  @Autowired
	//	private transient TurnierBracketRepository turnierBracketRepository;    
	/**
	 * 
	 * Startet das Turnier falls es nicht schon gestartet wurde.
	 */
	public void starteTurnier() {
		turnierStatus = TurnierStatus.GESTARTET;
	
	}
	
	public void beendeTurnier() {
		turnierStatus = TurnierStatus.BEENDET;
	
	}
	
	public void turnierBracketHinzufuegen(TurnierBracket turnierBracket) {
		turnierBrackets.add(turnierBracket);
		
	}
	
	public void shuffleTeilnehmer() {
		
		Collections.shuffle(teilnehmer);
	}

	public TurnierBracket getTurnierBracketAtPos(int position) {
		
	return turnierBrackets.get(position);	
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

	
	/**
	 *Turnier {0} wurde noch nicht beendet. Aktueller Status: {1}.
	 */
	@SuppressWarnings("serial")
	public static class TurnierIstNochNichtBeendetExc extends multex.Exc {
	}
	
	/**
	 * Es wurden schon alle Brackets im Turnier {0} erstellt.
	 */
	@SuppressWarnings("serial")
	public static class AlleBracketsSchonErstelltExc extends multex.Exc {
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