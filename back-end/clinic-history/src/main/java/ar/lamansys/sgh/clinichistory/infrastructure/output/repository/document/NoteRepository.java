package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT n.description " +
			"FROM Document d " +
			"JOIN Note n ON (d.otherNoteId = n.id) " +
			"WHERE d.id = :id ")
	String findEvolutionNoteByDocumentId(@Param("id") Long id);

}
