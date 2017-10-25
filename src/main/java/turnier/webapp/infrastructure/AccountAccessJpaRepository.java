package turnier.webapp.infrastructure;

import turnier.webapp.domain.Account;
import turnier.webapp.domain.AccountAccess;
import turnier.webapp.domain.Amount;
import turnier.webapp.domain.Client;
import turnier.webapp.domain.imports.AccountAccessRepository;
import turnier.webapp.infrastructure.imports.ImportedAccountAccessJpaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**A Repository for {@link turnier.webapp.domain.AccountAccess} link objects implemented with Spring Data JPA.
 * @author Christoph Knabe
 * @since 2017-03-06
 */
@Service
public class AccountAccessJpaRepository implements AccountAccessRepository {

    private final ImportedAccountAccessJpaRepository impl;

    @Autowired
    public AccountAccessJpaRepository(final ImportedAccountAccessJpaRepository impl) {
        this.impl = impl;
    }

    public void deleteAll(){impl.deleteAll();}

    public AccountAccess save(final AccountAccess accountAccess){
        return impl.save(accountAccess);
    }

    @Override
    public void delete(AccountAccess accountAccess) {
        impl.delete(accountAccess);
    }

    @Override
    public List<AccountAccess> findManagedAccountsOf(Client client, boolean asOwner) {
        return impl.findAllByClientAndIsOwnerGreaterThanEqualOrderByIdDesc(client, asOwner);
    }

    @Override
    public List<AccountAccess> findFullAccounts(final Amount minBalance) {
        return impl.findAllByAccountBalanceCentsGreaterThanEqualOrderByAccountBalanceCentsDescClientIdDesc(minBalance.getCents());
    }

    @Override
    public Optional<AccountAccess> find(final Client client, final Account account) {
        return impl.findOneByClientAndAccount(client, account);
    }

}
