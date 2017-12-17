//package turnier.webapp.domain;
//
//import static org.junit.Assert.*;
//
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///** Test driver for the Rich Domain Object {@linkplain Nutzer} */
//@RunWith(SpringRunner.class)
//// @DataJpaTest
//// @AutoConfigureTestDatabase
//@SpringBootTest
//
//public class NutzerTest {
//
//	/** Only for use in the cleanUp method! */
//	@Autowired
//	private CleanupService cleanupService;
//
//	@Before
//	public void setUp() {
//		cleanupService.deleteAll();
//	}
//
//	@Test
//	public void testNutzerSpeichern() {
//		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
//		assertNull(michael.getId());
//		// everything is fine, after saving the nutzer object, he becomes first the id
//		// from our db.
//		michael.nutzerSpeichern();
//		assertNotNull(michael.getId());
//		// I'm trying here to provoke exceptions done by multex
//		final Nutzer nutzerSchonHinterlegtExc = new Nutzer("kubacki", "kacper", "miku", "notyourbusiness",
//				"notmiku@miku.pl");
//		try {
//			nutzerSchonHinterlegtExc.nutzerSpeichern();
//			fail("Nutzer.BenutzernameSchonHinterlegtExc expected");
//		} catch (Nutzer.BenutzernameSchonHinterlegtExc expected) {
//		}
//		final Nutzer nutzerEmailSchonHinterlegtExc = new Nutzer("kubacki", "kacper", "mikuyolo", "notyourbusiness",
//				"miku@miku.pl");
//		try {
//			nutzerEmailSchonHinterlegtExc.nutzerSpeichern();
//			fail("Nutzer.EmailSchonHinterlegtExc expected");
//		} catch (Nutzer.EmailSchonHinterlegtExc expected) {
//		}
//		
//		// TODO Korrekter weg zum testen wegen dem extra Block
//		{
//			final Nutzer nutzerThatsNotAnEmailExc = new Nutzer("yo", "yo", "yoyo", "passwdyo", "yo");
//			try {
//				nutzerThatsNotAnEmailExc.nutzerSpeichern();
//				fail("Nutzer.ThatsNotAnEmailExc expected");
//			} catch (Nutzer.ThatsNotAnEmailExc expected) {
//			}
//		}
//		
//		// Nicht korrekter weg zum testen
//		{
//			final Nutzer nutzerThatsNotAnEmailExc = new Nutzer("yo", "yo", "yoyo", "passwdyo", "@");
//			try {
//				nutzerThatsNotAnEmailExc.nutzerSpeichern();
//				fail("Nutzer.ThatsNotAnEmailExc expected");
//			} catch (Nutzer.ThatsNotAnEmailExc expected) {
//			}
//		}
//		
//		
//		{
//			final Nutzer nutzerThatsNotAnEmailExc = new Nutzer("yo", "yo", "yoyo", "passwdyo", "sido@");
//			try {
//				
//				nutzerThatsNotAnEmailExc.nutzerSpeichern();
//				fail("Nutzer.ThatsNotAnEmailExc expected");
//			} catch (Nutzer.ThatsNotAnEmailExc expected) {
//			}
//		}
//		
//		
//		{
//			final Nutzer nutzerThatsNotAnEmailExc = new Nutzer("yo", "yo", "yoyo", "passwdyo", "@sido");
//			try {
//				
//				nutzerThatsNotAnEmailExc.nutzerSpeichern();
//				fail("Nutzer.ThatsNotAnEmailExc expected");
//			} catch (Nutzer.ThatsNotAnEmailExc expected) {
//			}
//		}
//		
//		
//		{
//			final Nutzer nutzerThatsNotAnEmailExc = new Nutzer("yo", "yo", "yoyo", "passwdyo", "miku@sido");
//			try {
//				nutzerThatsNotAnEmailExc.nutzerSpeichern();
//				fail("Nutzer.ThatsNotAnEmailExc expected");
//			} catch (Nutzer.ThatsNotAnEmailExc expected) {
//			}
//		}
//		
//		{
//			final Nutzer nutzerThatsNotAnEmailExc = new Nutzer("yo", "yo", "yoyo", "passwdyo", ".@.");
//			try {
//				nutzerThatsNotAnEmailExc.nutzerSpeichern();
//				fail("Nutzer.ThatsNotAnEmailExc expected");
//			} catch (Nutzer.ThatsNotAnEmailExc expected) {
//			}
//		}
//		
//		{
//			final Nutzer nutzerThatsNotAnEmailExc = new Nutzer("yo", "yo", "yoyo", "passwdyo", "@com.de");
//			try {
//				nutzerThatsNotAnEmailExc.nutzerSpeichern();
//				fail("Nutzer.ThatsNotAnEmailExc expected");
//			} catch (Nutzer.ThatsNotAnEmailExc expected) {
//			}
//		}
//		
//		// positive Test for an email.
//		final Nutzer nutzerThatsEmailIsFine = new Nutzer("yo", "yo", "yoyo", "passwdyo", "michal.kubacki@yahoo.de");
//		assertNull(nutzerThatsEmailIsFine.getId());
//		// everything is fine, after saving the nutzer object, he becomes first the id
//		// from our db.
//		nutzerThatsEmailIsFine.nutzerSpeichern();
//		assertNotNull(nutzerThatsEmailIsFine.getId());
//		final Nutzer nutzerPasswortNotAllowedExcTooShort = new Nutzer("john", "carmack", "johnny", "john",
//				"john.carmack@blizzard.com");
//		try {
//			nutzerPasswortNotAllowedExcTooShort.nutzerSpeichern();
//			fail("Nutzer.NeuesPasswortNotAllowedExc expected");
//		} catch (Nutzer.NeuesPasswortNotAllowedExc expected) {
//		}
//		final Nutzer nutzerPasswortNotAllowedExcTooLong = new Nutzer("gordon", "freeman", "gfreeman",
//				new String(new char[255]).replace("\0", "1"), "gordon.freeman@valve.com");
//		try {
//			nutzerPasswortNotAllowedExcTooLong.nutzerSpeichern();
//			fail("Nutzer.NeuesPasswortNotAllowedExc expected");
//		} catch (Nutzer.NeuesPasswortNotAllowedExc expected) {
//		}
//		// testing working Grenzfaelle for passwd
//		final Nutzer nutzerPasswortNotAllowedExcNotTooShort = new Nutzer("john", "carmack", "johnny", "john56",
//				"john.carmack@blizzard.com");
//		assertNull(nutzerPasswortNotAllowedExcNotTooShort.getId());
//		nutzerPasswortNotAllowedExcNotTooShort.nutzerSpeichern();
//		assertNotNull(nutzerPasswortNotAllowedExcNotTooShort.getId());
//		final Nutzer nutzerPasswortNotAllowedExcNotTooLong = new Nutzer("gordon", "freeman", "gfreeman",
//				new String(new char[254]).replace("\0", "1"), "gordon.freeman@valve.com");
//		assertNull(nutzerPasswortNotAllowedExcNotTooLong.getId());
//		nutzerPasswortNotAllowedExcNotTooLong.nutzerSpeichern();
//		assertNotNull(nutzerPasswortNotAllowedExcNotTooLong.getId());
//
//		// everything working still fine after the exceptions
//		final Nutzer fine = new Nutzer("kostka", "maciej", "dekized", "passwd", "dekized@tlen.pl");
//		assertNull(fine.getId());
//		fine.nutzerSpeichern();
//		assertNotNull(fine.getId());
//
//	}
//
//	@Test
//	public void testNutzerLoeschen() {
//		Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
//		assertNull(michael.getId());
//		michael.nutzerSpeichern();
//		assertNotNull(michael.getId());
//		michael.nutzerLoeschen(michael.getPasswort());
//		// if michael Nutzer have been succesfully deleted, that's mean i can save the
//		// same michael object into the db without any exception.
//		michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
//		assertNull(michael.getId());
//		michael.nutzerSpeichern();
//		assertNotNull(michael.getId());
//		// I'm trying to provoke an exception by giving the false passwd
//		try {
//
//			michael.nutzerLoeschen("thatsNotYourPasswdDummy");
//			fail("Nutzer.PasswortDoesntMatchExc expected");
//		} catch (Nutzer.PasswortDoesntMatchExc expected) {
//		}
//
//	}
//
//	@Test
//	public void testPasswortAendern() {
//		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
//		assertNull(michael.getId());
//		michael.nutzerSpeichern();
//		assertNotNull(michael.getId());
//		final Nutzer michael2 = michael.passwortAendern("notyourbusiness", "istdochyourbusiness");
//		assertEquals(michael.getId(), michael2.getId());
//		assertEquals(michael.getName(), michael2.getName());
//		assertEquals(michael.getVorname(), michael2.getVorname());
//		assertEquals(michael.getNutzername(), michael2.getNutzername());
//		assertEquals("istdochyourbusiness", michael2.getPasswort(), michael.getPasswort());
//		assertEquals(michael.getEmail(), michael2.getEmail());
//		// I'm trying to provoke an exception by givin the false passwd
//		try {
//			final Nutzer michaelFalsePasswd = michael.passwortAendern("notyourbusiness", "yoyoyoyo");
//			assertEquals(michael2, michaelFalsePasswd);
//			fail("Nutzer.PasswortDoesntMatchExc expected");
//		} catch (Nutzer.PasswortDoesntMatchExc expected) {
//		}
//		try {
//			final Nutzer michaelNewPasswdToShort = michael.passwortAendern("istdochyourbusiness", "a");
//			assertEquals(michael2, michaelNewPasswdToShort);
//			fail("Nutzer.NeuesPasswortNotAllowedExc expected");
//		} catch (Nutzer.NeuesPasswortNotAllowedExc expected) {
//		}
//		try {
//			final Nutzer michaelNewPasswdToLong = michael.passwortAendern("istdochyourbusiness",
//					new String(new char[255]).replace("\0", "1"));
//			assertEquals(michael2, michaelNewPasswdToLong);
//			fail("Nutzer.NeuesPasswortNotAllowedExc expected");
//		} catch (Nutzer.NeuesPasswortNotAllowedExc expected) {
//		}
//		// two exception at one time..
//		try {
//			final Nutzer michaelNewPasswdToLongAndOldPasswdFalse = michael.passwortAendern("sadjaoisdjs",
//					new String(new char[275]).replace("\0", "1"));
//			assertEquals(michael2, michaelNewPasswdToLongAndOldPasswdFalse);
//			fail("Nutzer.PasswortDoesntMatchExc expected");
//		} catch (Nutzer.PasswortDoesntMatchExc expected) {
//		}
//		// can i still change my passwd after all of this exceptions?
//		final Nutzer michael3 = michael.passwortAendern("istdochyourbusiness", "istdochnichtyourbusiness");
//		assertEquals(michael.getId(), michael3.getId());
//		assertEquals(michael.getName(), michael3.getName());
//		assertEquals(michael.getVorname(), michael3.getVorname());
//		assertEquals(michael.getNutzername(), michael3.getNutzername());
//		assertEquals("istdochnichtyourbusiness", michael3.getPasswort(), michael.getPasswort());
//		assertEquals(michael.getEmail(), michael3.getEmail());
//       //yes i can :-)
//	
//	}
//
//	@Test
//	public void testEmailAendern() {
//		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
//		final Nutzer maciej = new Nutzer("kostka", "maciek", "dekized", "notyourbusiness", "dekized@deki.pl");
//		assertNull(michael.getId());
//		michael.nutzerSpeichern();
//		assertNotNull(michael.getId());
//		// everything is fine
//		final Nutzer michael3 = michael.emailAendern("mikuyo@mikuyo.de", "notyourbusiness");
//		assertEquals(michael.getId(), michael3.getId());
//		assertEquals(michael.getName(), michael3.getName());
//		assertEquals(michael.getVorname(), michael3.getVorname());
//		assertEquals(michael.getNutzername(), michael3.getNutzername());
//		assertEquals(michael.getPasswort(), michael3.getPasswort());
//		assertEquals("mikuyo@mikuyo.de", michael.getEmail(), michael3.getEmail());
//		// email is not an email exceptions
//		try {
//			final Nutzer michael4 = michael.emailAendern(",", "notyourbusiness");
//			fail("Nutzer.ThatsNotAnEmailExc expected");
//		} catch (Nutzer.ThatsNotAnEmailExc expected) {
//		}
//		try {
//			final Nutzer michael5 = michael.emailAendern("@", "notyourbusiness");
//			fail("Nutzer.ThatsNotAnEmailExc expected");
//		} catch (Nutzer.ThatsNotAnEmailExc expected) {
//		}
//		try {
//			final Nutzer michael6 = michael.emailAendern("@adfa.pl", "notyourbusiness");
//			fail("Nutzer.ThatsNotAnEmailExc expected");
//		} catch (Nutzer.ThatsNotAnEmailExc expected) {
//		}
//		try {
//			final Nutzer michael7 = michael.emailAendern(".@.", "notyourbusiness");
//			fail("Nutzer.ThatsNotAnEmailExc expected");
//		} catch (Nutzer.ThatsNotAnEmailExc expected) {
//		}
//		try {
//			final Nutzer michael8 = michael.emailAendern("", "notyourbusiness");
//			fail("Nutzer.ThatsNotAnEmailExc expected");
//		} catch (Nutzer.ThatsNotAnEmailExc expected) {
//		}
//		try {
//			final Nutzer michael9 = michael.emailAendern("miku@", "notyourbusiness");
//			fail("Nutzer.ThatsNotAnEmailExc expected");
//		} catch (Nutzer.ThatsNotAnEmailExc expected) {
//		}
//		try {
//			final Nutzer michael10 = michael.emailAendern("@miku", "notyourbusiness");
//			fail("Nutzer.ThatsNotAnEmailExc expected");
//		} catch (Nutzer.ThatsNotAnEmailExc expected) {
//		}
//		try {
//			final Nutzer michael11 = michael.emailAendern("miku@miku", "notyourbusiness");
//			fail("Nutzer.ThatsNotAnEmailExc expected");
//		} catch (Nutzer.ThatsNotAnEmailExc expected) {
//		}
//		// im trying to provoke passwddoesntmatchexc
//		try {
//			final Nutzer michaelFalsePasswd = michael.emailAendern("kubacki.michal@yahoo.de", "dfasdfas");
//			assertEquals("mikuyo@mikuyo.de", michael.getEmail(), michaelFalsePasswd);
//			fail("Nutzer.PasswortDoesntMatchExc expected");
//		} catch (Nutzer.PasswortDoesntMatchExc expected) {
//		}
//		//im trying to provoke EmailSchonHinterlegtExc
//		try {
//			final Nutzer maciej2 = maciej.emailAendern("mikuyo@mikuyo.de", "notyourbusiness");
//			assertEquals("dekized@deki.pl", maciej.getEmail(), maciej2.getEmail());
//			fail("Nutzer.EmailSchonHinterlegtExc expected");
//		} catch (Nutzer.EmailSchonHinterlegtExc expected) {
//		}
//		
//		
//		
//		// two exceptions at the same time, it should throws passwortdoesntmatchexc cause it's more important
//		try {
//			final Nutzer michaelFalsePasswdAndEmail = michael.emailAendern("asdsad", "noyourbusiness");
//			assertEquals("mikuyo@mikuyo.de", michael.getEmail(), michaelFalsePasswdAndEmail.getEmail());
//			fail("Nutzer.PasswortDoesntMatchExc expected");
//		} catch (Nutzer.PasswortDoesntMatchExc expected) {
//		}
//       //everything is still working fine after throwing all of this exceptions
//		final Nutzer michaelworkingfine = michael.emailAendern("goodmail@mail.de", "notyourbusiness");
//		assertEquals(michael.getId(), michaelworkingfine.getId());
//		assertEquals(michael.getName(), michaelworkingfine.getName());
//		assertEquals(michael.getVorname(), michaelworkingfine.getVorname());
//		assertEquals(michael.getNutzername(), michaelworkingfine.getNutzername());
//		assertEquals(michael.getPasswort(), michaelworkingfine.getPasswort());
//		assertEquals("goodmail@mail.de", michael.getEmail(), michaelworkingfine.getEmail());
//	
//	}
//	
//	@Test
//	public void testFindNutzuer() {
//		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
//		final Nutzer maciej = new Nutzer("kostka", "maciej", "dekized", "yoyisdjfoisdjfo", "maciej@maciej.pl");
//		michael.nutzerSpeichern();
//		maciej.nutzerSpeichern();
//        final Nutzer nothing = maciej.findNutzer("blabla");
//        assertNull(nothing);
//        final Nutzer michaelFound = maciej.findNutzer("miku");
//        assertNotNull(michaelFound);
//        assertEquals(michael.getName(), michaelFound.getName());
//        assertEquals(michael.getVorname(), michaelFound.getVorname());
//        assertEquals(michael.getId(), michaelFound.getId());
//        assertEquals(michael.getEmail(), michaelFound.getEmail());
//        assertEquals(michael.getPasswort(), michaelFound.getPasswort());
//        assertEquals(michael.getNutzername(), michaelFound.getNutzername());
//		
//	}
//
//	@Test
//	public void testToString() {
//		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
//		assertEquals(
//				"Nutzer{id=null, name='kubacki', vorname='michal', nutzername='miku', passwort='notyourbusiness', email='miku@miku.pl'}",
//				michael.toString());
//		michael.nutzerSpeichern();
//		assertEquals("Nutzer{id=" + michael.getId()
//				+ ", name='kubacki', vorname='michal', nutzername='miku', passwort='notyourbusiness', email='miku@miku.pl'}",
//				michael.toString());
//	}
//
//}
