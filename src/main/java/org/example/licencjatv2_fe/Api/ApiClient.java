package org.example.licencjatv2_fe.Api;

import org.example.licencjatv2_fe.Classes.Task;
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
    private static final HttpClient client = HttpClient.newHttpClient();

    private static HttpResponse<String> sendGet(String pathWithParams) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + pathWithParams))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> sendPost(String path, String formData) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public static String login(String login, String password) throws IOException, InterruptedException {
        HttpResponse<String> response = sendGet("login?login=" + encode(login) + "&password=" + encode(password));
        return response.statusCode() == 200 ? response.body() : response.statusCode() == 401 ? null : "Błąd połączenia: HTTP " + response.statusCode();
    }

    public static String register(String login, String password) throws IOException, InterruptedException {
        String formData = "login=" + encode(login) + "&password=" + encode(password);
        HttpResponse<String> response = sendPost("register", formData);
        return response.statusCode() == 201 ? response.body() : "Rejestracja nieudana: HTTP " + response.statusCode() + " - " + response.body();
    }

    public static Workspace findByTag(String tag) throws IOException, InterruptedException {
        HttpResponse<String> response = sendGet("findWorkspaceByTag?tag=" + encode(tag));
        System.out.println("Odpowiedź z API: " + response.body());
        return response.statusCode() == 200 ? dtoService.workspaceMapping(response.body()) : null;
    }

    public static Workspace addWorkspaceToUser(String login, String password, String tag) throws IOException, InterruptedException {
        String formData = "login=" + encode(login) + "&password=" + encode(password) + "&tag=" + encode(tag);
        HttpResponse<String> response = sendPost("addWorkspace", formData);
        System.out.println("Odpowiedź z API: " + response.body());

        if (response.statusCode() == 200 && response.body().equals("Workspace added to user successfully.")) {
            Workspace newWorkspace = findByTag(tag);
            if (newWorkspace != null) return newWorkspace;
            System.out.println("Nie znaleziono workspace po tagu: " + tag);
        } else {
            System.out.println("Błąd odpowiedzi: " + response.body());
        }
        return null;
    }

    public static Workspace createWorkspace(String workspaceName) throws IOException, InterruptedException {
        String formData = "workspaceName=" + encode(workspaceName);
        HttpResponse<String> response = sendPost("createWorkspace", formData);
        return response.statusCode() == 201 ? dtoService.workspaceMapping(response.body()) : null;
    }

    public static Workspace createAndAddWorkspaceToUser(String login, String password, String workspaceName) throws IOException, InterruptedException {
        String formData = "login=" + encode(login) + "&password=" + encode(password) + "&name=" + encode(workspaceName);
        HttpResponse<String> response = sendPost("createAndAddWorkspace", formData);
        System.out.println("Create and Add Workspace - response: " + response.body());

        if (response.statusCode() == 200 && response.body().contains("successfully")) {
            Workspace temp = new Workspace();
            temp.setName(workspaceName);
            temp.setTag(workspaceName); // Zakładamy, że tag = name
            return temp;
        }
        return null;
    }
    public static String addTaskToWorkspace(String workspaceTag, String content, String deadline, String state) throws IOException, InterruptedException {
        String json = """
        {
            "content": "%s",
            "createdAt": "%s",
            "state": "%s",
            "deadline": "%s"
        }
        """.formatted(content, java.time.LocalDate.now(), state, deadline);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "workspaces/" + encode(workspaceTag) + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return response.body();
        } else {
            System.out.println("Błąd dodawania taska: HTTP " + response.statusCode() + " - " + response.body());
            return null;
        }
    }
    public static void updateTask(Task task) {
        try {
            String json = """
            {
                "id": %d,
                "content": "%s",
                "deadline": "%s",
                "state": "%s"
            }
            """.formatted(task.getId(), task.getContent(), task.getDeadline(), task.getState());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "workspaces/tasks/" + task.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Update Task response: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
