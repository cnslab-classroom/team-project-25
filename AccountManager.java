// 계좌 생성, 조회, 수정, 삭제를 처리하는 관리 모듈

import java.util.*;

public class AccountManager {
    private Map<String, Account> accounts = new HashMap<>();
    private TransactionHistory transactionHistory;

    public AccountManager() {
        this.transactionHistory = new TransactionHistory();
    }

    public AccountManager(TransactionHistory transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public void createAccount(String accountId, String accountPassword, String owner, double initialBalance) { 
        if (accounts.containsKey(accountId)) {
            throw new IllegalArgumentException("Account already exists.");
        }
        accounts.put(accountId, new Account(accountId, accountPassword, owner, initialBalance));
        transactionHistory.logTransaction("입금", accountId, initialBalance, new Date());
        System.out.println("Account created successfully.");
    }

    public void updateAccount(String accountId, String newOwner) {
        Account account = accounts.get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        account.setOwner(newOwner);
        System.out.println("Account updated successfully.");
    }

    public void deleteAccount(String accountId) {
        if (accounts.remove(accountId) == null) {
            throw new IllegalArgumentException("Account not found.");
        }
        System.out.println("Account deleted successfully.");
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public Map<String, Account> getAllAccounts() {
        return accounts;
    }
}