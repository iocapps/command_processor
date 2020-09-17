package com.comcast.command_processor;

import com.comcast.command_processor.controller.CommandController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommandProcessorApplicationTests {

    @Autowired
    private CommandController commandController;

    @Test
    void contextLoads() {
        assertThat(commandController).isNotNull();
    }

}
