package turnier.webapp.domain;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TurnierTest {

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

	@Test
	public void testKreireTurnierbaum() {
	}

	@Test
	public void testStarteTurnier() {
		final Nutzer organisator = new Nutzer("Kubacki", "Michal", "miku", "password", "miq@miq.pl");
		organisator.setId((long) 1);
		final Turnier turnier = new Turnier("turniername", "wyszynskiego 2", "20.12.2017", "13.40", organisator, 4);
		try {
			 turnier.starteTurnier();
			fail("Turnier.AnzahlTeilnehmerNoPowerOFTwoExc expected");
		} catch (Turnier.AnzahlTeilnehmerNoPowerOfTwoExc expected) {}
		
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
		
		try {
			 turnier.starteTurnier();
			fail("Turnier.AnzahlTeilnehmerNoPowerOFTwoExc expected");
		} catch (Turnier.AnzahlTeilnehmerNoPowerOfTwoExc expected) {}
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
	}

	@Test
	public void testBeendeTurnier() {
	}

}
