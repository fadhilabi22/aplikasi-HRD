package helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class DatabaseConnection {
    // Isi pake URL dan Key dari Dashboard Supabase lu masbro
    public static final String URL_API = "https://bpzghjuzzygpmhddygvd.supabase.co/rest/v1/";
    public static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJwemdoanV6enlncG1oZGR5Z3ZkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgwNDE2ODIsImV4cCI6MjA5MzYxNzY4Mn0.yEMQbDPn3uqocCEUF8zwJD-UywgK09UYHcsfDSOLHIw";

    public static String fetchData(String endpoint) throws Exception {
    // Pake URI biar gak deprecated, terus dikonversi ke URL
    URL url = new URI(URL_API + endpoint).toURL(); 
    
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("apikey", API_KEY);
    conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
    
    // Proses baca data tetep sama
    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = in.readLine()) != null) response.append(line);
    in.close();
    
    return response.toString();
}
    // Method tambahan buat Simpan, Edit, dan Hapus
public static int sendRequest(String method, String endpoint, String json) throws Exception {
    URL url = new URI(URL_API + endpoint).toURL();
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod(method);
    conn.setRequestProperty("apikey", API_KEY);
    conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
    conn.setRequestProperty("Content-Type", "application/json");

    if (json != null) {
        conn.setDoOutput(true);
        try (java.io.OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }
    }
    return conn.getResponseCode(); // Ngirim kode angka (misal 201 kalau sukses simpan)
}
}