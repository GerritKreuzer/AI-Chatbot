package OpenAiAPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OpenAiAPIHandler {
    private static String apiKey;
    private static final String apiKeyPath = "C:\\Users\\Gerrit\\Desktop\\AI-Chatbot\\API_KEY.txt";

    public OpenAiAPIHandler(){
        readApiKey(apiKeyPath);
    }

    private void readApiKey(String path) {
        try {
            Path filePath = Paths.get(path);
            apiKey = new String(Files.readAllBytes(filePath)).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
