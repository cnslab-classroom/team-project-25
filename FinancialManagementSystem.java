// 프로그램의 진입점, 모든 모듈을 통합하고 테스트하는 역할

import java.util.Date;

public class FinancialManagementSystem {
    public static void main(String[] args) {
        AccountManager accountManager = new AccountManager();
        TransactionManager transactionManager = new TransactionManager();
        TransactionHistory transactionHistory = new TransactionHistory();
        RealTimeTransactionProcessor realTimeProcessor = new RealTimeTransactionProcessor();

        accountManager.createAccount("123","password123","Kim", 5000);
        accountManager.createAccount("456","password123", "Lee", 3000);

        Account acc1 = accountManager.getAccount("123");
        Account acc2 = accountManager.getAccount("456");

        transactionManager.deposit(acc1, 1000);
        transactionManager.withdraw(acc1,"password123", 500);

        transactionHistory.logTransaction("Deposit", "123", 1000, new Date());
        transactionHistory.logTransaction("Withdrawal", "123", 500, new Date());

        realTimeProcessor.processTransaction(() -> {
            transactionManager.transfer(acc1, acc2, 2000);
            transactionHistory.logTransaction("Transfer", "123", 2000, new Date());
        });

        System.out.println("Transactions logged successfully.");
    }
}
