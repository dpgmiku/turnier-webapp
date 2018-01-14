package turnier.webapp.domain;

import javax.persistence.Entity;

import turnier.webapp.domain.base.EntityBase;

//@Entity sag uns bescheid, dass bei der Klasse JPA sich um eine JPA entity handelt
@Entity
public class Admin extends EntityBase<Admin> {
	
	private String adminname;
	private String passwort;

	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private Admin() {
	};
	
	/**
	 * Konstruktor für Admin objekt
	 * @param nutzername Einzigartiger Nutzername vom Nutzer 
	 * @param passwort Passwwort vom Admin
	 */
		public Admin(String adminname, String passwort) {

		this.adminname = adminname;
		this.passwort = passwort;

		}

		@Override
		public String toString() {
			return String.format("Admin{id=%d, adminname='%s', passwort='%s'}",
					getId(), adminname, passwort);
		}

}
