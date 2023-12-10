package OpenAiAPI;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        String userInput = "Hallo, das ist ein \"Test\",\n mit speziellen Zeichen. \\\\\\\'\\";
        String json = OpenAiAPIHandler.getOpenAiApiJsonInput(OpenAiAPIHandler.model, userInput, OpenAiAPIHandler.systemMessage, OpenAiAPIHandler.temperature);
        System.out.println(json);
        assert isValidJson(json);
        assert !isValidJson("reuihgiuüdrhbfgrdpiuzgbdizu");
    }

    @Test
    void getOpenAiApiResponse() {
        new OpenAiAPIHandler();
        String userInput = "Reply \"This is a test\"";
        String systemMessage = OpenAiAPIHandler.systemMessage;
        String response = OpenAiAPIHandler.getOpenAiApiResponse(systemMessage, userInput);
        System.out.println(response);
        assert response != null;
        assert isValidJson(response);
        assert response.contains("This is a test");
    }
}