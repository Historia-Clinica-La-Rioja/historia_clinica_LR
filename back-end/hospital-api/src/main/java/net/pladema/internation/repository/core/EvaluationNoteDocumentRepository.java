package net.pladema.internation.repository.core;

import net.pladema.internation.repository.core.entity.EvaluationNoteDocument;
import net.pladema.internation.repository.core.entity.EvaluationNoteDocumentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationNoteDocumentRepository extends JpaRepository<EvaluationNoteDocument, EvaluationNoteDocumentPK> {

}
