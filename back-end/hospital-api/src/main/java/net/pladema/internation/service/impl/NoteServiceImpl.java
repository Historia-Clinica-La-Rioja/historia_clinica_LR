package net.pladema.internation.service.impl;

import net.pladema.internation.repository.masterdata.NoteRepository;
import net.pladema.internation.repository.masterdata.entity.Note;
import net.pladema.internation.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger LOG = LoggerFactory.getLogger(NoteServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    @Override
    public Long createNote(String note) {
        LOG.debug("Input parameters -> note {}", note);
        if (note == null)
            return null;
        Note result = new Note(note);
        result = noteRepository.save(result);
        LOG.debug(OUTPUT, result);
        return result.getId();
    }
}
