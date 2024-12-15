// 거래 데이터 모델 (거래 유형, 계좌 ID, 금액, 날짜 포함)

import java.util.Date;

public class Transaction {
    private String type;
    private String accountId;
    private double amount;
    private Date date;

    public Transaction(String type, String accountId, double amount, Date date) {
        this.type = type;
        this.accountId = accountId;
        this.amount = amount;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }
}
