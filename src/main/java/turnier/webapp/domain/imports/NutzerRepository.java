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
	
	/**Returns the {@link Nutzer} object with the given email address, if existing.
     * @throws IllegalArgumentException  id is null
     */
	Nutzer findEmail(String email);
	
	/**
	 * updates email 
	 * @param id id of the {@link Nutzer} object, which have to update an email
	 * @param email new email address
	 */
	void updateEmail(Long id, String email);
	
	void updatePasswort(Long id, String passwort);
	
    /**Deletes all {@link Nutzer} objects. Useful for Test Cases with an empty database*/
    void deleteAll();
    
    /**Gives the Nutzer a unique, higher ID and saves the nutzer.
     * @return the modified instance*/
    Nutzer save(Nutzer nutzer);
    
    
    /**Finds all {@link Nutzer}s and returns them ordered by descending Nutzernames.*/
    List<Nutzer> findAll();

    /**Deletes {@link Nutzer} object with given Id*/
	void delete(Long id);
}
