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

		// TODO Korrekter weg zum testen wegen dem extra Block
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
			assertNull(turnierService.findNutzerByNutzername(michael.getNutzername()));
		}
		{
			final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
					"miku@miku.pl");
			assertNotNull(michael.getId());
		}
	}

	@Test
	public void testUpdateNutzerWithPassword() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		assertNotNull(michael.getId());
		final Nutzer michael2 = turnierService.updateNutzerWithPassword(michael, "notyourbusiness",
				"istdochyourbusiness");
		assertEquals(michael.getId(), michael2.getId());
		assertEquals(michael.getName(), michael2.getName());
		assertEquals(michael.getVorname(), michael2.getVorname());
		assertEquals(michael.getNutzername(), michael2.getNutzername());
		assertEquals("istdochyourbusiness", michael2.getPasswort(), michael.getPasswort());
		assertEquals(michael.getEmail(), michael2.getEmail());
		// I'm trying to provoke an exception by givin the false passwd
		try {
			final Nutzer michaelFalsePasswd = turnierService.updateNutzerWithPassword(michael, "notyourbusiness",
					"yoyoyoyo");
			assertEquals(michael2, michaelFalsePasswd);
			fail("Nutzer.PasswortDoesntMatchExc expected");
		} catch (Nutzer.PasswortDoesntMatchExc expected) {
		}
		try {
			final Nutzer michaelNewPasswdToShort = turnierService.updateNutzerWithPassword(michael,
					"istdochyourbusiness", "a");
			assertEquals(michael2, michaelNewPasswdToShort);
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		try {
			final Nutzer michaelNewPasswdToLong = turnierService.updateNutzerWithPassword(michael,
					"istdochyourbusiness", new String(new char[255]).replace("\0", "1"));
			assertEquals(michael2, michaelNewPasswdToLong);
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		// two exception at one time..
		try {
			final Nutzer michaelNewPasswdToLongAndOldPasswdFalse = turnierService.updateNutzerWithPassword(michael,
					"sadjaoisdjs", new String(new char[275]).replace("\0", "1"));
			assertEquals(michael2, michaelNewPasswdToLongAndOldPasswdFalse);
			fail("TurnierService.NeuesPasswortNotAllowedExc expected");
		} catch (TurnierService.NeuesPasswortNotAllowedExc expected) {
		}
		// can i still change my passwd after all of this exceptions?
		final Nutzer michael3 = turnierService.updateNutzerWithPassword(michael, "istdochyourbusiness",
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
	public void testUpdateEmail() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciek", "dekized", "notyourbusiness",
				"dekized@deki.pl");
		assertNotNull(michael.getId());
		;
		// everything is fine
		turnierService.updateEmail(michael, "notyourbusiness", "mikuyo@mikuyo.de");
		final Nutzer michael3 = turnierService.findNutzerByNutzername(michael.getNutzername());
		assertEquals(michael.getId(), michael3.getId());
		assertEquals(michael.getName(), michael3.getName());
		assertEquals(michael.getVorname(), michael3.getVorname());
		assertEquals(michael.getNutzername(), michael3.getNutzername());
		assertEquals(michael.getPasswort(), michael3.getPasswort());
		assertEquals("mikuyo@mikuyo.de", michael.getEmail(), michael3.getEmail());
		// email is not an email exceptions
		try {
			turnierService.updateEmail(michael, "notyourbusiness", ",");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.updateEmail(michael, "notyourbusiness", "@");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.updateEmail(michael, "notyourbusiness", "@adfa.pl");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.updateEmail(michael, "notyourbusiness", ".@.");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.updateEmail(michael, "notyourbusiness", "");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.updateEmail(michael, "notyourbusiness", "miku@");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.updateEmail(michael, "notyourbusiness", "@miku");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		try {
			turnierService.updateEmail(michael, "notyourbusiness", "miku@miku");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		// im trying to provoke passwddoesntmatchexc
		try {
			turnierService.updateEmail(michael, "dfasdfas", "kubacki.michal@yahoo.de");
			assertEquals("mikuyo@mikuyo.de", michael.getEmail());
			fail("Nutzer.PasswortDoesntMatchExc expected");
		} catch (Nutzer.PasswortDoesntMatchExc expected) {
		}
		// im trying to provoke EmailSchonHinterlegtExc
		try {
			turnierService.updateEmail(maciej, "notyourbusiness", "mikuyo@mikuyo.de");
			assertEquals("dekized@deki.pl", maciej.getEmail());
			fail("TurnierService.EmailSchonHinterlegtExc expected");
		} catch (TurnierService.EmailSchonHinterlegtExc expected) {
		}

		// two exceptions at the same time, it should throws passwortdoesntmatchexc
		// cause it's more important
		try {
			turnierService.updateEmail(michael, "noyourbusiness", "asdsad");
			fail("TurnierService.ThatsNotAnEmailExc expected");
		} catch (TurnierService.ThatsNotAnEmailExc expected) {
		}
		// everything is still working fine after throwing all of this exceptions
		turnierService.updateEmail(michael, "notyourbusiness", "goodmail@mail.de");
		final Nutzer michaelworkingfine = turnierService.findNutzerByNutzername(michael.getNutzername());
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
//		{
	//	final Turnier turnier = turnierService.turnierErstellen("turnier", "wyszynskiego 2", "20.2.207", "15:30", michael, 32);
	//	System.out.println(turnier == null);
	//	assertNotNull(turnier);
//		final Turnier turnierFound = turnierService.findTurnierByName(turnier.getName());
//		assertEquals(turnier, turnierFound);
//		assertEquals(turnier.getId(), turnierFound.getId());
//		assertEquals(turnier.getName(), turnierFound.getName());
//		assertEquals(turnier.getDatum(), turnierFound.getDatum());
//		assertEquals(michael, turnierFound.getOrganisator());
//		assertEquals(turnier.getTeilnehmer(), turnierFound.getTeilnehmer());
//		assertEquals(turnier.getMaxTeilnehmer(), turnierFound.getTeilnehmer());
//		assertEquals(turnier.getUhrzeit(), turnierFound.getUhrzeit());
//		}
//		try {
//			final Turnier turnierZuVielTeilnehmer =  turnierService.turnierErstellen("turniertest", "wyszynskiego 2", "20.2.207", "15:30", michael, 32);
//		fail("TurnierService.ZuVieleTeilnehmerExc excepted");
//		} catch (TurnierService.ZuVieleTeilnehmerExc expected) {}
//		try {
//			final Turnier turnierZuWenigTeilnehmer =  turnierService.turnierErstellen("turniertt", "wyszynskiego 2", "20.2.207", "15:30", michael, 1);
//		fail("TurnierService.ZuWenigTeilnehmerExc excepted");
//		} catch (TurnierService.ZuWenigTeilnehmerExc expected) {}
//		try {
//			final Turnier turnierAdresseZuKurz =  turnierService.turnierErstellen("turniertttt", "wy", "20.2.207", "15:30", michael, 4);
//		fail("TurnierService.KeineRichtigeEingabenTurnierExc excepted");
//		} catch (TurnierService.KeineRichtigeEingabenTurnierExc expected) {}
//		try {
//			final Turnier turnierNameMitZiffer =  turnierService.turnierErstellen("tur12nier", "wyasfasd", "20.2.207", "15:30", michael, 4);
//		fail("TurnierService.KeineRichtigeEingabenTurnierExc excepted");
//		} catch (TurnierService.KeineRichtigeEingabenTurnierExc expected) {}
//		try {
//			final Turnier turnierMitDemGleichenNamen =  turnierService.turnierErstellen("turnier", "wy", "20.2.207", "15:30", michael, 4);
//		fail("TurnierService.TurniernameSchonHinterlegtExc excepted");
//		} catch (TurnierService.TurniernameSchonHinterlegtExc expected) {}
//		{
//		final Turnier turnier = turnierService.turnierErstellen("turniertest", "wyszynskiego 2", "20.2.207", "15:30", michael, 32);
//		assertNotNull(turnier);
//		final Turnier turnierFound = turnierService.findTurnierByName(turnier.getName());
//		assertEquals(turnier, turnierFound);
//		assertEquals(turnier.getId(), turnierFound.getId());
//		assertEquals(turnier.getName(), turnierFound.getName());
//		assertEquals(turnier.getDatum(), turnierFound.getDatum());
//		assertEquals(turnier.getOrganisator(), turnierFound.getOrganisator());
//		assertEquals(turnier.getTeilnehmer(), turnierFound.getTeilnehmer());
//		assertEquals(turnier.getMaxTeilnehmer(), turnierFound.getTeilnehmer());
//		assertEquals(turnier.getUhrzeit(), turnierFound.getUhrzeit());
//		}
//	
//		
		
	}

	@Test
	public void testFindNutzerByNutzername() {
		final Nutzer michael = turnierService.nutzerSpeichern("kubacki", "michal", "miku", "notyourbusiness",
				"miku@miku.pl");
		final Nutzer maciej = turnierService.nutzerSpeichern("kostka", "maciej", "dekized", "yoyisdjfoisdjfo",
				"maciej@maciej.pl");
		final Nutzer nothing = turnierService.findNutzerByNutzername("blabla");
		assertNull(nothing);
		final Nutzer michaelFound = turnierService.findNutzerByNutzername("miku");
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
		final Nutzer nothing = turnierService.findNutzerByEmail("blabla");
		assertNull(nothing);
		final Nutzer michaelFound = turnierService.findNutzerByEmail("miku@miku.pl");
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
		List<Nutzer> nutzers = turnierService.findAllNutzers();
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
