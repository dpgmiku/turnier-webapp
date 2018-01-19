package turnier.webapp.infrastructure.imports;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import turnier.webapp.domain.Admin;

/**
 * Erforderliche Spring JPA Repository für Admin Objekten. Die Methoden wurden
 * nach Spring Data JPA Namenkonvention benannt. Die können mit Hilfe vom Spring
 * in der Zeit der Bean-Erstellung implementiert werden, die können aber auch
 * unabhängig vom Spring implementiert werden.
 * 
 * @author se2 Letzte Gruppe
 * @since 2018-01-14
 */

public interface ImportedAdminJpaRepository extends JpaRepository<Admin, Long> {

	void deleteAll();

	Admin save(Admin admin);

	Admin findOneByAdminname(String adminname);

	List<Admin> findAllByOrderByAdminnameAsc();

}
