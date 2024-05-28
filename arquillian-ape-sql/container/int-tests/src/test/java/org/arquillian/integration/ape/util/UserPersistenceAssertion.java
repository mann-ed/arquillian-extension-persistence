package org.arquillian.integration.ape.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.arquillian.integration.ape.example.UserAccount;

import jakarta.persistence.EntityManager;

public class UserPersistenceAssertion {

    private final EntityManager em;

    public UserPersistenceAssertion(EntityManager em) {
        this.em = em;
        this.em.clear();
    }

    public void assertUserAccountsStored() {
        @SuppressWarnings("unchecked")
        List<UserAccount> savedUserAccounts = em.createQuery(Query.selectAllInJPQL(UserAccount.class)).getResultList();
        assertThat(savedUserAccounts).isNotEmpty();
    }

    public void assertNoUserAccountsStored() {
        @SuppressWarnings("unchecked")
        List<UserAccount> savedUserAccounts = em.createQuery(Query.selectAllInJPQL(UserAccount.class)).getResultList();
        assertThat(savedUserAccounts).isEmpty();
    }
}
