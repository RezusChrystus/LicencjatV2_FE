package org.example.licencjatv2_fe.Classes;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Task {
    private Long id;
    private String content;
    private State State;
    private LocalDate createdAt;
    private LocalDate Deadline;

}
