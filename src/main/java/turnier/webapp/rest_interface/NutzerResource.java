package turnier.webapp.rest_interface;

import turnier.webapp.domain.Nutzer;

/**Data about a Nutzer of a Turnier-webapp. Usable as Data Transfer Object.*/
public class NutzerResource {
	

	
	    /**Unique ID of the nutzer.*/
		public Long id;

	    /**Vorname of the Nutzer.*/
	    public String vorname;
	    
	    /**Name of the Nutzer.*/
	    public String name;
	    
	    /**Nutzername of the Nutzer. It has to be unique, too.*/
		public String nutzername;

	    /**Passwort of the Nutzer.*/
		public String passwort;
		
	    /**Email of the Nutzer. It has to be unique, too.*/
		public String email;
		
		public int gewonneneSpiele;
		
		public int verloreneSpiele;
		
		public int gewonneneTurniere;
		

	    /**Necessary for Jackson*/
		public NutzerResource() {}

	    /**Constructs a NutzerResource with the data of the passed Nutzer entity.*/
	    public NutzerResource(final Nutzer entity) {
	    	this.id  = entity.getId();
	        this.vorname = entity.getVorname();
	        this.name = entity.getName();
	        this.nutzername = entity.getNutzername();
	        this.email = entity.getEmail();
	        this.passwort = entity.getPasswort();
	        this.gewonneneSpiele = entity.getGewonneneSpiele();
	        this.verloreneSpiele = entity.getVerloreneSpiele();
	        this.gewonneneTurniere = entity.getGewonneneTurniere();
	    }

	    
	    @Override
	    public String toString() {
	    	return String.format("Nutzer{id=%d, name='%s', vorname='%s', nutzername='%s', passwort='%s', email='%s', gewonneneSpiele='%d', verloreneSpiele='%d', gewonneneTurniere='%d'}", id, name, vorname, nutzername, passwort, email, gewonneneSpiele, verloreneSpiele, gewonneneTurniere);
	    }
	    
	}




