package turnier.webapp.domain;

import javax.persistence.Entity;

import turnier.webapp.domain.base.EntityBase;

/**
 * Admin mit einem adminname und passwort, kann die Nutzer- und
 * Turniereigenschaften ändern. Diese Entität ist ein Anemic Domain Objekt, denn
 * er hat nur die stupide Setters.
 */
// @Entity sag uns bescheid, dass bei der Klasse JPA sich um eine JPA entity
// handelt
@Entity
public class Admin extends EntityBase<Admin> {
	/** Administrator Name */
	private String adminname;
	/** Administrator-Passwort */
	private String passwort;

	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private Admin() {
	};

	/**
	 * Konstruktor für Admin Objekt
	 * 
	 * @param adminname
	 *            Einzigartiger Adminname vom Admin
	 * @param passwort
	 *            Passwwort vom Admin
	 */
	public Admin(String adminname, String passwort) {
		this.adminname = adminname;
		this.passwort = passwort;
	}

	/**
	 * Adminname getter
	 * 
	 * @return adminname - gibt der Administrator Name zurück
	 */
	public String getAdminname() {
		return adminname;
	}

	/**
	 * Adminname setter
	 * 
	 * @param adminname
	 *            - neuer Adminname
	 */
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	/**
	 * Passwort getter
	 * 
	 * @return passwort - gibt der Administrator-Passwort zurück
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * Passwort setter
	 * 
	 * @param passwort
	 *            - neuer Passwort
	 */
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}

	@Override
	public String toString() {
		return String.format("Admin{id=%d, adminname='%s', passwort='%s'}", getId(), adminname, passwort);
	}

}
