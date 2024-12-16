import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class SimpleWebApp {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8083), 0);
        server.createContext("/", new RootHandler());
        server.createContext("/process", new ProcessHandler());
        server.setExecutor(null); // Default executor
        server.start();
        System.out.println("Server started on port 8083");
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<html>" +
                    "<head><title>Enter Process Durations</title>" +
                    "<style>" +
                    "body {font-family: Arial, sans-serif; background-color: #2c3e50; color: #ecf0f1; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0;}" +
                    "h1 {color: #e74c3c; text-align: center;}" +
                    "form {background-color: #34495e; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.3); text-align: center;}" +
                    "input[type='number'] {width: calc(100% - 22px); padding: 10px; margin: 10px 0; border-radius: 4px; border: 1px solid #7f8c8d; background-color: #ecf0f1; color: #2c3e50;}" +
                    "input[type='submit'] {background-color: #e74c3c; color: white; border: none; padding: 10px 20px; cursor: pointer; border-radius: 4px;}" +
                    "input[type='submit']:hover {background-color: #c0392b;}" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<form action='/process' method='post'>" +
                    "<h1>Enter Process Data</h1>" +
                    "<label for='process1'>Process 1 (ID and Duration):</label><br>" +
                    "<input type='number' name='id1' placeholder='ID1' required> <input type='number' name='duration1' placeholder='Duration1' required><br>" +
                    "<label for='process2'>Process 2 (ID and Duration):</label><br>" +
                    "<input type='number' name='id2' placeholder='ID2' required> <input type='number' name='duration2' placeholder='Duration2' required><br>" +
                    "<label for='process3'>Process 3 (ID and Duration):</label><br>" +
                    "<input type='number' name='id3' placeholder='ID3' required> <input type='number' name='duration3' placeholder='Duration3' required><br>" +
                    "<label for='process4'>Process 4 (ID and Duration):</label><br>" +
                    "<input type='number' name='id4' placeholder='ID4' required> <input type='number' name='duration4' placeholder='Duration4' required><br>" +
                    "<input type='submit' value='Calculate'>" +
                    "</form>" +
                    "</body>" +
                    "</html>";

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    static class ProcessHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String[] data = query.split("&");

            int id1 = Integer.parseInt(removeDiacritics(URLDecoder.decode(data[0].split("=")[1], StandardCharsets.UTF_8)).trim());
            int duration1 = Integer.parseInt(removeDiacritics(URLDecoder.decode(data[1].split("=")[1], StandardCharsets.UTF_8)).trim());
            int id2 = Integer.parseInt(removeDiacritics(URLDecoder.decode(data[2].split("=")[1], StandardCharsets.UTF_8)).trim());
            int duration2 = Integer.parseInt(removeDiacritics(URLDecoder.decode(data[3].split("=")[1], StandardCharsets.UTF_8)).trim());
            int id3 = Integer.parseInt(removeDiacritics(URLDecoder.decode(data[4].split("=")[1], StandardCharsets.UTF_8)).trim());
            int duration3 = Integer.parseInt(removeDiacritics(URLDecoder.decode(data[5].split("=")[1], StandardCharsets.UTF_8)).trim());
            int id4 = Integer.parseInt(removeDiacritics(URLDecoder.decode(data[6].split("=")[1], StandardCharsets.UTF_8)).trim());
            int duration4 = Integer.parseInt(removeDiacritics(URLDecoder.decode(data[7].split("=")[1], StandardCharsets.UTF_8)).trim());

            double avgWaitTime = (4 * duration1 + 3 * duration2 + 2 * duration3 + duration4) / 4.0;
            double avgExecTime = (duration1 + duration2 + duration3 + duration4) / 4.0;

            String response = "<html>" +
                    "<head><title>Calculation Result</title>" +
                    "<style>" +
                    "body {font-family: Arial, sans-serif; background-color: #2c3e50; color: #ecf0f1; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0;}" +
                    "h1 {color: #e74c3c; text-align: center;}" +
                    "p {font-size: 18px; text-align: center;}" +
                    "a {color: #e74c3c; text-decoration: none; font-size: 18px;}" +
                    "a:hover {text-decoration: underline;}" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div>" +
                    "<h1>Calculation Result</h1>" +
                    "<p>Average waiting time is: " + String.format("%.2f", avgWaitTime) + " units</p>" +
                    "<p>Average execution time is: " + String.format("%.2f", avgExecTime) + " units</p>" +
                    "<a href='/'>Back to form</a>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

        private String removeDiacritics(String str) {
            if (str == null) {
                return null;
            }
            return str.replaceAll("[^\\x00-\\x7F]", "");
        }
    }
}
