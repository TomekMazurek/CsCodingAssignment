package com.github.tomekmazurek.codingassignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Logs")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LogEntity {

    @Id
    private String id;
    private Long duration;
    private String host;
    private String type;
    private boolean alert;

}
