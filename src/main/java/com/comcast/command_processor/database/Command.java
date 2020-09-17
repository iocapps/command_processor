package com.comcast.command_processor.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Data
@NoArgsConstructor
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String state;

    @NotBlank
    private String commandText;

    @PositiveOrZero
    private int frequency;

    public Command(String state, String commandText, int frequency) {
        this.state = state;
        this.commandText = commandText;
        this.frequency = frequency;
    }

    public void increment() {
        this.frequency++;
    }
}
