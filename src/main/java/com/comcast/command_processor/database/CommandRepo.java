package com.comcast.command_processor.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommandRepo extends JpaRepository<Command, Long> {
    Optional<Command> findByStateAndCommandText(String state, String commandText);

    @Query(value = "SELECT TOP 1 c.command_text FROM Command c WHERE c.state = ?1 ORDER BY c.frequency DESC",
            nativeQuery = true)
    String findFrequentCommandForState(String state);

    @Query(value = "SELECT TOP 3 c.command_text FROM COMMAND c GROUP BY c.command_text ORDER BY SUM(c.frequency) DESC",
            nativeQuery = true)
    List<String> findFrequentCommandsNationally();
}
