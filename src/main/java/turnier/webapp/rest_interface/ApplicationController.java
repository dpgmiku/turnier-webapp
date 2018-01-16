package turnier.webapp.rest_interface;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

	// TODO Teilnehmer hinzufügen soll POST sein nicht PUT
	// TODO Teilnehmer entfernen soll DELETE sein nicht PUT

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

	// For everyone (guests):
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

	// For the nutzer role all URIs under /admin:
	@PostMapping("/admin/")
	public ResponseEntity<AdminResource> createAdmin(@RequestBody final AdminResource adminResource,
			final HttpMethod method, final WebRequest request) {
		_print(method, request);
		if (adminResource.id != null) {
			throw create(AdminCreateWithIdExc.class, adminResource.adminname, adminResource.id);
		}
		final Admin adminSave = turnierService.adminSpeichern(adminResource.adminname, adminResource.passwort);

		return new ResponseEntity<>(new AdminResource(adminSave), HttpStatus.CREATED);

	}

	@PutMapping("/admin/{adminname}/nutzer/{nutzername}")
	public ResponseEntity<NutzerResource> changeNutzer(@RequestBody final NutzerResource nutzerResource,
			@PathVariable final String adminname, @PathVariable final String nutzername, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		if (nutzerResource.id != null) {
			throw create(NutzerCreateWithIdExc.class, nutzerResource.nutzername, nutzerResource.id);
		}
		String name = nutzerResource.name;
		String vorname = nutzerResource.vorname;
		String neuerNutzername = nutzerResource.nutzername;
		String passwort = nutzerResource.passwort;
		String email = nutzerResource.email;
		Nutzer nutzerBevor = turnierService.findNutzerByNutzername(nutzername);
		if (nutzerBevor == null) {
			throw create(NutzerArentHereExc.class, nutzername);
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

		turnierService.aendereNutzer(adminname, nutzername, name, vorname, nutzername, passwort, email);
		final Nutzer nutzerSave = turnierService.findNutzerByNutzername(nutzerResource.name);

		return new ResponseEntity<>(new NutzerResource(nutzerSave), HttpStatus.ACCEPTED);

	}

	@PutMapping("/admin/{adminname}/turnier/{turniername}")
	public ResponseEntity<TurnierResource> changeTurnier(@RequestBody final TurnierResource turnierResource,
			@PathVariable final String adminname, @PathVariable final String turniername, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		if (turnierResource.id != null) {
			throw create(TurnierCreateWithIdExc.class, turnierResource.name, turnierResource.id);
		}
		final Turnier turnierBevor = turnierService.findTurnierByName(turniername);
		if (turnierBevor == null) {
			throw create(TurnierArentHereExc.class, turniername);
		}
		String neuerName = turnierResource.name;
		String adresse = turnierResource.adresse;
		String datum = turnierResource.datum;
		String uhrzeit = turnierResource.uhrzeit;
		Integer maxTeilnehmer = turnierResource.maxTeilnehmer;
		
		if (neuerName == null) {
			neuerName = turnierBevor.getName();
		}
		
		if (adresse == null ) {
			adresse = turnierBevor.getAdresse();
		}
		if (datum == null) {
			datum = turnierBevor.getDatum();
		}
		if (uhrzeit == null) {
			uhrzeit = turnierBevor.getUhrzeit();	
		}
		
		if (maxTeilnehmer == null) {
			maxTeilnehmer = turnierBevor.getMaxTeilnehmer();
			
		}
		
		turnierService.turnierAendern(adminname, turniername, neuerName, adresse, datum, uhrzeit, maxTeilnehmer);
		
		final Turnier turnierSave = turnierService.findTurnierByName(neuerName);

		return new ResponseEntity<>(new TurnierResource(turnierSave), HttpStatus.ACCEPTED);

	}

	/**
	 * The admin to be created with adminname {0} must not have an ID, but has {1}
	 */
	public static class AdminCreateWithIdExc extends multex.Exc {
	}

	// For the nutzer role all URIs under /nutzer:

	/* füge ein neuer Nutzer Objekt in unserem DB */

	@PostMapping("/nutzer/")
	public ResponseEntity<NutzerResource> createNutzer(@RequestBody final NutzerResource nutzerResource,
			final HttpMethod method, final WebRequest request) {
		_print(method, request);
		if (nutzerResource.id != null) {
			throw create(NutzerCreateWithIdExc.class, nutzerResource.nutzername, nutzerResource.id);
		}
		final Nutzer nutzerSave = turnierService.nutzerSpeichern(nutzerResource.name, nutzerResource.vorname,
				nutzerResource.nutzername, nutzerResource.passwort, nutzerResource.email);
		return new ResponseEntity<>(new NutzerResource(nutzerSave), HttpStatus.CREATED);
	}

	/**
	 * The nutzer to be created with nutzername {0} must not have an ID, but has {1}
	 */
	public static class NutzerCreateWithIdExc extends multex.Exc {
	}

	/*
	 * fügt ein neuer Turnier Objekt mit dem Nutzer mit dem übergebenen nutzername
	 * als Organisator
	 */
	@PostMapping("/nutzer/{nutzername}/turnier/")
	public ResponseEntity<TurnierResource> createNutzer(@RequestBody final TurnierResource turnierResource,
			@PathVariable final String nutzername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		if (turnierResource.id != null) {
			throw create(TurnierCreateWithIdExc.class, turnierResource.name, turnierResource.id);
		}
		Nutzer findNutzer = turnierService.findNutzerByNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerArentHereExc.class, nutzername);
		}
		Turnier turnier = turnierService.turnierErstellen(turnierResource.name, turnierResource.adresse,
				turnierResource.datum, turnierResource.uhrzeit, findNutzer, turnierResource.maxTeilnehmer);

		return new ResponseEntity<>(new TurnierResource(turnier), HttpStatus.CREATED);
	}

	/*
	 * fügt teilnehmer mit dem übergebenen nutzername im Turnier mit übergebenen
	 * turniername hinzu
	 */
	@PutMapping("/nutzer/turnier/{turniername}/{nutzername}")
	public ResponseEntity<TurnierResource> addTeilnehnmerToTurnier(@PathVariable final String nutzername,
			@PathVariable final String turniername, final HttpMethod method, final WebRequest request) {
		_print(method, request);

		Nutzer findNutzer = turnierService.findNutzerByNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerArentHereExc.class, nutzername);
		}
		Turnier findTurnier = turnierService.findTurnierByName(turniername);
		findTurnier.anTurnierAnmelden(findNutzer);
		return new ResponseEntity<>(new TurnierResource(findTurnier), HttpStatus.ACCEPTED);
	}

	/*
	 * starte turnier mit dem übergebenen turniername nur, wenn der Nutzer mit dem
	 * übergebenen nutzername der organisator von diesem Turnier ist
	 */
	@PutMapping("/nutzer/turnier/start/{turniername}/{nutzername}/")
	public ResponseEntity<TurnierResource> starteTurnier(@PathVariable final String nutzername,
			@PathVariable final String turniername, final HttpMethod method, final WebRequest request) {
		_print(method, request);

		Nutzer findNutzer = turnierService.findNutzerByNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerArentHereExc.class, nutzername);
		}
		Turnier findTurnier = turnierService.findTurnierByName(turniername);
		if (findTurnier == null) {

			throw create(TurnierArentHereExc.class, turniername);
		}
		if (!(findTurnier.getOrganisator().equals(findNutzer))) {
			final String organisator = findTurnier.getOrganisator().getName();
			throw create(NotYourTurnierExc.class, nutzername, turniername, organisator);
		}
		Turnier turnier = turnierService.turnierStarten(findTurnier);
		return new ResponseEntity<>(new TurnierResource(turnier), HttpStatus.ACCEPTED);
	}

	/*
	 * starte turnier mit dem übergebenen turniername nur, wenn der Nutzer mit dem
	 * übergebenen nutzername der organisator von diesem Turnier ist
	 */
	@PutMapping("/nutzer/turnier/start/{turniername}/{position}/{erg1}/{erg2}")
	public ResponseEntity<TurnierResource> setteErgebnisseImTurnierBracket(@PathVariable final String position,
			@PathVariable final String turniername, @PathVariable final String erg1, @PathVariable final String erg2,
			final HttpMethod method, final WebRequest request) {
		_print(method, request);

		Turnier findTurnier = turnierService.findTurnierByName(turniername);
		if (findTurnier == null) {

			throw create(TurnierArentHereExc.class, turniername);
		}
		turnierService.setteErgebnisse(findTurnier, Integer.parseInt(position), Integer.parseInt(erg1),
				Integer.parseInt(erg2));
		return new ResponseEntity<>(new TurnierResource(findTurnier), HttpStatus.ACCEPTED);
	}

	/*
	 * entferne teilnehmer mit dem übergebenen nutzername aus dem Turnier mit dem
	 * übergebenen turniername
	 */
	@PutMapping("/nutzer/turnier/{turniername}/{nutzername}/delete")
	public ResponseEntity<TurnierResource> entferneTeilnehnmerAusTurnier(@PathVariable final String nutzername,
			@PathVariable final String turniername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		Turnier findTurnierByName = turnierService.findTurnierByName(turniername);
		Nutzer findNutzer = findTurnierByName.teilnehmerSuchen(nutzername);
		findTurnierByName.entferneTeilnehmer(findNutzer);
		return new ResponseEntity<>(new TurnierResource(findTurnierByName), HttpStatus.ACCEPTED);
	}

	// deletes nutzer object from db with given nutzername as parameter. The given
	// one password in Request Body must be the same as in db to make it possible
	@DeleteMapping("/nutzer/{nutzername}")
	public ResponseEntity<DeleteNutzerCommand> deleteNutzer(@RequestBody final DeleteNutzerCommand verifyPassword,
			@PathVariable final String nutzername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		// Nutzer dummy = new Nutzer("dummy", "dummy", "dummy","dummy123213",
		// "dummy@dummy.de");
		Nutzer deleteNutzer = turnierService.findNutzerByNutzername(nutzername);
		turnierService.nutzerLoeschen(deleteNutzer, verifyPassword.verifyPasswd);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// returns nutzer Object as JSON with the given nutzername as parameter if one
	// was found in DB
	@GetMapping(path = "/nutzer/{nutzername}")
	public ResponseEntity<NutzerResource> findNutzer(@PathVariable final String nutzername, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		// Nutzer dummy = new Nutzer("dummy", "dummy", "dummy","dummy123213",
		// "dummy@dummy.de");
		Nutzer findNutzer = turnierService.findNutzerByNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerArentHereExc.class, nutzername);

		}
		return new ResponseEntity<>(new NutzerResource(findNutzer), HttpStatus.OK);
	}

	/* finde alle nutzer Objekte im DB */
	@GetMapping(path = "/nutzer/")
	public ResponseEntity<NutzerResource[]> findNutzers(final HttpMethod method, final WebRequest request) {
		_print(method, request);
		final List<Nutzer> nutzers = turnierService.findAllNutzers();
		;
		if (nutzers == null) {
			throw new Exc("No Nutzer Object in our DB!");
		}
		return _nutzersToResources(nutzers);
	}

	/* finde alle turnier Objekte im DB */
	@GetMapping(path = "/nutzer/turnier/")
	public ResponseEntity<TurnierResource[]> findTurniers(final HttpMethod method, final WebRequest request) {
		_print(method, request);
		final List<Turnier> turniers = turnierService.findTurniers();

		if (turniers == null) {
			throw new Exc("No Turnier Object in our DB!");
		}
		return _turniersToResources(turniers);
	}

	/*
	 * finde alle Turniere Objekte im DB, wo der Nutzer mit dem übergebenen
	 * nutzername der Organisator ist.
	 */
	@GetMapping(path = "/nutzer/{nutzername}/turnier/")
	public ResponseEntity<TurnierResource[]> findOrganisatorTurniers(@PathVariable final String nutzername,
			final HttpMethod method, final WebRequest request) {
		_print(method, request);
		final Nutzer nutzer = turnierService.findNutzerByNutzername(nutzername);
		if (nutzer == null) {

			throw create(NutzerArentHereExc.class, nutzername);
		}
		final List<Turnier> turniers = turnierService.findTurnierByOrganisator(nutzer);

		if (turniers == null) {
			throw new Exc("No Turnier Object in our DB!");
		}
		return _turniersToResources(turniers);
	}

	/* hilfmethode zum transformation von nutzer objekt ins Nutzer Resource */
	private ResponseEntity<NutzerResource[]> _nutzersToResources(List<Nutzer> nutzers) {
		final Stream<NutzerResource> result = nutzers.stream().map(c -> new NutzerResource(c));
		final NutzerResource[] resultArray = result.toArray(size -> new NutzerResource[size]);
		return new ResponseEntity<>(resultArray, HttpStatus.OK);
	}

	/* hilfmethode zum transformation von turnier objekt ins Turnier Resource */
	private ResponseEntity<TurnierResource[]> _turniersToResources(List<Turnier> nutzers) {
		final Stream<TurnierResource> result = nutzers.stream().map(c -> new TurnierResource(c));
		final TurnierResource[] resultArray = result.toArray(size -> new TurnierResource[size]);
		return new ResponseEntity<>(resultArray, HttpStatus.OK);

	}

	/* gibt dem Turnier Objekt als JSON mit dem übergebenen turniername zurück */
	@GetMapping(path = "/nutzer/turnier/{turniername}")
	public ResponseEntity<TurnierResource> findTurnier(@PathVariable final String turniername, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		// Nutzer dummy = new Nutzer("dummy", "dummy", "dummy","dummy123213",
		// "dummy@dummy.de");
		Turnier findTurnier = turnierService.findTurnierByName(turniername);
		if (findTurnier == null) {
			throw create(TurnierArentHereExc.class, turniername);
		}
		return new ResponseEntity<>(new TurnierResource(findTurnier), HttpStatus.OK);
	}

	@GetMapping(path = "/nutzer/turnier/{turniername}/ergebnisse")
	public ResponseEntity<String> zeigeErgebnisse(@PathVariable final String turniername, final HttpMethod method,
			final WebRequest request) {
		_print(method, request);
		final Turnier findTurnier = turnierService.findTurnierByName(turniername);
		final String response = findTurnier.getTurnierErgebnisse();
		final ResponseEntity<String> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		return responseEntity;
	}

	/*
	 * lösche dem Turnier mit dem übergebenen turniername, wenn der Nutzer mit dem
	 * übergebenen nutzername Organisator ist
	 */
	@DeleteMapping(path = "/nutzer/{nutzername}/turnier/{turniername}")
	public ResponseEntity<TurnierResource> deleteTurnier(@PathVariable final String nutzername,
			@PathVariable final String turniername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		// Nutzer dummy = new Nutzer("dummy", "dummy", "dummy","dummy123213",
		// "dummy@dummy.de");

		Nutzer findNutzer = turnierService.findNutzerByNutzername(nutzername);
		if (findNutzer == null) {

			throw create(NutzerArentHereExc.class, nutzername);
		}
		Turnier findTurnier = turnierService.findTurnierByName(turniername);
		if (findTurnier == null) {
			throw create(TurnierArentHereExc.class, turniername);
		}
		turnierService.loescheEigenesTurnier(findNutzer, findTurnier);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// change the email of the given nutzername as parameter
	@PutMapping(path = "/nutzer/email/{nutzername}")
	public ResponseEntity<NutzerResource> updateEmail(@RequestBody final EmailChangeCommand command,
			@PathVariable final String nutzername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		// Nutzer dummy = new Nutzer("dummy", "dummy", "dummy","dummy123213",
		// "dummy@dummy.de");
		Nutzer findNutzer = turnierService.findNutzerByNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerArentHereExc.class, nutzername);

		}
		turnierService.updateEmail(findNutzer, command.verifyPasswd, command.newEmail);
		return new ResponseEntity<>(new NutzerResource(findNutzer), HttpStatus.OK);
	}

	// changes the password of the given nutzername as parameter
	@PutMapping(path = "/nutzer/password/{nutzername}")
	public ResponseEntity<NutzerResource> updatePassword(@RequestBody final PasswordChangeCommand command,
			@PathVariable final String nutzername, final HttpMethod method, final WebRequest request) {
		_print(method, request);
		// Nutzer dummy = new Nutzer("dummy", "dummy", "dummy","dummy123213",
		// "dummy@dummy.de");
		Nutzer findNutzer = turnierService.findNutzerByNutzername(nutzername);
		if (findNutzer == null) {
			throw create(NutzerArentHereExc.class, nutzername);

		}
		findNutzer = turnierService.updateNutzerWithPassword(findNutzer, command.verifyPasswd, command.newPassword);
		return new ResponseEntity<>(new NutzerResource(findNutzer), HttpStatus.OK);
	}

	/** {0} It's not your turnier. Owner of {1} is {2} */
	public static class NotYourTurnierExc extends multex.Exc {
	}

	/** The nutzer with the nutzername {0} doesn't exist in our db */
	public static class NutzerArentHereExc extends multex.Exc {
	}

	/** The nutzer with the turnierrname {0} doesn't exist in our db */
	public static class TurnierArentHereExc extends multex.Exc {
	}

	//
	// //For the client role all URIs under /client:
	//
	// @PostMapping("/client/account")
	// public ResponseEntity<AccountAccessResource> createAccount(
	// @RequestBody final String accountName,
	// final HttpMethod method, final WebRequest request
	// ){
	// _print(method, request);
	// final Client client = _findClient(request);
	// final AccountAccess r = client.createAccount(accountName);
	// final AccountAccessResource result = new AccountAccessResource(r);
	// return new ResponseEntity<>(result, HttpStatus.CREATED);
	// }
	//
	// /*Resource for a coarse grained business process according to
	// https://www.thoughtworks.com/de/insights/blog/rest-api-design-resource-modeling*/
	// @PostMapping("/client/deposit")
	// public ResponseEntity<Void> deposit(@RequestBody final DepositCommand
	// command,
	// final HttpMethod method, final WebRequest request
	// ){
	// _print(method, request);
	// final Client client = _findClient(request);
	// final Account destinationAccount =
	// client.findAccount(command.accountId).get();
	// final Amount amount = new Amount(command.amount);
	// client.deposit(destinationAccount, amount);
	// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	// }
	//
	// /*Resource for a coarse grained business process according to
	// https://www.thoughtworks.com/de/insights/blog/rest-api-design-resource-modeling*/
	// @PostMapping("/client/transfer")
	// public ResponseEntity<AccountResource> transfer(@RequestBody final
	// TransferCommand command,
	// final HttpMethod method, final WebRequest request
	// ){
	// _print(method, request);
	// final Client client = _findClient(request);
	// final Account sourceAccount =
	// client.findAccount(command.sourceAccountId).get();
	// final Account destinationAccount =
	// client.findAccount(command.destinationAccountId).get();
	// final Amount amount = new Amount(command.amount);
	// client.transfer(sourceAccount, destinationAccount, amount);
	// final AccountResource result = new AccountResource(sourceAccount);
	// return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
	// }
	//
	// /*Resource for a coarse grained business process according to
	// https://www.thoughtworks.com/de/insights/blog/rest-api-design-resource-modeling*/
	// @PostMapping("/client/manager")
	// public ResponseEntity<AccountAccessResource> addAccountManager(
	// @RequestBody final AddAccountManagerCommand command,
	// final HttpMethod method, final WebRequest request
	// ){
	// _print(method, request);
	// final Client client = _findClient(request);
	// final Account account = client.findAccount(command.accountId).get();
	// final Client manager = bankService.findClient(command.username).get();
	// final AccountAccessResource result = new
	// AccountAccessResource(client.addAccountManager(account, manager));
	// return new ResponseEntity<>(result, HttpStatus.CREATED);
	// }
	//
	// @GetMapping("/client/account")
	// public ResponseEntity<String> accountsReport(
	// final HttpMethod method, final WebRequest request
	// ){
	// _print(method, request);
	// final Client client = _findClient(request);
	// final String result = client.accountsReport();
	// return new ResponseEntity<>(result, HttpStatus.OK);
	// }
	//
	/**
	 * Prints a message containing the current class name, the HTTP method, and
	 * infos about the current request.
	 */
	private void _print(final HttpMethod method, final WebRequest request) {
		System.out.printf("%s %s %s\n", className, method, request);
	}

	/** The turnier to be created with name {0} must not have an ID, but has {1} */
	public static class TurnierCreateWithIdExc extends multex.Exc {
	}

	//
	// /**Returns a random birth date ranging from 18 years before now to 100 years
	// before now.*/
	// private LocalDate _randomClientBirthDate() {
	// final long nowEpochDay = LocalDate.now().toEpochDay();
	// final int minYears = 18;
	// final int maxYears = 100;
	// final long minEpochDay = nowEpochDay - 365*maxYears;
	// final long maxEpochDay = nowEpochDay - 365*minYears;
	// //See
	// https://stackoverflow.com/questions/34051291/generate-a-random-localdate-with-java-time
	// final long randomEpochDay = ThreadLocalRandom.current().nextLong(minEpochDay,
	// maxEpochDay);
	// return LocalDate.ofEpochDay(randomEpochDay);
	// }
	//
	// private ResponseEntity<ClientResource[]> _clientsToResources(final
	// List<Client> clients) {
	// final Stream<ClientResource> result = clients.stream().map(c -> new
	// ClientResource(c));
	// final ClientResource[] resultArray = result.toArray(size -> new
	// ClientResource[size]);
	// return new ResponseEntity<>(resultArray, HttpStatus.OK);
	// }
	//
	// /**Finds the Client for the username, which has been authenticated with this
	// web request.
	// * @throws NoClientForUserExc There is no client object with the username of
	// the authenticated user of this web request.
	// */
	// private Client _findClient(final WebRequest request) {
	// final String username = request.getRemoteUser();
	// return _findClient(username);
	// }
	//
	// /**Finds the Client for the given username.
	// * @throws NoClientForUserExc There is no client object with the given
	// username.
	// */
	// private Client _findClient(final String username) {
	// final Optional<Client> optionalClient = bankService.findClient(username);
	// if(!optionalClient.isPresent()) {
	// throw create(NoClientForUserExc.class, username);
	// }
	// final Client client = optionalClient.get();
	// return client;
	// }
	//
	// /**There is no Client object for the username {0}.*/
	// public static class NoClientForUserExc extends Exc{}
	//
	//
}
