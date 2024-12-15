// launcher GUI 구현

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LauncherGUI {
    private JFrame frame;
    private JTextField hostField;
    private JTextField portField;

    public LauncherGUI() {
        frame = new JFrame("Launcher");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4,1));
        panel.add(new JLabel("서버 호스트 (클라이언트 모드용):"));
        hostField = new JTextField("localhost");
        panel.add(hostField);

        panel.add(new JLabel("포트 번호 (기본:9999):"));
        portField = new JTextField("9999");
        panel.add(portField);

        JPanel buttonPanel = new JPanel(new GridLayout(1,2));
        JButton serverButton = new JButton("서버 모드");
        JButton clientButton = new JButton("클라이언트 모드");
        buttonPanel.add(serverButton);
        buttonPanel.add(clientButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runServerMode();
            }
        });

        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runClientMode();
            }
        });
    }

    private void runServerMode() {
        final TransactionHistory transactionHistory = new TransactionHistory();
        final AccountManager accountManager = new AccountManager(transactionHistory);
        final TransactionManager transactionManager = new TransactionManager(transactionHistory);
    
        int p = 9999;
        try {
            p = Integer.parseInt(portField.getText());
        } catch (NumberFormatException ignored) {}
    
        // p를 final로 받기 위해 finalPort로 변수 선언
        final int finalPort = p;
    
        // 별도 스레드로 서버 시작
        new Thread(() -> {
            NetworkServer server = new NetworkServer(finalPort, accountManager, transactionManager, transactionHistory);
            try {
                server.startServer();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    
        JOptionPane.showMessageDialog(frame, "서버가 포트 " + finalPort + "에서 실행 중입니다.");
    }
    

    private void runClientMode() {
        String host = hostField.getText();
        int port = 9999;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException ignored) {}

        // 클라이언트 GUI 실행
        FinancialManagementGUI gui = new FinancialManagementGUI(host, port);
        gui.show();
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        LauncherGUI launcher = new LauncherGUI();
        launcher.show();
    }
}
