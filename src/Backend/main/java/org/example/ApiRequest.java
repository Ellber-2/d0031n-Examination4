package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ApiRequest {

    private static String canvasApiUrl;

    public static void main(String[] args) {
        // Get the access token from the JSON file
        String accessToken = getAccessTokenFromFile();
        if (accessToken != null) {
            System.out.println("Access Token: " + accessToken); // Print the access token (for testing)

            // Set the Canvas API URL here
            setCanvasApiUrl();

            // Now use the access token in the API call
            if (canvasApiUrl != null) {
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    // Make the GET request to the Canvas API
                    HttpGet request = new HttpGet(canvasApiUrl);
                    // Add the Authorization header with Bearer token
                    request.addHeader("Authorization", "Bearer " + accessToken);

                    // Execute the request
                    try (CloseableHttpResponse response = httpClient.execute(request)) {
                        // Convert the response to a string
                        String jsonResponse = EntityUtils.toString(response.getEntity());
                        // Print the response (JSON data)
                        System.out.println("Response: " + jsonResponse);
                    }
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Canvas API URL is not set.");
            }
        } else {
            System.err.println("Access token not found.");
        }
    }
    private static void setCanvasApiUrl() {
        canvasApiUrl = "https://canvas.ltu.se/api/v1/courses"; // Replace with your Canvas domain and the correct endpoint
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
