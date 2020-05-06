package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.EvolutionNoteDocument;
import net.pladema.internation.repository.core.entity.EvolutionNoteDocumentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvolutionNoteDocumentRepository extends JpaRepository<EvolutionNoteDocument, EvolutionNoteDocumentPK> {

}
