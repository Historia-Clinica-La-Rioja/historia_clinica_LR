package ar.lamansys.sgh.clinichistory.application.document;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.document.visitor.DocumentVisitor;
import ar.lamansys.sgh.clinichistory.application.createDocumentFile.CreateDocumentFile;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.event.OnGenerateDocumentEvent;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snvs.application.ports.patient.PatientStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service(value = "common_document_factory")
public class CommonDocumentFactory implements DocumentFactory {

    @Qualifier(value = "save_components_visitor")
    private final DocumentVisitor saveComponentsVisitor;
    private final DocumentService documentService;
    private final CreateDocumentFile createDocumentFile;
    private final NoteService noteService;
    private final FeatureFlagsService featureFlagsService;
    private final PatientStorage patientStorage;

    @Override
    @Transactional
    public Long run(IDocumentBo documentBo, boolean createFile) {
        log.debug("Input parameters -> documentBo {} createFile {}", documentBo, createFile);

        Document doc = new Document(documentBo.getEncounterId(),
                documentBo.getDocumentStatusId(),
                documentBo.getDocumentType(),
                documentBo.getDocumentSource(),
                documentBo.getPatientId(),
                documentBo.getInstitutionId(),
                documentBo.getInitialDocumentId());
        this.loadNotes(doc, Optional.ofNullable(documentBo.getNotes()));

        var patientData = patientStorage.getPatientInfo(documentBo.getPatientId()).orElse(null);
        LocalDate patientInternmentAge = documentBo.getPatientInternmentAge();
        if (patientData != null && patientData.getBirthDate() != null && patientInternmentAge == null)
            doc.setPatientAgePeriod(Period.between(patientData.getBirthDate(), LocalDate.now()).toString());
        else if (patientData != null && patientData.getBirthDate() != null)
            doc.setPatientAgePeriod(Period.between(patientData.getBirthDate(), patientInternmentAge).toString());
        doc.setClinicalSpecialtyId(documentBo.getClinicalSpecialtyId());
        doc = documentService.save(doc);
        documentBo.setId(doc.getId());

        documentBo.accept(saveComponentsVisitor);

        if (createFile)
            this.generateDocument(documentBo);

        var result = documentBo.getId();
        log.debug("Output -> documentBo {}", result);
        return result;
    }

    private void loadNotes(Document document, Optional<DocumentObservationsBo> optNotes) {
        log.debug("Input parameters -> document {}, notes {}", document, optNotes);
        optNotes.ifPresent(notes -> {
            document.setCurrentIllnessNoteId(noteService.createNote(notes.getCurrentIllnessNote()));
            document.setPhysicalExamNoteId(noteService.createNote(notes.getPhysicalExamNote()));
            document.setStudiesSummaryNoteId(noteService.createNote(notes.getStudiesSummaryNote()));
            document.setEvolutionNoteId(noteService.createNote(notes.getEvolutionNote()));
            document.setClinicalImpressionNoteId(noteService.createNote(notes.getClinicalImpressionNote()));
            document.setOtherNoteId(noteService.createNote(notes.getOtherNote()));
            document.setIndicationsNoteId(noteService.createNote(notes.getIndicationsNote()));
        });
    }

    private void generateDocument(IDocumentBo documentBo) {
        OnGenerateDocumentEvent event = new OnGenerateDocumentEvent(documentBo);
        if (featureFlagsService.isOn(AppFeature.HABILITAR_GENERACION_ASINCRONICA_DOCUMENTOS_PDF))
            createDocumentFile.execute(event);
        else
            createDocumentFile.executeSync(event);
    }
}
