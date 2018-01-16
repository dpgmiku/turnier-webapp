package turnier.webapp.rest_interface;

import turnier.webapp.domain.Admin;

/**Data about a Admin of a Turnier-webapp. Usable as Data Transfer Object.*/
public class AdminResource {

	  /**Unique ID of the nutzer.*/
			public Long id;

		    /**Name of the Admin.*/
		    public String adminname;
		    
		    /**Passwort of the Admin.*/
		    public String passwort;
		    
		    /**Necessary for Jackson*/
			public AdminResource() {}

		    /**Constructs a NutzerResource with the data of the passed Nutzer entity.*/
		    public AdminResource(final Admin entity) {
		    	this.id  = entity.getId();
		        this.adminname = entity.getAdminname();
		        this.passwort = entity.getPasswort();
		       
		    }

		    
		    @Override
		    public String toString() {
		    	return String.format("Admin{id=%d, adminname='%s', passwort='%s'}", id, adminname, passwort);
		    }
}
