package com.github.tomekmazurek.codingassignment.service;

import com.github.tomekmazurek.codingassignment.dto.LogDto;
import com.github.tomekmazurek.codingassignment.model.LogEntity;
import com.github.tomekmazurek.codingassignment.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogService {

    private static Logger logger = LoggerFactory.getLogger(LogService.class);
    private LogRepository logRepository;
    private DbInitializer dbInitializer;

    public LogService(LogRepository logRepository, DbInitializer initializer) throws Exception {
        this.logRepository = logRepository;
        this.dbInitializer = initializer;
        dbInitializer.initializeDb();
    }

    public long processLogsReadFromFile(List<LogDto> logDtoListDto) {
        Map<String, LogEntity> convertedLogEntities = sortLogsAndConvertToMap(logDtoListDto);
        saveLogs(filterLogsThatDoesNotExistInDb(convertedLogEntities));
        return logRepository.getLogsIds().size();
    }

    private void saveLogs(Map<String, LogEntity> logEntities) {
        logger.info(">> Saving to database");
        logEntities.forEach((s, logEntity) -> {
            logRepository.saveLog(logEntity);
        });
        logger.info(">> Saving finished");
        logEntities.clear();
    }

    private Map<String, LogEntity> sortLogsAndConvertToMap(List<LogDto> logDtos) {
        logger.info(">> Sorting Logs");
        logDtos.sort(Comparator.comparing(LogDto::getId));
        logger.info(">> Logs sorted");
        Map<String, LogEntity> logEntities = new HashMap<>();
        logger.info(">> Creating entities started");
        while (logDtos.size() >= 2) {
            LogEntity logEntity = createLogEntityFromPairOfLogs(logDtos.get(0), logDtos.get(1));
            logEntities.put(logEntity.getId(), logEntity);
            logDtos.remove(1);
            logDtos.remove(0);
        }
        logger.info(">> Entities created: {} total", logEntities.size());
        return logEntities;
    }

    private List<LogEntity> getAllLogs() {
        return logRepository.getLogs();
    }

    private Map<String, LogEntity> filterLogsThatDoesNotExistInDb(Map<String, LogEntity> logEntityMap) {
        logger.info(">> Checking if Entities exist in database");
        long initialRead = logEntityMap.size();
        List<String> existingLogs = logRepository.getLogsIds();
        for (String id : existingLogs) {
            logEntityMap.remove(id);
        }
        logger.info(">>{} of LogEntities read from file exist in database, {} entities will be stored",(logEntityMap.size()-initialRead), logEntityMap.size());
        return logEntityMap;
    }

    private LogEntity createLogEntityFromPairOfLogs(LogDto firstLog, LogDto secondLog) {
        LogEntity entity = new LogEntity();
        if (firstLog.getState().equals("FINISHED")) {
            entity.setDuration(firstLog.getTimestamp() - secondLog.getTimestamp());
        } else {
            entity.setDuration(secondLog.getTimestamp() - firstLog.getTimestamp());
        }
        entity.setId(firstLog.getId());
        entity.setAlert(entity.getDuration() > 4 ? true : false);
        entity.setHost(firstLog.getHost());
        entity.setType(firstLog.getType());
        return entity;
    }
}
