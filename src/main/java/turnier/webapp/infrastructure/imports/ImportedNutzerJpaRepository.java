package turnier.webapp.infrastructure.imports;

import turnier.webapp.domain.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;


/**Required Spring JPA repository for Nutzers. The methods are named according to the Spring Data JPA convention.
 * They can be implemented by Spring during bean creation, but can be implemented independently of Spring, too.
 * @author se2 Letzte Gruppe
 * @since 2017-10-01
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Spring Data Query Methods</a>
 *for deleteById @see <a href="https://stackoverflow.com/questions/23723025/spring-data-delete-by-is-supported">Transactional is needed for delation by</a>
 */

public interface ImportedNutzerJpaRepository extends JpaRepository<Nutzer, Long>{
	
    /**Deletes all Nutzers. Useful for test scenarios in order to start with an empty Nutzer set*/
    void deleteAll();
    
    Nutzer save(Nutzer nutzer);
        
    Nutzer findOneByNutzername(String nutzername);
    
    List<Nutzer> findAllByOrderByNutzernameAsc();

	Nutzer findOneByEmail(String email);
	
	@Transactional
	@Modifying
	@Query("update Nutzer n set n.email = ?1 where n.id = ?2")
	void setEmailById(String email, Long id);
    
	@Transactional
	@Modifying
	@Query("update Nutzer n set n.passwort = ?1 where n.id = ?2")
	void setPasswortById(String passwort, Long id);
    
	@Transactional
	void deleteById(Long id);
	

    
    
    

	

}
