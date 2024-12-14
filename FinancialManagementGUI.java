// 프로그램의 그래픽 사용자 인터페이스(GUI)를 제공하며, 사용자가 계좌 관리 및 거래를 수행할 수 있는 화면

import java.awt.*;
import java.util.Date;
import java.util.Map;
import javax.swing.*;

public class FinancialManagementGUI {
    private JFrame frame;
    private AccountManager accountManager;
    private TransactionManager transactionManager;
    private TransactionHistory transactionHistory;

    public FinancialManagementGUI() {
        // 초기화
        transactionHistory = new TransactionHistory();
        accountManager = new AccountManager(transactionHistory);
        transactionManager = new TransactionManager();

        // GUI 구성
        frame = new JFrame("Financial Management System");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 탭 구성
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("계좌 관리", createAccountPanel());
        tabbedPane.add("거래 관리", createTransactionPanel());
        tabbedPane.add("거래 내역", createHistoryPanel());

        frame.add(tabbedPane);
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));

        JTextField accountIdField = new JTextField();
        JTextField accountPasswordField = new JTextField();
        JTextField ownerField = new JTextField();
        JTextField balanceField = new JTextField();
        JButton createButton = new JButton("계좌 생성");

        panel.add(new JLabel("계좌 ID:"));
        panel.add(accountIdField);
        panel.add(new JLabel("계좌 PASSWORD:"));
        panel.add(accountPasswordField);
        panel.add(new JLabel("소유자:"));
        panel.add(ownerField);
        panel.add(new JLabel("초기 잔액:"));
        panel.add(balanceField);
        panel.add(createButton);

        createButton.addActionListener(e -> {
            try {
                String accountId = accountIdField.getText();
                String accountPassword = accountPasswordField.getText();
                String owner = ownerField.getText();
                double balance = Double.parseDouble(balanceField.getText());
                accountManager.createAccount(accountId,accountPassword, owner, balance);
                JOptionPane.showMessageDialog(frame, "계좌가 생성되었습니다!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "오류: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));

        JTextField accountIdField = new JTextField();
        JTextField accountPasswordField = new JTextField();
        JTextField amountField = new JTextField();
        JButton depositButton = new JButton("입금");
        JButton withdrawButton = new JButton("출금");

        panel.add(new JLabel("계좌 ID:"));
        panel.add(accountIdField);
        panel.add(new JLabel("계좌 Password:"));
        panel.add(accountPasswordField);
        panel.add(new JLabel("금액:"));
        panel.add(amountField);
        panel.add(depositButton);
        panel.add(withdrawButton);

        depositButton.addActionListener(e -> {
            try {
                String accountId = accountIdField.getText();
                //String accountPassword = accountPasswordField.getText();
                double amount = Double.parseDouble(amountField.getText());
                Account account = accountManager.getAccount(accountId);
                transactionManager.deposit(account, amount);
                transactionHistory.logTransaction("입금", accountId, amount, new Date());
                JOptionPane.showMessageDialog(frame, "입금 완료!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "오류: " + ex.getMessage());
            }
        });

        withdrawButton.addActionListener(e -> {
            try {
                String accountId = accountIdField.getText();
                String accountPassword = accountPasswordField.getText();
                double amount = Double.parseDouble(amountField.getText());
                Account account = accountManager.getAccount(accountId);
                transactionManager.withdraw(account,accountPassword, amount);
                transactionHistory.logTransaction("출금", accountId, amount, new Date());
                JOptionPane.showMessageDialog(frame, "출금 완료!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "오류: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);

        JButton refreshButton = new JButton("내역 갱신");
        panel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();

            // 각 계좌의 현재 잔액과 거래 내역 추가
            sb.append("==== 계좌 정보 ====\n");
            for (Map.Entry<String, Account> entry : accountManager.getAllAccounts().entrySet()) {
                Account account = entry.getValue();
                sb.append("계좌 ID: ").append(account.getAccountId())
                .append(", 소유자: ").append(account.getOwner())
                .append(", 잔액: ").append(account.getBalance()).append("원\n");
            }

            sb.append("\n==== 거래 내역 ====\n");
            for (Transaction transaction : transactionHistory.getAllTransactions()) {
                sb.append(transaction.getType()).append(" - ")
                .append(transaction.getAccountId()).append(" - ")
                .append(transaction.getAmount()).append("원 - ")
                .append(transaction.getDate()).append("\n");
            }

            if (transactionHistory.getAllTransactions().isEmpty()) {
                sb.append("거래 내역이 없습니다.");
            }

            historyArea.setText(sb.toString());
        });

        return panel;
    }    

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        FinancialManagementGUI gui = new FinancialManagementGUI();
        gui.show();
    }
}