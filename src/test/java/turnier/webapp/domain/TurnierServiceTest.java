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
	public void testNutzerSpeichern() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		assertNotNull(michael.getId());
		// everything is fine, after saving the nutzer object, he becomes first the id
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
				final Nutzer nutzerThatsNotAnEmailExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo", "passwdyo",
						"yo");
				fail("TurnierService.ThatsNotAnEmailExc expected");
			} catch (TurnierService.ThatsNotAnEmailExc expected) {
			}
		}

		{
			try {
				final Nutzer nutzerThatsNotAnEmailExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo", "passwdyo",
						"@");
				fail("TurnierService.ThatsNotAnEmailExc expected");
			} catch (TurnierService.ThatsNotAnEmailExc expected) {
			}
		}

		{
			try {
				final Nutzer nutzerThatsNotAnEmailExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo", "passwdyo",
						"sido@");
				fail("TurnierService.ThatsNotAnEmailExc expected");
			} catch (TurnierService.ThatsNotAnEmailExc expected) {
			}

		}

		{
			try {
				final Nutzer nutzerThatsNotAnEmailExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo", "passwdyo",
						"@sido");
				fail("TurnierService.ThatsNotAnEmailExc expected");
			} catch (TurnierService.ThatsNotAnEmailExc expected) {
			}

		}

		{
			try {
				final Nutzer nutzerThatsNotAnEmailExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo", "passwdyo",
						"miku@sido");
				fail("TurnierService.ThatsNotAnEmailExc expected");
			} catch (TurnierService.ThatsNotAnEmailExc expected) {
			}

		}

		{
			try {
				final Nutzer nutzerThatsNotAnEmailExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo", "passwdyo",
						".@.");
				fail("TurnierService.ThatsNotAnEmailExc expected");
			} catch (TurnierService.ThatsNotAnEmailExc expected) {
			}

		}

		{
			try {
				final Nutzer nutzerThatsNotAnEmailExc = turnierService.nutzerSpeichern("yo", "yo", "yoyo", "passwdyo",
						"@com.de");
				fail("TurnierService.ThatsNotAnEmailExc expected");
			} catch (TurnierService.ThatsNotAnEmailExc expected) {
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
	public void testUpdateNutzerWithPassword() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		assertNotNull(michael.getId());
		final Nutzer michael2 = turnierService.nutzerPasswortAendern(michael, "notyourbusiness",
				"istdochyourbusiness");
		assertEquals(michael.getId(), michael2.getId());
		assertEquals(michael.getName(), michael2.getName());
		assertEquals(michael.getVorname(), michael2.getVorname());
		assertEquals(michael.getNutzername(), michael2.getNutzername());
		assertEquals("istdochyourbusiness", michael2.getPasswort(), michael.getPasswort());
		assertEquals(michael.getEmail(), michael2.getEmail());
		// I'm trying to provoke an exception by givin the false passwd
		try {
			final Nutzer michaelFalsePasswd = turnierService.nutzerPasswortAendern(michael, "notyourbusiness",
					"yoyoyoyo");
			assertEquals(michael2, michaelFalsePasswd);
			fail("Nutzer.PasswortDoesntMatchExc expected");
		} catch (Nutzer.PasswortDoesntMatchExc expected) {
		}
		try {
			final Nutzer michaelNewPasswdToShort = turnierService.nutzerPasswortAendern(michael,
					"istdochyourbusiness", "a");
			assertEquals(michael2, michaelNewPasswdToShort);
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		try {
			final Nutzer michaelNewPasswdToLong = turnierService.nutzerPasswortAendern(michael,
					"istdochyourbusiness", new String(new char[255]).replace("\0", "1"));
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
	public void testFindTurnierByName() {
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
	public void testFindTurniers() {
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
	public void testFindTurnierByOrganisator() {
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
		final List<Turnier> turnierFound = turnierService.findTurnierByOrganisator(michael);
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
	public void testUpdateEmail() {
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
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "@");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "@adfa.pl");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", ".@.");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "miku@");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "@miku");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.emailAendern(michael, "notyourbusiness", "miku@miku");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		// im trying to provoke passwddoesntmatchexc
		try {
			turnierService.emailAendern(michael, "dfasdfas", "kubacki.michal@yahoo.de");
			assertEquals("mikuyo@mikuyo.de", michael.getEmail());
			fail("Nutzer.PasswortDoesntMatchExc expected");
		} catch (Nutzer.PasswortDoesntMatchExc expected) {
		}
		// im trying to provoke EmailSchonHinterlegtExc
		try {
			turnierService.emailAendern(maciej, "notyourbusiness", "mikuyo@mikuyo.de");
			assertEquals("dekized@deki.pl", maciej.getEmail());
			fail("TurnierService.EmailSchonHinterlegtExc expected");
		} catch (TurnierService.EmailSchonHinterlegtExc expected) {
		}

		// two exceptions at the same time, it should throws passwortdoesntmatchexc
		// cause it's more important
		try {
			turnierService.emailAendern(michael, "noyourbusiness", "asdsad");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
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
	public void testTurnierErstellen() {
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
			final Turnier turnierMitDemGleichenNamen = turnierService.turnierErstellen("turnier", "wy", "20.2.207",
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

	}

	@Test
	public void testFindNutzerByNutzername() {
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
	public void testFindNutzerByEmail() {
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
