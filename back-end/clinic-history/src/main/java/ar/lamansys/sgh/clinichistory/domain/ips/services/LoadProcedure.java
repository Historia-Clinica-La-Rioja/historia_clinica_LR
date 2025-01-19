package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.NoteRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Note;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProceduresRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Procedure;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.ProceduresStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoadProcedure {

    public static final String OUTPUT = "Output -> {}";

    private final ProceduresRepository proceduresRepository;
    private final ProceduresStatusRepository proceduresStatusRepository;
    private final DocumentService documentService;
    private final SnomedService snomedService;
    private final NoteRepository noteRepository;

    public ProcedureBo run(Integer patientId, Long documentId, ProcedureBo procedureBo) {
        log.debug("Input parameters -> patientId {}, documentId {}, procedureBo {}", patientId, documentId, procedureBo);
        this.assertRequiredFields(documentId, procedureBo);
        if (procedureBo.getId() == null) {
            Integer snomedId = snomedService.getSnomedId(procedureBo.getSnomed())
                    .orElseGet(() -> snomedService.createSnomedTerm(procedureBo.getSnomed()));
            Procedure procedure = saveProcedure(patientId, procedureBo, snomedId);
            procedureBo.setId(procedure.getId());
            procedureBo.setStatusId(procedure.getStatusId());
            procedureBo.setStatus(getStatus(procedure.getStatusId()));
        }
        documentService.createDocumentProcedure(documentId, procedureBo.getId());
        log.debug(OUTPUT, procedureBo);
        return procedureBo;
    }

    private void assertRequiredFields(Long documentId, ProcedureBo procedureBo) {
        Assert.notNull(documentId, "El identificador del documento es obligatorio");
        Assert.notNull(procedureBo, "Parámetro de procedimiento no puede ser vacío");
    }

    private Procedure saveProcedure(Integer patientId, ProcedureBo procedureBo, Integer snomedId) {
        log.debug("Input parameters -> patientId {}, procedureBo {}, snomedId {}", patientId, procedureBo, snomedId);
        Procedure result = new Procedure(
                patientId,
                snomedId,
                procedureBo.getStatusId(), procedureBo.getPerformedDate(),
                procedureBo.getType(),
                procedureBo.getIsPrimary());
        if (procedureBo.getNote() != null) {
            Long noteId = noteRepository.save(new Note(procedureBo.getNote())).getId();
            result.setNoteId(noteId);
        }
        result = proceduresRepository.save(result);
        log.debug("Procedure saved -> {}", result.getId());
        log.debug(OUTPUT, result);
        return result;
    }

    private String getStatus(String id) {
        return proceduresStatusRepository.findById(id).map(ProceduresStatus::getDescription).orElse(null);
    }

}
