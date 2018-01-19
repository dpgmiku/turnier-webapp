package turnier.webapp.infrastructure.imports;


import org.springframework.data.jpa.repository.JpaRepository;

import turnier.webapp.domain.TurnierBracket;

/**
 * Erforderliche Spring JPA Repository für TurnierBracket Objekten. Die Methoden
 * wurden nach Spring Data JPA Namenkonvention benannt. Die können mit Hilfe vom
 * Spring in der Zeit der Bean-Erstellung implementiert werden, die können aber
 * auch unabhängig vom Spring implementiert werden.
 * 
 * @author se2 Letzte Gruppe
 * @since 2018-01-14
 */
public interface ImportedTurnierBracketJpaRepository  extends JpaRepository<TurnierBracket, Long> {

   
	TurnierBracket save(TurnierBracket turnierBracket);
	
	void deleteAll();

	
    void delete(TurnierBracket turnierBracket);


}
