package turnier.webapp.infrastructure.imports;

import java.util.List;

import turnier.webapp.domain.Admin;

/**Required Spring JPA repository for Admins. The methods are named according to the Spring Data JPA convention.
 * They can be implemented by Spring during bean creation, but can be implemented independently of Spring, too.
 * @author se2 Letzte Gruppe
 * @since 2018-01-14
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Spring Data Query Methods</a>
 *for deleteById @see <a href="https://stackoverflow.com/questions/23723025/spring-data-delete-by-is-supported">Transactional is needed for delation by</a>
 */

public interface ImportedAdminJpaRepository {

    /**Deletes all Admins. Useful for test scenarios in order to start with an empty Nutzer set*/
	 void deleteAll();
	 
	 Admin save(Admin admin);

	Admin findOneByAdminname(String adminname);

	List<Admin> findAllByOrderByAdminnameAsc();
	 
	 
	 
	 
}
