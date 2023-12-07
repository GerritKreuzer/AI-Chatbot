package Server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class Server {

    static final String indexPath = "C:\\Users\\Gerrit\\Desktop\\AI-Chatbot\\src\\main\\java\\index.html";
    static final File htmlFile = new File(indexPath);
    static byte[] fileBytes;

    public static void main(String[] args) throws IOException {
        int port = 80;
        fileBytes = Files.readAllBytes(htmlFile.toPath());
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHandler());
        System.out.println("Server started on port " + port);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set the response headers
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, 0);

            // Send the HTML content as the response
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
            }
        }
    }
}
