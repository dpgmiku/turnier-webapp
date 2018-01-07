package turnier.webapp.domain;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;


import turnier.webapp.domain.base.EntityBase;
import turnier.webapp.domain.imports.NutzerRepository;

import static multex.MultexUtil.create;


@Entity
public class TurnierBracket extends EntityBase<TurnierBracket>{
	
	String nutzername1;
	String nutzername2;
	int ergebnis1;
	int ergebnis2;
    String gewinner;
    String verlierer;
    
	
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
	
	   @Autowired
	private transient NutzerRepository nutzerRepository;    
	   
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
	Nutzer gewinnerNutzer = nutzerRepository.find(this.gewinner);
	Nutzer verliererNutzer = nutzerRepository.find(this.verlierer);
	gewinnerNutzer.hatGewonnen();
	verliererNutzer.hatVerloren();
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
	
	/**
	 * Nutzer mit dem Nutzername {0} hat schon gegen {1} gewonnen
	 */
	@SuppressWarnings("serial")
	public static class ErgebnisSchonDaExc extends multex.Exc {
	}
	
		

	

}
