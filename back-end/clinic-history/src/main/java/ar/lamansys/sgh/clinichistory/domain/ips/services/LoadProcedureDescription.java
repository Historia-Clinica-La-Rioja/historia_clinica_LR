package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentProcedureDescriptionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentProcedureDescription;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadProcedureDescription {

    private final DocumentProcedureDescriptionRepository documentProcedureDescriptionRepository;
    private final NoteService noteService;

    public ProcedureDescriptionBo run(Long documentId, Optional<ProcedureDescriptionBo> procedureDescription) {
        log.debug("Input parameters -> documentId {} procedureDescription {}", documentId, procedureDescription);

        procedureDescription.ifPresent((procedureDescriptionBo -> {
            Long noteId = noteService.createNote(procedureDescriptionBo.getNote());
            Short asa = procedureDescriptionBo.getAsa();
            DocumentProcedureDescription saved = documentProcedureDescriptionRepository.save(new DocumentProcedureDescription(documentId, noteId, asa));
            procedureDescriptionBo.setId(saved.getDocumentId());
        }));

        log.debug("Output -> {}", procedureDescription);
        return procedureDescription.orElse(null);
    }
}
