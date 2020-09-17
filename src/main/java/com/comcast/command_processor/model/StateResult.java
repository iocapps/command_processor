package com.comcast.command_processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StateResult {
    private String mostFrequentCommand;
    private long startProcessTime;
    private long stopProcessTime;
}
