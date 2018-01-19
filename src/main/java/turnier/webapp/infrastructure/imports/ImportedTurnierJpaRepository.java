package turnier.webapp.infrastructure.imports;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.Turnier;

/**Erforderliche Spring JPA Repository für Turnier Objekten. Die Methoden wurden nach Spring Data JPA Namenkonvention benannt.
 * Die können mit Hilfe vom Spring in der Zeit der Bean-Erstellung implementiert werden, die können aber auch unabhängig vom Spring implementiert werden.
 * @author se2 Letzte Gruppe
 * @since 2018-01-14
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Spring Data Query Methods</a>
 *for deleteById @see <a href="https://stackoverflow.com/questions/23723025/spring-data-delete-by-is-supported">Transactional is needed for delation by</a>
 */

public interface ImportedTurnierJpaRepository extends JpaRepository<Turnier, Long> {

	void deleteAll();

	Turnier findOneByName(String name);

	@Transactional
	void deleteById(Long id);

	Turnier save(Turnier turnier);

	List<Turnier> findAllByOrderByNameAsc();

	List<Turnier> findByOrganisator(Nutzer organisator);

}
