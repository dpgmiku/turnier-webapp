package turnier.webapp.domain.imports;

import java.util.List;

import turnier.webapp.domain.Admin;

public interface AdminRepository {

	/**Erforderliche Repository für {@link Admin} Objekte.
	 * @author se2 letzte Gruppe
	 * @version 2018-01-14
	 * @since 2018-01-14
	 */
	
	 /**gib zürück ein {@link Admin} Objekt mit dem übergebenen adminname, wenn es existiert.
     * @throws IllegalArgumentException  id is null
     */
	Admin find(String adminname);
	
	  /**Gibt einem Admin eine einzigartige, größere ID und speichert dem Admin im DB.
     * @return modifizierte Instanz*/
    Admin save(Admin admin);
    /**Fidet alle {@link Admin}s und gibt alle zurück in einer Liste, descending beim adminname.*/
    List<Admin> findAll();
    
	  /**löscht alle {@link Admin} Objekte. Brauchbar für Test Cases mit einem leeren DB*/
    void deleteAll();
}
