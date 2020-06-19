package net.pladema.clinichistory.documents.repository;

import net.pladema.clinichistory.documents.repository.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}
