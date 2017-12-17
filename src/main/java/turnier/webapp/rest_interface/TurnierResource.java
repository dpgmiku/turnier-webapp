package turnier.webapp.rest_interface;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.Turnier;
import turnier.webapp.domain.TurnierStatus;

/**Data about a Turnier of a Turnier-webapp. Usable as Data Transfer Object.*/
public class TurnierResource {
	
	
    /**Unique ID of the turnier.*/
		public Long id;

	    /**Name of the turnier.*/
	    public String name;
	    
	    /**Adresse of the turnier.*/
	    public String adresse;
	    
	    /**Datum of the turnier*/
		public String datum;

	    /**Uhrzeit of the turnier.*/
		public String uhrzeit;
		
	    /**Organisator of the Turnier.*/
		public Nutzer organisator;
		
	    /**Max Anzahl of Teilneher in the turnier.*/
		public int maxTeilnehmer;
		
	    /**turnierStatus.*/
		public String turnierStatus;
		
		/**All of the Teilnehmer saved in array.*/
		public List<Nutzer> teilnehmer;
		
		

	    /**Necessary for Jackson*/
		public TurnierResource() {}

	    /**Constructs a NutzerResource with the data of the passed Nutzer entity.*/
	    public TurnierResource(final Turnier entity) {
	    	this.id  = entity.getId();
	        this.name = entity.getName();
	        this.adresse = entity.getAdresse();
	        this.datum = entity.getDatum();
	        this.uhrzeit = entity.getUhrzeit();
	        this.organisator = entity.getOrganisator();
	        this.maxTeilnehmer = entity.getMaxTeilnehmer();
	        this.turnierStatus = entity.getTurnierStatus().toString();
	        this.teilnehmer = entity.getTeilnehmer();
	    }

	    
		@Override
		public String toString() {
			return String.format("Turnier{id=%d, name='%s', adresse='%s', datum='%s', uhrzeit='%s', organisator='%s, maxTeilnehmer=%d, turnierstatus='%s', teilnehmer='%s'}",
					id, name, name, adresse, datum.toString(), uhrzeit.toString(), organisator.toString(), maxTeilnehmer, turnierStatus.toString(), teilnehmer.toString());
		}

}
