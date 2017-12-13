package turnier.webapp.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.time.LocalTime;

/** Test driver for the Rich Domain Object {@linkplain Turnier} */
@RunWith(SpringRunner.class)
// @DataJpaTest
// @AutoConfigureTestDatabase
@SpringBootTest

public class TurnierTest {
	
	/** Only for use in the cleanUp method! */
	@Autowired
	private CleanupService cleanupService;
	
	@Before
	public void setUp() {
		cleanupService.deleteAll();
	}
	
	@Test
	public void testTeilnehmerSuchen() {
		Nutzer david = new Nutzer("radobenko", "david", "sodrek", "LassKnacken", "david@nsa.com");
		LocalDate datum = LocalDate.of(2018, 7, 15);
		LocalTime uhrzeit = LocalTime.of(12, 0);
		Turnier turnier = new Turnier("PPT", "Teilstr 7, 12245 Berlin", datum, uhrzeit , david, 100);
		turnier.getTeilnehmer().add(david);
		Nutzer test = turnier.teilnehmerSuchen("radobenko");
		assertNotNull(test);
//		fail("Not yet implemented");
	}

//	@Test
//	public void testKreireTurnierbaum() {
//		Nutzer david = new Nutzer("radobenko", "david", "sodrek", "LassKnacken", "david@nsa.com");
//		LocalDate datum = LocalDate.of(2018, 7, 15);
//		LocalTime uhrzeit = LocalTime.of(12, 0);
//		Turnier turnier = new Turnier("PPT", "Teilstr 7, 12245 Berlin", datum, uhrzeit , david, 100);
//		turnier.getTeilnehmer().add(david);
//		
//		TurnierBracket bracket = turnier.kreireTurnierbaum(turnier.getTeilnehmer());
//		assertNotNull(bracket);
//		
////		fail("Not yet implemented");
//	}

	@Test
	public void testStarteTurnier() {
		Nutzer david = new Nutzer("radobenko", "david", "sodrek", "LassKnacken", "david@nsa.com");
		LocalDate datum = LocalDate.of(2018, 7, 15);
		LocalTime uhrzeit = LocalTime.of(12, 0);
		Turnier turnier = new Turnier("PPT", "Teilstr 7, 12245 Berlin", datum, uhrzeit , david, 100);
		turnier.setTurnierStatus(TurnierStatus.GESTARTET);
		assertTrue(turnier.getTurnierStatus() == TurnierStatus.GESTARTET);
//		fail("Not yet implemented");
	}

	// TODO beende die Tests
//	@Test
//	public void testBeendeTurnier() {
//		Nutzer david = new Nutzer("radobenko", "david", "sodrek", "LassKnacken", "david@nsa.com");
//		LocalDate datum = LocalDate.of(2018, 7, 15);
//		LocalTime uhrzeit = LocalTime.of(12, 0);
//		Turnier turnier = new Turnier("PPT", "Teilstr 7, 12245 Berlin", datum, uhrzeit , david, 100);
//		turnier.setTurnierStatus(TurnierStatus.BEENDET);
//		try {
//			turnier.setTurnierStatus(TurnierStatus.BEENDET);
//			fail("Turnier hat schon den Status Beendet");
//		} catch(Turnier.GleicherTurnierStatusExc expected) {
//			
//		}
////		fail("Not yet implemented");
//	}
	
	

}
