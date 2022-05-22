package net.pladema.clinichistory.outpatient.createoutpatient.service;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTermsValidatorUtils;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientDocumentBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientDocumentException;
import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientDocumentExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.Collections;


@Service
public class CreateOutpatientDocumentServiceImpl implements CreateOutpatientDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOutpatientDocumentServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final DocumentFactory documentFactory;

    private final UpdateOutpatientConsultationService updateOutpatientConsultationService;

    private final DateTimeProvider dateTimeProvider;

    public CreateOutpatientDocumentServiceImpl(DocumentFactory documentFactory,
                                               UpdateOutpatientConsultationService updateOutpatientConsultationService,
                                               DateTimeProvider dateTimeProvider) {
        this.documentFactory = documentFactory;
        this.updateOutpatientConsultationService = updateOutpatientConsultationService;
        this.dateTimeProvider = dateTimeProvider;
    }


    @Override
    public OutpatientDocumentBo execute(OutpatientDocumentBo outpatient) {
        LOG.debug("Input parameters -> outpatient {}", outpatient);

        assertContextValid(outpatient);
		LocalDateTime now = dateTimeProvider.nowDateTime();
		outpatient.setPerformedDate(now);
        outpatient.setId(documentFactory.run(outpatient, true));

        updateOutpatientConsultationService.updateOutpatientDocId(outpatient.getEncounterId(), outpatient.getId());
        LOG.debug(OUTPUT, outpatient);

        return outpatient;
    }

    private void assertContextValid(OutpatientDocumentBo outpatient) {
        if (outpatient.getInstitutionId() == null)
            throw new ConstraintViolationException("El id de la institución es obligatorio", Collections.emptySet());
        if (outpatient.getEncounterId() == null)
            throw new ConstraintViolationException("El id del encuentro asociado es obligatorio", Collections.emptySet());

        CreateOutpatientDocumentException repeatedErrors = new CreateOutpatientDocumentException(CreateOutpatientDocumentExceptionEnum.REPEATED_CLINICAL_TERM);
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(outpatient.getReasons()))
            repeatedErrors.addError("Motivos repetidos");

        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(outpatient.getProblems()))
            repeatedErrors.addError("Problemas médicos repetidos");

        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(outpatient.getFamilyHistories()))
            repeatedErrors.addError("Antecedentes familiares repetidos");

        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(outpatient.getAllergies()))
            repeatedErrors.addError("Alergias repetidas");

        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(outpatient.getProcedures()))
            repeatedErrors.addError("Procedimientos repetidos");
        if (repeatedErrors.hasErrors())
            throw repeatedErrors;
    }


}

