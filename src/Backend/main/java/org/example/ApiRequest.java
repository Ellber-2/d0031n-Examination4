package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class ApiRequest {

    private static String canvasApiUrl;
    private static String accessToken;

    public static void main(String[] args) {
        // Get the access token from the JSON file
        accessToken = getAccessTokenFromFile();
        if (accessToken != null) {
            System.out.println("Access Token: " + accessToken); // Print the access token (for testing)

            // Set the Canvas API URL
            setCanvasApiUrl();

            // Now use the access token in the API call to create an event
            if (canvasApiUrl != null) {
                createCalendarEvent();
            } else {
                System.err.println("Canvas API URL is not set.");
            }
        } else {
            System.err.println("Access token not found.");
        }
    }

    private static void createCalendarEvent() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create a POST request for creating a calendar event
            HttpPost request = new HttpPost(canvasApiUrl + "/calendar_events.json");

            // Create the form data payload
            String formData = "calendar_event[context_code]=user_146018" +
                    "&calendar_event[title]=Test" +
                    "&calendar_event[start_at]=2025-01-04T14:00:00" +
                    "&calendar_event[end_at]=2025-01-04T14:45:00" +
                    "&calendar_event[description]=testing";

            // Set the body of the request using StringEntity
            StringEntity entity = new StringEntity(formData, ContentType.APPLICATION_FORM_URLENCODED);
            request.setEntity(entity);

            // Add the Authorization header with Bearer token
            request.addHeader("Authorization", "Bearer " + accessToken);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Get the status code of the response
                int statusCode = response.getCode();

                // Convert the response to a string
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Check if the response is successful
                if (statusCode == 201) {
                    System.out.println("Request was created!");
                } else {
                    System.out.println("Request failed with status code: " + statusCode);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static void setCanvasApiUrl() {
        canvasApiUrl = "https://canvas.ltu.se/api/v1"; // Replace with your Canvas domain
    }

    // Method to read the access token from the JSON file
    private static String getAccessTokenFromFile() {
        try {
            // Path to the JSON file in Documents folder
            String filePath = Paths.get(System.getProperty("user.home"), "Documents", "config.json").toString();

            // Create a Jackson ObjectMapper to parse the JSON file
            ObjectMapper objectMapper = new ObjectMapper();

            // Read the file and parse it into a JsonNode
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Extract the access token from the JSON
            JsonNode accessTokenNode = rootNode.path("access_token");

            // Return the access token as a string
            if (accessTokenNode.isTextual()) {
                return accessTokenNode.asText();
            } else {
                return null; // Token not found or not a string
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null; // In case of error (e.g., file not found)
        }
    }
}