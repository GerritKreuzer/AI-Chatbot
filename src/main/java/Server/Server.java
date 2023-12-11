package Server;

import OpenAiAPI.OpenAiAPIHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Server {
    private static final int port = 80;
    private static String apiKeyPath;
    private static String accessKeysPath;

    public static void main(String[] args) throws IOException {
        getPaths();
        OpenAiAPIHandler.readApiKey(apiKeyPath);
        MyHttpHandler.readAccessKeys(accessKeysPath);
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new MyHttpHandler());
        System.out.println("Server started on port " + port);
        server.start();
    }

    private static void getPaths() throws IOException {
        List<String> paths = Files.readAllLines(Paths.get("src/main/resources/paths.txt"));
        apiKeyPath = paths.get(0);
        accessKeysPath = paths.get(1);
    }

    public static byte[] getFileBytes() throws IOException {
        File htmlFile = new File("index.html");
        return Files.readAllBytes(htmlFile.toPath());
    }
}
