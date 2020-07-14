package net.pladema.clinichistory.hospitalization.service.maindiagnoses;

import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.generalstate.HealthConditionGeneralStateService;
import net.pladema.clinichistory.ips.repository.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.ips.repository.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
import net.pladema.clinichistory.ips.service.HealthConditionService;
import net.pladema.clinichistory.ips.service.domain.DiagnosisBo;
import net.pladema.clinichistory.ips.service.domain.DocumentObservationsBo;
import net.pladema.clinichistory.ips.service.domain.HealthConditionBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class MainDiagnosesServiceImpl implements MainDiagnosesService {

    private static final Logger LOG = LoggerFactory.getLogger(MainDiagnosesServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentService documentService;

    private final InternmentEpisodeService internmentEpisodeService;

    private final NoteService noteService;

    private final HealthConditionService healthConditionService;

    private final HealthConditionGeneralStateService healthConditionGeneralStateService;

    public MainDiagnosesServiceImpl(DocumentService documentService,
                                    InternmentEpisodeService internmentEpisodeService,
                                    NoteService noteService,
                                    HealthConditionService healthConditionService,
                                    HealthConditionGeneralStateService healthConditionGeneralStateService) {
        this.documentService = documentService;
        this.internmentEpisodeService = internmentEpisodeService;
        this.noteService = noteService;
        this.healthConditionService = healthConditionService;
        this.healthConditionGeneralStateService = healthConditionGeneralStateService;
    }

    @Override
    public Long createDocument(Integer internmentEpisodeId, Integer patientId, MainDiagnosisBo mainDiagnosisBo) {
        LOG.debug("Input parameters -> intermentEpisodeId {}, patientId {}, mainDiagnosisBo {}", internmentEpisodeId, patientId, mainDiagnosisBo);

        Document document = new Document(internmentEpisodeId, DocumentStatus.FINAL, DocumentType.EVALUATION_NOTE, SourceType.INTERNACION);
        loadNotes(document, Optional.ofNullable(mainDiagnosisBo.getNotes()));
        document = documentService.save(document);
        Long result = document.getId();

        mainDiagnosisBo.getMainDiagnosis().setVerificationId(ConditionVerificationStatus.CONFIRMED); // main diagnosiss always confirmed

        HealthConditionBo currentMainDiagnose = healthConditionGeneralStateService.getMainDiagnosisGeneralState(internmentEpisodeId);
        if (!currentMainDiagnose.getSnomed().equals(mainDiagnosisBo.getMainDiagnosis().getSnomed()))
            downgradeToAlternativeDiagnose(patientId, document.getId(), currentMainDiagnose);
        healthConditionService.loadMainDiagnosis(patientId, result, Optional.ofNullable(mainDiagnosisBo.getMainDiagnosis()));

        internmentEpisodeService.addEvolutionNote(internmentEpisodeId, document.getId());

        LOG.debug(OUTPUT, result);
        return result;
    }

    private void downgradeToAlternativeDiagnose(Integer patientId, Long docId, HealthConditionBo currentMainDiagnose) {
        LOG.debug("Input parameters -> patientId {}, docId {}, currentMainDiagnose {}", patientId, docId, currentMainDiagnose);
        DiagnosisBo diagnosisBo = new DiagnosisBo();
        diagnosisBo.setMain(false);
        diagnosisBo.setSnomed(currentMainDiagnose.getSnomed());
        diagnosisBo.setStatus(currentMainDiagnose.getStatus());
        diagnosisBo.setStatusId(currentMainDiagnose.getStatusId());
        diagnosisBo.setVerification(currentMainDiagnose.getVerification());
        diagnosisBo.setVerificationId(currentMainDiagnose.getVerificationId());
        healthConditionService.loadDiagnosis(patientId,docId, Arrays.asList(diagnosisBo));
    }

    private Document loadNotes(Document evolutionNote, Optional<DocumentObservationsBo> optNotes) {
        LOG.debug("Input parameters -> evolutionNote {}, notes {}", evolutionNote, optNotes);
        optNotes.ifPresent(notes -> {
            evolutionNote.setCurrentIllnessNoteId(noteService.createNote(notes.getCurrentIllnessNote()));
            evolutionNote.setPhysicalExamNoteId(noteService.createNote(notes.getPhysicalExamNote()));
            evolutionNote.setStudiesSummaryNoteId(noteService.createNote(notes.getStudiesSummaryNote()));
            evolutionNote.setEvolutionNoteId(noteService.createNote(notes.getEvolutionNote()));
            evolutionNote.setClinicalImpressionNoteId(noteService.createNote(notes.getClinicalImpressionNote()));
            evolutionNote.setOtherNoteId(noteService.createNote(notes.getOtherNote()));
        });
        LOG.debug(OUTPUT, evolutionNote);
        return evolutionNote;
    }


}
