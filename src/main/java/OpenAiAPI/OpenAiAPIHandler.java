package OpenAiAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OpenAiAPIHandler {
    private static String apiKey;
    private static final String apiKeyPath = "C:\\Users\\Gerrit\\Documents\\API_KEY.txt";
    public static final String model = "gpt-3.5-turbo";
    public static final String systemMessage = "You are an informative assistant.";
    public static final double temperature = 0.5;

    public OpenAiAPIHandler(){
        readApiKey(apiKeyPath);
    }

    static void readApiKey(String path) {
        try {
            Path filePath = Paths.get(path);
            apiKey = new String(Files.readAllBytes(filePath)).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getOpenAiApiJsonInput(String model, String userInput, String systemMessage, double temperature) {
        try {
            // Create JSON object
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("model", model);

            // Create messages array
            JSONArray messagesArray = new JSONArray();

            // Add system message if provided
            if (systemMessage != null && !systemMessage.isEmpty()) {
                JSONObject systemMessageObject = new JSONObject();
                systemMessageObject.put("role", "system");
                systemMessageObject.put("content", systemMessage);
                messagesArray.put(systemMessageObject);
            }

            // Add user message
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", userInput);
            messagesArray.put(userMessage);

            jsonInput.put("messages", messagesArray);
            jsonInput.put("temperature", temperature);

            // Convert JSON object to string
            return jsonInput.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getOpenAiApiResponse(String systemMessage, String userInput) {
        try {
            // OpenAI API endpoint
            URL url = new URL("https://api.openai.com/v1/chat/completions");

            // Create connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Set request headers
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            // Enable input/output streams
            connection.setDoOutput(true);

            // Create JSON payload
            String jsonInputString = getOpenAiApiJsonInput(model, userInput, systemMessage, temperature);

            // Write JSON payload to the connection's output stream
            try (OutputStream os = connection.getOutputStream()) {
                assert jsonInputString != null;
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
