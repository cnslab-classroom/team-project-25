// 서버

import java.io.*;
import java.net.*;

public class NetworkServer {
    private int port;
    private AccountManager accountManager;
    private TransactionManager transactionManager;
    private TransactionHistory transactionHistory;

    public NetworkServer(int port, AccountManager accountManager, TransactionManager transactionManager, TransactionHistory transactionHistory) {
        this.port = port;
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
        this.transactionHistory = transactionHistory;
    }

    public void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                String response = processRequest(request);
                // 응답 전송
                out.println(response);
                out.println("END_OF_RESPONSE");
            }
        } catch (Exception e) {
            // 네트워크 문제로 인한 예외처리
        }
    }

    private String processRequest(String request) {
        String[] parts = request.split("\\|");
        String command = parts[0].toUpperCase();

        try {
            switch (command) {
                case "CREATE": {
                    String accountId = parts[1];
                    String password = parts[2];
                    String owner = parts[3];
                    double balance = Double.parseDouble(parts[4]);
                    accountManager.createAccount(accountId, password, owner, balance);
                    return "OK:ACCOUNT_CREATED";
                }
                case "DEPOSIT": {
                    String accountId = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    Account acc = accountManager.getAccount(accountId);
                    if (acc == null) return "ERROR:ACCOUNT_NOT_FOUND";
                    transactionManager.deposit(acc, amount);
                    return "OK:DEPOSITED";
                }
                case "WITHDRAW": {
                    String accountId = parts[1];
                    String password = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    Account acc = accountManager.getAccount(accountId);
                    if (acc == null) return "ERROR:ACCOUNT_NOT_FOUND";
                    transactionManager.withdraw(acc, password, amount);
                    return "OK:WITHDRAWN";
                }
                case "TRANSFER": {
                    String fromId = parts[1];
                    String password = parts[2];
                    String toId = parts[3];
                    double amount = Double.parseDouble(parts[4]);
                    Account fromAcc = accountManager.getAccount(fromId);
                    Account toAcc = accountManager.getAccount(toId);
                    if (fromAcc == null || toAcc == null) return "ERROR:ACCOUNT_NOT_FOUND";
                    transactionManager.transfer(fromAcc, toAcc, password, amount);
                    return "OK:TRANSFERRED";
                }
                case "GET_HISTORY": {
                    String accountId = parts[1];
                    String password = parts[2];
                    Account acc = accountManager.getAccount(accountId);
                    if (acc == null) return "ERROR:ACCOUNT_NOT_FOUND";
                    if (!acc.getAccountPassword().equals(password)) return "ERROR:WRONG_PASSWORD";

                    StringBuilder sb = new StringBuilder();
                    sb.append("OK:HISTORY\n");
                    sb.append("ACCOUNT_ID:").append(acc.getAccountId()).append("\n");
                    sb.append("OWNER:").append(acc.getOwner()).append("\n");
                    sb.append("BALANCE:").append(acc.getBalance()).append("\n");
                    sb.append("TRANSACTIONS:\n");
                    for (Transaction t : transactionHistory.getAllTransactions()) {
                        if (t.getAccountId().equals(accountId)) {
                            sb.append(t.getType()).append("|")
                              .append(t.getAmount()).append("|")
                              .append(t.getDate().toString()).append("\n");
                        }
                    }
                    return sb.toString().trim();
                }
                default:
                    return "ERROR:UNKNOWN_COMMAND";
            }
        } catch (Exception e) {
            return "ERROR:" + e.getMessage();
        }
    }
}
