package net.pladema.internation.repository.masterdata;

import net.pladema.internation.repository.masterdata.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}
