package edu.java.services.jdbc;

import edu.java.exceptions.DuplicateTgChatLinkBindException;
import edu.java.exceptions.TgChatLinkBindNotFoundException;
import edu.java.model.TgChatLinkBind;
import edu.java.repositories.jdbc.JdbcTgChatLinkBindRepository;
import edu.java.services.TgChatLinkBindService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTgChatLinkBindService implements TgChatLinkBindService {

    private final JdbcTgChatLinkBindRepository jdbcTgChatLinkBindRepository;

    @Override
    public TgChatLinkBind addTgChatLinkBind(long tgChatId, long linkId) {
        if (jdbcTgChatLinkBindRepository.findByTgchatIdAndLinkId(tgChatId, linkId).isPresent()) {
            throw new DuplicateTgChatLinkBindException();
        }
        TgChatLinkBind tgChatLinkBind = new TgChatLinkBind();
        tgChatLinkBind.setTgChatId(tgChatId);
        tgChatLinkBind.setLinkId(linkId);
        return jdbcTgChatLinkBindRepository
            .save(tgChatLinkBind)
            .orElseThrow();
    }

    @Override
    public TgChatLinkBind getByTgchatIdAndLinkId(long tgChatId, long linkId) {
        return jdbcTgChatLinkBindRepository
            .findByTgchatIdAndLinkId(tgChatId, linkId)
            .orElseThrow(TgChatLinkBindNotFoundException::new);
    }

    @Override
    public List<TgChatLinkBind> getAllByTgchatId(long tgChatId) {
        return jdbcTgChatLinkBindRepository.findAllByTgchatId(tgChatId);
    }

    @Override
    public List<TgChatLinkBind> getAll() {
        return jdbcTgChatLinkBindRepository.findAll();
    }

    public Boolean existByLinkId(long linkId) {
        return jdbcTgChatLinkBindRepository.existByLinkId(linkId);
    }

    @Override
    public void deleteByTgchatIdAndLinkId(long tgChatId, long linkId) {
        jdbcTgChatLinkBindRepository.deleteByTgchatIdAndLinkId(tgChatId, linkId);
    }

    @Override
    public void deleteAllByTgchatId(long tgChatId) {
        jdbcTgChatLinkBindRepository.deleteAllByTgchatId(tgChatId);
    }

    @Override
    public List<TgChatLinkBind> getAllByLinkId(long linkId) {
        return jdbcTgChatLinkBindRepository.findAllByLinkId(linkId);
    }
}
