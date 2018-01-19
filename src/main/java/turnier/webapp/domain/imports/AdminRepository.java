package turnier.webapp.domain.imports;

import java.util.List;

import turnier.webapp.domain.Admin;

/**
 * Erforderliche Repository für {@link Admin} Objekte.
 * 
 * @author se2 letzte Gruppe
 * @version 2018-01-14
 * @since 2018-01-14
 */
public interface AdminRepository {

	/**
	 * gib zurück ein {@link Admin} Objekt mit dem übergebenen adminname, wenn es
	 * existiert.
	 * 
	 * @param adminname
	 *            Der Name vom gesuchten Administrator
	 * @return das gefundene Admin Objekt
	 */
	Admin find(String adminname);

	/**
	 * Gibt einem Admin eine einzigartige, größere ID und speichert dem Admin im DB.
	 * 
	 * @param admin
	 *            {@link Admin} Objekt zum speichern
	 * @return modifizierte Instanz
	 */
	Admin save(Admin admin);

	/**
	 * Findet alle {@link Admin}s und gibt alle zurück in einer Liste, "adminname"
	 * aufsteigend sortiert
	 * 
	 * @return alle gefundene {@link Admin} Objekt als Liste
	 */
	List<Admin> findAll();

	/**
	 * löscht alle {@link Admin} Objekte. Brauchbar für Test Cases mit einem leeren
	 * DB
	 */
	void deleteAll();
}
