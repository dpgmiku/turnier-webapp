package turnier.webapp.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.Admin;
import turnier.webapp.domain.imports.AdminRepository;
import turnier.webapp.infrastructure.imports.ImportedAdminJpaRepository;

/**
 * Repository für Adminentitäten, die mit Spring Data JPA implementiert wurden.
 * 
 * @author se2 letzte Gruppe
 * @since 2017-01-14
 */
// Komponente ohne Zustand, nur mit Methoden
@Service
public class AdminJpaRepository implements AdminRepository {

	private final ImportedAdminJpaRepository impl;

	@Autowired
	public AdminJpaRepository(final ImportedAdminJpaRepository impl) {
		this.impl = impl;
	}

	@Override
	public Admin find(String adminname) {
		return impl.findOneByAdminname(adminname);

	}

	@Override
	public Admin save(Admin admin) {
		return impl.save(admin);
	}

	@Override
	public List<Admin> findAll() {
		return impl.findAllByOrderByAdminnameAsc();
	}

	@Override
	public void deleteAll() {
		impl.deleteAll();
	}

}
