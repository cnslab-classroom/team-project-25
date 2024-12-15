// 클라이언트

import java.io.*;
import java.net.*;

public class NetworkClient {
    private String host;
    private int port;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public synchronized String sendRequest(String request) {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 요청 전송
            out.println(request);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                if ("END_OF_RESPONSE".equals(line)) {
                    // 서버 응답의 끝을 의미하는 구분자를 만난 경우 루프 종료
                    break;
                }
                sb.append(line).append("\n");
            }

            return sb.toString().trim();

        } catch (Exception e) {
            return "ERROR:" + e.getMessage();
        }
    }
}
