package turnier.webapp.infrastructure.imports;

import org.springframework.data.jpa.repository.JpaRepository;

import turnier.webapp.domain.Account;
import turnier.webapp.domain.AccountAccess;
import turnier.webapp.domain.Client;

import java.util.List;
import java.util.Optional;

/**Required repository for {@link turnier.webapp.domain.AccountAccess} objects. The methods are named according to the Spring Data JPA convention.
 * They can be implemented by Spring during bean creation, but can be implemented independently of Spring, too.
 * @author Christoph Knabe
 * @since 2017-03-06
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation">Spring Data Query Methods</a>
 */
public interface ImportedAccountAccessJpaRepository extends JpaRepository<AccountAccess, Long> {

    void deleteAll();

    AccountAccess save(AccountAccess account);

    void delete(AccountAccess account);

    List<AccountAccess> findAllByClientAndIsOwnerGreaterThanEqualOrderByIdDesc(Client client, boolean asOwner);

    Optional<AccountAccess> findOneByClientAndAccount(Client client, Account account);

    List<AccountAccess> findAllByAccountBalanceCentsGreaterThanEqualOrderByAccountBalanceCentsDescClientIdDesc(long minCents);

}
