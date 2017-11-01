package turnier.webapp.domain;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class TurnierBracket {
	@Id
	final String id;
	
	public TurnierBracket(String id) {
		
		this.id = id;
	}
	//externe Klasse, jetzt gilt nur als placeholder

}
