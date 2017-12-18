package turnier.webapp.domain.imports;

import java.util.Optional;
import java.util.List;

import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.Turnier;


public interface TurnierRepository {
	
	/**Required repository for {@link Turnier} objects.
	 * @author se2 letzte Gruppe
	 * @version 2017-12-12
	 * @since 2017-12-12
	 */
	
	
    /**Returns the {@link Turnier} object with the given name, if existing.
     * @param name name of the turnier
     * @throws IllegalArgumentException  id is null
     */
	Turnier find(String name);
	
	/**Returns all of {@link Turnier} objects as list, which {@link Nutzer) organised , if existing.
     * @param nutzer nutzer organiser
     * @throws IllegalArgumentException  id is null
     */
	List<Turnier> findTurniereVonNutzer(Nutzer nutzer);
	
//	void updateEmail(Long id, String email);
	
//	void updatePasswort(Long id, String passwort);
	
    /**Deletes all {@link Turnier} objects. Useful for Test Cases with an empty database*/
    void deleteAll();
    
    /**Gives the {@link Turnier} a unique, higher ID and saves the turnier.
     * @return the modified instance*/
    Turnier save(Turnier turnier);
    
    
    /**Finds all {@link Turnier}s and returns them ordered by descending name.*/
    List<Turnier> findAll();

    /**Deletes {@link Turnier} object with given Id*/
	void delete(Long id);

}
