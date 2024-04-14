package edu.java.services;

import edu.java.model.TgChatLinkBind;
import java.util.List;

public interface TgChatLinkBindService {

    TgChatLinkBind addTgChatLinkBind(long tgChatId, long linkId);

    TgChatLinkBind getByTgchatIdAndLinkId(long tgChatId, long linkId);

    List<TgChatLinkBind> getAllByTgchatId(long tgChatId);

    List<TgChatLinkBind> getAll();

    void deleteByTgchatIdAndLinkId(long tgChatId, long linkId);

    void deleteAllByTgchatId(long tgChatId);

    List<TgChatLinkBind> getAllByLinkId(long linkId);

}
