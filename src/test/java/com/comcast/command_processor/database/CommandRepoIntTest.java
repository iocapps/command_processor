package com.comcast.command_processor.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ExtendWith(SpringExtension.class)
class CommandRepoIntTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommandRepo commandRepo;

    List<Command> commands = List.of(
            new Command("alabama", "cnn", 2),
            new Command("alabama", "nbc", 1),
            new Command("florida", "show me movies", 1),
            new Command("florida", "stranger things", 1),
            new Command("florida", "game of thrones", 1),
            new Command("florida", "turn off the tv", 2),
            new Command("maryland", "show me comedies", 1),
            new Command("maryland", "game of thrones", 3),
            new Command("maryland", "turn off the tv", 1));

    @BeforeEach
    void setUp() {
        commands.forEach(entityManager::persist);
        entityManager.flush();
    }

    @Test
    void findByStateAndCommandText() {
        Optional<Command> command = commandRepo.findByStateAndCommandText("Wyoming", "cnn");
        assertThat(command).isEmpty();

        command = commandRepo.findByStateAndCommandText("alabama", "cnn");
        assertThat(command).isNotEmpty();
        Command c = command.get();
        assert(c.getState().equals("alabama"));
        assert(c.getCommandText().equals("cnn"));
        assert(c.getFrequency() == 2);

        command = commandRepo.findByStateAndCommandText("alabama", "abc");
        assertThat(command).isEmpty();

        command = commandRepo.findByStateAndCommandText("california", "cnn");
        assertThat(command).isEmpty();
    }

    @Test
    void findFrequentCommandForState() {
        List<String> frequentCommands = List.of(
            commandRepo.findFrequentCommandForState("alabama"),
            commandRepo.findFrequentCommandForState("florida"),
            commandRepo.findFrequentCommandForState("maryland")
        );

        List<String> frequentCommandsAnswers = List.of("cnn", "turn off the tv", "game of thrones");
        assert(frequentCommands.equals(frequentCommandsAnswers));
    }

    @Test
    void findFrequentCommandsNationally() {
        List<String> commands = commandRepo.findFrequentCommandsNationally();
        assertThat(commands.equals(List.of("game of thrones", "turn off the tv", "cnn")));
    }
}