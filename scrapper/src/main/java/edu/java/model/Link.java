package edu.java.model;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Link {

    private long linkId;
    private String url;
    private Timestamp lastCheckAt;
}
