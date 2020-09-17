package com.comcast.command_processor.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CommandResponse {

    private Map<String, StateResult> stateResults;
    private List<String> topCommandsNationally;

    @JsonAnyGetter
    public Map<String, StateResult> getStateResults() {
        return stateResults;
    }

    public List<String> getTopCommandsNationally() {
        return topCommandsNationally;
    }
}
