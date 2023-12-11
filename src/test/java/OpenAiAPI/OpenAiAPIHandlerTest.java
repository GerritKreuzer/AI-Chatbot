package OpenAiAPI;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class OpenAiAPIHandlerTest {

    public static boolean isValidJson(String jsonString) {
        try {
            new JSONObject(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    void getOpenAiApiJsonInput(){
        String userInput = "Hallo, das ist ein \"Test\",\n mit speziellen Zeichen. \\\\\\'\\";
        String json = OpenAiAPIHandler.getOpenAiApiJsonInput(OpenAiAPIHandler.defaultModel, userInput, OpenAiAPIHandler.defaultSystemMessage, OpenAiAPIHandler.temperature);
        System.out.println(json);
        assert isValidJson(json);
    }

    @Test
    void getOpenAiApiResponse() {
        OpenAiAPIHandler.readApiKey("C:\\Users\\Gerrit\\Documents\\API_KEY.txt");
        String userInput = "Reply \"This is a test\"";
        String response = OpenAiAPIHandler.getOpenAiApiResponse(OpenAiAPIHandler.defaultSystemMessage, userInput);
        System.out.println(response);
        assert response != null;
        assert isValidJson(response);
        assert response.contains("This is a test");
    }
}