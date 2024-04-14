package edu.java.services;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkResponse;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.model.Link;
import java.sql.Timestamp;
import java.util.List;

public interface LinkService {

    ListLinksResponse getAllLinksByTgChatId(long tgChatId);

    LinkResponse addLinkForTgChatId(long tgChatId, AddLinkRequest addLinkRequest);

    void deleteLinkForTgChatId(long tgChatId, RemoveLinkRequest removeLinkRequest);

    List<Link> getAllWhereLastCheckAtAfterParam(Timestamp timestamp);

    void updateAllLastCheckAtByLinkIdList(List<Long> linkIdList, Timestamp newLastCheckValue);

}
