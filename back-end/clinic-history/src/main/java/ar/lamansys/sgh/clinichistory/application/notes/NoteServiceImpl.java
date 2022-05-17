package ar.lamansys.sgh.clinichistory.application.notes;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.NoteRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Note;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public String getDescriptionById(Long noteId) {
        LOG.debug("Input parameters -> noteId {}", noteId);
        if (noteId == null)
            return null;
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        String result = noteOptional.map(Note::getDescription).orElse(null);
        LOG.debug(OUTPUT, result);
        return result;
    }
}
