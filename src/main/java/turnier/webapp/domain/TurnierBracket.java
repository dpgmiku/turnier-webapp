package turnier.webapp.domain;
import javax.persistence.Entity;
import javax.persistence.Id;

import turnier.webapp.domain.base.EntityBase;


@Entity
public class TurnierBracket extends EntityBase<TurnierBracket>{

	private String id;
	
	/** Necessary for JPA entities internally. */
	@SuppressWarnings("unused")
	public TurnierBracket() {
	};
	
	public TurnierBracket(String id) {
		
		this.id = id;
	}
	
		

	//externe Klasse, jetzt gilt nur als placeholder

}
