// 프로그램의 그래픽 사용자 인터페이스(GUI)를 제공하며, 사용자가 계좌 관리 및 거래를 수행할 수 있는 화면

import java.awt.*;
import javax.swing.*;

public class FinancialManagementGUI {
    private JFrame frame;
    private NetworkClient networkClient;

    public FinancialManagementGUI(String host, int port) {
        // 초기화
        networkClient = new NetworkClient(host, port);

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

                String request = "CREATE|" + accountId + "|" + accountPassword + "|" + owner + "|" + balance;
                String response = networkClient.sendRequest(request);
                if (response.startsWith("OK")) {
                    JOptionPane.showMessageDialog(frame, "계좌가 생성되었습니다!");
                } else {
                    JOptionPane.showMessageDialog(frame, "오류: " + response);
                }
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
            String accountId = accountIdField.getText();
            try {
                double amount = Double.parseDouble(amountField.getText());
                String req = "DEPOSIT|" + accountId + "|" + amount;
                String res = networkClient.sendRequest(req);
                if (res.startsWith("OK")) {
                    JOptionPane.showMessageDialog(frame, "입금 완료!");
                } else {
                    JOptionPane.showMessageDialog(frame, "오류: " + res);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "오류: " + ex.getMessage());
            }
        });

        withdrawButton.addActionListener(e -> {
            String accountId = accountIdField.getText();
            String password = accountPasswordField.getText();
            try {
                double amount = Double.parseDouble(amountField.getText());
                String req = "WITHDRAW|" + accountId + "|" + password + "|" + amount;
                String res = networkClient.sendRequest(req);
                if (res.startsWith("OK")) {
                    JOptionPane.showMessageDialog(frame, "출금 완료!");
                } else {
                    JOptionPane.showMessageDialog(frame, "오류: " + res);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "오류: " + ex.getMessage());
            }
        });

        // 송금 버튼 클릭 시 처리
        transferButton.addActionListener(e -> {
            String accountId = accountIdField.getText();
            String password = accountPasswordField.getText();
            String toId = transferAccountIdField.getText();
            try {
                double amount = Double.parseDouble(amountField.getText());
                String req = "TRANSFER|" + accountId + "|" + password + "|" + toId + "|" + amount;
                String res = networkClient.sendRequest(req);
                if (res.startsWith("OK")) {
                    JOptionPane.showMessageDialog(frame, "송금 완료!");
                } else {
                    JOptionPane.showMessageDialog(frame, "오류: " + res);
                }
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
    
        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        panel.add(new JScrollPane(historyArea), BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String accountId = accountIdField.getText();
            String password = new String(accountPasswordField.getPassword());
            String req = "GET_HISTORY|" + accountId + "|" + password;
            String res = networkClient.sendRequest(req);
            if (res.startsWith("OK:HISTORY")) {
                historyArea.setText(res.substring(res.indexOf("\n")+1));
            } else {
                JOptionPane.showMessageDialog(frame, "오류: " + res);
            }
        });
    
        return panel;
    }      

    public void show() {
        frame.setVisible(true);
    }
}