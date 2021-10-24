package com.github.tomekmazurek.codingassignment;

import com.github.tomekmazurek.codingassignment.dto.LogDto;
import com.github.tomekmazurek.codingassignment.filehandling.FileReader;
import com.github.tomekmazurek.codingassignment.repository.LogRepository;
import com.github.tomekmazurek.codingassignment.service.DbInitializer;
import com.github.tomekmazurek.codingassignment.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);
    private final String DEFAULT_LOGFILE_PATH = "src/main/resources/logfile.txt";

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        logger.info(">> Application started");
        Application application = new Application();
        application.run("jdbc:hsqldb:file:db-data/mydatabase");
        long finishedTime = System.currentTimeMillis();
        logger.info(">> Application finished. Overall time " + (finishedTime-startTime) +"ms");

    }

    public void run(String connectionString) throws Exception {
        logger.debug(">> Initializing components...");
        DbInitializer initializer = new DbInitializer(connectionString);
        FileReader reader = new FileReader();
        Scanner scanner = new Scanner(System.in);
        LogRepository logRepository = new LogRepository();
        LogService logService = new LogService(logRepository, initializer);
        logger.debug(">> Initialization of components finished");

        System.out.println("Enter path to logfile \n" +
                "(if empty default logfile.txt from \"src/main/resources\" will be processed");
        String providedPath = scanner.nextLine();
        scanner.close();
        long startTime = System.currentTimeMillis();
        logger.info(">> Reading data from file...");
        List<LogDto> logs = reader.processFile(providedPath.trim().length() == 0 ? DEFAULT_LOGFILE_PATH : providedPath);
        long endTime = System.currentTimeMillis();
        logger.info(">> Finished...\n >> Read " + logs.size() + " objects in " + (endTime - startTime) + "ms");
        logger.info(">> Processing data...");
        long logEntities = logService.processLogsReadFromFile(logs);
        logger.info(">> Processing finished. " + logEntities + " records exist in database.");
    }
}


