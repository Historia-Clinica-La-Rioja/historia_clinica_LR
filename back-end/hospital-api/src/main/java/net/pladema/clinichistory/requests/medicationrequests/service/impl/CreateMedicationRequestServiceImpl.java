package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.hospitalization.service.documents.validation.DosageValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.SnomedValidator;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.entity.MedicationRequest;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class CreateMedicationRequestServiceImpl implements CreateMedicationRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateMedicationRequestServiceImpl.class);

    private final MedicationRequestRepository medicationRequestRepository;

    private final DocumentFactory documentFactory;

    public CreateMedicationRequestServiceImpl(MedicationRequestRepository medicationRequestRepository, DocumentFactory documentFactory) {
        this.medicationRequestRepository = medicationRequestRepository;
        this.documentFactory = documentFactory;
    }

    @Override
    @Transactional
    public Integer execute(Integer institutionId, MedicationRequestBo medicationRequest) {
        LOG.debug("Input parameters -> {}, {} ", institutionId, medicationRequest);

        assertRequiredFields(institutionId, medicationRequest);

        MedicationRequest newMR = createMedicationRequest(institutionId, medicationRequest);
        medicationRequest.setEncounterId(newMR.getId());
        documentFactory.run(medicationRequest);

        return newMR.getId();
    }

    private void assertRequiredFields(Integer institutionId, MedicationRequestBo medicationRequest) {
        Assert.notNull(institutionId, "El identificador de la institución es obligatorio");
        Assert.notNull(medicationRequest, "La receta es obligatoria");
        Assert.notNull(medicationRequest.getPatientId(), "El paciente es obligatorio");
        Assert.notNull(medicationRequest.getDoctorId(), "El identificador del médico es obligatorio");
        if (medicationRequest.isHasRecipe())
            Assert.notNull(medicationRequest.getMedicalCoverageId(), "El identificador de la cobertura médica es obligatorio");
        Assert.notEmpty(medicationRequest.getMedications(), "La receta tiene que tener asociada al menos una medicación");

        SnomedValidator snomedValidator =  new SnomedValidator();
        DosageValidator dosageValidator = new DosageValidator();
        medicationRequest.getMedications().forEach(md -> {
            snomedValidator.isValid(md.getSnomed());
            Assert.notNull(md.getHealthCondition(), "La medicación tiene que estar asociada a un problema");
            Assert.notNull(md.getHealthCondition().getId(), "La medicación tiene que estar asociada a un problema");
            dosageValidator.isValid(md.getDosage());
        });
    }

    private MedicationRequest createMedicationRequest(Integer institutionId, MedicationRequestBo medicationRequest) {
        MedicationRequest result = new MedicationRequest();
        result.setPatientId(medicationRequest.getPatientId());
        result.setInstitutionId(institutionId);
        result.setMedicalCoverageId(medicationRequest.getMedicalCoverageId());
        result.setDoctorId(medicationRequest.getDoctorId());
        result.setHasRecipe(medicationRequest.isHasRecipe());
        result = medicationRequestRepository.save(result);
        return result;
    }
}
