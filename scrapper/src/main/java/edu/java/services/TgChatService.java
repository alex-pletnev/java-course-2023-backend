package edu.java.services;

import edu.java.dto.AddTgChatRequest;
import edu.java.model.TgChat;

public interface TgChatService {

    void registerTgChat(AddTgChatRequest addTgChatRequest);

    void deleteTgChat(long tgChatId);

    TgChat getByTgChatId(long tgChatId);
}
