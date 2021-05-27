package net.pladema.clinichistory.hospitalization.service.anamnesis.impl;

import net.pladema.clinichistory.documents.events.OnGenerateInternmentDocumentEvent;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.EDocumentType;
import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.ips.domain.ClinicalTerm;
import net.pladema.clinichistory.documents.service.ips.domain.DiagnosisBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.CreateAnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.clinichistory.hospitalization.service.documents.validation.AnthropometricDataValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.EffectiveVitalSignTimeValidator;
import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.sgx.featureflags.AppFeature;
import net.pladema.sgx.pdf.PDFDocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CreateAnamnesisServiceImpl implements CreateAnamnesisService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateAnamnesisServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateDocumentFile createDocumentFile;

    private final FeatureFlagsService featureFlagsService;

    public CreateAnamnesisServiceImpl(DocumentFactory documentFactory,
                                      InternmentEpisodeService internmentEpisodeService,
                                      CreateDocumentFile createDocumentFile,
                                      FeatureFlagsService featureFlagsService) {
        this.documentFactory = documentFactory;
        this.internmentEpisodeService = internmentEpisodeService;
        this.createDocumentFile = createDocumentFile;
        this.featureFlagsService = featureFlagsService;
    }

    @Override
    @Transactional
    public AnamnesisBo createDocument(Integer institutionId, AnamnesisBo anamnesis)
            throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> anamnesis {}", anamnesis);

        var internmentEpisode = internmentEpisodeService.getInternmentEpisode(anamnesis.getEncounterId(), institutionId);
        anamnesis.setPatientId(internmentEpisode.getPatientId());

        assertAnamnesisValid(anamnesis);
        assertDoesNotHaveAnamnesis(internmentEpisode);
        assertEffectiveVitalSignTimeValid(anamnesis, internmentEpisode.getEntryDate());
        assertAnthropometricData(anamnesis);

        
        anamnesis.setId(documentFactory.run(anamnesis));

        internmentEpisodeService.updateAnamnesisDocumentId(anamnesis.getEncounterId(), anamnesis.getId());
        LOG.debug(OUTPUT, anamnesis);
        generateDocument(anamnesis, institutionId);

        return anamnesis;
    }

    private void assertAnamnesisValid(AnamnesisBo anamnesis) {
        anamnesis.validateSelf();
        assertMainDiagnosisValid(anamnesis);
        if (repeatedClinicalTerms(anamnesis.getDiagnosis()))
            throw new ConstraintViolationException("Diagnósticos secundarios repetidos", Collections.emptySet());
        if (repeatedClinicalTerms(anamnesis.getPersonalHistories()))
            throw new ConstraintViolationException("Antecedentes personales repetidos", Collections.emptySet());
        if (repeatedClinicalTerms(anamnesis.getFamilyHistories()))
            throw new ConstraintViolationException("Antecedentes familiares repetidos", Collections.emptySet());
        if (repeatedClinicalTerms(anamnesis.getProcedures()))
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


    private void assertEffectiveVitalSignTimeValid(AnamnesisBo anamnesis, LocalDate entryDate) {
        var validator = new EffectiveVitalSignTimeValidator();
        validator.isValid(anamnesis, entryDate);
    }

    private void assertAnthropometricData(AnamnesisBo anamnesis) {
        var validator = new AnthropometricDataValidator();
        validator.isValid(anamnesis);
    }

    private void assertMainDiagnosisValid(AnamnesisBo anamnesis) {
        if (featureFlagsService.isOn(AppFeature.MAIN_DIAGNOSIS_REQUIRED)
                && anamnesis.getMainDiagnosis() == null)
            throw new ConstraintViolationException("Diagnóstico principal obligatorio", Collections.emptySet());
        if (anamnesis.getMainDiagnosis() == null)
            return;
        if (anamnesis.getAlternativeDiagnosis() == null)
            return;
        SnomedBo snomedMainDiagnosis = anamnesis.getMainDiagnosis().getSnomed();
        if(anamnesis.getAlternativeDiagnosis().stream()
                .map(DiagnosisBo::getSnomed)
                .anyMatch(d -> d.equals(snomedMainDiagnosis))){
            throw new ConstraintViolationException("Diagnostico principal duplicado en los secundarios", Collections.emptySet());
        }
    }

    private void assertDoesNotHaveAnamnesis(InternmentEpisode internmentEpisode) {
        if(internmentEpisode.getAnamnesisDocId() != null) {
            throw new ConstraintViolationException("Esta internación ya posee una anamnesis", Collections.emptySet());
        }
    }

    private void generateDocument(AnamnesisBo anamnesis, Integer institutionId) throws IOException, PDFDocumentException {
        OnGenerateInternmentDocumentEvent event = new OnGenerateInternmentDocumentEvent(anamnesis, institutionId, anamnesis.getEncounterId(),
                EDocumentType.map(DocumentType.ANAMNESIS), anamnesis.getPatientId());
        createDocumentFile.execute(event);
    }

}
