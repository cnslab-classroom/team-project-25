// 거래 내역 기록 및 조회 기능 제공

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionHistory {
    private List<Transaction> transactions;

    public TransactionHistory() {
        transactions = new ArrayList<>();
    }

    public void logTransaction(String type, String accountId, double amount, Date date) {
        transactions.add(new Transaction(type, accountId, amount, date));
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }
}
