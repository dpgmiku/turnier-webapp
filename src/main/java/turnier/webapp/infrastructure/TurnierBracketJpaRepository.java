package turnier.webapp.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import turnier.webapp.domain.TurnierBracket;
import turnier.webapp.domain.imports.TurnierBracketRepository;
import turnier.webapp.infrastructure.imports.ImportedTurnierBracketJpaRepository;
/**
 * Repository für Turnierbracketentitäten, die mit Spring Data JPA implementiert wurden.
 * 
 * @author se2 letzte Gruppe
 * @since 2017-10-01
 */
@Service
public class TurnierBracketJpaRepository implements TurnierBracketRepository {

	private final ImportedTurnierBracketJpaRepository impl;

	// Für alle Parameter werden typmaessig passende Beans (Komponenten) gesucht
	// oder erzeugt
	@Autowired
	public TurnierBracketJpaRepository(final ImportedTurnierBracketJpaRepository impl) {
		this.impl = impl;
	}

	@Override
	public TurnierBracket save(TurnierBracket turnierBracket) {
		return impl.save(turnierBracket);
	}

	@Override
	public void deleteAll() {
		impl.deleteAll();
	}

	@Override
	public void delete(TurnierBracket turnierBracket) {
		impl.delete(turnierBracket);
	}

}
