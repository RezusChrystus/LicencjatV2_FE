package org.example.licencjatv2_fe.Classes;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long id;
    private String login;
    private String password;
    List<Workspace> workspaceList;
}
