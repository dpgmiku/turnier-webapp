package turnier.webapp.domain;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** Test driver for the Rich Domain Object {@linkplain Nutzer} */
@RunWith(SpringRunner.class)
// @DataJpaTest
// @AutoConfigureTestDatabase
@SpringBootTest

public class TurnierServiceTest {

	/** Only for use in the cleanUp method! */
	@Autowired
	private CleanupService cleanupService;

	@Autowired
	private TurnierService turnierService;

	@Before
	public void setUp() {
		cleanupService.deleteAll();
	}

	@Test
	public void testAdminSpeichern() {
		final Admin admin = turnierService.adminSpeichern("miku", "passwort");
		assertNotNull(admin.getId());
		assertEquals("miku", admin.getAdminname());
		assertEquals("passwort", admin.getPasswort());
		try {
			final Admin adminPasswortGehtNicht = turnierService.adminSpeichern("miku2", "pass");
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		try {
			final Admin adminPasswortGehtNichtUndAdminnameAuch = turnierService.adminSpeichern("miku", "pass");
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		try {
			final Admin adminAdminnameSchonHinterlegt = turnierService.adminSpeichern("miku", "passdghdhrdh");
			fail("TurnierService.AdminnameSchonHinterlegtExc expected");
		} catch (TurnierService.AdminnameSchonHinterlegtExc expected) {
		}
		final Admin mussgehen = turnierService.adminSpeichern("mikusolltegehen", "passwort");
		assertNotNull(mussgehen.getId());
		assertEquals("mikusolltegehen", mussgehen.getAdminname());
		assertEquals("passwort", mussgehen.getPasswort());
	}

	@Test
	public void testNutzerSpeichern() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		assertNotNull(michael.getId());
		try {

			final Nutzer nutzerSchonHinterlegtExc = turnierService.nutzerSpeichern("kubacki", "kacper", "miku",
					"notyourbusiness", "notmiku@miku.pl");
			fail("TurnierService.BenutzernameSchonHinterlegtExc expected");
		} catch (TurnierService.BenutzernameSchonHinterlegtExc expected) {
		}

		try {
			final Nutzer nutzerEmailSchonHinterlegtExc = turnierService.nutzerSpeichern("kubacki", "kacper", "mikuyolo",
					"notyourbusiness", "miku@miku.pl");
			fail("TurnierService.EmailSchonHinterlegtExc expected");
		} catch (TurnierService.EmailSchonHinterlegtExc expected) {
		}

		{
			try {
				final Nutzer nutzerEsIstKeineEmailAdresseExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo",
						"passwdyo", "yo");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}
		}

		{
			try {
				final Nutzer nutzerEsIstKeineEmailAdresseExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo",
						"passwdyo", "@");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}
		}

		{
			try {
				final Nutzer nutzerEsIstKeineEmailAdresseExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo",
						"passwdyo", "sido@");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}

		}

		{
			try {
				final Nutzer nutzerEsIstKeineEmailAdresseExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo",
						"passwdyo", "@sido");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}

		}

		{
			try {
				final Nutzer nutzerEsIstKeineEmailAdresseExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo",
						"passwdyo", "miku@sido");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}

		}

		{
			try {
				final Nutzer nutzerEsIstKeineEmailAdresseExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo",
						"passwdyo", ".@.");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}

		}

		{
			try {
				final Nutzer nutzerEsIstKeineEmailAdresseExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo",
						"passwdyo", "@com.de");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}

		}

		// positive Test for an email.
		final Nutzer nutzerThatsEmailIsFine = turnierService.nutzerSpeichern("yo", "yo", "yoyo", "passwdyo",
				"michal.kubacki@yahoo.de");
		assertNotNull(nutzerThatsEmailIsFine.getId());
		// everything is fine, after saving the nutzer object, he becomes first the id
		// from our db.
		try {
			final Nutzer nutzerPasswortNotAllowedExcTooShort = turnierService.nutzerSpeichern("john", "carmack",
					"johnny", "john", "john.carmack@blizzard.com");
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		try {
			final Nutzer nutzerPasswortNotAllowedExcTooLong = turnierService.nutzerSpeichern("gordon", "freeman",
					"gfreeman", new String(new char[255]).replace("\0", "1"), "gordon.freeman@valve.com");
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		// testing working Grenzfaelle for passwd
		final Nutzer nutzerPasswortNotAllowedExcNotTooShort = turnierService.nutzerSpeichern("john", "carmack",
				"johnny", "john56", "john.carmack@blizzard.com");
		assertNotNull(nutzerPasswortNotAllowedExcNotTooShort.getId());
		final Nutzer nutzerPasswortNotAllowedExcNotTooLong = turnierService.nutzerSpeichern("gordon", "freeman",
				"gfreeman", new String(new char[254]).replace("\0", "1"), "gordon.freeman@valve.com");
		assertNotNull(nutzerPasswortNotAllowedExcNotTooLong.getId());
		// everything working still fine after the exceptions
		final Nutzer fine = turnierService.nutzerSpeichern("kostka", "maciej", "dekized", "passwd", "dekized@tlen.pl");
		assertNotNull(fine.getId());
	}

	@Test
	public void testAendereNutzer() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		assertNotNull(michael.getId());
		assertEquals("kubacki", michael.getName());
		assertEquals("michal", michael.getVorname());
		assertEquals("miku", michael.getNutzername());
		assertEquals("notyourbusiness", michael.getPasswort());
		assertEquals("miku@miku.pl", michael.getEmail());
		final Admin admin = turnierService.adminSpeichern("admin", "passwort");
		assertNotNull(admin.getId());
		turnierService.aendereNutzer("admin", "miku", "newname", "newvorname", "miqmiq", "passwort", "miku@miku.de");
		assertNull(turnierService.findeNutzerMitNutzername("miku"));
		final Nutzer miqmiq = turnierService.findeNutzerMitNutzername("miqmiq");
		assertNotNull(michael.getId());
		assertEquals("newname", miqmiq.getName());
		assertEquals("newvorname", miqmiq.getVorname());
		assertEquals("miqmiq", miqmiq.getNutzername());
		assertEquals("passwort", miqmiq.getPasswort());
		assertEquals("miku@miku.de", miqmiq.getEmail());
		final Nutzer michaelnew = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		assertNotNull(michaelnew.getId());
		assertEquals("kubacki", michaelnew.getName());
		assertEquals("michal", michaelnew.getVorname());
		assertEquals("miku", michaelnew.getNutzername());
		assertEquals("notyourbusiness", michaelnew.getPasswort());
		assertEquals("miku@miku.pl", michaelnew.getEmail());
		try {

			turnierService.aendereNutzer("admin", "miqmiq", "kubacki", "kacper", "miku", "notyourbusiness",
					"notmiku@miku.pl");
			fail("TurnierService.BenutzernameSchonHinterlegtExc expected");
		} catch (TurnierService.BenutzernameSchonHinterlegtExc expected) {
		}

		try {
			turnierService.aendereNutzer("admin", "miqmiq", "kubacki", "kacper", "mikuyolo", "notyourbusiness",
					"miku@miku.pl");
			fail("TurnierService.EmailSchonHinterlegtExc expected");
		} catch (TurnierService.EmailSchonHinterlegtExc expected) {
		}

		{
			try {
				turnierService.aendereNutzer("admin", "miqmiq", "yo", "yo", "yoyo", "passwdyo", "yo");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}
		}

		{
			try {
				turnierService.aendereNutzer("admin", "miqmiq", "yo", "yo", "yoyo", "passwdyo", "@");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}
		}

		{
			try {
				turnierService.aendereNutzer("admin", "miqmiq", "yo", "yo", "yoyo", "passwdyo", "sido@");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}

		}

		{
			try {
				turnierService.aendereNutzer("admin", "miqmiq", "yo", "yo", "yoyo", "passwdyo", "@sido");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}

		}

		{
			try {
				turnierService.aendereNutzer("admin", "miqmiq", "yo", "yo", "yoyo", "passwdyo", "miku@sido");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}

		}

		{
			try {
				turnierService.aendereNutzer("admin", "miqmiq", "yo", "yo", "yoyo", "passwdyo", ".@.");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}

		}

		{
			try {
				turnierService.aendereNutzer("admin", "miqmiq", "yo", "yo", "yoyo", "passwdyo", "@com.de");
				fail("TurnierService.EsIstKeineEmailAdresseExc expected");
			} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
			}

		}

		// positive Test for an email.
		turnierService.aendereNutzer("admin", "miqmiq", "yo", "yo", "miqmiq", "passwdyo", "michal.kubacki@yahoo.de");
		assertNull(turnierService.findeNutzerMitEmail("miku@miku.de"));
		final Nutzer nutzerMitEmail = turnierService.findeNutzerMitEmail("michal.kubacki@yahoo.de");
		assertEquals("michal.kubacki@yahoo.de", nutzerMitEmail.getEmail());

		try {
			turnierService.aendereNutzer("admin", "miqmiq", "john", "carmack", "johnny", "john",
					"john.carmack@blizzard.com");
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		try {
			turnierService.aendereNutzer("admin", "miqmiq", "gordon", "freeman", "gfreeman",
					new String(new char[256]).replace("\0", "1"), "gordon.freeman@valve.com");
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		// testing working Grenzfaelle for passwd
		turnierService.aendereNutzer("admin", "miqmiq", "john", "carmack", "miqmiq", "john56",
				"john.carmack@blizzard.com");
		final Nutzer nutzerPasswortKlein = turnierService.findeNutzerMitNutzername("miqmiq");
		assertEquals(michael.getId(), nutzerPasswortKlein.getId());
		assertEquals("john56", nutzerPasswortKlein.getPasswort());
		turnierService.aendereNutzer("admin", "miqmiq", "gordon", "freeman", "miqmiq",
				new String(new char[254]).replace("\0", "1"), "gordon.freeman@valve.com");
		final Nutzer nutzerPasswortGross = turnierService.findeNutzerMitNutzername("miqmiq");
		assertEquals(michael.getId(), nutzerPasswortGross.getId());
		assertEquals((new String(new char[254]).replace("\0", "1")), nutzerPasswortGross.getPasswort());
		try {
			turnierService.aendereNutzer("adminadmin", "miqmiq", "gordon", "freeman", "gfreeman", "lkjsdldkjasas",
					"gordon.freeman@valve.com");
			fail("TurnierService.AdminExistiertNichtExc expected");
		} catch (TurnierService.AdminExistiertNichtExc expected) {
		}

		try {
			turnierService.aendereNutzer("admin", "nutzergibtesnicht", "gordon", "freeman", "gfreeman", "lkjsdldkjasas",
					"gordon.freeman@valve.com");
			fail("TurnierService.NutzerGibtEsNichtExc expected");
		} catch (TurnierService.NutzerGibtEsNichtExc expected) {
		}

	}

	@Test
	public void testNutzerLoeschen() {
		{
			final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
					"miku@miku.pl");
			assertNotNull(michael.getId());
			turnierService.nutzerLoeschen(michael, michael.getPasswort());
			// if michael Nutzer have been succesfully deleted, that's mean i can save the
			// same michael object into the db without any exception.
			assertNull(turnierService.findeNutzerMitNutzername(michael.getNutzername()));
		}
		{
			final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
					"miku@miku.pl");
			assertNotNull(michael.getId());
		}
	}

	@Test
	public void testLoescheEigenesTurnier() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciej", "dekized", "notyourbusiness",
				"maciej@tlen.pl");
		final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30",
				michael, 4);
		try {
			final Turnier turnierNotInDB = new Turnier("blablabla", "dasfds", "dsfdsa", "dasfdsa", maciej, 32);
			turnierService.loescheEigenesTurnier(michael, turnierNotInDB);
			fail("TurnierService.TurnierGibtEsNichtExc excepted");
		} catch (TurnierService.TurnierGibtEsNichtExc excepted) {
		}
		try {
			turnierService.loescheEigenesTurnier(maciej, turnier);
			fail("TurnierService.EsIstNichtDeinTurnierExc excepted");
		} catch (TurnierService.EsIstNichtDeinTurnierExc excepted) {
		}
		assertNotNull(turnier);
		final Turnier turnierFound = turnierService.findeTurnierMitName(turnier.getName());
		assertEquals(turnier, turnierFound);
		assertEquals(turnier.getId(), turnierFound.getId());
		assertEquals(turnier.getName(), turnierFound.getName());
		assertEquals(turnier.getDatum(), turnierFound.getDatum());
		assertEquals(michael, turnierFound.getOrganisator());
		assertEquals(turnier.getTeilnehmer().isEmpty(), turnierFound.getTeilnehmer().isEmpty());
		assertEquals(turnier.getMaxTeilnehmer(), turnierFound.getMaxTeilnehmer());
		assertEquals(turnier.getUhrzeit(), turnierFound.getUhrzeit());
		try {
			turnierService.loescheEigenesTurnier(maciej, turnier);
			fail("TurnierService.EsIstNichtDeinTurnierExc excepted");
		} catch (TurnierService.EsIstNichtDeinTurnierExc excepted) {
		}
		turnierService.loescheEigenesTurnier(michael, turnier);
		assertNull(turnierService.findeTurnierMitName(turnier.getName()));
		// try to delete a turnier with a logged teilnehmer, should working fine
		final Turnier turnierWithTeilnehmer = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207",
				"15:30", michael, 4);
		turnierService.anTurnierAnmelden(turnierWithTeilnehmer, michael);
		turnierService.anTurnierAnmelden(turnierWithTeilnehmer, maciej);
		turnierService.turnierStarten(turnierWithTeilnehmer);
		assertFalse(turnierWithTeilnehmer.getTurnierBrackets().isEmpty());
		assertNotNull(turnier);
		final Turnier turnierWithTeilnehmerFound = turnierService.findeTurnierMitName(turnier.getName());
		assertEquals(turnierWithTeilnehmer, turnierWithTeilnehmerFound);
		assertEquals(turnierWithTeilnehmer.getId(), turnierWithTeilnehmerFound.getId());
		assertEquals(turnierWithTeilnehmer.getName(), turnierWithTeilnehmerFound.getName());
		assertEquals(turnierWithTeilnehmer.getDatum(), turnierWithTeilnehmerFound.getDatum());
		assertEquals(michael, turnierWithTeilnehmerFound.getOrganisator());
		assertEquals(2, turnierWithTeilnehmer.getTeilnehmer().size(),
				turnierWithTeilnehmerFound.getTeilnehmer().size());
		assertEquals(turnierWithTeilnehmer.getMaxTeilnehmer(), turnierWithTeilnehmerFound.getMaxTeilnehmer());
		assertEquals(turnierWithTeilnehmer.getUhrzeit(), turnierWithTeilnehmerFound.getUhrzeit());
		turnierService.loescheEigenesTurnier(michael, turnierWithTeilnehmer);
		assertNull(turnierService.findeTurnierMitName(turnierWithTeilnehmer.getName()));
	}

	@Test
	public void testNutzerPasswortAendern() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		assertNotNull(michael.getId());
		final Nutzer michael2 = turnierService.nutzerPasswortAendern(michael, "notyourbusiness", "istdochyourbusiness");
		assertEquals(michael.getId(), michael2.getId());
		assertEquals(michael.getName(), michael2.getName());
		assertEquals(michael.getVorname(), michael2.getVorname());
		assertEquals(michael.getNutzername(), michael2.getNutzername());
		assertEquals("istdochyourbusiness", michael2.getPasswort(), michael.getPasswort());
		assertEquals(michael.getEmail(), michael2.getEmail());
		try {
			final Nutzer michaelNewPasswdToShort = turnierService.nutzerPasswortAendern(michael, "istdochyourbusiness",
					"a");
			assertEquals(michael2, michaelNewPasswdToShort);
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		try {
			final Nutzer michaelNewPasswdToLong = turnierService.nutzerPasswortAendern(michael, "istdochyourbusiness",
					new String(new char[255]).replace("\0", "1"));
			assertEquals(michael2, michaelNewPasswdToLong);
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		// two exception at one time..
		try {
			final Nutzer michaelNewPasswdToLongAndOldPasswdFalse = turnierService.nutzerPasswortAendern(michael,
					"sadjaoisdjs", new String(new char[275]).replace("\0", "1"));
			assertEquals(michael2, michaelNewPasswdToLongAndOldPasswdFalse);
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		// can i still change my passwd after all of this exceptions?
		final Nutzer michael3 = turnierService.nutzerPasswortAendern(michael, "istdochyourbusiness",
				"istdochnichtyourbusiness");
		assertEquals(michael.getId(), michael3.getId());
		assertEquals(michael.getName(), michael3.getName());
		assertEquals(michael.getVorname(), michael3.getVorname());
		assertEquals(michael.getNutzername(), michael3.getNutzername());
		assertEquals("istdochnichtyourbusiness", michael3.getPasswort(), michael.getPasswort());
		assertEquals(michael.getEmail(), michael3.getEmail());
		// yes i can :-)

	}

	@Test
	public void testFindeTurnierMitName() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		System.out.println(michael.toString());
		{
			final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30",
					michael, 32);
			System.out.println(turnier.toString());
			assertNotNull(turnier);
			final Turnier turnierFound = turnierService.findeTurnierMitName(turnier.getName());
			assertEquals(turnier, turnierFound);
			assertEquals(turnier.getId(), turnierFound.getId());
			assertEquals(turnier.getName(), turnierFound.getName());
			assertEquals(turnier.getDatum(), turnierFound.getDatum());
			assertEquals(michael, turnierFound.getOrganisator());
			assertEquals(turnier.getTeilnehmer().isEmpty(), turnierFound.getTeilnehmer().isEmpty());
			assertEquals(turnier.getMaxTeilnehmer(), turnierFound.getMaxTeilnehmer());
			assertEquals(turnier.getUhrzeit(), turnierFound.getUhrzeit());
		}
		Turnier turnier = turnierService.findeTurnierMitName("notaname");
		assertNull(turnier);

	}

	@Test
	public void testFindAlleTurniere() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		{
			final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30",
					michael, 32);
			assertNotNull(turnier);
			final Turnier turnier2 = turnierService.turnierErstellen("turniertwo", "wyszynskiego 2", "20.2.207",
					"15:30", michael, 32);
			final List<Turnier> turnierFound = turnierService.findeAlleTurniere();
			final Turnier turnierFirst = turnierFound.get(0);
			assertEquals(turnier, turnierFirst);
			assertEquals(turnier.getId(), turnierFirst.getId());
			assertEquals(turnier.getName(), turnierFirst.getName());
			assertEquals(turnier.getDatum(), turnierFirst.getDatum());
			assertEquals(michael, turnierFirst.getOrganisator());
			assertEquals(turnier.getTeilnehmer().isEmpty(), turnierFirst.getTeilnehmer().isEmpty());
			assertEquals(turnier.getMaxTeilnehmer(), turnierFirst.getMaxTeilnehmer());
			assertEquals(turnier.getUhrzeit(), turnierFirst.getUhrzeit());
			final Turnier turnierSecond = turnierFound.get(1);
			assertEquals(turnier2, turnierSecond);
			assertEquals(turnier2.getId(), turnierSecond.getId());
			assertEquals(turnier2.getName(), turnierSecond.getName());
			assertEquals(turnier2.getDatum(), turnierSecond.getDatum());
			assertEquals(michael, turnierSecond.getOrganisator());
			assertEquals(turnier2.getTeilnehmer().isEmpty(), turnierSecond.getTeilnehmer().isEmpty());
			assertEquals(turnier2.getMaxTeilnehmer(), turnierSecond.getMaxTeilnehmer());
			assertEquals(turnier2.getUhrzeit(), turnierSecond.getUhrzeit());

		}
	}

	@Test
	public void testAnTurnierAnmelden() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciej", "dekized", "notyourbusiness",
				"maciej@tlen.pl");
		final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30",
				michael, 4);
		assertNotNull(turnier);
		final Turnier turnierFound = turnierService.findeTurnierMitName(turnier.getName());
		assertEquals(turnier, turnierFound);
		assertEquals(turnier.getId(), turnierFound.getId());
		assertEquals(turnier.getName(), turnierFound.getName());
		assertEquals(turnier.getDatum(), turnierFound.getDatum());
		assertEquals(michael, turnierFound.getOrganisator());
		assertEquals(turnier.getTeilnehmer().isEmpty(), turnierFound.getTeilnehmer().isEmpty());
		assertEquals(turnier.getMaxTeilnehmer(), turnierFound.getMaxTeilnehmer());
		assertEquals(turnier.getUhrzeit(), turnierFound.getUhrzeit());
		turnierService.anTurnierAnmelden(turnier, michael);
		assertEquals(1, turnier.getTeilnehmer().size());
		turnierService.anTurnierAnmelden(turnier, maciej);
		assertEquals(2, turnier.getTeilnehmer().size());
		try {
			turnierService.anTurnierAnmelden(turnier, maciej);
			fail("TurnierService.DuBistSchonAngemeldetExc expected");
		} catch (TurnierService.DuBistSchonAngemeldetExc expected) {
		}
		turnier.starteTurnier();
		final Nutzer david = turnierService.nutzerSpeichern("radobenko", "david", "sodrek", "notyourbusiness",
				"asfdas@asdfds.de");

		try {
			turnierService.anTurnierAnmelden(turnier, david);
			fail("TurnierService.TurnierStatusFailExc expected");
		} catch (TurnierService.TurnierStatusFailExc expected) {
		}

	}

	@Test
	public void testEntferneTeilnehmer() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciej", "dekized", "notyourbusiness",
				"maciej@tlen.pl");
		final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30",
				michael, 4);
		assertNotNull(turnier);
		final Turnier turnierFound = turnierService.findeTurnierMitName(turnier.getName());
		assertEquals(turnier, turnierFound);
		assertEquals(turnier.getId(), turnierFound.getId());
		assertEquals(turnier.getName(), turnierFound.getName());
		assertEquals(turnier.getDatum(), turnierFound.getDatum());
		assertEquals(michael, turnierFound.getOrganisator());
		assertEquals(turnier.getTeilnehmer().isEmpty(), turnierFound.getTeilnehmer().isEmpty());
		assertEquals(turnier.getMaxTeilnehmer(), turnierFound.getMaxTeilnehmer());
		assertEquals(turnier.getUhrzeit(), turnierFound.getUhrzeit());
		turnierService.anTurnierAnmelden(turnier, michael);
		assertEquals(1, turnier.getTeilnehmer().size());
		try {
			turnierService.entferneTeilnehmer(maciej, turnier, michael);
			fail("TurnierService.EsIstNichtDeinTurnierExc excepted");
		} catch (TurnierService.EsIstNichtDeinTurnierExc excepted) {
		}
		try {
			turnierService.entferneTeilnehmer(michael, turnier, maciej);
			fail("TurnierService.TeilnehmerGibtEsNichtExc excepted");
		} catch (TurnierService.TeilnehmerGibtEsNichtExc excepted) {
		}
		try {
			final Turnier turnierNotInDB = new Turnier("blablabla", "dasfds", "dsfdsa", "dasfdsa", maciej, 32);
			turnierService.entferneTeilnehmer(michael, turnierNotInDB, michael);
			fail("TurnierService.TurnierGibtEsNichtExc excepted");
		} catch (TurnierService.TurnierGibtEsNichtExc excepted) {
		}

		turnierService.entferneTeilnehmer(michael, turnier, michael);
		assertEquals(0, turnier.getTeilnehmer().size());

	}

	@Test
	public void testFindeTurnierMitVeranstalter() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciej", "dekized", "notyourbusiness",
				"maciej@tlen.pl");

		final Turnier turnierNotByOrg = turnierService.turnierErstellen("blabla", "wyszynskiego 2", "20.2.207", "15:30",
				maciej, 32);
		assertNotNull(turnierNotByOrg);
		final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30",
				michael, 32);
		assertNotNull(turnier);
		final Turnier turnier2 = turnierService.turnierErstellen("turniertwo", "wyszynskiego 2", "20.2.207", "15:30",
				michael, 32);
		final List<Turnier> turnierFound = turnierService.findeTurnierMitVeranstalter(michael);
		assertEquals(2, turnierFound.size());
		final Turnier turnierFirst = turnierFound.get(0);
		assertEquals(turnier, turnierFirst);
		assertEquals(turnier.getId(), turnierFirst.getId());
		assertEquals(turnier.getName(), turnierFirst.getName());
		assertEquals(turnier.getDatum(), turnierFirst.getDatum());
		assertEquals(michael, turnierFirst.getOrganisator());
		assertEquals(turnier.getTeilnehmer().isEmpty(), turnierFirst.getTeilnehmer().isEmpty());
		assertEquals(turnier.getMaxTeilnehmer(), turnierFirst.getMaxTeilnehmer());
		assertEquals(turnier.getUhrzeit(), turnierFirst.getUhrzeit());
		final Turnier turnierSecond = turnierFound.get(1);
		assertEquals(turnier2, turnierSecond);
		assertEquals(turnier2.getId(), turnierSecond.getId());
		assertEquals(turnier2.getName(), turnierSecond.getName());
		assertEquals(turnier2.getDatum(), turnierSecond.getDatum());
		assertEquals(michael, turnierSecond.getOrganisator());
		assertEquals(turnier2.getTeilnehmer().isEmpty(), turnierSecond.getTeilnehmer().isEmpty());
		assertEquals(turnier2.getMaxTeilnehmer(), turnierSecond.getMaxTeilnehmer());
		assertEquals(turnier2.getUhrzeit(), turnierSecond.getUhrzeit());

	}

	@Test
	public void testEmailAendern() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciek", "dekized", "notyourbusiness",
				"dekized@deki.pl");
		assertNotNull(michael.getId());
		;
		// everything is fine
		turnierService.emailAendern(michael, "notyourbusiness", "mikuyo@mikuyo.de");
		final Nutzer michael3 = turnierService.findeNutzerMitNutzername(michael.getNutzername());
		assertEquals(michael.getId(), michael3.getId());
		assertEquals(michael.getName(), michael3.getName());
		assertEquals(michael.getVorname(), michael3.getVorname());
		assertEquals(michael.getNutzername(), michael3.getNutzername());
		assertEquals(michael.getPasswort(), michael3.getPasswort());
		assertEquals("mikuyo@mikuyo.de", michael.getEmail(), michael3.getEmail());
		// email is not an email exceptions
		try {
			turnierService.emailAendern(michael, "notyourbusiness", ",");
			fail("TurnierService.EsIstKeineEmailAdresseExc expected");
		} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "@");
			fail("TurnierService.EsIstKeineEmailAdresseExc expected");
		} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "@adfa.pl");
			fail("TurnierService.EsIstKeineEmailAdresseExc expected");
		} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", ".@.");
			fail("TurnierService.EsIstKeineEmailAdresseExc expected");
		} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "");
			fail("TurnierService.EsIstKeineEmailAdresseExc expected");
		} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "miku@");
			fail("TurnierService.EsIstKeineEmailAdresseExc expected");
		} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "@miku");
			fail("TurnierService.EsIstKeineEmailAdresseExc expected");
		} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "miku@miku");
			fail("TurnierService.EsIstKeineEmailAdresseExc expected");
		} catch (TurnierService.EsIstKeineEmailAdresseExc expected) {
		}
		// im trying to provoke EmailSchonHinterlegtExc
		try {
			turnierService.emailAendern(maciej, "notyourbusiness", "mikuyo@mikuyo.de");
			assertEquals("dekized@deki.pl", maciej.getEmail());
			fail("TurnierService.EmailSchonHinterlegtExc expected");
		} catch (TurnierService.EmailSchonHinterlegtExc expected) {
		}
		// everything is still working fine after throwing all of this exceptions
		turnierService.emailAendern(michael, "notyourbusiness", "goodmail@mail.de");
		final Nutzer michaelworkingfine = turnierService.findeNutzerMitNutzername(michael.getNutzername());
		assertEquals(michael.getId(), michaelworkingfine.getId());
		assertEquals(michael.getName(), michaelworkingfine.getName());
		assertEquals(michael.getVorname(), michaelworkingfine.getVorname());
		assertEquals(michael.getNutzername(), michaelworkingfine.getNutzername());
		assertEquals(michael.getPasswort(), michaelworkingfine.getPasswort());
		assertEquals("goodmail@mail.de", michael.getEmail(), michaelworkingfine.getEmail());

	}

	@Test
	public void turnierStartenTest() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		assertNotNull(michael.getId());
		final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30",
				michael, 6);
		assertNotNull(turnier.getId());
		try {
			turnierService.turnierStarten(turnier);
			fail("TurnierService.AnzahlTeilnehmerNoPowerOfTwoExc excepted");
		} catch (TurnierService.AnzahlTeilnehmerNoPowerOfTwoExc excepted) {
		}
		turnierService.anTurnierAnmelden(turnier, michael);
		final Nutzer michael2 = turnierService.nutzerSpeichern("kubacki", "michal", "2miku", "notyourbusiness",
				"2miku@miku.pl");
		turnierService.anTurnierAnmelden(turnier, michael2);
		assertEquals(2, turnier.getTeilnehmer().size());
		assertEquals(turnier.getTurnierStatus().toString(), "OFFEN");
		assertEquals(0, turnier.getTurnierBrackets().size());
		turnierService.turnierStarten(turnier);
		assertEquals(turnier.getTurnierStatus().toString(), "GESTARTET");
		assertEquals(1, turnier.getTurnierBrackets().size());
	}

	@Test
	public void testSetteErgebnisse() {

		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		assertNotNull(michael.getId());
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciej", "dekized", "notyourbusiness",
				"maciej@tlen.pl");
		assertNotNull(maciej.getId());
		final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30",
				michael, 4);
		assertNotNull(turnier.getId());
		turnierService.anTurnierAnmelden(turnier, michael);
		turnierService.anTurnierAnmelden(turnier, maciej);
		try {
			turnierService.setteErgebnisse(turnier, 0, 7, 6);
			fail("TurnierService.TurnierStatusFailExc excepted");
		} catch (TurnierService.TurnierStatusFailExc expected) {
		}
		assertEquals(0, turnier.getTurnierBrackets().size());
		turnierService.turnierStarten(turnier);
		assertEquals(1, turnier.getTurnierBrackets().size());
		assertEquals("GESTARTET", turnier.getTurnierStatus().toString());
		turnierService.setteErgebnisse(turnier, 0, 7, 6);
		final Nutzer michaelNachErstenSpiel = turnierService.findeNutzerMitNutzername("miku");
		final Nutzer maciejNachErstenSpiel = turnierService.findeNutzerMitNutzername("dekized");
		assertEquals(1, michaelNachErstenSpiel.getGewonneneSpiele());
		assertEquals(0, maciejNachErstenSpiel.getGewonneneSpiele());
		assertEquals(1, maciejNachErstenSpiel.getVerloreneSpiele());
		assertEquals(0, michaelNachErstenSpiel.getVerloreneSpiele());
		assertEquals(1, michaelNachErstenSpiel.getGewonneneTurniere());
		assertEquals(0, maciejNachErstenSpiel.getGewonneneTurniere());
		final Turnier turnierBeendet = turnierService.findeTurnierMitName("turnier");
		assertEquals("miku", turnierBeendet.getTurnierBracketAtPos(0).getGewinner());
		assertEquals("dekized", turnierBeendet.getTurnierBracketAtPos(0).getVerlierer());
		assertEquals("BEENDET", turnierBeendet.getTurnierStatus().toString());
		// turnier mit 4 nutzern
		final Nutzer michael2 = turnierService.nutzerSpeichern("kubacki", "michal", "miku2", "notyourbusiness",
				"miku2@miku.pl");
		assertNotNull(michael2.getId());
		final Nutzer maciej2 = turnierService.nutzerSpeichern("kostka", "maciej", "dekized2", "notyourbusiness",
				"maciej2@tlen.pl");
		assertNotNull(maciej2.getId());
		final Turnier turnierzwei = turnierService.turnierErstellen("turnierzwei", "wyszynskiego 2", "20.2.207",
				"15:30", michael, 4);
		assertNotNull(turnier.getId());
		turnierService.anTurnierAnmelden(turnierzwei, michael);
		turnierService.anTurnierAnmelden(turnierzwei, maciej);
		turnierService.anTurnierAnmelden(turnierzwei, michael2);
		turnierService.anTurnierAnmelden(turnierzwei, maciej2);
		try {
			turnierService.setteErgebnisse(turnierzwei, 0, 7, 6);
			fail("TurnierService.TurnierStatusFailExc excepted");
		} catch (TurnierService.TurnierStatusFailExc expected) {
		}
		assertEquals(0, turnierzwei.getTurnierBrackets().size());
		turnierService.turnierStarten(turnierzwei);
		assertEquals("GESTARTET", turnierzwei.getTurnierStatus().toString());
		assertEquals(2, turnierzwei.getTurnierBrackets().size());
		turnierService.setteErgebnisse(turnierzwei, 0, 7, 6);
		final Nutzer michaelNachZweitenSpiel = turnierService.findeNutzerMitNutzername("miku");
		final Nutzer maciejNachZweitenSpiel = turnierService.findeNutzerMitNutzername("dekized");
		assertEquals(2, michaelNachZweitenSpiel.getGewonneneSpiele());
		assertEquals(0, maciejNachZweitenSpiel.getGewonneneSpiele());
		assertEquals(2, maciejNachZweitenSpiel.getVerloreneSpiele());
		assertEquals(0, michaelNachZweitenSpiel.getVerloreneSpiele());
		assertEquals(1, michaelNachZweitenSpiel.getGewonneneTurniere());
		assertEquals(0, maciejNachZweitenSpiel.getGewonneneTurniere());
		assertEquals("miku", turnierzwei.getTurnierBracketAtPos(0).getGewinner());
		assertEquals("dekized", turnierzwei.getTurnierBracketAtPos(0).getVerlierer());
		assertEquals(2, turnierzwei.getTurnierBrackets().size());
		turnierService.setteErgebnisse(turnierzwei, 1, 7, 6);
		final Nutzer michael2NachErstenSpiel = turnierService.findeNutzerMitNutzername("miku2");
		final Nutzer maciej2NachErstenSpiel = turnierService.findeNutzerMitNutzername("dekized2");
		assertEquals(1, michael2NachErstenSpiel.getGewonneneSpiele());
		assertEquals(0, maciej2NachErstenSpiel.getGewonneneSpiele());
		assertEquals(1, maciej2NachErstenSpiel.getVerloreneSpiele());
		assertEquals(0, michael2NachErstenSpiel.getVerloreneSpiele());
		assertEquals(0, michael2NachErstenSpiel.getGewonneneTurniere());
		assertEquals(0, maciej2NachErstenSpiel.getGewonneneTurniere());
		assertEquals("miku2", turnierzwei.getTurnierBracketAtPos(1).getGewinner());
		assertEquals("dekized2", turnierzwei.getTurnierBracketAtPos(1).getVerlierer());
		assertEquals(3, turnierzwei.getTurnierBrackets().size());
		turnierService.setteErgebnisse(turnierzwei, 2, 7, 6);
		final Nutzer michaelNachDrittenSpiel = turnierService.findeNutzerMitNutzername("miku");
		final Nutzer michael2NachZweitenSpiel = turnierService.findeNutzerMitNutzername("miku2");
		assertEquals(3, michaelNachDrittenSpiel.getGewonneneSpiele());
		assertEquals(1, michael2NachZweitenSpiel.getGewonneneSpiele());
		assertEquals(0, michaelNachDrittenSpiel.getVerloreneSpiele());
		assertEquals(1, michael2NachZweitenSpiel.getVerloreneSpiele());
		assertEquals(0, michael2NachZweitenSpiel.getGewonneneTurniere());
		assertEquals(2, michaelNachDrittenSpiel.getGewonneneTurniere());
		assertEquals("miku", turnierzwei.getTurnierBracketAtPos(2).getGewinner());
		assertEquals("miku2", turnierzwei.getTurnierBracketAtPos(2).getVerlierer());
		assertEquals("BEENDET", turnierzwei.getTurnierStatus().toString());
	}

	@Test
	public void testTurnierErstellen() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		{
			final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30",
					michael, 32);
			System.out.println(turnier.toString());
			assertNotNull(turnier);
			final Turnier turnierFound = turnierService.findeTurnierMitName(turnier.getName());
			assertEquals(turnier, turnierFound);
			assertEquals(turnier.getId(), turnierFound.getId());
			assertEquals(turnier.getName(), turnierFound.getName());
			assertEquals(turnier.getDatum(), turnierFound.getDatum());
			assertEquals(michael, turnierFound.getOrganisator());
			assertEquals(turnier.getTeilnehmer().isEmpty(), turnierFound.getTeilnehmer().isEmpty());
			assertEquals(turnier.getMaxTeilnehmer(), turnierFound.getMaxTeilnehmer());
			assertEquals(turnier.getUhrzeit(), turnierFound.getUhrzeit());
			assertEquals(turnier.getTurnierBrackets().isEmpty(), turnierFound.getTurnierBrackets().isEmpty());

		}
		try {
			final Turnier turnierZuVielTeilnehmer = turnierService.turnierErstellen("turniertest", "wyszynskiego 2",
					"20.2.207", "15:30", michael, 34);
			fail("TurnierService.ZuVieleTeilnehmerExc excepted");
		} catch (TurnierService.ZuVieleTeilnehmerExc expected) {
		}
		try {
			final Turnier turnierZuWenigTeilnehmer = turnierService.turnierErstellen("turniertt", "wyszynskiego 2",
					"20.2.207", "15:30", michael, 1);
			fail("TurnierService.ZuWenigTeilnehmerExc excepted");
		} catch (TurnierService.ZuWenigTeilnehmerExc expected) {
		}
		try {
			final Turnier turnierAdresseZuKurz = turnierService.turnierErstellen("turniertttt", "wy", "20.2.207",
					"15:30", michael, 2);
			fail("TurnierService.KeineRichtigeEingabenTurnierExc excepted");
		} catch (TurnierService.KeineRichtigeEingabenTurnierExc expected) {
		}
		try {
			final Turnier turnierNameMitZiffer = turnierService.turnierErstellen("tur12nier", "wyasfasd", "20.2.207",
					"15:30", michael, 4);
			fail("TurnierService.KeineRichtigeEingabenTurnierExc excepted");
		} catch (TurnierService.KeineRichtigeEingabenTurnierExc expected) {
		}
		try {
			final Turnier turnierMitZuKurzerAdresse = turnierService.turnierErstellen("turnier", "wy", "20.2.207",
					"15:30", michael, 4);
			fail("TurnierService.KeineRichtigeEingabenTurnierExc expected");
		} catch (TurnierService.KeineRichtigeEingabenTurnierExc expected) {
		}
		{
			final Turnier turnier = turnierService.turnierErstellen("turniertest", "wyszynskiego 2", "20.2.207",
					"15:30", michael, 32);
			assertNotNull(turnier);
			final Turnier turnierFound = turnierService.findeTurnierMitName(turnier.getName());
			assertEquals(turnier, turnierFound);
			assertEquals(turnier.getId(), turnierFound.getId());
			assertEquals(turnier.getName(), turnierFound.getName());
			assertEquals(turnier.getDatum(), turnierFound.getDatum());
			assertEquals(turnier.getOrganisator(), turnierFound.getOrganisator());
			assertEquals(turnier.getTeilnehmer().size(), turnierFound.getTeilnehmer().size());
			assertEquals(turnier.getMaxTeilnehmer(), turnierFound.getMaxTeilnehmer());
			assertEquals(turnier.getUhrzeit(), turnierFound.getUhrzeit());
		}
		{
			try {
				final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207",
						"15:30", michael, 32);
				fail("TurnierService.TurniernameSchonHinterlegtExc expected");
			} catch (TurnierService.TurniernameSchonHinterlegtExc expected) {
			}
			;
		}
	}

	@Test
	public void testTurnierAendern() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Admin admin = turnierService.adminSpeichern("admin", "passwd");
		System.out.println(michael.toString());

		final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30",
				michael, 32);
		assertNotNull(turnier);
		final Turnier turnierFound = turnierService.findeTurnierMitName(turnier.getName());
		assertEquals(turnier, turnierFound);
		assertEquals(turnier.getId(), turnierFound.getId());
		assertEquals(turnier.getName(), turnierFound.getName());
		assertEquals(turnier.getDatum(), turnierFound.getDatum());
		assertEquals(michael, turnierFound.getOrganisator());
		assertEquals(turnier.getTeilnehmer().isEmpty(), turnierFound.getTeilnehmer().isEmpty());
		assertEquals(turnier.getMaxTeilnehmer(), turnierFound.getMaxTeilnehmer());
		assertEquals(turnier.getUhrzeit(), turnierFound.getUhrzeit());
		assertEquals(turnier.getTurnierBrackets().isEmpty(), turnierFound.getTurnierBrackets().isEmpty());

		try {
			turnierService.turnierAendern("admin", "turnier", "turniertest", "wyszynskiego 2", "20.2.207", "15:30", 34);
			fail("TurnierService.ZuVieleTeilnehmerExc excepted");
		} catch (TurnierService.ZuVieleTeilnehmerExc expected) {
		}
		try {
			turnierService.turnierAendern("admin", "turnier", "turniertt", "wyszynskiego 2", "20.2.207", "15:30", 1);
			fail("TurnierService.ZuWenigTeilnehmerExc excepted");
		} catch (TurnierService.ZuWenigTeilnehmerExc expected) {
		}
		try {
			turnierService.turnierAendern("admin", "turnier", "turniertttt", "wy", "20.2.207", "15:30", 2);
			fail("TurnierService.KeineRichtigeEingabenTurnierExc excepted");
		} catch (TurnierService.KeineRichtigeEingabenTurnierExc expected) {
		}
		try {
			turnierService.turnierAendern("admin", "turnier", "tur12nier", "wyasfasd", "20.2.207", "15:30", 4);
			fail("TurnierService.KeineRichtigeEingabenTurnierExc excepted");
		} catch (TurnierService.KeineRichtigeEingabenTurnierExc expected) {
		}
		try {
			turnierService.turnierAendern("admin", "turnier", "turnier", "wy", "20.2.207", "15:30", 4);
			fail("TurnierService.KeineRichtigeEingabenTurnierExc expected");
		} catch (TurnierService.KeineRichtigeEingabenTurnierExc expected) {
		}

		turnierService.turnierAendern("admin", "turnier", "turniertest", "wyszynskiego 2", "20.2.207", "15:30", 32);
		assertNull(turnierService.findeTurnierMitName("turnier"));
		final Turnier turniertest = turnierService.findeTurnierMitName("turniertest");
		assertNotNull(turniertest);
		assertEquals(turnier.getId(), turniertest.getId());
		assertEquals("turniertest", turniertest.getName());
		assertEquals(turnier.getDatum(), turniertest.getDatum());
		assertEquals(turnier.getOrganisator(), turniertest.getOrganisator());
		assertEquals(turnier.getTeilnehmer().size(), turniertest.getTeilnehmer().size());
		assertEquals(turnier.getMaxTeilnehmer(), turniertest.getMaxTeilnehmer());
		assertEquals(turnier.getUhrzeit(), turniertest.getUhrzeit());

		final Turnier turnierGehtNicht = turnierService.turnierErstellen("turniergehtnicht", "wyszynskiego 2",
				"20.2.207", "15:30", michael, 32);
		try {
			turnierService.turnierAendern("admin", "turniertest", "turniergehtnicht", "wyszynskiego 2", "20.2.207",
					"15:30", 32);
			fail("TurnierService.TurniernameSchonHinterlegtExc expected");
		} catch (TurnierService.TurniernameSchonHinterlegtExc expected) {
		}
		try {
			turnierService.turnierAendern("adminnein", "turniertest", "turniergehtnicht", "wyszynskiego 2", "20.2.207",
					"15:30", 32);
			fail("TurnierService.AdminExistiertNichtExc expected");
		} catch (TurnierService.AdminExistiertNichtExc expected) {
		}

		try {
			turnierService.turnierAendern("admin", "turniernein", "turniergehtnicht", "wyszynskiego 2", "20.2.207",
					"15:30", 32);
			fail("TurnierService.TurnierGibtEsNichtExc expected");
		} catch (TurnierService.TurnierGibtEsNichtExc expected) {
		}

	}

	@Test
	public void testFindeNutzerMitNutzername() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciej", "dekized", "yoyisdjfoisdjfo",
				"maciej@maciej.pl");
		final Nutzer nothing = turnierService.findeNutzerMitNutzername("blabla");
		assertNull(nothing);
		final Nutzer michaelFound = turnierService.findeNutzerMitNutzername("miku");
		assertNotNull(michaelFound);
		assertEquals(michael.getName(), michaelFound.getName());
		assertEquals(michael.getVorname(), michaelFound.getVorname());
		assertEquals(michael.getId(), michaelFound.getId());
		assertEquals(michael.getEmail(), michaelFound.getEmail());
		assertEquals(michael.getPasswort(), michaelFound.getPasswort());
		assertEquals(michael.getNutzername(), michaelFound.getNutzername());
	}

	@Test
	public void testFindeNutzerMitEmail() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciej", "dekized", "yoyisdjfoisdjfo",
				"maciej@maciej.pl");
		final Nutzer nothing = turnierService.findeNutzerMitEmail("blabla");
		assertNull(nothing);
		final Nutzer michaelFound = turnierService.findeNutzerMitEmail("miku@miku.pl");
		assertNotNull(michaelFound);
		assertEquals(michael.getName(), michaelFound.getName());
		assertEquals(michael.getVorname(), michaelFound.getVorname());
		assertEquals(michael.getId(), michaelFound.getId());
		assertEquals(michael.getEmail(), michaelFound.getEmail());
		assertEquals(michael.getPasswort(), michaelFound.getPasswort());
		assertEquals(michael.getNutzername(), michaelFound.getNutzername());
	}

	@Test
	public void testFindAllNutzers() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciej", "dekized", "yoyisdjfoisdjfo",
				"maciej@maciej.pl");
		List<Nutzer> nutzers = turnierService.findeAlleNutzer();
		assertEquals(2, nutzers.size());
		final Nutzer michaelFound = nutzers.get(1);
		assertNotNull(michaelFound);
		assertEquals(michael.getName(), michaelFound.getName());
		assertEquals(michael.getVorname(), michaelFound.getVorname());
		assertEquals(michael.getId(), michaelFound.getId());
		assertEquals(michael.getEmail(), michaelFound.getEmail());
		assertEquals(michael.getPasswort(), michaelFound.getPasswort());
		assertEquals(michael.getNutzername(), michaelFound.getNutzername());
		final Nutzer maciejFound = nutzers.get(0);
		assertNotNull(maciejFound);
		assertEquals(maciej.getName(), maciejFound.getName());
		assertEquals(maciej.getVorname(), maciejFound.getVorname());
		assertEquals(maciej.getId(), maciejFound.getId());
		assertEquals(maciej.getEmail(), maciejFound.getEmail());
		assertEquals(maciej.getPasswort(), maciejFound.getPasswort());
		assertEquals(maciej.getNutzername(), maciejFound.getNutzername());
	}

}
