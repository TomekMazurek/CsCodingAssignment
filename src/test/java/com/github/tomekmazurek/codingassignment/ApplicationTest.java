package com.github.tomekmazurek.codingassignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomekmazurek.codingassignment.dto.LogDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

class ApplicationTest {

    @Test
    void generateTestFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Set<LogDto> logs = new HashSet<>();
        File file = new File("test2.txt");
        long beginningDate = Timestamp.valueOf("2020-01-01 00:00:00").getTime();
        long endingDate = Timestamp.valueOf("2020-01-05 00:00:00").getTime();
        long diff = endingDate - beginningDate + 1;
        for (int i = 0; i < 100000; i++) {
            long timestamp = beginningDate + (long) (Math.random() * diff);
            String generatedId = RandomStringUtils.randomAlphabetic(10);
            String stateSTARTED = "STARTED";
            String stateFinished = "FINISHED";
            LogDto logDtoStarted = LogDto.builder()
                    .id(generatedId)
                    .state(stateSTARTED)
                    .timestamp(timestamp)
                    .build();
            LogDto logDtoFinished = LogDto.builder()
                    .id(generatedId)
                    .state(stateFinished)
                    .timestamp(timestamp + (long) (Math.random() * 10))
                    .build();
            if (!logs.contains(logDtoStarted.getId())) {
                logs.add(logDtoStarted);
                logs.add(logDtoFinished);
            }
        }
        logs.forEach(logDto -> {
            try {
                FileUtils.write(file, mapper.writeValueAsString(logDto) + "\n", true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}