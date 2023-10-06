import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CardGameServer {
    private static CardGame cardGame = new CardGame();

    public static void main(String[] args) throws Exception {

        int port = 8080;
        String host = "127.0.0.1";
        HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);

        server.createContext("/start", new StartHandler());
        server.createContext("/shuffle", new ShuffleHandler());
        server.createContext("/bet", new BetHandler());

        server.start();
    }

    static class StartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                int initialBalance = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                String response = cardGame.startGame(initialBalance);
                sendResponse(exchange, response);
            }
        }
    }

    static class ShuffleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = cardGame.shuffleGame();
                sendResponse(exchange, response);
            }
        }
    }

    static class BetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, String> params = parseQuery(exchange.getRequestURI().getQuery());
                int betAmount = Integer.parseInt(params.get("betAmount"));
                String betType = params.get("betType");
                String response = cardGame.placeBet(betAmount, betType);
                sendResponse(exchange, response);
            }
        }
    }

    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }
}
