package org.example.licencjatv2_fe.DTO;

import org.example.licencjatv2_fe.Classes.State;
import org.example.licencjatv2_fe.Classes.Task;
import org.example.licencjatv2_fe.Classes.User;
import org.example.licencjatv2_fe.Classes.Workspace;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DTOService {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DTOService() {}

    public User userMapping(String userInfo) {
        JSONObject userJson = new JSONObject(userInfo);
        User user = new User();
        user.setId(userJson.getLong("id"));
        user.setLogin(userJson.getString("login"));
        user.setPassword(userJson.getString("password"));

        List<Workspace> workspaceList = new ArrayList<>();
        JSONArray workspaces = userJson.getJSONArray("workspaces");
        for (int i = 0; i < workspaces.length(); i++) {
            workspaceList.add(workspaceMapping(workspaces.getJSONObject(i).toString()));
        }
        user.setWorkspaceList(workspaceList);
        return user;
    }

    public Workspace workspaceMapping(String workspaceInfo) {
        System.out.println("Otrzymana odpowiedź do mapowania: " + workspaceInfo);
        JSONObject workspaceJson = new JSONObject(workspaceInfo);
        Workspace workspace = new Workspace();
        workspace.setId(workspaceJson.getLong("id"));
        workspace.setName(workspaceJson.getString("name"));
        workspace.setTag(workspaceJson.getString("tag"));

        List<Task> taskList = new ArrayList<>();
        JSONArray tasks = workspaceJson.getJSONArray("tasks");
        for (int j = 0; j < tasks.length(); j++) {
            taskList.add(taskMapping(tasks.getJSONObject(j).toString()));
        }
        workspace.setTaskList(taskList);
        return workspace;
    }



    public Task taskMapping(String taskInfo) {
        JSONObject taskJson = new JSONObject(taskInfo);
        Task task = new Task();
        task.setId(taskJson.getLong("id"));
        task.setContent(taskJson.getString("content"));
        task.setState(State.valueOf(taskJson.getString("state").toUpperCase().replace(" ", "_")));
        task.setDeadline(LocalDate.parse(taskJson.getString("deadline"), dateFormatter));
        task.setCreatedAt(LocalDate.parse(taskJson.getString("created_at"), dateFormatter));
        return task;
    }
}
