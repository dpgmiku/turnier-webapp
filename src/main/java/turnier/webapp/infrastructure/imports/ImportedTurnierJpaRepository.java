package turnier.webapp.infrastructure.imports;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.Turnier;


/**Required Spring JPA repository for Turniers. The methods are named according to the Spring Data JPA convention.
 * They can be implemented by Spring during bean creation, but can be implemented independently of Spring, too.
 * @author se2 Letzte Gruppe
 * @since 2017-12-12
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Spring Data Query Methods</a>
 *for deleteById @see <a href="https://stackoverflow.com/questions/23723025/spring-data-delete-by-is-supported">Transactional is needed for delation by</a>
 */


public interface ImportedTurnierJpaRepository extends JpaRepository<Turnier, Long> {

	Turnier findOneByName(String name);
	
	@Transactional
	void deleteById(Long id);

	Turnier save(Turnier turnier);

	List<Turnier> findAllByOrderByNameAsc();

	List<Turnier> findByOrganisator(Nutzer organisator);

    /**Deletes all Nutzers. Useful for test scenarios in order to start with an empty Nutzer set*/
	void deleteAll();

}
