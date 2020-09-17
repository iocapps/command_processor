package com.comcast.command_processor.service;

import com.comcast.command_processor.database.Command;
import com.comcast.command_processor.database.CommandRepo;
import com.comcast.command_processor.model.CommandResponse;
import com.comcast.command_processor.model.SingleCommand;
import com.comcast.command_processor.model.StateResult;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommandService {

    private final CommandRepo commandRepo;

    CommandService(CommandRepo commandRepo) {
        this.commandRepo = commandRepo;
    }

    /**
     * Processes the entire request body by iterating over the Json data and returns a response
     * which has most frequent commands per state and nationally.
     * This methods waits until a request is processed completely before processing another.
     *
     * @param commandRequest - A map which includes request data
     * @return - A response with an analysis of the data
     */
    public synchronized CommandResponse processRequest(Map<String, List<SingleCommand>> commandRequest) {
        Map<String, StateResult> stateMap = new LinkedHashMap<>(); //LinkedHashMap to save the order of insertion

        commandRequest.forEach((state, commandsList) -> {
            commandsList.forEach((singleCommand) -> {
                long startProcessTime = System.currentTimeMillis();

                String stateText = state.trim().toLowerCase();
                String commandText = singleCommand.getCommand().trim().toLowerCase();
                saveCommand(stateText, commandText);
                String frequentCommandForState = commandRepo.findFrequentCommandForState(stateText);

                long stopProcessTime = System.currentTimeMillis();
                stateMap.put(stateText, new StateResult(frequentCommandForState, startProcessTime, stopProcessTime));
            });
        });

        return new CommandResponse(stateMap, commandRepo.findFrequentCommandsNationally());
    }

    /**
     * Creates a new command or updates an existing command
     * And then saves it in the database
     *
     * @param state       - state name
     * @param commandText - command phrase
     */
    public void saveCommand(String state, String commandText) {
        Command newCommand = commandRepo
                .findByStateAndCommandText(state, commandText)
                .map(command -> {
                    command.increment();
                    return command;
                }).orElse(new Command(state, commandText, 1));
        commandRepo.save(newCommand);
    }
}
