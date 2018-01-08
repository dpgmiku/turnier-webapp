package turnier.webapp.infrastructure.imports;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import turnier.webapp.domain.Nutzer;
import turnier.webapp.domain.Turnier;
import turnier.webapp.domain.TurnierBracket;

public interface ImportedTurnierBracketJpaRepository  extends JpaRepository<TurnierBracket, Long> {

   
	TurnierBracket save(TurnierBracket turnierBracket);

	


}
