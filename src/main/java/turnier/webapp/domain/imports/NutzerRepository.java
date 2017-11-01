package turnier.webapp.domain.imports;

import java.util.Optional;
import java.util.List;

import turnier.webapp.domain.Nutzer;


public interface NutzerRepository {
	
	/**Required repository for {@link Nutzer} objects.
	 * @author se2 letzte Gruppe
	 * @version 2017-11-01
	 * @since 2017-10-01
	 */
	
	
    /**Returns the {@link Nutzer} object with the given nutzername, if existing.
     * @throws IllegalArgumentException  id is null
     */
	Nutzer find(String nutzername);
	
	
	
    /**Deletes all {@link Nutzer} objects. Useful for Test Cases with an empty database*/
    void deleteAll();
    
    /**Gives the Nutzer a unique, higher ID and saves the nutzer.
     * @return the modified instance*/
    Nutzer save(Nutzer nutzer);
    
    
    /**Finds all {@link Nutzer}s and returns them ordered by descending Nutzernames.*/
    List<Nutzer> findAll();
}
