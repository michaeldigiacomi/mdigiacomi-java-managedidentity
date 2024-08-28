package  com.vena;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.ManagedIdentityCredential;
import com.azure.identity.ManagedIdentityCredentialBuilder;

public class App {
    private static final String FUNCTION_URI = "https://mdigiacomi-test-app.azurewebsites.net";
    private static final String FUNCTION_TRIGGER_PATH = "api/httpTrigger1";
    private static final String API_ID= "api://d1ce2d1f-f535-44c3-a1fc-f92876a76232";
    private static final String NAME = "Mike";

    public static void main(String[] args) {
        try {
            // Create ManagedIdentityCredential
            ManagedIdentityCredential managedIdentityCredential = new ManagedIdentityCredentialBuilder().build();


            // Create TokenRequestContext and set the scope
            TokenRequestContext tokenRequestContext = new TokenRequestContext();
            tokenRequestContext.addScopes(API_ID + "/.default");

            // Acquire access token
            AccessToken accessToken = managedIdentityCredential.getToken(tokenRequestContext).block();

            // Create the request URI
            String requestUri = String.format("%s/%s", FUNCTION_URI, FUNCTION_TRIGGER_PATH);

            // Create HTTP request with the access token in the Authorization header
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(requestUri))
                    .header("Authorization", "Bearer " + accessToken.getToken())
                    .GET()
                    .build();

            // Send the HTTP request
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the result
            System.out.println(response.body());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
