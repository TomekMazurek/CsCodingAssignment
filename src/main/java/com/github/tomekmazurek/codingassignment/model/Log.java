package com.github.tomekmazurek.codingassignment.model;

import lombok.*;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Log {

    private String id;
    private String state;
    private Timestamp timestamp;
    private String type;
    private String host;

}
