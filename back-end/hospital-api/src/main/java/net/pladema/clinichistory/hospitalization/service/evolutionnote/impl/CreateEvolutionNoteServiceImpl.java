package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import net.pladema.clinichistory.documents.events.OnGenerateInternmentDocumentEvent;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.EDocumentType;
import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.generalstate.HealthConditionGeneralStateService;
import net.pladema.clinichistory.documents.service.ips.domain.ClinicalTerm;
import net.pladema.clinichistory.documents.service.ips.domain.DiagnosisBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.AnthropometricDataValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.EffectiveVitalSignTimeValidator;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.sgx.pdf.PDFDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CreateEvolutionNoteServiceImpl implements CreateEvolutionNoteService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateEvolutionNoteServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateDocumentFile createDocumentFile;

    private final HealthConditionGeneralStateService healthConditionGeneralStateService;

    public CreateEvolutionNoteServiceImpl(DocumentFactory documentFactory,
                                          InternmentEpisodeService internmentEpisodeService,
                                          CreateDocumentFile createDocumentFile,
                                          HealthConditionGeneralStateService healthConditionGeneralStateService) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.createDocumentFile = createDocumentFile;
        this.healthConditionGeneralStateService = healthConditionGeneralStateService;
    }

    @Override
    public EvolutionNoteBo execute(Integer institutionId, EvolutionNoteBo evolutionNote) throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> institutionId {}, evolutionNote {}", institutionId, evolutionNote);

        var internmentEpisode = internmentEpisodeService.getInternmentEpisode(evolutionNote.getEncounterId(), institutionId);
        evolutionNote.setPatientId(internmentEpisode.getPatientId());


        assertDoesNotHaveEpicrisis(internmentEpisode);
        assertEvolutionNoteValid(evolutionNote);
        assertEffectiveVitalSignTimeValid(evolutionNote, internmentEpisode.getEntryDate());
        assertDiagnosisValid(evolutionNote, internmentEpisode);
        assertAnthropometricData(evolutionNote);


        evolutionNote.setId(documentFactory.run(evolutionNote));

        internmentEpisodeService.addEvolutionNote(internmentEpisode.getId(), evolutionNote.getId());


        LOG.debug(OUTPUT, evolutionNote);

        generateDocument(evolutionNote, institutionId);
        return evolutionNote;
    }

    private void assertEvolutionNoteValid(EvolutionNoteBo evolutionNote) {
        evolutionNote.validateSelf();
        if (repeatedClinicalTerms(evolutionNote.getDiagnosis()))
            throw new ConstraintViolationException("Diagnósticos secundarios repetidos", Collections.emptySet());
        if (repeatedClinicalTerms(evolutionNote.getProcedures()))
            throw new ConstraintViolationException("Procedimientos repetidos", Collections.emptySet());
    }

    private boolean repeatedClinicalTerms(List<? extends ClinicalTerm> clinicalTerms) {
        if (clinicalTerms == null || clinicalTerms.isEmpty())
            return false;
        final Set<SnomedBo> set = new HashSet<>();
        for (ClinicalTerm ct : clinicalTerms)
            if (!set.add(ct.getSnomed()))
                return true;
        return false;
    }

    private void assertAnthropometricData(EvolutionNoteBo evolutionNoteBo) {
        var validator = new AnthropometricDataValidator();
        validator.isValid(evolutionNoteBo);
    }

    private void assertDoesNotHaveEpicrisis(InternmentEpisode internmentEpisode) {
        if(internmentEpisode.getEpicrisisDocId() != null) {
            throw new ConstraintViolationException("Esta internación ya posee una epicrisis", Collections.emptySet());
        }
    }

    private void assertDiagnosisValid(EvolutionNoteBo evolutionNote, InternmentEpisode internmentEpisode) {
        if (evolutionNote.getDiagnosis() == null || evolutionNote.getDiagnosis().isEmpty())
            return;
        HealthConditionBo mainDiagnosis = healthConditionGeneralStateService.getMainDiagnosisGeneralState(internmentEpisode.getId());
        if (mainDiagnosis == null)
            return;
        if (evolutionNote.getDiagnosis().stream()
                .map(DiagnosisBo::getSnomed)
                .anyMatch(d -> d.equals(mainDiagnosis.getSnomed()))) {
            throw new ConstraintViolationException("Diagnostico principal duplicado en los secundarios", Collections.emptySet());
        }
    }

    private void assertEffectiveVitalSignTimeValid(EvolutionNoteBo evolutionNote, LocalDate entryDate) {
        var validator = new EffectiveVitalSignTimeValidator();
        validator.isValid(evolutionNote, entryDate);
    }

    private void generateDocument(EvolutionNoteBo evolutionNote, Integer institutionId) throws IOException, PDFDocumentException {
        OnGenerateInternmentDocumentEvent event = new OnGenerateInternmentDocumentEvent(evolutionNote, institutionId, evolutionNote.getEncounterId(),
                EDocumentType.map(DocumentType.EVALUATION_NOTE), evolutionNote.getPatientId());
        createDocumentFile.execute(event);
    }
}
