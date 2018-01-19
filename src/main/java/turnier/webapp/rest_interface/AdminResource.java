package turnier.webapp.rest_interface;

import turnier.webapp.domain.Admin;

/**
 * Daten über Admin vom Turnier-Webapp. Brauchbar für Data Transfer Object.
 */
public class AdminResource {

	/** Einzigartige ID-Nummer vom Admin */
	public Long id;

	/** Der Name vom Admin. */
	public String adminname;

	/** Das Passwort vom Admin. */
	public String passwort;

	/** erforderlich für Jackson */
	public AdminResource() {
	}

	/**
	 * kreirt AdminResource mit den Daten aus übergebenen Adminentität.
	 * 
	 * @param entity
	 *            admin Objekt
	 */
	public AdminResource(final Admin entity) {
		this.id = entity.getId();
		this.adminname = entity.getAdminname();
		this.passwort = entity.getPasswort();

	}

	@Override
	public String toString() {
		return String.format("Admin{id=%d, adminname='%s', passwort='%s'}", id, adminname, passwort);
	}
}
