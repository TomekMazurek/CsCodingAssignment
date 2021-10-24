package com.github.tomekmazurek.codingassignment.filehandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomekmazurek.codingassignment.dto.LogDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileReader {

    private static Logger logger = LoggerFactory.getLogger(FileReader.class);
    private List<LogDto> logs;
    private ObjectMapper mapper;

    public List<LogDto> processFile(String filename) throws IOException {
        logger.debug(">> Inside processFile method");
        File file = new File(filename);
        logger.info(">> File: " + file.getAbsolutePath() + " found");
        logger.info(">> Processing the file");
        LineIterator lineIterator = FileUtils.lineIterator(file, "UTF-8");
        while (lineIterator.hasNext()) {

            LogDto tmpLog = mapper.readValue(lineIterator.nextLine(), LogDto.class);
            logs.add(tmpLog);
            logger.debug(">> " + tmpLog.toString() + " added");
        }
        return logs;
    }

    public FileReader() {
        this.logs = new LinkedList<>();
        this.mapper = new ObjectMapper();
    }
}
