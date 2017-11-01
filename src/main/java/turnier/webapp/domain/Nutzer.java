package turnier.webapp.domain;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity

public class Nutzer {
	
	private String name;
	private String vorname;
	@Id
	private String nutzername;
	private String passwort;
	private String email;
	
	public Nutzer(String name, String vorname, String nutzername, String passwort, String email) {
		
		this.setName(name);
		this.setVorname(vorname);
		this.setNutzername(nutzername);
		this.setPasswort(passwort);
		this.setEmail(email);
	
		
	}
	
	public Nutzer() {
		
		
	}
	;
	
	
	public void nutzerLoeschen(String nutzer) {
		
		
	}
	
	public void passwortAendern(String altesPasswort, String neuesPasswort) {
		
		
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getVorname() {
		return vorname;
	}


	public void setVorname(String vorname) {
		this.vorname = vorname;
	}


	public String getNutzername() {
		return nutzername;
	}


	public void setNutzername(String nutzername) {
		this.nutzername = nutzername;
	}


	public String getPasswort() {
		return passwort;
	}


	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	


}
