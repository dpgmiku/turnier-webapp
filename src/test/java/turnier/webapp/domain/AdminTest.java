package turnier.webapp.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AdminTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetAdminname() {
		final Admin admin = new Admin("admin", "admin123");
		assertEquals("admin", admin.getAdminname());
	}

	@Test
	public void testSetAdminname() {
		final Admin admin = new Admin("admin", "admin123");
		assertEquals("admin", admin.getAdminname());
		admin.setAdminname("neuadmin");
		assertEquals("neuadmin", admin.getAdminname());
	}

	@Test
	public void testGetPasswort() {
		final Admin admin = new Admin("admin", "admin123");
		assertEquals("admin123", admin.getPasswort());
	}

	@Test
	public void testSetPasswort() {
		final Admin admin = new Admin("admin", "admin123");
		assertEquals("admin123", admin.getPasswort());
		admin.setPasswort("123admin");
		assertEquals("123admin", admin.getPasswort());
	}

	@Test
	public void testToString() {
		final Admin admin = new Admin("admin", "admin123");
		assertEquals("Admin{id=null, adminname='admin', passwort='admin123'}", admin.toString());
	}

}
