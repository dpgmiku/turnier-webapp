//package turnier.webapp.domain.imports;
//
//import static org.junit.Assert.*;
//
//import org.junit.Before;
//import org.junit.Test;
//
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import turnier.webapp.domain.Nutzer;
//import turnier.webapp.domain.Turnier;
//
///**Test driver for the Rich Domain Object {@linkplain Turnier}*/
//@RunWith(SpringRunner.class)
////@DataJpaTest
////@AutoConfigureTestDatabase
//@SpringBootTest
//
//public class TurnierRepositoryTest {
//
//	@Autowired
//	private TurnierRepository turnierRepository;
//	
//	@Before
//	public void setUp() throws Exception {
//		turnierRepository.deleteAll();
//	}
//	
//	@Test
//	public void testFind() {
//		Nutzer david = new Nutzer("radobenko", "david", "sodrek", "LassKnacken", "david@nsa.com");
//		LocalDate datum = LocalDate.of(2018, 7, 15);
//		LocalTime uhrzeit = LocalTime.of(12, 0);
//		Turnier turnier = new Turnier("PPT", "Teilstr 7, 12245 Berlin", datum, uhrzeit , david, 100);
//	}
//	
//	@Test
//	public void testFindTurniereVonNutzer() {
//		Nutzer david = new Nutzer("radobenko", "david", "sodrek", "LassKnacken", "david@nsa.com");
//		LocalDate datum = LocalDate.of(2018, 7, 15);
//		LocalTime uhrzeit = LocalTime.of(12, 0);
//		Turnier turnier = new Turnier("PPT", "Teilstr 7, 12245 Berlin", datum, uhrzeit , david, 100);
//	}
//	
//	@Test
//	public void testDeleteAll() {
//		Nutzer david = new Nutzer("radobenko", "david", "sodrek", "LassKnacken", "david@nsa.com");
//		LocalDate datum = LocalDate.of(2018, 7, 15);
//		LocalTime uhrzeit = LocalTime.of(12, 0);
//		Turnier turnier = new Turnier("PPT", "Teilstr 7, 12245 Berlin", datum, uhrzeit , david, 100);
//	}
//	
//	@Test
//	public void testSave() {
//		Nutzer david = new Nutzer("radobenko", "david", "sodrek", "LassKnacken", "david@nsa.com");
//		LocalDate datum = LocalDate.of(2018, 7, 15);
//		LocalTime uhrzeit = LocalTime.of(12, 0);
//		Turnier turnier = new Turnier("PPT", "Teilstr 7, 12245 Berlin", datum, uhrzeit , david, 100);
//	}
//	
//	@Test
//	public void testFindAll() {
//		Nutzer david = new Nutzer("radobenko", "david", "sodrek", "LassKnacken", "david@nsa.com");
//		LocalDate datum = LocalDate.of(2018, 7, 15);
//		LocalTime uhrzeit = LocalTime.of(12, 0);
//		Turnier turnier = new Turnier("PPT", "Teilstr 7, 12245 Berlin", datum, uhrzeit , david, 100);
//	}
//	
//	@Test
//	public void testDelete() {
//		Nutzer david = new Nutzer("radobenko", "david", "sodrek", "LassKnacken", "david@nsa.com");
//		LocalDate datum = LocalDate.of(2018, 7, 15);
//		LocalTime uhrzeit = LocalTime.of(12, 0);
//		Turnier turnier = new Turnier("PPT", "Teilstr 7, 12245 Berlin", datum, uhrzeit , david, 100);
//	}
//	
//}
