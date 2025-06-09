package org.example.licencjatv2_fe.Classes;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Workspace {
    private Long id;
    private String name;
    private String tag;

    // To jest to, co faktycznie przychodzi z backendu jako "tasks"
    private List<Task> tasks;

    // Twoje metody dostępu mogą pozostać bez zmian
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Task> getTaskList() {
        // jeśli tasks to null, to zwróć pustą listę, aby uniknąć NullPointerException
        return tasks == null ? new ArrayList<>() : tasks;
    }

    public void setTaskList(List<Task> taskList) {
        this.tasks = taskList;
    }
}