package org.example.licencjatv2_fe.Api;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/";

    public static String login(String login, String password) throws IOException, InterruptedException {
        // Budowanie URI z parametrami
        String urlWithParams = BASE_URL + "login?login=" + login + "&password=" + password;

        // Tworzenie klienta i zapytania
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .GET()
                .build();

        // Wysłanie zapytania
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Obsługa odpowiedzi
        if (response.statusCode() == 200) {
            return response.body();
        } else if (response.statusCode() == 401) {
            return null;
        } else {
            return "Błąd połączenia: HTTP " + response.statusCode();
        }
    }

    public static String register(String login, String password) throws IOException, InterruptedException {
        String url = BASE_URL + "register";

        // Budowanie ciała zapytania w formacie application/x-www-form-urlencoded
        String formData = "login=" + URLEncoder.encode(login, StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        // Tworzenie klienta i zapytania
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        // Wysłanie zapytania
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Obsługa odpowiedzi
        if (response.statusCode() == 201) {
            return response.body(); // Użytkownik utworzony pomyślnie
        } else {
            return "Rejestracja nieudana: HTTP " + response.statusCode() + " - " + response.body();
        }
    }
}
