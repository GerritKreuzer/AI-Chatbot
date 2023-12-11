package Server;

import OpenAiAPI.OpenAiAPIHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MyHttpHandler implements HttpHandler {

    private static final List<String> accessKeys = new ArrayList<>();

    public static void readAccessKeys(String path){
        try {
            Path filePath = Paths.get(path);
            List<String> lines = Files.readAllLines(filePath);
            accessKeys.addAll(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetRequest(exchange);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            handlePostRequest(exchange);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        // Set the response headers
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, 0);

        // Send the HTML content as the response
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(Server.fileBytes);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        // check authorization
        if (!allowAccess(exchange)) {
            exchange.sendResponseHeaders(401, 0);
            return;
        }
        // Get the request body
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        String openAiApiResponse = OpenAiAPIHandler.getOpenAiApiResponse(OpenAiAPIHandler.defaultSystemMessage, requestBody);
        assert openAiApiResponse != null;
        String chatResponse = parseResponse(openAiApiResponse);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, chatResponse.length());

        // Send the OpenAI API response as the response
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(chatResponse.getBytes());
        }
    }

    private static boolean allowAccess(HttpExchange exchange){
        Headers headers = exchange.getRequestHeaders();
        if (!headers.containsKey("Authorization")) return false;
        String authorizationHeader = headers.getFirst("Authorization");
        return accessKeys.contains(authorizationHeader);
    }

    private static String parseResponse(String response) {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject message = jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message");
        return message.getString("content");
    }
}
