package net.pladema.clinichistory.documents.service;

public interface NoteService {

    Long createNote(String note);

    String getDescriptionById(Long id);
}
