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
			fail("Nutzer.PasswortIstFalschExc expected");
		} catch (Nutzer.PasswortIstFalschExc expected) {
		}
		assertTrue(michael.passwortVerifizieren("notyourbusiness"));
		michael.passwortAendern("notyourbusiness", "blablabla");
		try {
			michael.passwortVerifizieren("notyourbusiness");
			fail("Nutzer.PasswortIstFalschExc expected");
		} catch (Nutzer.PasswortIstFalschExc expected) {
		}

		assertTrue(michael.passwortVerifizieren("blablabla"));

	}

	@Test
	public void testPasswortAendern() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals("notyourbusiness", michael.getPasswort());
		try {
			michael.passwortAendern("notapasswd", "newpasswd");
			fail("Nutzer.PasswortIstFalschExc expected");
		} catch (Nutzer.PasswortIstFalschExc expected) {
		}
		michael.passwortAendern("notyourbusiness", "blablabla");
		try {
			michael.passwortAendern("notyourbusiness", "newpasswd");
			fail("Nutzer.PasswortIstFalschExc expected");
		} catch (Nutzer.PasswortIstFalschExc expected) {
		}

		assertTrue(michael.passwortVerifizieren("blablabla"));
	}

	@Test
	public void testEmailAendern() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals("miku@miku.pl", michael.getEmail());

		try {
			michael.emailAendern("bla@bla.pl", "asfa");
			fail("Nutzer.PasswortIstFalschExc expected");
		} catch (Nutzer.PasswortIstFalschExc expected) {
		}
		michael.emailAendern("bla@bla.pl", "notyourbusiness");
		assertEquals("bla@bla.pl", michael.getEmail());
	}

	@Test
	public void testHatTurnierGewonnen() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals(0, michael.getGewonneneTurniere());
		michael.hatTurnierGewonnen();
		assertEquals(1, michael.getGewonneneTurniere());
		michael.hatTurnierGewonnen();
		assertEquals(2, michael.getGewonneneTurniere());
	}

	@Test
	public void testHatGewonnen() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals(0, michael.getGewonneneSpiele());
		michael.hatGewonnen();
		assertEquals(1, michael.getGewonneneSpiele());
		michael.hatGewonnen();
		assertEquals(2, michael.getGewonneneSpiele());
	}

	@Test
	public void testHatVerloren() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals(0, michael.getVerloreneSpiele());
		michael.hatVerloren();
		assertEquals(1, michael.getVerloreneSpiele());
		michael.hatVerloren();
		assertEquals(2, michael.getVerloreneSpiele());

	}

	@Test
	public void testFuerAdminNutzerAendern() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals("kubacki", michael.getName());
		assertEquals("michal", michael.getVorname());
		assertEquals("miku", michael.getNutzername());
		assertEquals("notyourbusiness", michael.getPasswort());
		assertEquals("miku@miku.pl", michael.getEmail());
		assertEquals(0, michael.getGewonneneTurniere());
		assertEquals(0, michael.getVerloreneSpiele());
		assertEquals(0, michael.getGewonneneSpiele());
		michael.fuerAdminNutzerAendern("newname", "newvorname", "newnutzername", "newpasswort", "newemail@email.de");
		assertEquals("newname", michael.getName());
		assertEquals("newvorname", michael.getVorname());
		assertEquals("newnutzername", michael.getNutzername());
		assertEquals("newpasswort", michael.getPasswort());
		assertEquals("newemail@email.de", michael.getEmail());
		assertEquals(0, michael.getGewonneneTurniere());
		assertEquals(0, michael.getVerloreneSpiele());
		assertEquals(0, michael.getGewonneneSpiele());
		michael.fuerAdminNutzerAendern("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals("kubacki", michael.getName());
		assertEquals("michal", michael.getVorname());
		assertEquals("miku", michael.getNutzername());
		assertEquals("notyourbusiness", michael.getPasswort());
		assertEquals("miku@miku.pl", michael.getEmail());
		assertEquals(0, michael.getGewonneneTurniere());
		assertEquals(0, michael.getVerloreneSpiele());
		assertEquals(0, michael.getGewonneneSpiele());
	}

	@Test
	public void testToString() {
		final Nutzer michael = new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@miku.pl");
		assertEquals(
				"Nutzer{id=null, name='kubacki', vorname='michal', nutzername='miku', passwort='notyourbusiness', email='miku@miku.pl', gewonneneSpiele='0', verloreneSpiele='0', gewonneneTurniere='0'}",
				michael.toString());
	}
}
