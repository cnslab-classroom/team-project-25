// 프로그램의 그래픽 사용자 인터페이스(GUI)를 제공하며, 사용자가 계좌 관리 및 거래를 수행할 수 있는 화면

import java.awt.*;
import java.util.Date;
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
        transactionManager = new TransactionManager(transactionHistory);

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
        JButton clearButton = new JButton("CLEAR");

        panel.add(new JLabel("계좌 ID:"));
        panel.add(accountIdField);
        panel.add(new JLabel("계좌 PASSWORD:"));
        panel.add(accountPasswordField);
        panel.add(new JLabel("소유자:"));
        panel.add(ownerField);
        panel.add(new JLabel("초기 잔액:"));
        panel.add(balanceField);
        panel.add(createButton);
        panel.add(clearButton);

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

        clearButton.addActionListener(e -> {
            accountIdField.setText("");
            accountPasswordField.setText("");
            ownerField.setText("");
            balanceField.setText("");
        });

        return panel;
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 1)); // 송금 버튼을 위한 추가 행을 추가합니다.

        JTextField accountIdField = new JTextField();
        JTextField accountPasswordField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField transferAccountIdField = new JTextField(); // 송금할 계좌 ID 입력 필드 추가
        JButton depositButton = new JButton("입금");
        JButton withdrawButton = new JButton("출금");
        JButton transferButton = new JButton("송금");  // 송금 버튼 추가
        JButton clearButton = new JButton("CLEAR");

        panel.add(new JLabel("계좌 ID:"));
        panel.add(accountIdField);
        panel.add(new JLabel("계좌 Password:"));
        panel.add(accountPasswordField);
        panel.add(new JLabel("금액:"));
        panel.add(amountField);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(new JLabel("송금할 계좌 ID:"));  // 송금할 계좌 ID 라벨 추가
        panel.add(transferAccountIdField);
        panel.add(transferButton); // 송금 버튼 추가
        panel.add(clearButton);

        depositButton.addActionListener(e -> {
            try {
                String accountId = accountIdField.getText();
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
                transactionManager.withdraw(account, accountPassword, amount);
                transactionHistory.logTransaction("출금", accountId, amount, new Date());
                JOptionPane.showMessageDialog(frame, "출금 완료!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "오류: " + ex.getMessage());
            }
        });

        // 송금 버튼 클릭 시 처리
        transferButton.addActionListener(e -> {
            try {
                String accountId = accountIdField.getText();
                String accountPassword = accountPasswordField.getText();
                String transferAccountId = transferAccountIdField.getText();  // 송금할 계좌 ID
                double amount = Double.parseDouble(amountField.getText());
                Account fromAccount = accountManager.getAccount(accountId);
                Account toAccount = accountManager.getAccount(transferAccountId);

                // 송금 처리
                transactionManager.transfer(fromAccount, toAccount, accountPassword, amount);
                // transactionHistory.logTransaction("송금", accountId, amount, new Date());
                JOptionPane.showMessageDialog(frame, "송금 완료!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "오류: " + ex.getMessage());
            }
        });

        clearButton.addActionListener(e -> {
            accountIdField.setText("");
            accountPasswordField.setText("");
            amountField.setText("");
            transferAccountIdField.setText("");
        });

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
    
        // 입력 필드
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JTextField accountIdField = new JTextField();
        JPasswordField accountPasswordField = new JPasswordField();
        JButton searchButton = new JButton("조회");
    
        inputPanel.add(new JLabel("계좌 ID:"));
        inputPanel.add(accountIdField);
        inputPanel.add(new JLabel("계좌 비밀번호:"));
        inputPanel.add(accountPasswordField);
        inputPanel.add(new JLabel());
        inputPanel.add(searchButton);
    
        panel.add(inputPanel, BorderLayout.NORTH);
    
        // 거래 내역 출력 필드
        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        panel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
    
        // 조회 버튼 클릭 이벤트
        searchButton.addActionListener(e -> {
            String accountId = accountIdField.getText();
            String accountPassword = new String(accountPasswordField.getPassword());
    
            try {
                // 계좌 정보 확인
                Account account = accountManager.getAccount(accountId);
                if (account == null) {
                    throw new IllegalArgumentException("해당 계좌가 존재하지 않습니다.");
                }
                if (!account.getAccountPassword().equals(accountPassword)) {
                    throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                }
    
                // 거래 내역 및 잔액 표시
                StringBuilder sb = new StringBuilder();
                sb.append("==== 계좌 정보 ====\n")
                  .append("계좌 ID: ").append(account.getAccountId()).append("\n")
                  .append("소유자: ").append(account.getOwner()).append("\n")
                  .append("잔액: ").append(account.getBalance()).append("원\n\n");
    
                sb.append("==== 거래 내역 ====\n");
                boolean hasTransactions = false;
                for (Transaction transaction : transactionHistory.getAllTransactions()) {
                    if (transaction.getAccountId().equals(accountId)) {
                        sb.append(transaction.getType()).append(" - ")
                          .append(transaction.getAmount()).append("원 - ")
                          .append(transaction.getDate()).append("\n");
                        hasTransactions = true;
                    }
                }
    
                if (!hasTransactions) {
                    sb.append("거래 내역이 없습니다.");
                }
    
                historyArea.setText(sb.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "오류: " + ex.getMessage());
            }
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