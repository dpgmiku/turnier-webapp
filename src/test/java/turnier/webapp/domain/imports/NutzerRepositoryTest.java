package turnier.webapp.domain.imports;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.junit4.SpringRunner;

import turnier.webapp.domain.Nutzer;

/**Test driver for the Rich Domain Object {@linkplain Nutzer}*/
@RunWith(SpringRunner.class)
//@DataJpaTest
//@AutoConfigureTestDatabase
@SpringBootTest


public class NutzerRepositoryTest {

	
	@Autowired
	private NutzerRepository nutzerRepository;
	
	@Before
	public void setUp() throws Exception {
		nutzerRepository.deleteAll();
    
	}
	
	@Test
	public void testSave() {
		final Nutzer michael = nutzerRepository.save(new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@fakeemail.com"));
        assertNotNull(michael);
        assertEquals("kubacki", michael.getName());
        assertEquals("michal", michael.getVorname());
        assertEquals("miku", michael.getNutzername());
        assertEquals("notyourbusiness", michael.getPasswort());
        assertEquals("miku@fakeemail.com", michael.getEmail());
        assertNotNull(michael.getId());
	
	}

	@Test
	public void testFind() {
		final Nutzer michael = nutzerRepository.save(new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@fakeemail.com"));
	//	final Nutzer david = nutzerRepository.save(new Nutzer("radobenko", "david", "davidr", "notyourbusiness2", "david@fakeemail.com"));
		final Nutzer michael2 = nutzerRepository.find("miku");
		final Nutzer nobody = nutzerRepository.find("nobody");
		assertEquals(michael.getEmail(), michael2.getEmail());
		assertEquals(michael.getName(), michael2.getName());
		assertEquals(michael.getVorname(), michael2.getVorname());
		assertEquals(michael.getPasswort(), michael2.getPasswort());
		assertEquals(michael.getNutzername(), michael2.getNutzername());
		assertNull(nobody);
		

	}

	@Test
	public void testFindEmail() {
		final Nutzer michael = nutzerRepository.save(new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@fakeemail.com"));
		final Nutzer michael2 = nutzerRepository.findEmail("miku@fakeemail.com");
		final Nutzer noEmailnoNutzer = nutzerRepository.findEmail("thismailisnotindb@db.com");
		assertEquals(michael.getEmail(), michael2.getEmail());
		assertEquals(michael.getName(), michael2.getName());
		assertEquals(michael.getVorname(), michael2.getVorname());
		assertEquals(michael.getPasswort(), michael2.getPasswort());
		assertEquals(michael.getNutzername(), michael2.getNutzername());
	    assertNull(noEmailnoNutzer);
	}
	
	
	@Test
	public void testDeleteAll() {
  Nutzer michael = nutzerRepository.save(new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@fakeemail.com"));  
  Nutzer david = nutzerRepository.save(new Nutzer("radobenko", "david", "davidr", "notyourbusiness2", "david@fakeemail.com"));
	nutzerRepository.deleteAll();
	 michael = nutzerRepository.find("miku");
	david = nutzerRepository.find("davidr");
	assertNull(michael);
	assertNull(david);
	
	}
	
	@Test
	public void testDelete() {
	Nutzer michael = nutzerRepository.save(new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@fakeemail.com"));  
	Nutzer david = nutzerRepository.save(new Nutzer("radobenko", "david", "davidr", "notyourbusiness2", "david@fakeemail.com"));
    Nutzer michael2 = nutzerRepository.find(michael.getNutzername());
    final Nutzer david2 = nutzerRepository.find(david.getNutzername());
    assertEquals(michael.getEmail(), michael2.getEmail());
	assertEquals(michael.getName(), michael2.getName());
	assertEquals(michael.getVorname(), michael2.getVorname());
	assertEquals(michael.getPasswort(), michael2.getPasswort());
	assertEquals(michael.getNutzername(), michael2.getNutzername());
    assertEquals(david.getEmail(), david2.getEmail());
assertEquals(david.getName(), david2.getName());
assertEquals(david.getVorname(), david2.getVorname());
assertEquals(david.getPasswort(), david2.getPasswort());
assertEquals(david.getNutzername(), david2.getNutzername());
	nutzerRepository.delete(michael.getId());
    nutzerRepository.delete(david.getId());
    michael = nutzerRepository.find(michael.getNutzername());
    david = nutzerRepository.find(david.getNutzername()); 
    assertNull(michael);
    assertNull(david);
	michael = nutzerRepository.save(new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@fakeemail.com"));  
	michael2 = nutzerRepository.find(michael.getNutzername());
	nutzerRepository.delete(Long.valueOf(5231));
    assertEquals(michael.getEmail(), michael2.getEmail());
	assertEquals(michael.getName(), michael2.getName());
	assertEquals(michael.getVorname(), michael2.getVorname());
	assertEquals(michael.getPasswort(), michael2.getPasswort());
	assertEquals(michael.getNutzername(), michael2.getNutzername());
	nutzerRepository.delete(michael.getId());
    michael = nutzerRepository.find(michael.getNutzername());
    assertNull(michael);
	

	}

	@Test
	public void testFindAll() {
		final Nutzer michael = nutzerRepository.save(new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@fakeemail.com"));  
		final  Nutzer david = nutzerRepository.save(new Nutzer("radobenko", "david", "davidr", "notyourbusiness2", "david@fakeemail.com"));
     List<Nutzer> nutzerListe = nutzerRepository.findAll();
     System.out.println(nutzerListe.size());
     assertEquals(2, nutzerListe.size());
     final Nutzer michael2 = nutzerListe.get(1);
     assertEquals(michael.getEmail(), michael2.getEmail());
		assertEquals(michael.getName(), michael2.getName());
		assertEquals(michael.getVorname(), michael2.getVorname());
		assertEquals(michael.getPasswort(), michael2.getPasswort());
		assertEquals(michael.getNutzername(), michael2.getNutzername());
	     final Nutzer david2 = nutzerListe.get(0);
	    	     assertEquals(david.getEmail(), david2.getEmail());
			assertEquals(david.getName(), david2.getName());
			assertEquals(david.getVorname(), david2.getVorname());
			assertEquals(david.getPasswort(), david2.getPasswort());
			assertEquals(david.getNutzername(), david2.getNutzername());
	}
	    
//	@Test
//	public void testUpdateEmail() {
//		final Nutzer michael = nutzerRepository.save(new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@fakeemail.com"));  
//        nutzerRepository.updateEmail(michael.getId(), "newemail@fakeemail.com");
//        final Nutzer michael2  = nutzerRepository.find(michael.getNutzername());
//  		assertEquals(michael.getId(), michael2.getId());
//      		assertEquals(michael.getName(), michael2.getName());
//      		assertEquals(michael.getVorname(), michael2.getVorname());
//      		assertEquals(michael.getPasswort(), michael2.getPasswort());
//      		assertEquals(michael.getNutzername(), michael2.getNutzername());
//            assertNotEquals(michael.getEmail(), michael2.getEmail());
//            assertEquals("newemail@fakeemail.com", michael2.getEmail());
//            nutzerRepository.updateEmail(Long.valueOf(5), "newemail@fakeemail.com");
//        	assertEquals(michael.getId(), michael2.getId());
//      		assertEquals(michael.getName(), michael2.getName());
//      		assertEquals(michael.getVorname(), michael2.getVorname());
//      		assertEquals(michael.getPasswort(), michael2.getPasswort());
//      		assertEquals(michael.getNutzername(), michael2.getNutzername());
//            assertNotEquals(michael.getEmail(), michael2.getEmail());
//            assertEquals("newemail@fakeemail.com", michael2.getEmail());
//
//		
//	}
//     @Test
//     public void testUpdatePasswort() {
// 		final Nutzer michael = nutzerRepository.save(new Nutzer("kubacki", "michal", "miku", "notyourbusiness", "miku@fakeemail.com"));  
//        nutzerRepository.updatePasswort(michael.getId(), "newpasswd123");
//        final Nutzer michael2  = nutzerRepository.find(michael.getNutzername());
//  		assertEquals(michael.getId(), michael2.getId());
//      		assertEquals(michael.getName(), michael2.getName());
//      		assertEquals(michael.getVorname(), michael2.getVorname());
//      		assertNotEquals(michael.getPasswort(), michael2.getPasswort());
//      		assertEquals(michael.getNutzername(), michael2.getNutzername());
//            assertEquals(michael.getEmail(), michael2.getEmail());
//            assertEquals("newpasswd123", michael2.getPasswort());
//            nutzerRepository.updatePasswort(Long.valueOf(5), "newnewnew123");
//      		assertEquals(michael.getId(), michael2.getId());
//      		assertEquals(michael.getName(), michael2.getName());
//      		assertEquals(michael.getVorname(), michael2.getVorname());
//      		assertNotEquals(michael.getPasswort(), michael2.getPasswort());
//      		assertEquals(michael.getNutzername(), michael2.getNutzername());
//            assertEquals(michael.getEmail(), michael2.getEmail());
//            assertEquals("newpasswd123", michael2.getPasswort());
//    	 
//     }
	
	
		
}
