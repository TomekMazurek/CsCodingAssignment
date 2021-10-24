package com.github.tomekmazurek.codingassignment.repository;

import com.github.tomekmazurek.codingassignment.hibernate.HibernateUtil;
import com.github.tomekmazurek.codingassignment.model.LogEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogRepository {

    private static Logger logger = LoggerFactory.getLogger(LogRepository.class);
    private Transaction transaction;

    public LogRepository() {
        this.transaction = null;
    }

    public void saveLog(LogEntity logEntity) {
        logger.debug(">> Saving entity {}", logEntity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(logEntity);
            transaction.commit();
        } catch (Exception exception) {
            logger.warn(">> Exception {}", exception.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
        }
    }

    public List<String> getLogsIds() {
        List<String> logsIds = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            logsIds = session.createQuery("select l.id from LogEntity l", String.class).getResultList();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            exception.printStackTrace();
        }
        return logsIds;
    }

    public List<LogEntity> getLogs() {
        List<LogEntity> logEntities = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            logEntities = session.createQuery("from LogEntity", LogEntity.class).getResultList();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return Collections.unmodifiableList(logEntities);
    }
}
