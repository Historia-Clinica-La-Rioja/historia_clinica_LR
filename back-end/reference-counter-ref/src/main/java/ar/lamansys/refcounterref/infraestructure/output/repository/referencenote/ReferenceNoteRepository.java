package ar.lamansys.refcounterref.infraestructure.output.repository.referencenote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferenceNoteRepository extends JpaRepository<ReferenceNote, Integer> {
}