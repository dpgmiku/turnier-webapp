package turnier.webapp.infrastructure.imports;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import turnier.webapp.domain.TurnierBracket;

public interface ImportedTurnierBracketJpaRepository  extends JpaRepository<TurnierBracket, Long> {

   
	TurnierBracket save(TurnierBracket turnierBracket);
	
	void deleteAll();

	
    void delete(TurnierBracket turnierBracket);


}
