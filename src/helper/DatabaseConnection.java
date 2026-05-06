package helper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DatabaseConnection {
    public static final String URL_API = "https://bpzghjuzzygpmhddygvd.supabase.co/rest/v1/";
    public static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJwemdoanV6enlncG1oZGR5Z3ZkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgwNDE2ODIsImV4cCI6MjA5MzYxNzY4Mn0.yEMQbDPn3uqocCEUF8zwJD-UywgK09UYHcsfDSOLHIw";

    public static String fetchData(String endpoint) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_API + endpoint))
                .header("apikey", API_KEY)
                .header("Authorization", "Bearer " + API_KEY)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}