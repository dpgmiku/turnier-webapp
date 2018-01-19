package turnier.webapp.infrastructure.imports;

/**Erforderliche Spring JPA Repository für Nutzer Objekten. Die Methoden wurden nach Spring Data JPA Namenkonvention benannt.
 * Die können mit Hilfe vom Spring in der Zeit der Bean-Erstellung implementiert werden, die können aber auch unabhängig vom Spring implementiert werden.
 * @author se2 Letzte Gruppe
 * @since 2018-01-14
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Spring Data Query Methods</a>
 *for deleteById @see <a href="https://stackoverflow.com/questions/23723025/spring-data-delete-by-is-supported">Transactional is needed for delation by</a>
 */
import turnier.webapp.domain.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImportedNutzerJpaRepository extends JpaRepository<Nutzer, Long> {

	void deleteAll();

	Nutzer save(Nutzer nutzer);

	Nutzer findOneByNutzername(String nutzername);

	List<Nutzer> findAllByOrderByNutzernameAsc();

	Nutzer findOneByEmail(String email);

	@Transactional
	void deleteById(Long id);

}
