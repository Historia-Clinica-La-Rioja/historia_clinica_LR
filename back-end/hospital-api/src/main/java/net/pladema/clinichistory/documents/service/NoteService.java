package net.pladema.clinichistory.documents.service;

import java.util.List;

public interface NoteService {

    Long createNote(String note);

    void deleteAllNotes(List<Long> notesToDelete);

    String getDescriptionById(Long clinicalImpressionNoteId);
}
