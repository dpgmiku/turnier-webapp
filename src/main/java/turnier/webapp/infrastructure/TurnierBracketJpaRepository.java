package turnier.webapp.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.TurnierBracket;
import turnier.webapp.domain.imports.TurnierBracketRepository;
import turnier.webapp.infrastructure.imports.ImportedTurnierBracketJpaRepository;
import turnier.webapp.infrastructure.imports.ImportedTurnierJpaRepository;

@Service
public class TurnierBracketJpaRepository implements TurnierBracketRepository {

	private final ImportedTurnierBracketJpaRepository impl;

	
	//Für alle Parameter werden typmaessig passende Beans (Komponenten) gesucht oder erzeugt
	   @Autowired
	    public TurnierBracketJpaRepository(final ImportedTurnierBracketJpaRepository impl) {
	        this.impl = impl;
	    }

	@Override
	public TurnierBracket save(TurnierBracket turnierBracket) {
		return impl.save(turnierBracket);
	}

}
