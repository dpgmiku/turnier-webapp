package turnier.webapp.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TurnierBracketTest {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSetGewinner() {
		final TurnierBracket turnierBracket = new TurnierBracket("miku", "dekized");
		assertEquals("miku", turnierBracket.getNutzername1());
		assertEquals("dekized", turnierBracket.getNutzername2());
		assertEquals(0, turnierBracket.getErgebnis1());
		assertEquals(0, turnierBracket.getErgebnis2());
		assertEquals("", turnierBracket.getGewinner());
		assertEquals("", turnierBracket.getVerlierer());
		turnierBracket.setGewinner(2, 3);
		assertEquals("miku", turnierBracket.getNutzername1());
		assertEquals("dekized", turnierBracket.getNutzername2());
		assertEquals(2, turnierBracket.getErgebnis1());
		assertEquals(3, turnierBracket.getErgebnis2());
		assertEquals("dekized", turnierBracket.getGewinner());
		assertEquals("miku", turnierBracket.getVerlierer());
		try {
			turnierBracket.setGewinner(4, 5);
			fail("TurnierBracket.ErgebnisSchonDaExc expected");
		} catch (TurnierBracket.ErgebnisSchonDaExc expected) {
		}
		assertEquals("miku", turnierBracket.getNutzername1());
		assertEquals("dekized", turnierBracket.getNutzername2());
		assertEquals(2, turnierBracket.getErgebnis1());
		assertEquals(3, turnierBracket.getErgebnis2());
		assertEquals("dekized", turnierBracket.getGewinner());
		assertEquals("miku", turnierBracket.getVerlierer());
	}

	@Test
	public void testToString() {
		final TurnierBracket turnierBracket = new TurnierBracket("miku", "dekized");
		assertEquals(
				"TurnierBracket{id='null', nutzername1='miku', nutzername2='dekized', ergebnis1='0', ergebnis2='0', gewinner='', verlierer=''}",
				turnierBracket.toString());
		turnierBracket.setGewinner(2, 3);
		assertEquals(
				"TurnierBracket{id='null', nutzername1='miku', nutzername2='dekized', ergebnis1='2', ergebnis2='3', gewinner='dekized', verlierer='miku'}",
				turnierBracket.toString());

	}

}
