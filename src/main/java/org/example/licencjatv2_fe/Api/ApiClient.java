package org.example.licencjatv2_fe.Api;

import org.example.licencjatv2_fe.Classes.User;
import org.example.licencjatv2_fe.Classes.Workspace;
import org.example.licencjatv2_fe.DTO.DTOService;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/";
    private static final DTOService dtoService = new DTOService();

    public static String login(String login, String password) throws IOException, InterruptedException {
        String urlWithParams = BASE_URL + "login?login=" + login + "&password=" + password;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

        String formData = "login=" + URLEncoder.encode(login, StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return response.body();
        } else {
            return "Rejestracja nieudana: HTTP " + response.statusCode() + " - " + response.body();
        }
    }

    public static Workspace findByTag(String tag) throws IOException, InterruptedException {
        String urlWithParams = BASE_URL + "findWorkspaceByTag?tag=" + URLEncoder.encode(tag, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Odpowiedź z API: " + response.body());  // Logowanie odpowiedzi

        if (response.statusCode() == 200) {
            return dtoService.workspaceMapping(response.body());
        } else {
            return null;
        }
    }

    public static Workspace addWorkspaceToUser(String login, String password, String tag) throws IOException, InterruptedException {
        String url = BASE_URL + "addWorkspace";

        String formData = "login=" + URLEncoder.encode(login, StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8) +
                "&tag=" + URLEncoder.encode(tag, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Odpowiedź z API: " + response.body());  // Logowanie odpowiedzi

        if (response.statusCode() == 200) {
            // Jeśli odpowiedź to komunikat, nie mapuj tego na JSON
            if (response.body().equals("Workspace added to user successfully.")) {
                // Możesz zwrócić pusty workspace z tagiem, lub pobrać go później po tagu
                Workspace newWorkspace = findByTag(tag);
                if (newWorkspace != null) {
                    return newWorkspace;
                } else {
                    // Obsłuż brak workspace, jeśli nie udało się go znaleźć
                    System.out.println("Nie znaleziono workspace po tagu: " + tag);
                }
            }
        } else {
            // Jeśli status nie jest 200, sprawdź inne błędy
            System.out.println("Błąd odpowiedzi: " + response.body());
        }
        return null;  // Zwróć null w przypadku braku odpowiedzi
    }



}
