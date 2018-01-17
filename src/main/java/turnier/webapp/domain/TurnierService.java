package turnier.webapp.domain;

import static multex.MultexUtil.create;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.imports.AdminRepository;
import turnier.webapp.domain.imports.NutzerRepository;
import turnier.webapp.domain.imports.TurnierBracketRepository;
import turnier.webapp.domain.imports.TurnierRepository;

/**
 * Es ist ein Domainmodel zur Erstellung der Turniere
 * 
 * @author Michal Kubacki
 * @since 2017-12-15
 *
 */
@Service
public class TurnierService {

	// Required repositories as by Ports and Adapters Pattern:
	private final NutzerRepository nutzerRepository;
	private final TurnierRepository turnierRepository;
	private final TurnierBracketRepository turnierBracketRepository;
	private final AdminRepository adminRepository;

	/**
	 * Konstruktor für den TurnierService
	 * 
	 * @param nutzerRepository
	 *            Nimmt sich das Nutzer Repository Interface
	 * @param turnierRepository
	 *            Nimmt sich das Turnier Repository Interface
	 * @param adminRepository
	 *            Nimmt sich das Admin Repository Interface
	 * @param turnierBrackRepository
	 *            Nimmt sich das TurnierBracket Repository Interface
	 */
	@Autowired
	public TurnierService(final NutzerRepository nutzerRepository,
			final TurnierBracketRepository turnierBracketRepository, final TurnierRepository turnierRepository,
			final AdminRepository adminRepository) {
		this.nutzerRepository = nutzerRepository;
		this.turnierRepository = turnierRepository;
		this.turnierBracketRepository = turnierBracketRepository;
		this.adminRepository = adminRepository;
	}

	// Admin

	/**
	 * Command: Kreirt einen neuen Admin
	 * 
	 * @param adminname
	 *            Der Name vom Admin
	 * @param passwort
	 *            Das Passwort vom Admin
	 * @return admin gibt der erstellten Admin in DB zurück
	 * @throws NeuesPasswortNotAllowedExc
	 *             Passwort wuerde nicht zugelassen
	 * @throws AdminnameSchonHinterlegtExc
	 *             adminname befindet sich schon im DB
	 */
	public Admin adminSpeichern(final String adminname, final String passwort)
			throws NeuesPasswortNotAllowedExc, AdminnameSchonHinterlegtExc {
		if (!(passwortLaengePruefen(passwort))) {
			throw create(NeuesPasswortNotAllowedExc.class, passwort, adminname);
		}
		Admin findAdmin = adminRepository.find(adminname);
		if (findAdmin != null) {
			throw create(AdminnameSchonHinterlegtExc.class, adminname, findAdmin.getId());
		}
		Admin admin = new Admin(adminname, passwort);
		return adminRepository.save(admin);
	}

	// Nutzer	
	/**Command: Kreirt einen neuen Nutzer
	 * 
	 * @param name Der Name vom Nutzer
	 * @param vorname Der Vorname vom Nutzer
	 * @param nutzername Der einzigartige Name vom Nutzer
	 * @param passwort Das Posswort vom Nutzer
	 * @param email Das Email vom Nutzer
	 * @return nutzer Gibt der erstellten Nutzer zurück.
	 * @throws NeuesPasswortNotAllowedExc passwort ist kürzer als 6 Zeichen oder länger als 255 Zeichen
	 * @throws ThatsNotAnEmailExc email entpricht nicht die Email Norm	
	 * @throws EmailSchonHinterlegtExc email hat schon ein anderer Benutzer hinterlegt
	 * @throws BenutzernameSchonHinterlegtExc benutzername befindet sich schon im Datenbank
	 */
	public Nutzer nutzerSpeichern(final String name, final String vorname, final String nutzername,
			final String passwort, final String email) throws NeuesPasswortNotAllowedExc, ThatsNotAnEmailExc,
			EmailSchonHinterlegtExc, BenutzernameSchonHinterlegtExc {
		if (!(passwortLaengePruefen(passwort))) {

			throw create(NeuesPasswortNotAllowedExc.class, passwort, nutzername);
		}
		if (!(emailAufGueltigkeitPruefen(email))) {
			throw create(ThatsNotAnEmailExc.class, email);
		}
		final Nutzer findNutzer = findNutzerByEmail(email);
		if (findNutzer != null) {
			throw create(EmailSchonHinterlegtExc.class, email, findNutzer.getId());
		}
		final Nutzer findNutzerNutzername = findNutzerByNutzername(nutzername);
		if (findNutzerNutzername != null) {
			throw create(BenutzernameSchonHinterlegtExc.class, nutzername, findNutzerNutzername.getId());

		}
		return nutzerRepository.save(new Nutzer(name, vorname, nutzername, passwort, email));

	}

	/**
	 * Command: ändert Eigenschaften der Nutzer
	 * 
	 * @param adminname
	 *            Name vom Admin
	 * @param nutzername
	 *            alter Nutzername vom Nutzer
	 * @param name
	 *            neuer Name vom Nutzer
	 * @param vorname
	 *            neuer vorname vom Nutzer
	 * @param neuerNutzername
	 *            neuer Nutzername vom Nutzer
	 * @param passwort
	 *            neues Passwort
	 * @param email
	 *            neue Email
	 * @throws NeuesPasswortNotAllowedExc
	 *             passwort ist kürzer als 6 Zeichen oder länger als 255 Zeichen
	 * @throws ThatsNotAnEmailExc
	 *             email entpricht nicht die Email Norm
	 * @throws EmailSchonHinterlegtExc
	 *             email hat schon ein anderer Benutzer hinterlegt
	 * @throws BenutzernameSchonHinterlegtExc
	 *             benutzername befindet sich schon im Datenbank
	 * @throws NutzerGibtEsNichtExc
	 *             Nutzername existiert im DB nicht
	 * @throws AdminExistiertNichtExc
	 *             Adminname existiert im DB nicht
	 */
	public void aendereNutzer(final String adminname, final String nutzername, final String name, final String vorname,
			final String neuerNutzername, final String passwort, final String email)
			throws NeuesPasswortNotAllowedExc, ThatsNotAnEmailExc, EmailSchonHinterlegtExc,
			BenutzernameSchonHinterlegtExc, NutzerGibtEsNichtExc, AdminExistiertNichtExc {
		if (!(passwortLaengePruefen(passwort))) {
			throw create(NeuesPasswortNotAllowedExc.class, passwort, nutzername);
		}
		if (!(emailAufGueltigkeitPruefen(email))) {
			throw create(ThatsNotAnEmailExc.class, email);
		}
		final Admin admin = adminRepository.find(adminname);
		if (admin == null) {
			throw create(AdminExistiertNichtExc.class, adminname);
		}
		final Nutzer nutzer = findNutzerByNutzername(nutzername);
		if (nutzer == null) {
			throw create(NutzerGibtEsNichtExc.class, nutzername);
		}
		final Nutzer findNutzer = findNutzerByEmail(email);
		if (findNutzer != null && findNutzer.getId() != nutzer.getId()) {
			throw create(EmailSchonHinterlegtExc.class, email, findNutzer.getId());
		}
		final Nutzer findNutzerNutzername = findNutzerByNutzername(neuerNutzername);
		if (findNutzerNutzername != null && nutzer.getId() != findNutzerNutzername.getId()) {
			throw create(BenutzernameSchonHinterlegtExc.class, neuerNutzername, findNutzerNutzername.getId());
		}
		nutzer.fuerAdminNutzerAendern(name, vorname, neuerNutzername, passwort, email);
		nutzerRepository.save(nutzer);

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
	 * @throws NeuesPasswortNotAllowedExc
	 *             passwort ist kürzer als 6 Zeichen oder länger als 255 Zeichen
	 */
	public Nutzer nutzerPasswortAendern(final Nutzer nutzer, final String altesPasswort, final String neuesPasswort)
			throws NeuesPasswortNotAllowedExc {
		if (!(passwortLaengePruefen(neuesPasswort))) {

			throw create(NeuesPasswortNotAllowedExc.class, neuesPasswort, nutzer.getNutzername());
		}

		nutzer.passwortAendern(altesPasswort, neuesPasswort);
		return nutzerRepository.save(nutzer);

	}

	/**
	 * Command: Löscht den gegebenen {@link Nutzer}
	 * 
	 * @param nutzer
	 *            Das Nutzer Objekt was geloescht werden soll.
	 * @param passwort
	 *            Das passwort das zum verifizieren gebraucht wird.
	 */
	public void nutzerLoeschen(final Nutzer nutzer, final String passwort) {
		if (nutzer.getPasswort().equals(passwort)) {

			nutzerRepository.delete(nutzer.getId());
		}
	}

	/**
	 * Command: Updatet die E-Mail Adresse des Nutzers {@link Nutzer} gegen einer neuen E-Mail Adresse
	 * @param nutzer Nutzer, der die Email-Adresse ändern will
	 * @param passwort passwort zur Verifizierung
	 * @param email neue Email auf die man das ändern will
	 * @throws ThatsNotAnEmailExc
	 * @throws EmailSchonHinterlegtExc
	 */
	public void emailAendern(final Nutzer nutzer, final String passwort, final String email)
			throws ThatsNotAnEmailExc, EmailSchonHinterlegtExc {
		if (!(emailAufGueltigkeitPruefen(email))) {
			throw create(ThatsNotAnEmailExc.class, email);
		}

		Nutzer findNutzer = nutzerRepository.findEmail(email);
		if (findNutzer != null) {
			throw create(EmailSchonHinterlegtExc.class, email, findNutzer.getId());
		}

		nutzer.emailAendern(email, passwort);
		nutzerRepository.save(nutzer);
	}

	// Turnier
	/**
	 * Command: startet das Turnier
	 * 
	 * @param turnier
	 *            das Turnier, das gestartet werden soll
	 * @return turnier, der gestartet wurde
	 * @throws AnzahlTeilnehmerNoPowerOfTwoExc
	 *             Anzahl der Teilnehmer ist keine Potenz von zwei
	 */
	public Turnier turnierStarten(Turnier turnier) throws AnzahlTeilnehmerNoPowerOfTwoExc {
		List<Nutzer> nutzer = turnier.getTeilnehmer();
		if (!(istPotenzVonZwei(nutzer.size()))) {

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

	/**
	 * Command: Ergebnisse von einem TurnierBracket werden im Turnier gesetzt
	 * 
	 * @param turnier
	 *            Ergebnisse von einem TurnierBracket von diesem Turnier
	 * @param position
	 *            Stelle des TurnierBrackets
	 * @param ergebnis1
	 *            Ergebnis vom ersten Nutzer
	 * @param ergebnis2
	 *            Ergebnis vom zweiten Nutzer
	 * @throws TurnierStatusFailExc
	 *             turnier befindet sich nicht im Status gestartet
	 */
	public void setteErgebnisse(Turnier turnier, int position, int ergebnis1, int ergebnis2)
			throws TurnierStatusFailExc {
		if (turnier.getTurnierStatus() != TurnierStatus.GESTARTET) {
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
		} else {
			for (int i = 0; i < size; i = i + 2) {
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
	 * Command: Entfernt den gegeben Nutzer aus dem gegebenen Turnier.
	 * 
	 * @param owner
	 *            Der Organisator des Turniers
	 * @param turnier
	 *            Das Turnier wovon der Teilnehmer entfernt werden soll.
	 * @param nutzer
	 *            Der Nutzer der Entfernt werden soll
	 * @throws TurnierGibtEsNichtExc
	 *             Turnier mit diesem Name existiert nicht im Db
	 * @throws TeilnehmerGibtEsNichtExc
	 *             Nutzer mit diesem Name existiert nicht in diesem Turnier
	 * @throws EsIstNichtDeinTurnierExc
	 *             Dieses Turnier hast Du nicht erstellt
	 */
	public void entferneTeilnehmer(Nutzer owner, Turnier turnier, Nutzer nutzer)
			throws TurnierGibtEsNichtExc, TeilnehmerGibtEsNichtExc, EsIstNichtDeinTurnierExc {
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
	 * Command: Erstellt ein Turnier
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
	 * @throws ZuVieleTeilnehmerExc
	 *             max. Anzahl der Teilnehmer ist größer als 32
	 * @throws ZuWenigTeilnehmerExc
	 *             max. Anzahl der Teilnehmer ist kleiner als 2
	 * @throws KeineRichtigeEingabenTurnierExc
	 *             Adresselänge ist kürzer als 4 oder beinhaltet Nummern
	 * @throws TurniernameSchonHinterlegtExc
	 *             ein Turnier mit diesem Name existiert schon im DB
	 */
	public Turnier turnierErstellen(String name, String adresse, String datum, String uhrzeit, Nutzer nutzer,
			int maxTeilnehmer) throws ZuVieleTeilnehmerExc, ZuWenigTeilnehmerExc, KeineRichtigeEingabenTurnierExc,
			TurniernameSchonHinterlegtExc {
		if (maxTeilnehmer > 32)
			throw create(ZuVieleTeilnehmerExc.class, maxTeilnehmer);
		if (maxTeilnehmer < 2)
			throw create(ZuWenigTeilnehmerExc.class, maxTeilnehmer);
		if (adresse.length() <= 3 || turniernameBeinhaltetNummer(name)) {

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
	 * Command: ändert Eigenschaften eines Turniers
	 * 
	 * @param adminname
	 *            Name vom Administrator
	 * @param name
	 *            alter Name vom Turnier
	 * @param neuerName
	 *            neuer Name vom Turnier
	 * @param adresse
	 *            neue Adresse vom Turnier
	 * @param datum
	 *            neues Datum vom Turnier
	 * @param uhrzeit
	 *            neues Uhrzeit vom Turnier
	 * @param maxTeilnehmer
	 *            neue max. Anzahl der Teilnehmer
	 * @throws ZuVieleTeilnehmerExc
	 *             max. Anzahl der Teilnehmer ist größer als 32
	 * @throws ZuWenigTeilnehmerExc
	 *             max. Anzahl der Teilnehmer ist kleiner als 2
	 * @throws KeineRichtigeEingabenTurnierExc
	 *             Adresselänge ist kürzer als 4 oder beinhaltet Nummern
	 * @throws TurniernameSchonHinterlegtExc
	 *             ein Turnier mit diesem Name existiert schon im DB
	 * @throws AdminExistiertNichtExc
	 *             Admin mit diesem Name existiert im DB nicht
	 * @throws TurnierGibtEsNichtExc
	 *             Turnier mit diesem Name existiert im DB nicht
	 */
	public void turnierAendern(final String adminname, final String name, final String neuerName, final String adresse,
			final String datum, final String uhrzeit, final int maxTeilnehmer)
			throws ZuVieleTeilnehmerExc, ZuWenigTeilnehmerExc, KeineRichtigeEingabenTurnierExc,
			TurniernameSchonHinterlegtExc, AdminExistiertNichtExc, TurnierGibtEsNichtExc {
		if (maxTeilnehmer > 32)
			throw create(ZuVieleTeilnehmerExc.class, maxTeilnehmer);
		if (maxTeilnehmer < 2)
			throw create(ZuWenigTeilnehmerExc.class, maxTeilnehmer);
		if (adresse.length() <= 3 || turniernameBeinhaltetNummer(neuerName)) {

			throw create(KeineRichtigeEingabenTurnierExc.class, neuerName);
		}
		final Admin admin = adminRepository.find(adminname);
		if (admin == null) {
			throw create(AdminExistiertNichtExc.class, adminname);
		}
		final Turnier turnier = turnierRepository.find(name);
		if (turnier == null) {
			throw create(TurnierGibtEsNichtExc.class, name);
		}
		final Turnier turnierNeuerName = turnierRepository.find(neuerName);
		if (turnierNeuerName != null && turnier.getId() != turnierNeuerName.getId()) {

			throw create(TurniernameSchonHinterlegtExc.class, name, turnierNeuerName.getId());
		}
		turnier.fuerAdminTurnierAendern(neuerName, adresse, datum, uhrzeit, maxTeilnehmer);
		turnierRepository.save(turnier);
	}

	/**
	 * Command: Löscht das gegebene Turnier vom gegebenen Nutzer
	 * 
	 * @param nutzer
	 *            Der Organisator dessen Turnier geloescht wird.
	 * @param turnier
	 *            Das Turnier was gelöscht wird.
	 * @throws TurnierGibtEsNichtExc
	 *             Turnier mit diesem Name existiert im DB nicht
	 * @throws EsIstNichtDeinTurnierExc
	 *             Dieses Turnier hast Du nicht erstellt
	 */
	public void loescheEigenesTurnier(Nutzer nutzer, Turnier turnier)
			throws TurnierGibtEsNichtExc, EsIstNichtDeinTurnierExc {
		if (turnierRepository.find(turnier.getName()) == null)
			throw create(TurnierGibtEsNichtExc.class, turnier.getName());
		if (!(nutzer.getNutzername().equals(turnier.getOrganisator().getNutzername())))
			throw create(EsIstNichtDeinTurnierExc.class, turnier.getName(), nutzer.getNutzername());
		final List<TurnierBracket> turnierBrackets = turnier.getTurnierBrackets();
		if (!(turnierBrackets.isEmpty())) {
			for (TurnierBracket turnierBracket : turnierBrackets) {

				turnierBracketRepository.delete(turnierBracket);
			}
		}
		turnierRepository.delete(turnier.getId());

	}

	/**
	 * /** Command: Meldet ein Nutzer für das Turnier an
	 * 
	 * @param turnier
	 *            Das Turnier wo der Nutzer sich anmeldet.
	 * @param nutzer
	 *            Der Nutzer der angemeldet werden soll.
	 * @throws DuBistSchonAngemeldetExc
	 *             Dieser Nutzer ist schon für dieses Turneir angemeldet
	 * @throws TurnierStatusFailExc
	 *             Dieses Turnier ist nicht mehr offen
	 */
	public void anTurnierAnmelden(Turnier turnier, Nutzer nutzer)
			throws DuBistSchonAngemeldetExc, TurnierStatusFailExc {
		if (turnier.getTeilnehmer().contains(nutzer))
			throw create(DuBistSchonAngemeldetExc.class, nutzer.getNutzername(), turnier.getName());
		final TurnierStatus turnierStatus = turnier.getTurnierStatus();
		if (turnierStatus != TurnierStatus.OFFEN)
			throw create(TurnierStatusFailExc.class, turnier.getName(), turnierStatus.toString());
		turnier.anTurnierAnmelden(nutzer);
		turnierRepository.save(turnier);
	}

	// queries
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
	 * @return nutzer Gibt den Nutzer, falls gefunden, zurueck.
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
	 * Query: Findet alle Turniere die vom gegebenen Nutzer erstellt worden sind.
	 * 
	 * @param organisator
	 *            Der Organisator wessen Turniere gelistet werden soll.
	 * @return Gibt eine Liste von Turnieren zurück der der gegebene Nutzer erstellt
	 *         hat.
	 */
	public List<Turnier> findTurnierByOrganisator(final Nutzer organisator) {

		return turnierRepository.findTurniereVonNutzer(organisator);
	}

	/** Query: Findet alle erstellten Nutzer die nach Nutzernamen sortiert sind. */
	public List<Nutzer> findAllNutzers() {
		return nutzerRepository.findAll();
	}

	// private Methoden

	/**
	 * Query: prüft, ob Länge vom Passwort großer als 5 oder kleiner als 255 ist
	 * 
	 * @param passwort
	 *            Passwort zum prüfen
	 * @return wahr, wenn Länge zugelassen ist, anderfalls false
	 */
	private Boolean passwortLaengePruefen(final String passwort) {
		final int passwortLength = passwort.length();
		if (passwortLength <= 5 || passwortLength >= 255) {
			return false;
		}
		return true;
	}

	/**
	 * Query: prüft, ob Emailadressennorm gültig sind
	 * 
	 * @param email
	 *            email zum prüfen
	 * @return wahr, wenn Email zugelassen wird, anderfalls false
	 */
	private Boolean emailAufGueltigkeitPruefen(final String email) {
		final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
				Pattern.CASE_INSENSITIVE);
		final Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		return (matcher.find());

	}

	/**
	 * Query: Prüft ob Anzahl der Teilnehmer eine Potenz von zwei ist
	 * 
	 * @param nummer
	 *            Nummer ist Teilnehmeranzahl
	 * @return Gibt zurück ob Anzahl der Teilnehmer eine Potenz von zwei ist.
	 */
	private boolean istPotenzVonZwei(int nummer) {

		return nummer >= 2 && ((nummer & (nummer - 1)) == 0);
	}

	/**
	 * Query: prüft, ob der Turniername eine Nummer beihnaltet
	 * 
	 * @param name
	 *            name vom Turnier zum prüfen
	 * @return wahr, wenn Turniername mindestens eine Nummer beinhaltet, anderfalls
	 *         false
	 */
	private Boolean turniernameBeinhaltetNummer(final String name) {
		return name.matches(".*\\d+.*");

	}

	// Exceptions

	/**
	 * Anzahl der Teilnehmer {0} ist keine Potenz von zwei.
	 * 
	 */
	@SuppressWarnings("serial")
	public static class AnzahlTeilnehmerNoPowerOfTwoExc extends multex.Exc {
	}

	/** Nutzername {0} ist schon im Turnier {1} angemeldet */
	@SuppressWarnings("serial")
	public static class DuBistSchonAngemeldetExc extends multex.Exc {
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

	/** Nutzer {0} existiert gar nicht im Turnier Webapp */
	@SuppressWarnings("serial")
	public static class NutzerGibtEsNichtExc extends multex.Exc {
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
	 * Neues Passwort {0} für {1} ist kürzer als 6 Zeichen oder länger als 255
	 * Zeichen.
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

	/** Es existiert schon ein Admin mit diesem Adminname {0} mit dem ID {1} */
	@SuppressWarnings("serial")
	public static class AdminnameSchonHinterlegtExc extends multex.Exc {
	}

	/** Es existiert schon ein Turnier mit diesem Turniername {0} mit dem ID {1} */
	@SuppressWarnings("serial")
	public static class TurniernameSchonHinterlegtExc extends multex.Exc {
	}

	/** Admin mit dem Name {0} existiert nicht */
	@SuppressWarnings("serial")
	public static class AdminExistiertNichtExc extends multex.Exc {
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

}