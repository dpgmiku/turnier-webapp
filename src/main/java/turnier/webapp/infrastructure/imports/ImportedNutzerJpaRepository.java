package turnier.webapp.infrastructure.imports;

import turnier.webapp.domain.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;


/**Required Spring JPA repository for Nutzers. The methods are named according to the Spring Data JPA convention.
 * They can be implemented by Spring during bean creation, but can be implemented independently of Spring, too.
 * @author se2 Letzte Gruppe
 * @since 2017-10-01
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Spring Data Query Methods</a>
 */

public interface ImportedNutzerJpaRepository extends JpaRepository<Nutzer, Long>{
	
    /**Deletes all Nutzers. Useful for test scenarions in order to start with an empty Nutzer set*/
    void deleteAll();
    
    Nutzer save(Nutzer nutzer);

    void delete(Nutzer nutzer);
    
    Nutzer findOneByNutzername(String nutzername);
    
    List<Nutzer> findAllByOrderByNutzernameAsc();

    
    
    

	

}
