package Server;

import OpenAiAPI.OpenAiAPIHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class Server {
    static final int port = 80;
    static final String apiKeyPath = "C:\\Users\\Gerrit\\Documents\\API_KEY.txt";
    static final String accessKeysPath = "C:\\Users\\Gerrit\\Documents\\CHATBOT_ACCESS_KEYS.txt";
    static final String indexPath = "C:\\Users\\Gerrit\\Desktop\\AI-Chatbot\\index.html";
    static final File htmlFile = new File(indexPath);
    static final byte[] fileBytes;

    static {
        try {
            fileBytes = Files.readAllBytes(htmlFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        OpenAiAPIHandler.readApiKey(apiKeyPath);
        MyHttpHandler.readAccessKeys(accessKeysPath);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHttpHandler());
        System.out.println("Server started on port " + port);
        server.start();
    }
}
