package turnier.webapp.rest_interface;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import multex.Exc;
import turnier.webapp.domain.Admin;
import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.Turnier;
import turnier.webapp.domain.TurnierService;

import static multex.MultexUtil.create;

/**
 * A Spring Web MVC controller offering a REST service for accessing all
 * external operations of the application.
 */
// Made transactional according to the answer of Rogério at
// https://stackoverflow.com/questions/23118789/why-we-shouldnt-make-a-spring-mvc-controller-transactional
@Transactional
@RestController
public class ApplicationController {

	private final TurnierService turnierService;

	private final String className = getClass().getSimpleName();

	@Autowired
	public ApplicationController(final TurnierService turnierService) {
		this.turnierService = turnierService;
	}
	/*
	 * A good resource for the design of REST URIs is
	 * https://blog.mwaysolutions.com/2014/06/05/10-best-practices-for-better-
	 * restful-api/ But for simplification of access control we will group the URIs
	 * by the roles, which may access them. So URIs starting with /bank are for
	 * bankers, URIs starting with /client are for clients. For further
	 * simplification we will not include the username of a Client into his URI, but
	 * each request to a URI starting with /client will infer the concerned username
	 * from the authenticated user.
	 */

	// Für alle:
	//
	@GetMapping(path = "/")
	public ResponseEntity<String> home(final WebSecurityConfig config, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		final ResponseEntity<String> responseEntity = new ResponseEntity<>(
				"Welcome to the Turnier Webapp. Predefined users with empty passwords are "
						+ config.predefinedUsernames() + ". Use URIs under /nutzer/ or /admin/",
				HttpStatus.OK);
		return responseEntity;
	}

	// Für die Admin Rolle unter URI /admin:
	/**
	 * kreirt einen neuen Admin Objekt und speichert ihn im DB
	 */
	@PostMapping("/admin/")
	public ResponseEntity<AdminResource> createAdmin(@RequestBody final AdminResource adminResource,
			final HttpMethod method, final WebRequest request) {
		_print(method, request);
		if (adminResource.id != null) {
			throw create(AdminMitIdErstelltExc.class, adminResource.adminname, adminResource.id);
		}
		final Admin adminSave = turnierService.adminSpeichern(adminResource.adminname, adminResource.passwort);

		return new ResponseEntity<>(new AdminResource(adminSave), HttpStatus.CREATED);

	}

	/**
	 * ändert die Eigenschaften des Nutzers
	 * 
	 * @param adminname
	 *            Der Name vom Admin wird im Pfad eingebeben
	 * @param nutzername
	 *            Der Name vom Nutzer wird im Pfad eingegeben
	 */
	@PutMapping("/admin/{adminname}/nutzer/{nutzername}")
	public ResponseEntity<NutzerResource> changeNutzer(@RequestBody final NutzerResource nutzerResource,
			@PathVariable final String adminname, @PathVariable final String nutzername, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		if (nutzerResource.id != null) {
			throw create(NutzerMitIdErstelltExc.class, nutzerResource.nutzername, nutzerResource.id);
		}
		String name = nutzerResource.name;
		String vorname = nutzerResource.vorname;
		String neuerNutzername = nutzerResource.nutzername;
		String passwort = nutzerResource.passwort;
		String email = nutzerResource.email;
		final Nutzer nutzerBevor = turnierService.findeNutzerMitNutzername(nutzername);
		if (nutzerBevor == null) {
			throw create(NutzerGibtEsNichtExc.class, nutzername);
		}

		if (name == null) {

			name = nutzerBevor.getName();
		}

		if (vorname == null) {

			vorname = nutzerBevor.getVorname();
		}

		if (neuerNutzername == null) {

			neuerNutzername = nutzerBevor.getNutzername();
		}

		if (passwort == null) {

			passwort = nutzerBevor.getPasswort();
		}

		if (email == null) {
			email = nutzerBevor.getEmail();
		}

		turnierService.aendereNutzer(adminname, nutzername, name, vorname, neuerNutzername, passwort, email);
		final Nutzer nutzerSave = turnierService.findeNutzerMitNutzername(neuerNutzername);

		return new ResponseEntity<>(new NutzerResource(nutzerSave), HttpStatus.ACCEPTED);

	}

	/**
	 * ändert Eigenschaften des Turniers
	 * 
	 * @param adminname
	 *            Der Name vom Admin wird im Pfad eingegeben.
	 * @param turniername
	 *            Der Name vom Turnier wird im Pfad eingegeben
	 */
	@PutMapping("/admin/{adminname}/turnier/{turniername}")
	public ResponseEntity<TurnierResource> changeTurnier(@RequestBody final TurnierResource turnierResource,
			@PathVariable final String adminname, @PathVariable final String turniername, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		if (turnierResource.id != null) {
			throw create(TurnierMitIdErstelltExc.class, turnierResource.name, turnierResource.id);
		}
		final Turnier turnierBevor = turnierService.findeTurnierMitName(turniername);
		if (turnierBevor == null) {
			throw create(TurnierGibtEsNichtExc.class, turniername);
		}
		String neuerName = turnierResource.name;
		String adresse = turnierResource.adresse;
		String datum = turnierResource.datum;
		String uhrzeit = turnierResource.uhrzeit;
		int maxTeilnehmer = turnierResource.maxTeilnehmer;

		if (neuerName == null) {
			neuerName = turnierBevor.getName();
		}

		if (adresse == null) {
			adresse = turnierBevor.getAdresse();
		}
		if (datum == null) {
			datum = turnierBevor.getDatum();
		}
		if (uhrzeit == null) {
			uhrzeit = turnierBevor.getUhrzeit();
		}

		if (maxTeilnehmer == 0) {
			maxTeilnehmer = turnierBevor.getMaxTeilnehmer();

		}

		turnierService.turnierAendern(adminname, turniername, neuerName, adresse, datum, uhrzeit, maxTeilnehmer);

		final Turnier turnierSave = turnierService.findeTurnierMitName(neuerName);

		return new ResponseEntity<>(new TurnierResource(turnierSave), HttpStatus.ACCEPTED);

	}

	// Für die Nutzer Rolle unter URI /nutzer:

	/** füge ein neuer Nutzer Objekt in unserem DB hinzu */

	@PostMapping("/nutzer/")
	public ResponseEntity<NutzerResource> createNutzer(@RequestBody final NutzerResource nutzerResource,
			final HttpMethod method, final WebRequest request) {
		_print(method, request);
		if (nutzerResource.id != null) {
			throw create(NutzerMitIdErstelltExc.class, nutzerResource.nutzername, nutzerResource.id);
		}
		final Nutzer nutzerSave = turnierService.nutzerSpeichern(nutzerResource.name, nutzerResource.vorname,
				nutzerResource.nutzername, nutzerResource.passwort, nutzerResource.email);
		return new ResponseEntity<>(new NutzerResource(nutzerSave), HttpStatus.CREATED);
	}

	/**
	 * fügt ein neuer Turnier Objekt mit dem Nutzer mit dem übergebenen nutzername
	 * als Veranstalter hinzu
	 * 
	 * @param nutzername
	 *            Der Name vom Nutzer wird im Pfad eingegeben
	 */
	@PostMapping("/nutzer/{nutzername}/turnier/")
	public ResponseEntity<TurnierResource> createNutzer(@RequestBody final TurnierResource turnierResource,
			@PathVariable final String nutzername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		if (turnierResource.id != null) {
			throw create(TurnierMitIdErstelltExc.class, turnierResource.name, turnierResource.id);
		}
		final Nutzer findNutzer = turnierService.findeNutzerMitNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerGibtEsNichtExc.class, nutzername);
		}
		final Turnier turnier = turnierService.turnierErstellen(turnierResource.name, turnierResource.adresse,
				turnierResource.datum, turnierResource.uhrzeit, findNutzer, turnierResource.maxTeilnehmer);

		return new ResponseEntity<>(new TurnierResource(turnier), HttpStatus.CREATED);
	}

	/**
	 * fügt teilnehmer mit dem übergebenen nutzername im Turnier mit übergebenen
	 * turniername hinzu
	 * 
	 * @param turniername
	 *            Der Name vom Turnier wird im Pfad eingegeben
	 * @param nutzername
	 *            Der Name vom Nutzer wird im Pfad eingegeben
	 */
	@PutMapping("/nutzer/turnier/{turniername}/{nutzername}")
	public ResponseEntity<TurnierResource> addTeilnehnmerToTurnier(@PathVariable final String nutzername,
			@PathVariable final String turniername, final HttpMethod method, final WebRequest request) {
		_print(method, request);

		final Nutzer findNutzer = turnierService.findeNutzerMitNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerGibtEsNichtExc.class, nutzername);
		}
		final Turnier findTurnier = turnierService.findeTurnierMitName(turniername);
		turnierService.anTurnierAnmelden(findTurnier, findNutzer);
		return new ResponseEntity<>(new TurnierResource(findTurnier), HttpStatus.ACCEPTED);
	}

	/**
	 * starte turnier mit dem übergebenen turniername nur, wenn der Nutzer mit dem
	 * übergebenen nutzername der organisator von diesem Turnier ist
	 * 
	 * @param turniername
	 *            Der Name vom Turnier wird im Pfad eingegeben
	 * @param nutzername
	 *            Der Name vom Nutzer wird im Pfad eingegeben
	 * 
	 */
	@PutMapping("/nutzer/turnier/start/{turniername}/{nutzername}/")
	public ResponseEntity<TurnierResource> starteTurnier(@PathVariable final String nutzername,
			@PathVariable final String turniername, final HttpMethod method, final WebRequest request) {
		_print(method, request);

		final Nutzer findNutzer = turnierService.findeNutzerMitNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerGibtEsNichtExc.class, nutzername);
		}
		final Turnier findTurnier = turnierService.findeTurnierMitName(turniername);
		if (findTurnier == null) {

			throw create(TurnierGibtEsNichtExc.class, turniername);
		}
		if (!(findTurnier.getOrganisator().equals(findNutzer))) {
			final String organisator = findTurnier.getOrganisator().getName();
			throw create(EsIstNichtDeinTurnierExc.class, nutzername, turniername, organisator);
		}
		final Turnier turnier = turnierService.turnierStarten(findTurnier);
		return new ResponseEntity<>(new TurnierResource(turnier), HttpStatus.ACCEPTED);
	}

	/**
	 * übergibt die TurnierBracketErgebnisse eines Turniers
	 * 
	 * @param turniername
	 *            Der Name vom Turnier wird im Pfad eingegeben
	 * @param position
	 *            Stelle im TurnierBracket wird im Pfad eingegeben
	 * @param erg1
	 *            Das Ergebnis vom ersten Nutzer wird im Pfad eingegeben
	 * @param erg2
	 *            Das Ergebnis vom zweiten Nutzer wird im Pfad eingegeben
	 * 
	 */
	@PutMapping("/nutzer/turnier/start/{turniername}/{position}/{erg1}/{erg2}")
	public ResponseEntity<TurnierResource> setteErgebnisseImTurnierBracket(@PathVariable final String position,
			@PathVariable final String turniername, @PathVariable final String erg1, @PathVariable final String erg2,
			final HttpMethod method, final WebRequest request) {
		_print(method, request);

		final Turnier findTurnier = turnierService.findeTurnierMitName(turniername);
		if (findTurnier == null) {

			throw create(TurnierGibtEsNichtExc.class, turniername);
		}
		turnierService.setteErgebnisse(findTurnier, Integer.parseInt(position), Integer.parseInt(erg1),
				Integer.parseInt(erg2));
		return new ResponseEntity<>(new TurnierResource(findTurnier), HttpStatus.ACCEPTED);
	}

	/**
	 * entferne teilnehmer mit dem übergebenen nutzername aus dem Turnier mit dem
	 * übergebenen turniername
	 * 
	 * @param turniername
	 *            Der Name vom Turnier wird im Pfad eingegeben
	 * @param nutzername
	 *            Der Name vom Nutzer wird im Pfad eingegeben
	 */
	@DeleteMapping("/nutzer/turnier/{turniername}/{nutzername}/delete")
	public ResponseEntity<TurnierResource> entferneTeilnehnmerAusTurnier(@PathVariable final String nutzername,
			@PathVariable final String turniername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		final Turnier findTurnierByName = turnierService.findeTurnierMitName(turniername);
		final Nutzer findNutzer = findTurnierByName.teilnehmerSuchen(nutzername);
		findTurnierByName.entferneTeilnehmer(findNutzer);
		return new ResponseEntity<>(new TurnierResource(findTurnierByName), HttpStatus.ACCEPTED);
	}

	/**
	 * löscht dem Nutzer aus DB
	 * 
	 * @param nutzername
	 *            Der Name vom Nutzer wird im Pfad eingegeben
	 */
	@DeleteMapping("/nutzer/{nutzername}")
	public ResponseEntity<DeleteNutzerCommand> deleteNutzer(@RequestBody final DeleteNutzerCommand verifyPassword,
			@PathVariable final String nutzername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		final Nutzer deleteNutzer = turnierService.findeNutzerMitNutzername(nutzername);
		turnierService.nutzerLoeschen(deleteNutzer, verifyPassword.verifyPasswd);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * gibt dem Nutzer Objekt mit dem übergebenen Nutzername zurück
	 * 
	 * @param nutzername
	 *            Der Name vom Nutzer wird im Pfad eingegeben
	 * 
	 */

	@GetMapping(path = "/nutzer/{nutzername}")
	public ResponseEntity<NutzerResource> findNutzer(@PathVariable final String nutzername, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		final Nutzer findNutzer = turnierService.findeNutzerMitNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerGibtEsNichtExc.class, nutzername);

		}
		return new ResponseEntity<>(new NutzerResource(findNutzer), HttpStatus.OK);
	}

	/** finde alle nutzer Objekte im DB */
	@GetMapping(path = "/nutzer/")
	public ResponseEntity<NutzerResource[]> findNutzers(final HttpMethod method, final WebRequest request) {
		_print(method, request);
		final List<Nutzer> nutzers = turnierService.findeAlleNutzer();
		;
		if (nutzers.isEmpty()) {
			throw new Exc("Kein Nutzer Objekt im unseren DB");
		}
		return _nutzersToResources(nutzers);
	}

	/** finde alle turnier Objekte im DB */
	@GetMapping(path = "/nutzer/turnier/")
	public ResponseEntity<TurnierResource[]> findTurniers(final HttpMethod method, final WebRequest request) {
		_print(method, request);
		final List<Turnier> turniers = turnierService.findeAlleTurniere();

		if (turniers.isEmpty()) {
			throw new Exc("Kein Turnier Objekt im unseren DB!");
		}
		return _turniersToResources(turniers);
	}

	/**
	 * finde alle Turniere Objekte im DB, wo der Nutzer mit dem übergebenen
	 * nutzername der Organisator ist.
	 * 
	 * @param nutzername
	 *            Der Name vom Nutzer wird im Pfad eingegeben
	 * 
	 */
	@GetMapping(path = "/nutzer/{nutzername}/turnier/")
	public ResponseEntity<TurnierResource[]> findOrganisatorTurniers(@PathVariable final String nutzername,
			final HttpMethod method, final WebRequest request) {
		_print(method, request);
		final Nutzer nutzer = turnierService.findeNutzerMitNutzername(nutzername);
		if (nutzer == null) {

			throw create(NutzerGibtEsNichtExc.class, nutzername);
		}
		final List<Turnier> turniers = turnierService.findeTurnierMitVeranstalter(nutzer);

		if (turniers == null) {
			throw new Exc("No Turnier Object in our DB!");
		}
		return _turniersToResources(turniers);
	}

	/** hilfmethode zum transformation von nutzer objekt ins Nutzer Resource */
	private ResponseEntity<NutzerResource[]> _nutzersToResources(List<Nutzer> nutzers) {
		final Stream<NutzerResource> result = nutzers.stream().map(c -> new NutzerResource(c));
		final NutzerResource[] resultArray = result.toArray(size -> new NutzerResource[size]);
		return new ResponseEntity<>(resultArray, HttpStatus.OK);
	}

	/** hilfmethode zum transformation von turnier objekt ins Turnier Resource */
	private ResponseEntity<TurnierResource[]> _turniersToResources(List<Turnier> nutzers) {
		final Stream<TurnierResource> result = nutzers.stream().map(c -> new TurnierResource(c));
		final TurnierResource[] resultArray = result.toArray(size -> new TurnierResource[size]);
		return new ResponseEntity<>(resultArray, HttpStatus.OK);

	}

	/**
	 * gibt dem Turnier Objekt als JSON mit dem übergebenen turniername zurück
	 * 
	 * @param turniername
	 *            Der Name vom Turnier wird im Pfad eingegeben
	 */
	@GetMapping(path = "/nutzer/turnier/{turniername}")
	public ResponseEntity<TurnierResource> findTurnier(@PathVariable final String turniername, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		final Turnier findTurnier = turnierService.findeTurnierMitName(turniername);
		if (findTurnier == null) {
			throw create(TurnierGibtEsNichtExc.class, turniername);
		}
		return new ResponseEntity<>(new TurnierResource(findTurnier), HttpStatus.OK);
	}

	/**
	 * zeigt Ergebnisse eines Turniers, das schon beendet ist
	 * 
	 * @param turniername
	 *            Der Name vom Turnier wird im Pfad eingegeben
	 */
	@GetMapping(path = "/nutzer/turnier/{turniername}/ergebnisse")
	public ResponseEntity<String> zeigeErgebnisse(@PathVariable final String turniername, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		final Turnier findTurnier = turnierService.findeTurnierMitName(turniername);
		final String response = findTurnier.getTurnierErgebnisse();
		final ResponseEntity<String> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		return responseEntity;
	}

	/**
	 * lösche dem Turnier mit dem übergebenen turniername, wenn der Nutzer mit dem
	 * übergebenen nutzername Organisator ist
	 * 
	 * @param nutzername
	 *            Der Name vom Nutzer
	 * @param turniername
	 *            Der Name vom Turnier
	 */
	@DeleteMapping(path = "/nutzer/{nutzername}/turnier/{turniername}")
	public ResponseEntity<TurnierResource> deleteTurnier(@PathVariable final String nutzername,
			@PathVariable final String turniername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		final Nutzer findNutzer = turnierService.findeNutzerMitNutzername(nutzername);
		if (findNutzer == null) {

			throw create(NutzerGibtEsNichtExc.class, nutzername);
		}
		final Turnier findTurnier = turnierService.findeTurnierMitName(turniername);
		if (findTurnier == null) {
			throw create(TurnierGibtEsNichtExc.class, turniername);
		}
		turnierService.loescheEigenesTurnier(findNutzer, findTurnier);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * ändert die Emailadresse vom Nutzer
	 * 
	 * @param nutzername
	 *            Der Name vom Nutzer
	 */
	@PutMapping(path = "/nutzer/email/{nutzername}")
	public ResponseEntity<NutzerResource> updateEmail(@RequestBody final EmailChangeCommand command,
			@PathVariable final String nutzername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		final Nutzer findNutzer = turnierService.findeNutzerMitNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerGibtEsNichtExc.class, nutzername);

		}
		turnierService.emailAendern(findNutzer, command.verifyPasswd, command.newEmail);
		return new ResponseEntity<>(new NutzerResource(findNutzer), HttpStatus.OK);
	}

	/**
	 * ändert das Passwort vom Nutzer
	 * 
	 * @param nutzername
	 *            Der Name vom Nutzer
	 */
	@PutMapping(path = "/nutzer/password/{nutzername}")
	public ResponseEntity<NutzerResource> updatePassword(@RequestBody final PasswordChangeCommand command,
			@PathVariable final String nutzername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		Nutzer findNutzer = turnierService.findeNutzerMitNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerGibtEsNichtExc.class, nutzername);

		}
		findNutzer = turnierService.nutzerPasswortAendern(findNutzer, command.verifyPasswd, command.newPassword);
		return new ResponseEntity<>(new NutzerResource(findNutzer), HttpStatus.OK);
	}

	/**
	 * Prints a message containing the current class name, the HTTP method, and
	 * infos about the current request.
	 */
	private void _print(final HttpMethod method, final WebRequest request) {
		System.out.printf("%s %s %s\n", className, method, request);
	}

	/**
	 * Turnier {0} sollte kein ID-Parameter übergeben werden, wurde ihn aber {1}
	 * uebergeben
	 */
	public static class TurnierMitIdErstelltExc extends multex.Exc {
	}

	/**
	 * Admin {0} sollte kein ID Parameter ID übergeben werden, wurde ihn aber {1}
	 * uebergeben
	 */
	public static class AdminMitIdErstelltExc extends multex.Exc {
	}

	/**
	 * Nutzer {0} sollte kein ID Parameter ID übergeben werden, wurde ihn aber {1}
	 * uebergeben
	 */
	public static class NutzerMitIdErstelltExc extends multex.Exc {
	}

	/** {0} Es ist nicht dein Turnier. Veranstalter vom {1} ist {2} */
	public static class EsIstNichtDeinTurnierExc extends multex.Exc {
	}

	/** Nutzer mit diesem Name {0} existiert nicht in unserem DB */
	public static class NutzerGibtEsNichtExc extends multex.Exc {
	}

	/** Turnier mit diesem Name {0} existiert nicht in unserem DB */
	public static class TurnierGibtEsNichtExc extends multex.Exc {
	}
}
