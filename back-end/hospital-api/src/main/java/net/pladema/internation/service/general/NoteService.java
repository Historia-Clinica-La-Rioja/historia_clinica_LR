package net.pladema.internation.service.general;

import java.util.List;

public interface NoteService {

    Long createNote(String note);

    void deleteAllNotes(List<Long> notesToDelete);

    String getDescriptionById(Long clinicalImpressionNoteId);
}
