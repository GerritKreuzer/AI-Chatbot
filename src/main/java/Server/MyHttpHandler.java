package Server;

import OpenAiAPI.OpenAiAPIHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MyHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Handling: " + exchange.getRequestMethod() + " Request");
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetRequest(exchange);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            //handlePostRequest(exchange);
            thisMethodIsJustForTesting(exchange);
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

    private void thisMethodIsJustForTesting(HttpExchange exchange) throws IOException {
        // Get the request body
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        System.out.println("Requestbody: " + requestBody);

        String openAiApiResponse = "This is just a test";

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, openAiApiResponse.length());

        // Send the OpenAI API response as the response
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(openAiApiResponse.getBytes());
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        // Get the request body
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        System.out.println("Requestbody: " + requestBody);

        String openAiApiResponse = OpenAiAPIHandler.getOpenAiApiResponse(OpenAiAPIHandler.defaultSystemMessage, requestBody);
        assert openAiApiResponse != null;
        System.out.println("APIresponse: " + openAiApiResponse);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, openAiApiResponse.length());

        // Send the OpenAI API response as the response
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(openAiApiResponse.getBytes());
        }
    }
}
