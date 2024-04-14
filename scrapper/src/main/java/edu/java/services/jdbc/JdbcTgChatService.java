package edu.java.services.jdbc;

import edu.java.dto.AddTgChatRequest;
import edu.java.exceptions.DuplicateRegistrationException;
import edu.java.exceptions.RegistrationException;
import edu.java.exceptions.UndefinedTgChatIdException;
import edu.java.model.TgChat;
import edu.java.repositories.jdbc.JdbcTgChatRepository;
import edu.java.services.TgChatService;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {

    private final JdbcTgChatRepository jdbcTgChatRepository;

    @Override
    @Transactional
    public void registerTgChat(AddTgChatRequest addTgChatRequest) {
        if (jdbcTgChatRepository.findByTgchatId(addTgChatRequest.tgChatId()).isPresent()) {
            throw new DuplicateRegistrationException();
        }
        TgChat tgChat = new TgChat();
        tgChat.setTgChatId(addTgChatRequest.tgChatId());
        tgChat.setTag(addTgChatRequest.tag());
        tgChat.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        if (!jdbcTgChatRepository
            .save(tgChat)
            .orElseThrow(RegistrationException::new)
            .equals(tgChat)) {
            throw new RegistrationException();
        }

    }

    @Override
    @Transactional
    public void deleteTgChat(long tgChatId) {
        if (jdbcTgChatRepository.findByTgchatId(tgChatId).isEmpty()) {
            throw new UndefinedTgChatIdException();
        }
        jdbcTgChatRepository.deleteByTgchatId(tgChatId);
    }

    @Override
    public TgChat getByTgChatId(long tgChatId) {
        return jdbcTgChatRepository.findByTgchatId(tgChatId).orElseThrow(UndefinedTgChatIdException::new);
    }
}
