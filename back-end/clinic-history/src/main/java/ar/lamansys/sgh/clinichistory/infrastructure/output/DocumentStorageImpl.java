package ar.lamansys.sgh.clinichistory.infrastructure.output;

import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.application.ports.DocumentStorage;
import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentStorageImpl implements DocumentStorage {

    private final DocumentRepository documentRepository;
    private final NoteService noteService;

    @Override
    public Optional<DocumentBo> getMinimalDocumentBo(Long documentId) {
        return documentRepository.findById(documentId)
                .map(this::getMinimalDocumentBo);
    }

    @Override
    public Optional<DocumentBo> getDocumentBo(Long documentId) {
        return documentRepository.findById(documentId)
                .map(this::getCompleteDocumentBo);
    }

    private DocumentBo getMinimalDocumentBo(Document document) {
        var result = new DocumentBo();
        result.setId(document.getId());
        result.setPerformedDate(document.getCreatedOn());
        return result;
    }

    private DocumentBo getCompleteDocumentBo(Document document) {

        DocumentObservationsBo documentObservationsBo = new DocumentObservationsBo();
        documentObservationsBo.setOtherNote(noteService.getDescriptionById(document.getOtherNoteId()));
        documentObservationsBo.setIndicationsNote(noteService.getDescriptionById(document.getIndicationsNoteId()));
        documentObservationsBo.setEvolutionNote(noteService.getDescriptionById(document.getEvolutionNoteId()));
        documentObservationsBo.setPhysicalExamNote(noteService.getDescriptionById(document.getPhysicalExamNoteId()));
        documentObservationsBo.setStudiesSummaryNote(noteService.getDescriptionById(document.getStudiesSummaryNoteId()));
        documentObservationsBo.setClinicalImpressionNote(noteService.getDescriptionById(document.getClinicalImpressionNoteId()));
        documentObservationsBo.setCurrentIllnessNote(noteService.getDescriptionById(document.getCurrentIllnessNoteId()));

        return DocumentBo.builder()
                .id(document.getId())
                .encounterId(document.getSourceId())
                .documentSource(document.getSourceTypeId())
                .documentType(document.getTypeId())
                .performedDate(document.getCreatedOn())
                .clinicalSpecialtyId(document.getClinicalSpecialtyId())
                .patientId(document.getPatientId())
                .institutionId(document.getInstitutionId())
                .notes(documentObservationsBo)
                .build();
    }
}
