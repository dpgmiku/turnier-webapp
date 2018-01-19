package turnier.webapp.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TurnierTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFuerAdminTurnierAendern() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 2);
		assertNotNull(organisator);
		assertNotNull(turnier);
		assertEquals("turniername", turnier.getName());
		assertEquals("wyszynskiego 2", turnier.getAdresse());
		assertEquals("20.12.2017", turnier.getDatum());
		assertEquals("13.40", turnier.getUhrzeit());
		assertEquals(organisator, turnier.getOrganisator());
		assertEquals(2, turnier.getMaxTeilnehmer());
		turnier.fuerAdminTurnierAendern("newname", "newadresse", "newdatum", "newuhrzeit", 4);
		assertEquals("newname", turnier.getName());
		assertEquals("newadresse", turnier.getAdresse());
		assertEquals("newdatum", turnier.getDatum());
		assertEquals("newuhrzeit", turnier.getUhrzeit());
		assertEquals(organisator, turnier.getOrganisator());
		assertEquals(4, turnier.getMaxTeilnehmer());
		turnier.starteTurnier();
		assertEquals("GESTARTET", turnier.getTurnierStatus().toString());
		try {
			turnier.fuerAdminTurnierAendern("newname", "newadresse", "newdatum", "newuhrzeit", 4);
			fail("Turnier.TurnierAendernNichtZugelassenExcc expected");
		} catch (Turnier.TurnierAendernNichtZugelassenExc expected) {
		}
		turnier.beendeTurnier();
		assertEquals("BEENDET", turnier.getTurnierStatus().toString());
		try {
			turnier.fuerAdminTurnierAendern("newname", "newadresse", "newdatum", "newuhrzeit", 4);
			fail("Turnier.TurnierAendernNichtZugelassenExcc expected");
		} catch (Turnier.TurnierAendernNichtZugelassenExc expected) {
		}

	}

	@Test
	public void testAnTurnierAnmelden() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 2);
		assertNotNull(organisator);
		assertNotNull(turnier);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(0, anzahlTeilnehmer);
		}
		turnier.anTurnierAnmelden(organisator);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(1, anzahlTeilnehmer);
		}
		{
			final Nutzer nutzer = turnier.teilnehmerSuchen(organisator.getNutzername());
			assertNotNull(nutzer);
			assertEquals(organisator, nutzer);
			assertEquals(organisator.getName(), nutzer.getName());
			assertEquals(organisator.getVorname(), nutzer.getVorname());
			assertEquals(organisator.getNutzername(), nutzer.getNutzername());
			assertEquals(organisator.getPasswort(), nutzer.getPasswort());
			assertEquals(organisator.getEmail(), nutzer.getEmail());
		}
		{
			final Nutzer teilnehmer = new Nutzer("Radobenko", "David", "sodrek", "passwort", "david@david.de");
			teilnehmer.setId((long) 2);
			turnier.anTurnierAnmelden(teilnehmer);
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(2, anzahlTeilnehmer);
			assertEquals(turnier.getMaxTeilnehmer(), anzahlTeilnehmer);
			final Nutzer nutzer = turnier.teilnehmerSuchen(teilnehmer.getNutzername());
			assertNotNull(nutzer);
			assertEquals(teilnehmer, nutzer);
			assertEquals(teilnehmer.getName(), nutzer.getName());
			assertEquals(teilnehmer.getVorname(), nutzer.getVorname());
			assertEquals(teilnehmer.getNutzername(), nutzer.getNutzername());
			assertEquals(teilnehmer.getPasswort(), nutzer.getPasswort());
			assertEquals(teilnehmer.getEmail(), nutzer.getEmail());
		}
		// teste worst cases
		final Nutzer teilnehmer = new Nutzer("Darwesh", "Siamend", "playboy51", "passwort", "siamend@51.de");
		teilnehmer.setId((long) 3);
		try {
			turnier.anTurnierAnmelden(teilnehmer);
			fail("Turnier.IstVollExc expected");
		} catch (Turnier.IstVollExc expected) {
		}
		// delete one teilnehmer, so i can show that after this exception some nutzer
		// can login into turnier, too
		turnier.entferneTeilnehmer(organisator);
		{
			try {
				final Nutzer nutzer = turnier.teilnehmerSuchen(organisator.getNutzername());
				fail("Turnier.KeinTeilnehmerInDiesemTurnierExc expected");
			} catch (Turnier.KeinTeilnehmerInDiesemTurnierExc expected) {
			}
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(1, anzahlTeilnehmer);
		}
		turnier.anTurnierAnmelden(teilnehmer);
		final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
		assertEquals(2, anzahlTeilnehmer);
		assertEquals(turnier.getMaxTeilnehmer(), anzahlTeilnehmer);
		final Nutzer nutzer = turnier.teilnehmerSuchen(teilnehmer.getNutzername());
		assertNotNull(nutzer);
		assertEquals(teilnehmer, nutzer);
		assertEquals(teilnehmer.getName(), nutzer.getName());
		assertEquals(teilnehmer.getVorname(), nutzer.getVorname());
		assertEquals(teilnehmer.getNutzername(), nutzer.getNutzername());
		assertEquals(teilnehmer.getPasswort(), nutzer.getPasswort());
		assertEquals(teilnehmer.getEmail(), nutzer.getEmail());
		// man kann sich auch nicht nach dem gestarteten und beendeteten Turnier
		// anmelden
		turnier.starteTurnier();
		assertEquals("GESTARTET", turnier.getTurnierStatus().toString());
		try {
			turnier.anTurnierAnmelden(teilnehmer);
			fail("Turnier.FuegeTeilnehmerNichtZugelassenExc expected");
		} catch (Turnier.FuegeTeilnehmerNichtZugelassenExc expected) {
		}
		turnier.beendeTurnier();
		assertEquals("BEENDET", turnier.getTurnierStatus().toString());
		try {
			turnier.anTurnierAnmelden(teilnehmer);
			fail("Turnier.FuegeTeilnehmerNichtZugelassenExc expected");
		} catch (Turnier.FuegeTeilnehmerNichtZugelassenExc expected) {
		}
	}

	@Test
	public void testIstVoll() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 2);
		assertNotNull(organisator);
		assertNotNull(turnier);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(0, anzahlTeilnehmer);
		}
		turnier.anTurnierAnmelden(organisator);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(1, anzahlTeilnehmer);
		}
		final Nutzer teilnehmereins = new Nutzer("Kubacki", "Michal", "miku1", "password", "miq1@miq.pl");
		final Nutzer teilnehmerzwei = new Nutzer("Kubacki", "Michal", "miku2", "password", "miq2@miq.pl");
		turnier.anTurnierAnmelden(teilnehmereins);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(2, anzahlTeilnehmer);
			assertEquals(turnier.getMaxTeilnehmer(), anzahlTeilnehmer);
		}

		try {
			turnier.anTurnierAnmelden(teilnehmerzwei);
			fail("Turnier.IstVollExc excepted");
		} catch (Turnier.IstVollExc expected) {
		}

	}

	@Test
	public void testEntferneTeilnehmer() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 4);
		turnier.anTurnierAnmelden(organisator);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(1, anzahlTeilnehmer);
		}
		{
			final Nutzer nutzer = turnier.teilnehmerSuchen(organisator.getNutzername());
			assertNotNull(nutzer);
			assertEquals(organisator, nutzer);
			assertEquals(organisator.getName(), nutzer.getName());
			assertEquals(organisator.getVorname(), nutzer.getVorname());
			assertEquals(organisator.getNutzername(), nutzer.getNutzername());
			assertEquals(organisator.getPasswort(), nutzer.getPasswort());
			assertEquals(organisator.getEmail(), nutzer.getEmail());
		}
		turnier.entferneTeilnehmer(organisator);
		{
			try {
				final Nutzer nutzer = turnier.teilnehmerSuchen(organisator.getNutzername());
				fail("Turnier.KeinTeilnehmerInDiesemTurnierExc expected");
			} catch (Turnier.KeinTeilnehmerInDiesemTurnierExc expected) {
			}
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(0, anzahlTeilnehmer);

		}
		turnier.anTurnierAnmelden(organisator);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(1, anzahlTeilnehmer);
		}
		{
			final Nutzer nutzer = turnier.teilnehmerSuchen(organisator.getNutzername());
			assertNotNull(nutzer);
			assertEquals(organisator, nutzer);
			assertEquals(organisator.getName(), nutzer.getName());
			assertEquals(organisator.getVorname(), nutzer.getVorname());
			assertEquals(organisator.getNutzername(), nutzer.getNutzername());
			assertEquals(organisator.getPasswort(), nutzer.getPasswort());
			assertEquals(organisator.getEmail(), nutzer.getEmail());
		}
		final Nutzer teilnehmer = new Nutzer("Radobenko", "David", "sodrek", "passwort", "david@david.de");
		teilnehmer.setId((long) 2);
		turnier.anTurnierAnmelden(teilnehmer);
		final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
		assertEquals(2, anzahlTeilnehmer);
		final Nutzer nutzer = turnier.teilnehmerSuchen(teilnehmer.getNutzername());
		assertNotNull(nutzer);
		assertEquals(teilnehmer, nutzer);
		assertEquals(teilnehmer.getName(), nutzer.getName());
		assertEquals(teilnehmer.getVorname(), nutzer.getVorname());
		assertEquals(teilnehmer.getNutzername(), nutzer.getNutzername());
		assertEquals(teilnehmer.getPasswort(), nutzer.getPasswort());
		assertEquals(teilnehmer.getEmail(), nutzer.getEmail());
		turnier.starteTurnier();
		try {
			turnier.entferneTeilnehmer(organisator);
			fail("Turnier.EntferneTeilnehmerNichtZugelassenExc expected");
		} catch (Turnier.EntferneTeilnehmerNichtZugelassenExc expected) {
		}
		turnier.beendeTurnier();
		try {
			turnier.entferneTeilnehmer(organisator);
			fail("Turnier.EntferneTeilnehmerNichtZugelassenExc expected");
		} catch (Turnier.EntferneTeilnehmerNichtZugelassenExc expected) {
		}

	}

	@Test
	public void testTeilnehmerSuchen() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 4);
		turnier.anTurnierAnmelden(organisator);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(1, anzahlTeilnehmer);
		}
		{
			final Nutzer nutzer = turnier.teilnehmerSuchen(organisator.getNutzername());
			assertNotNull(nutzer);
			assertEquals(organisator, nutzer);
			assertEquals(organisator.getName(), nutzer.getName());
			assertEquals(organisator.getVorname(), nutzer.getVorname());
			assertEquals(organisator.getNutzername(), nutzer.getNutzername());
			assertEquals(organisator.getPasswort(), nutzer.getPasswort());
			assertEquals(organisator.getEmail(), nutzer.getEmail());
		}
		turnier.entferneTeilnehmer(organisator);
		{
			try {
				final Nutzer nutzer = turnier.teilnehmerSuchen(organisator.getNutzername());
				fail("Turnier.KeinTeilnehmerInDiesemTurnierExc expected");
			} catch (Turnier.KeinTeilnehmerInDiesemTurnierExc expected) {
			}
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(0, anzahlTeilnehmer);

		}
		turnier.anTurnierAnmelden(organisator);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(1, anzahlTeilnehmer);

			final Nutzer nutzer = turnier.teilnehmerSuchen(organisator.getNutzername());
			assertNotNull(nutzer);
			assertEquals(organisator, nutzer);
			assertEquals(organisator.getName(), nutzer.getName());
			assertEquals(organisator.getVorname(), nutzer.getVorname());
			assertEquals(organisator.getNutzername(), nutzer.getNutzername());
			assertEquals(organisator.getPasswort(), nutzer.getPasswort());
			assertEquals(organisator.getEmail(), nutzer.getEmail());

		}
	}

	public void testGetTurnierErgebnisse() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 4);
		try {
			turnier.getTurnierErgebnisse();
			fail("Turnier.TurnierIstNochNichtBeendetExc excepted");
		} catch (Turnier.TurnierIstNochNichtBeendetExc expected) {
		}
		turnier.anTurnierAnmelden(organisator);
		final Nutzer teilnehmer = new Nutzer("Kubacki", "Michal", "miku2", "password", "miq2@miq.pl");
		turnier.anTurnierAnmelden(teilnehmer);
		turnier.starteTurnier();
		final TurnierBracket turnierBracket = new TurnierBracket("miku", "miku2");
		turnier.turnierBracketHinzufuegen(turnierBracket);
		turnierBracket.setGewinner(3, 2);
		turnier.beendeTurnier();
		assertEquals("miku\nmiku2\n", turnier.getTurnierErgebnisse());

	}

	@Test
	public void testStarteTurnier() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 4);

		turnier.anTurnierAnmelden(organisator);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(1, anzahlTeilnehmer);
		}
		{
			final Nutzer nutzer = turnier.teilnehmerSuchen(organisator.getNutzername());
			assertNotNull(nutzer);
			assertEquals(organisator, nutzer);
			assertEquals(organisator.getName(), nutzer.getName());
			assertEquals(organisator.getVorname(), nutzer.getVorname());
			assertEquals(organisator.getNutzername(), nutzer.getNutzername());
			assertEquals(organisator.getPasswort(), nutzer.getPasswort());
			assertEquals(organisator.getEmail(), nutzer.getEmail());
		}
		final Nutzer teilnehmer = new Nutzer("Radobenko", "David", "sodrek", "passwort", "david@david.de");
		teilnehmer.setId((long) 2);
		turnier.anTurnierAnmelden(teilnehmer);
		final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
		assertEquals(2, anzahlTeilnehmer);
		final Nutzer nutzer = turnier.teilnehmerSuchen(teilnehmer.getNutzername());
		assertNotNull(nutzer);
		assertEquals(teilnehmer, nutzer);
		assertEquals(teilnehmer.getName(), nutzer.getName());
		assertEquals(teilnehmer.getVorname(), nutzer.getVorname());
		assertEquals(teilnehmer.getNutzername(), nutzer.getNutzername());
		assertEquals(teilnehmer.getPasswort(), nutzer.getPasswort());
		assertEquals(teilnehmer.getEmail(), nutzer.getEmail());
		turnier.starteTurnier();
		assertEquals("GESTARTET", turnier.getTurnierStatus().toString());
	}

	@Test
	public void testBeendeTurnier() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 4);

		turnier.anTurnierAnmelden(organisator);
		{
			final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
			assertEquals(1, anzahlTeilnehmer);
		}
		{
			final Nutzer nutzer = turnier.teilnehmerSuchen(organisator.getNutzername());
			assertNotNull(nutzer);
			assertEquals(organisator, nutzer);
			assertEquals(organisator.getName(), nutzer.getName());
			assertEquals(organisator.getVorname(), nutzer.getVorname());
			assertEquals(organisator.getNutzername(), nutzer.getNutzername());
			assertEquals(organisator.getPasswort(), nutzer.getPasswort());
			assertEquals(organisator.getEmail(), nutzer.getEmail());
		}
		final Nutzer teilnehmer = new Nutzer("Radobenko", "David", "sodrek", "passwort", "david@david.de");
		teilnehmer.setId((long) 2);
		turnier.anTurnierAnmelden(teilnehmer);
		final int anzahlTeilnehmer = turnier.getTeilnehmer().size();
		assertEquals(2, anzahlTeilnehmer);
		final Nutzer nutzer = turnier.teilnehmerSuchen(teilnehmer.getNutzername());
		assertNotNull(nutzer);
		assertEquals(teilnehmer, nutzer);
		assertEquals(teilnehmer.getName(), nutzer.getName());
		assertEquals(teilnehmer.getVorname(), nutzer.getVorname());
		assertEquals(teilnehmer.getNutzername(), nutzer.getNutzername());
		assertEquals(teilnehmer.getPasswort(), nutzer.getPasswort());
		assertEquals(teilnehmer.getEmail(), nutzer.getEmail());
		turnier.starteTurnier();
		assertEquals("GESTARTET", turnier.getTurnierStatus().toString());
		turnier.beendeTurnier();
		assertEquals("BEENDET", turnier.getTurnierStatus().toString());
	}

	@Test
	public void testTurnierBracketHinzufuegen() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 4);
		turnier.anTurnierAnmelden(organisator);
		final Nutzer teilnehmer = new Nutzer("Kubacki", "Michal", "miku2", "password", "miq2@miq.pl");
		turnier.anTurnierAnmelden(teilnehmer);
		assertEquals(0, turnier.getTurnierBrackets().size());
		turnier.starteTurnier();
		final TurnierBracket turnierBracket = new TurnierBracket("miku", "miku2");
		turnierBracket.setId(((long) 1));
		turnier.turnierBracketHinzufuegen(turnierBracket);
		assertEquals(1, turnier.getTurnierBrackets().size());
		assertTrue(turnier.getTurnierBrackets().contains(turnierBracket));
		turnierBracket.setGewinner(3, 2);
		assertEquals(1, turnier.getTurnierBrackets().size());
		assertTrue(turnier.getTurnierBrackets().contains(turnierBracket));

	}

	@Test
	public void testShuffleTeilnehmer() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 4);
		turnier.anTurnierAnmelden(organisator);
		final Nutzer teilnehmer = new Nutzer("Kubacki", "Michal", "miku2", "password", "miq2@miq.pl");
		teilnehmer.setId((long) 2);
		turnier.anTurnierAnmelden(teilnehmer);
		assertEquals(2, turnier.getTeilnehmer().size());
		assertTrue(turnier.getTeilnehmer().contains(organisator));
		assertTrue(turnier.getTeilnehmer().contains(teilnehmer));
		turnier.shuffleTeilnehmer();
		assertEquals(2, turnier.getTeilnehmer().size());
		assertTrue(turnier.getTeilnehmer().contains(organisator));
		assertTrue(turnier.getTeilnehmer().contains(teilnehmer));
	}
	
	@Test
	public void testGetTurnierBracketAtPos() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 4);
		turnier.anTurnierAnmelden(organisator);
		final Nutzer teilnehmer = new Nutzer("Kubacki", "Michal", "miku2", "password", "miq2@miq.pl");
		turnier.anTurnierAnmelden(teilnehmer);
		assertEquals(0, turnier.getTurnierBrackets().size());
		turnier.starteTurnier();
		final TurnierBracket turnierBracket = new TurnierBracket("miku", "miku2");
		turnierBracket.setId(((long) 1));
		turnier.turnierBracketHinzufuegen(turnierBracket);
		assertEquals(1, turnier.getTurnierBrackets().size());
		assertTrue(turnier.getTurnierBrackets().contains(turnierBracket));
		turnierBracket.setGewinner(3, 2);
		assertEquals(1, turnier.getTurnierBrackets().size());
		assertTrue(turnier.getTurnierBrackets().contains(turnierBracket));
        assertEquals(turnierBracket, turnier.getTurnierBracketAtPos(0));
		final TurnierBracket turnierBracketzwei = new TurnierBracket("miku3", "miku4");
		turnierBracketzwei.setId(((long) 2));
		turnier.turnierBracketHinzufuegen(turnierBracketzwei);
		assertEquals(2, turnier.getTurnierBrackets().size());
		assertTrue(turnier.getTurnierBrackets().contains(turnierBracketzwei));
		turnierBracketzwei.setGewinner(3, 2);
		assertEquals(2, turnier.getTurnierBrackets().size());
		assertTrue(turnier.getTurnierBrackets().contains(turnierBracketzwei));
        assertEquals(turnierBracketzwei, turnier.getTurnierBracketAtPos(1));

	}
}
