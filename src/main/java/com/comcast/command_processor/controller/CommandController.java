package com.comcast.command_processor.controller;

import com.comcast.command_processor.model.CommandResponse;
import com.comcast.command_processor.model.SingleCommand;
import com.comcast.command_processor.service.CommandService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
public class CommandController {

    private final CommandService commandService;

    CommandController(CommandService commandService){
        this.commandService = commandService;
    }

    @PostMapping("/commands")
    CommandResponse processCommands(@RequestBody Map<String, List<SingleCommand>> commandRequest) {
        return commandService.processRequest(commandRequest);
    }
}
