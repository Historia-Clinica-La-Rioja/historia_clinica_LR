package ar.lamansys.sgh.clinichistory.application.notes;

public interface NoteService {

    Long createNote(String note);

    String getDescriptionById(Long id);
}
