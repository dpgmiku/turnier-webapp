package turnier.webapp.domain;

import static multex.MultexUtil.create;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.Turnier.AlleBracketsSchonErstelltExc;
import turnier.webapp.domain.imports.NutzerRepository;
import turnier.webapp.domain.imports.TurnierBracketRepository;
import turnier.webapp.domain.imports.TurnierRepository;

/**
 * This is a domain service for creating tourniers.
 * 
 * @author Michal Kubacki
 * @since 2017-12-15
 *
 */
@Service
// @Secured("NUTZER") //Only role NUTZER may call the methods in this service
// class.You can apply this annotation at the class or at the method level.
public class TurnierService {

	// Required repositories as by Ports and Adapters Pattern:
	private final NutzerRepository nutzerRepository;
	private final TurnierRepository turnierRepository;
	private final TurnierBracketRepository turnierBracketRepository;

	/**
	 * Konstruktor für den TurnierService
	 * 
	 * @param nutzerRepository
	 *            Nimmt sich das Nutzer Repository Interface
	 * @param turnierRepository
	 *            Nimmt sich das Turnier Repository Interface
	 */
	@Autowired
	public TurnierService(final NutzerRepository nutzerRepository,
			final TurnierBracketRepository turnierBracketRepository, final TurnierRepository turnierRepository) {
		this.nutzerRepository = nutzerRepository;
		this.turnierRepository = turnierRepository;
		this.turnierBracketRepository = turnierBracketRepository;
	}

	/**
	 * Command: Kreirt einen neuen Nutzer
	 * 
	 * @param name
	 *            Der Name vom Nutzer
	 * @param vorname
	 *            Der Vorname vom Nutzer
	 * @param nutzername
	 *            Der einzigartige Nutzername vom Nutzer
	 * @param passwort
	 *            Das Passwort vom Nutzer
	 * @param email
	 *            Die Email Adresse vom Nutzer
	 * @return nutzer Gibt der erstellten Nutzer zurück.
	 * @throws BenutzernameSchonHinterlegtExc
	 *             benutzername befindet sich schon im Datenbank
	 * @throws EmailSchonHinterlegtExc
	 *             email hat schon ein anderer Benutzer hinterlegt
	 * @throws ThatsNotAnEmailExc
	 *             email entpricht nicht die Email Norm
	 * @throws NeuesPasswortNotAllowedExc
	 *             passwort ist kürzer als 6 Zeichen oder länger als 255 Zeichen
	 * 
	 */
	// TODO Fehlerbehandlung wie bei EntferneTeilnehmer
	public Nutzer nutzerSpeichern(final String name, final String vorname, final String nutzername,
			final String passwort, final String email) {
		final int passwortLength = passwort.length();
		if (passwortLength > 5 && passwortLength < 255) {
			final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
			if (matcher.find()) {
				Nutzer findNutzer = findNutzerByEmail(email);
				if (findNutzer == null) {
					Nutzer findNutzerNutzername = findNutzerByNutzername(nutzername);
					if (findNutzerNutzername == null) {
						return nutzerRepository.save(new Nutzer(name, vorname, nutzername, passwort, email));
					} else
						throw create(BenutzernameSchonHinterlegtExc.class, nutzername, findNutzerNutzername.getId());
				} else
					throw create(EmailSchonHinterlegtExc.class, email, findNutzer.getId());
			} else
				throw create(ThatsNotAnEmailExc.class, email);
		} else
			throw create(NeuesPasswortNotAllowedExc.class, passwort, nutzername);
	}

	/**
	 * Command: updated das Passwort vom gegebenen {@link Nutzer} mit einem neuem
	 * Passwort.
	 * 
	 * @param nutzer
	 *            Der Nutzer dessen Passwort geändert wird.
	 * @param altesPasswort
	 *            Das alte Passwort das verifiziert werden muss.
	 * @param neuesPasswort
	 *            Das neue Password was neu gesetzt wird.
	 * @return nutzer Gibt den Nutzer zurück dessen Passwort geändert wurde.
	 */
	public Nutzer updateNutzerWithPassword(final Nutzer nutzer, final String altesPasswort,
			final String neuesPasswort) {

		final int passwortLaenge = neuesPasswort.length();
		if (passwortLaenge > 5 && passwortLaenge < 255) {
			nutzer.passwortAendern(altesPasswort, neuesPasswort);
			return nutzerRepository.save(nutzer);
		}
		throw create(NeuesPasswortNotAllowedExc.class, neuesPasswort, nutzer.getNutzername());

	}

	/**
	 * Command: Löscht den gegebenen {@link Nutzer}
	 * 
	 * @param nutzer
	 *            Das Nutzer Objekt was geloescht werden soll.
	 * @param passwort
	 *            Das passwort das zum verifizieren gebraucht wird.
	 */
	public void nutzerLoeschen(final Nutzer nutzer, final String passwort) throws ThatsNotAnEmailExc {
		if (nutzer.getPasswort().equals(passwort)) {

			nutzerRepository.delete(nutzer.getId());
		}
	}

	/**
	 * Command: Updatet die E-Mail Adresse des Nutzern {@link Nutzer} gegen einer
	 * neuen E-Mail Adresse.
	 * 
	 * @throws EmailSchonHinterlegtExc
	 *             * @param email neue Email auf die man das ändern will
	 * @param passwort
	 *            passwort zur Verifizierung
	 * @throws Nutzer.PasswortDoesntMatchExc
	 *             passwort stimmt nicht mit dem gespeicherten Passwort im DB
	 *             überein
	 * @throws ThatsNotAnEmailExc
	 *             email entpricht nicht die Email Norm
	 * @throws EmailSchonHinterlegtExc
	 *             email hat schon ein anderer Benutzer hinterlegt
	 */
	public void updateEmail(final Nutzer nutzer, final String passwort, final String email)
			throws ThatsNotAnEmailExc, EmailSchonHinterlegtExc {
		{
			final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
			if (matcher.find()) {
				Nutzer findNutzer = nutzerRepository.findEmail(email);
				if (findNutzer == null) {
					nutzer.emailAendern(email, passwort);
					nutzerRepository.save(nutzer);

				} else
					throw create(EmailSchonHinterlegtExc.class, email, findNutzer.getId());
			} else
				throw create(ThatsNotAnEmailExc.class, email);

		}
	}

//	public TurnierBracket turnierBracketSpeichern(Turnier turnier, TurnierBracket turnierBracket) {
//
//		return turnierBracketRepository.save(turnierBracket);

//	}

	public Turnier turnierStarten(Turnier turnier) {
		List<Nutzer> nutzer = turnier.getTeilnehmer();
		if (!(isPowerOfTwo(nutzer.size()))) {

			throw create(AnzahlTeilnehmerNoPowerOfTwoExc.class, nutzer.size());
		}
		turnier.starteTurnier();
		turnier.shuffleTeilnehmer();
		turnierRepository.save(turnier);
		nutzer = turnier.getTeilnehmer();
		for (int i = 0; i < nutzer.size(); i = i + 2) {
			TurnierBracket turnierBracket = new TurnierBracket(nutzer.get(i).getNutzername(),
					nutzer.get(i + 1).getNutzername());
			turnierBracket = turnierBracketRepository.save(turnierBracket);
			turnier.turnierBracketHinzufuegen(turnierBracket);
		}
		return turnierRepository.save(turnier);

	}

	public void setteErgebnisse(Turnier turnier, int position, int ergebnis1, int ergebnis2) {
		if (turnier.getTurnierStatus() != TurnierStatus.GESTARTET ) {
			throw create(TurnierStatusFailExc.class, turnier.getTurnierStatus());	
		}
	   TurnierBracket turnierBracket = turnier.getTurnierBracketAtPos(position);
	   turnierBracket.setGewinner(ergebnis1, ergebnis2);
		Nutzer gewinnerNutzer = nutzerRepository.find(turnierBracket.getGewinner());
		Nutzer verliererNutzer = nutzerRepository.find(turnierBracket.getVerlierer());
		gewinnerNutzer.hatGewonnen();
		verliererNutzer.hatVerloren();
	   nutzerRepository.save(gewinnerNutzer);
	   nutzerRepository.save(verliererNutzer);
	   turnierBracketRepository.save(turnierBracket);
	   List<TurnierBracket> turnierBrackets = turnier.getTurnierBrackets();
	   final int size = turnierBrackets.size();
	  if (size >= (turnier.getTeilnehmer().size() - 1)) {
		        turnier.beendeTurnier();
				Nutzer nutzerGewinner = nutzerRepository.find(turnierBracket.getGewinner());
	            nutzerGewinner.hatTurnierGewonnen();
	            nutzerRepository.save(nutzerGewinner);
			}	
	  else 
	  {
	  for(int i = 0;i<size;i=i+2)
	{
		TurnierBracket turnierBracket1 = turnierBrackets.get(i);
		TurnierBracket turnierBracket2 = turnierBrackets.get(i + 1);
		if (!(turnierBracket1.getGewinner().equals("")) && !(turnierBracket2.getGewinner().equals(""))) {
			TurnierBracket newTurnierBracket = new TurnierBracket(turnierBracket1.getGewinner(),
					turnierBracket2.getGewinner());
			newTurnierBracket = turnierBracketRepository.save(newTurnierBracket);
			turnier.turnierBracketHinzufuegen(newTurnierBracket);
		}
		turnierRepository.save(turnier);
	}
	  }

}

/**
 * Anzahl der Teilnehmer {0} ist kein Power von 2.
 * 
 */
@SuppressWarnings("serial")
public static class AnzahlTeilnehmerNoPowerOfTwoExc extends multex.Exc {
	}

	/**
	 * Prüft ob die Teilnehmer = 2^x sind. Z.b. 32 oder 64 Teilnehmer.
	 * 
	 * @param number
	 *            Number ist Teilnehmeranzahl
	 * @return Gibt zurück ob die Teilnehmer anzahl = 2^x ist.
	 */
	private boolean isPowerOfTwo(int number) {

		return number >= 2 && ((number & (number - 1)) == 0);
	}

	/**
	 * Entfernt den gegeben Nutzer aus dem gegebenen Turnier.
	 * 
	 * @param owner
	 *            Der Organisator des Turniers
	 * @param turnier
	 *            Das Turnier wovon der Teilnehmer entfernt werden soll.
	 * @param nutzer
	 *            Der Nutzer der Entfernt werden soll
	 */
	public void entferneTeilnehmer(Nutzer owner, Turnier turnier, Nutzer nutzer) {
		if (turnierRepository.find(turnier.getName()) == null)
			throw create(TurnierGibtEsNichtExc.class, turnier.getName());
		List<Nutzer> turnierTeilnehmerList = turnier.getTeilnehmer();
		if (!(turnierTeilnehmerList.contains(nutzer)))
			throw create(TeilnehmerGibtEsNichtExc.class, nutzer.getNutzername(), turnier.getName());
		if (!(owner.getNutzername().equals(turnier.getOrganisator().getNutzername())))
			throw create(EsIstNichtDeinTurnierExc.class, turnier.getName(), owner.getNutzername());
		turnier.entferneTeilnehmer(nutzer);
		turnierRepository.save(turnier);
	}

	/**
	 * Erstellt ein Turnier
	 * 
	 * @param name
	 *            Der Name des Turniers
	 * @param adresse
	 *            Die Adresse des Turniers
	 * @param datum
	 *            Das Datum wann das Turnier anfängt.
	 * @param uhrzeit
	 *            Die Uhrzeit wann das Turnier stattfinden soll.
	 * @param nutzer
	 *            Der Organisator der das Turnier organisiert.
	 * @param maxTeilnehmer
	 *            Die anzahl an erlaubten Nutzer für das Turnier.
	 * @return Gibt das erstellte Turnier zurück.
	 */
	public Turnier turnierErstellen(String name, String adresse, String datum, String uhrzeit, Nutzer nutzer,
			int maxTeilnehmer) {
		if (maxTeilnehmer > 32)
			throw create(ZuVieleTeilnehmerExc.class, maxTeilnehmer);
		if (maxTeilnehmer < 2)
			throw create(ZuWenigTeilnehmerExc.class, maxTeilnehmer);
		final Boolean containsNumber = name.matches(".*\\d+.*");
		if (adresse.length() <= 3 || containsNumber) {

			throw create(KeineRichtigeEingabenTurnierExc.class, name);
		}
		final Turnier findTurnier = this.findTurnierByName(name);
		if (findTurnier != null) {

			throw create(TurniernameSchonHinterlegtExc.class, name, findTurnier.getId());
		}
		;

		final Turnier turnier = new Turnier(name, adresse, datum, uhrzeit, nutzer, maxTeilnehmer);

		return turnierRepository.save(turnier);
	}

	/**
	 * Turnier {0} name enthält Ziffer, Ort hat weniger als 3 Zeichen und oder
	 * maxTeilnehmer ist keine Ziffer
	 */
	@SuppressWarnings("serial")
	public static class KeineRichtigeEingabenTurnierExc extends multex.Exc {
	}

	/** maxTeilnehmer {0} ist größer als 32 */
	@SuppressWarnings("serial")
	public static class ZuVieleTeilnehmerExc extends multex.Exc {
	}

	/** maxTeilnehmer {0} ist kleiner als 2 */
	@SuppressWarnings("serial")
	public static class ZuWenigTeilnehmerExc extends multex.Exc {
	}

	/**
	 * Query: Findet einen Nutzer mithilfe der E-mail Adresse.
	 * 
	 * @param email
	 *            Die gegebene E-Mail, die in der Nutzer Repository gefunden werden
	 *            soll.
	 * @return Gibt den Nutzer, falls gefunden, zurueck.
	 */
	public Nutzer findNutzerByEmail(final String email) {
		return nutzerRepository.findEmail(email);
	}

	/**
	 * Query: Findet einen Nutzer mithilfe des Nutzernamens.
	 * 
	 * @param nutzername
	 *            Der Nutzernamen, der in der Nutzer Repository gefunden werden
	 *            soll.
	 * @return Gibt den Nutzer, falls gefunden, zurueck.
	 */
	public Nutzer findNutzerByNutzername(final String nutzername) {
		return nutzerRepository.find(nutzername);
	}

	/**
	 * Query: Findet ein Turnier mithilfe des Namens.
	 * 
	 * @param name
	 *            Der Name des Turniers
	 * @return Das gefundene Turnier.
	 */
	public Turnier findTurnierByName(final String name) {

		return turnierRepository.find(name);
	}

	/**
	 * Query: Findet alle erstellten Turniere
	 * 
	 * @return Gibt eine Liste von allen Turnieren zurück.
	 */
	public List<Turnier> findTurniers() {

		return turnierRepository.findAll();
	}

	/**
	 * Findet alle Turniere die vom gegebenen Nutzer erstellt worden sind.
	 * 
	 * @param organisator
	 *            Der Organisator wessen Turniere gelistet werden soll.
	 * @return Gibt eine Liste von Turnieren zurück der der gegebene Nutzer erstellt
	 *         hat.
	 */
	public List<Turnier> findTurnierByOrganisator(final Nutzer organisator) {

		return turnierRepository.findTurniereVonNutzer(organisator);
	}

	/**
	 * Löscht das gegebene Turnier vom gegebenen Nutzer
	 * 
	 * @param nutzer
	 *            Der Organisator dessen Turnier geloescht wird.
	 * @param turnier
	 *            Das Turnier was gelöscht wird.
	 */
	public void loescheEigenesTurnier(Nutzer nutzer, Turnier turnier) {
		if (turnierRepository.find(turnier.getName()) == null)
			throw create(TurnierGibtEsNichtExc.class, turnier.getName());
		if (!(nutzer.getNutzername().equals(turnier.getOrganisator().getNutzername())))
			throw create(EsIstNichtDeinTurnierExc.class, turnier.getName(), nutzer.getNutzername());
		turnierRepository.delete(turnier.getId());

	}

	/**
	 * Meldet ein Nutzer für das Turnier an
	 * 
	 * @param turnier
	 *            Das Turnier wo der Nutzer sich anmeldet.
	 * @param nutzer
	 *            Der Nutzer der angemeldet werden soll.
	 */
	public void anTurnierAnmelden(Turnier turnier, Nutzer nutzer) {
		if (turnier.getTeilnehmer().contains(nutzer))
			throw create(DuBistSchonAngemeldetExc.class, nutzer.getNutzername(), turnier.getName());
		final TurnierStatus turnierStatus = turnier.getTurnierStatus();
		if (turnierStatus != TurnierStatus.OFFEN)
			throw create(TurnierStatusFailExc.class, turnier.getName(), turnierStatus.toString());
		turnier.anTurnierAnmelden(nutzer);
		turnierRepository.save(turnier);
	}

	/** Nutzername {0} ist schon im Turnier {1} angemeldet */
	@SuppressWarnings("serial")
	public static class DuBistSchonAngemeldetExc extends multex.Exc {
	}
	// methods from Teilnehmer Klasse - unecessary split, because OneToOne

	/** Query: Findet alle erstellten Nutzer die nach Nutzernamen sortiert sind. */
	public List<Nutzer> findAllNutzers() {
		return nutzerRepository.findAll();
	}

	/**
	 * Turnier {0} hat Status {1}. Es ist unmöglich dieser Vorgang durchzuführen
	 */
	@SuppressWarnings("serial")
	public static class TurnierStatusFailExc extends multex.Exc {
	}

	/** Turnier {0} existiert gar nicht im Turnier Webapp */
	@SuppressWarnings("serial")
	public static class TurnierGibtEsNichtExc extends multex.Exc {
	}

	/** Teilnehmer {0} existiert gar nicht im Turnier {1} */
	@SuppressWarnings("serial")
	public static class TeilnehmerGibtEsNichtExc extends multex.Exc {
	}

	/** Turnier {0} gehört nicht zu diesem Teilnehmer {1} */
	@SuppressWarnings("serial")
	public static class EsIstNichtDeinTurnierExc extends multex.Exc {
	}

	/**
	 * Neues Passwort {0} für den Nutzername {1} ist kürzer als 6 Zeichen oder
	 * länger als 255 Zeichen.
	 */
	@SuppressWarnings("serial")
	public static class NeuesPasswortNotAllowedExc extends multex.Exc {
	}

	/** {0} entspricht nicht der Email-Adressen-Norm. */
	@SuppressWarnings("serial")
	public static class ThatsNotAnEmailExc extends multex.Exc {
	}

	/** Es existiert schon ein Nutzer mit dieser Email {0} mit dem ID {1} */
	@SuppressWarnings("serial")
	public static class EmailSchonHinterlegtExc extends multex.Exc {
	}

	/** Es existiert schon ein Nutzer mit diesem Nutzername {0} mit dem ID {1} */
	@SuppressWarnings("serial")
	public static class BenutzernameSchonHinterlegtExc extends multex.Exc {
	}

	/** Es existiert schon ein Turnier mit diesem Turniername {0} mit dem ID {1} */
	@SuppressWarnings("serial")
	public static class TurniernameSchonHinterlegtExc extends multex.Exc {
	}

}