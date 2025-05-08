package org.example.licencjatv2_fe.Classes;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Workspace {
    private Long id;
    private String name;
    private String tag;
    private List<Task> taskList;
 }
