package com.comcast.command_processor.service;

import com.comcast.command_processor.database.CommandRepo;
import com.comcast.command_processor.model.CommandResponse;
import com.comcast.command_processor.model.SingleCommand;
import com.comcast.command_processor.model.StateResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommandServiceTest {
    private final int TOTAL_REQUESTS = 3;

    @Autowired
    private CommandRepo commandRepo;

    @Autowired
    private CommandService commandService;

    List<Map<String, List<SingleCommand>>> requests;

    @BeforeEach
    void setUp() {
        requests = new ArrayList<>();
        for (int i = 1; i <= TOTAL_REQUESTS; i++) {
            requests.add(getRequestMapFromJsonFile(i));
        }
    }

    /**
     * Loads files from Resources directory
     */
    Map<String, List<SingleCommand>> getRequestMapFromJsonFile(int fileNumber) {
        Map<String, List<SingleCommand>> map = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("request" + fileNumber + ".json").getFile());
            map = mapper.readValue(file, new TypeReference<Map<String, List<SingleCommand>>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Test
    void processRequest() {
        CommandResponse response = commandService.processRequest(requests.get(0));
        assert (response.getTopCommandsNationally().isEmpty());
        assert (response.getStateResults().isEmpty());

        response = commandService.processRequest(requests.get(1));
        assert (response.getTopCommandsNationally().equals(List.of("game of thrones", "turn off the tv", "cnn")));
        StateResult stateResult = response.getStateResults().get("alabama");
        assert (stateResult.getMostFrequentCommand().equals("cnn"));
        stateResult = response.getStateResults().get("florida");
        assert (stateResult.getMostFrequentCommand().equals("turn off the tv"));
        stateResult = response.getStateResults().get("maryland");
        assert (stateResult.getMostFrequentCommand().equals("game of thrones"));

        response = commandService.processRequest(requests.get(2));
        assert (response.getTopCommandsNationally().equals(List.of("turn off the tv", "nbc", "game of thrones")));
        stateResult = response.getStateResults().get("texas");
        assert (stateResult.getMostFrequentCommand().equals("nbc"));
        stateResult = response.getStateResults().get("california");
        assert (stateResult.getMostFrequentCommand().equals("turn off the tv"));
    }

    @Test
    void saveCommand() {
        commandRepo.deleteAll();

        assertThat(commandRepo.findByStateAndCommandText("texas", "cnn")).isEmpty();
        commandService.saveCommand("texas", "cnn");
        assertThat(commandRepo.findByStateAndCommandText("texas", "cnn")).isNotEmpty();
        assert (commandRepo.findByStateAndCommandText("texas", "cnn").get().getFrequency() == 1);

        commandService.saveCommand("texas", "cnn");
        assert (commandRepo.findByStateAndCommandText("texas", "cnn").get().getFrequency() == 2);
        assertThat(commandRepo.findByStateAndCommandText("texas", "turn off the tv")).isEmpty();
    }
}