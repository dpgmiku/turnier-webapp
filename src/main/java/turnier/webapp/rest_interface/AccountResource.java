package turnier.webapp.rest_interface;

import turnier.webapp.domain.Account;

/**Data about an Account of a bank. Usable as Data Transfer Object.*/
public class AccountResource {

    /**Unique ID of the Account.*/
	public Long id;

    /**Distinguishing name of the Account for the owning Client.*/
    public String name;

	/**The balance of the Account in euros.*/
    public double balance;


    /**Necessary for Jackson*/
	public AccountResource() {}

    /**Constructs an AccountResource with the data of the passed Account entity.*/
    public AccountResource(final Account entity) {
    	this.id = entity.getId();
        this.name = entity.getName();
        this.balance = entity.getBalance().toDouble();
    }

    @Override
    public String toString() {
        return String.format(
                "Account{id=%d, name='%s', balance='%s'}",
                id, name, balance);
    }
    
}

