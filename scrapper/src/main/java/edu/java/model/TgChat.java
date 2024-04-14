package edu.java.model;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class TgChat {

    private long tgChatId;
    private String tag;
    private Timestamp createdAt;
}
