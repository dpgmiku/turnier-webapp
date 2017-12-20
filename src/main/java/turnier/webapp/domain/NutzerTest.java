package turnier.webapp.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NutzerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPasswortVerifizieren() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertTrue(michael.passwortVerifizieren("notyourbusiness"));
		try {
			michael.passwortVerifizieren("notapasswd");
			fail("Nutzer.PasswortDoesntMatchExc expected");
		} catch (Nutzer.PasswortDoesntMatchExc expected) {
		}
		assertTrue(michael.passwortVerifizieren("notyourbusiness"));
		michael.passwortAendern("notyourbusiness", "blablabla");
		try {
			michael.passwortVerifizieren("notyourbusiness");
			fail("Nutzer.PasswortDoesntMatchExc expected");
		} catch (Nutzer.PasswortDoesntMatchExc expected) {
		}

		assertTrue(michael.passwortVerifizieren("blablabla"));

	}

	@Test
	public void testPasswortAendern() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals("notyourbusiness", michael.getPasswort());
		try {
			michael.passwortAendern("notapasswd", "newpasswd");
			fail("Nutzer.PasswortDoesntMatchExc expected");
		} catch (Nutzer.PasswortDoesntMatchExc expected) {
		}
		michael.passwortAendern("notyourbusiness", "blablabla");
		try {
			michael.passwortAendern("notyourbusiness", "newpasswd");
			fail("Nutzer.PasswortDoesntMatchExc expected");
		} catch (Nutzer.PasswortDoesntMatchExc expected) {
		}

		assertTrue(michael.passwortVerifizieren("blablabla"));
	}

	@Test
	public void testEmailAendern() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals("miku@miku.pl", michael.getEmail());

		try {
			michael.emailAendern("bla@bla.pl", "asfa");
			fail("Nutzer.PasswortDoesntMatchExc expected");
		} catch (Nutzer.PasswortDoesntMatchExc expected) {
		}
		michael.emailAendern("bla@bla.pl", "notyourbusiness");
		assertEquals("bla@bla.pl", michael.getEmail());
	}

	@Test
	public void testToString() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals(
				"Nutzer{id=null, name='kubacki', vorname='michal', nutzername='miku', passwort='notyourbusiness', email='miku@miku.pl'}",
				michael.toString());
	}
}
