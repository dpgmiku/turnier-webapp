package turnier.webapp.domain;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;


import turnier.webapp.domain.base.EntityBase;
import turnier.webapp.domain.imports.NutzerRepository;

import static multex.MultexUtil.create;


@Entity
public class TurnierBracket extends EntityBase<TurnierBracket>{
	
	private String nutzername1;
	private String nutzername2;
	private int ergebnis1;
	private int ergebnis2;
 

	public void setNutzername2(String nutzername2) {
		this.nutzername2 = nutzername2;
	}

	private String gewinner;
    private String verlierer;
    
	
	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	private TurnierBracket() {
	};
	
	public TurnierBracket(String nutzername1, String nutzername2) {
	this.nutzername1 = nutzername1;
	this.nutzername2 = nutzername2;
	this.ergebnis1 = 0;
	this.ergebnis2 = 0;
	this.gewinner = "";
	this.verlierer= "";
	}
  
	   
	public void setGewinner(int ergebnis1, int ergebnis2) {
	if (!(gewinner.equals(""))) throw create(TurnierBracket.ErgebnisSchonDaExc.class, gewinner, verlierer);
	this.ergebnis1 = ergebnis1;
	this.ergebnis2 = ergebnis2;
	if (ergebnis1 > ergebnis2) {
    gewinner = nutzername1;
    verlierer = nutzername2;
	}
	else
	{
	gewinner = nutzername2;
	verlierer = nutzername1;
	}

	}
	
	public String getGewinner() {
		
		return gewinner;
	}
	
	public String getVerlierer() {
		
		return verlierer;
	}
	
	public int getErgebnis1() {
		
		return ergebnis1;
	}
	
	public int getErgebnis2() {
		
		return ergebnis2;
	}
	
	public void setGewinner(String gewinner) {
		
		this.gewinner = gewinner;
	}
	
	public void setVerlierer(String verlierer) {
		
		this.verlierer = verlierer;
	}
	
	   public String getNutzername1() {
			return nutzername1;
		}

		public void setNutzername1(String nutzername1) {
			this.nutzername1 = nutzername1;
		}

		public String getNutzername2() {
			return nutzername2;
		}
	
	/**
	 * Nutzer mit dem Nutzername {0} hat schon gegen {1} gewonnen
	 */
	@SuppressWarnings("serial")
	public static class ErgebnisSchonDaExc extends multex.Exc {
	}
	
	
	@Override
	public String toString() {
		return String.format("TurnierBracket{id='%d', nutzername1='%s', nutzername2='%s', ergebnis1='%d', ergebnis2='%d', gewinner='%s', verlierer='%s'}", 
				getId(), nutzername1, nutzername2, ergebnis1, ergebnis2, gewinner, verlierer);
			
	}
	

	

}
