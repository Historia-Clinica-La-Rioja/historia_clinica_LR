package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.DosageValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.PatientInfoValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.SnomedValidator;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.entity.MedicationRequest;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CreateMedicationRequestServiceImpl implements CreateMedicationRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateMedicationRequestServiceImpl.class);

    private final MedicationRequestRepository medicationRequestRepository;

    private final DocumentFactory documentFactory;

    private final HealthConditionService healthConditionService;

    public CreateMedicationRequestServiceImpl(MedicationRequestRepository medicationRequestRepository,
                                              DocumentFactory documentFactory,
                                              HealthConditionService healthConditionService) {
        this.medicationRequestRepository = medicationRequestRepository;
        this.documentFactory = documentFactory;
        this.healthConditionService = healthConditionService;
    }

    @Override
    public Integer execute(MedicationRequestBo medicationRequest) {
        LOG.debug("Input parameters -> medicationRequest {} ", medicationRequest);
        assertRequiredFields(medicationRequest);
        assertNoDuplicatedMedications(medicationRequest);

        Map<Integer, HealthConditionBo> healthConditionMap = healthConditionService.getLastHealthCondition(
                medicationRequest.getPatientInfo().getId(),
                medicationRequest.getMedications().stream()
                        .map(MedicationBo::getHealthCondition)
                        .map(HealthConditionBo::getId).collect(Collectors.toList()));

        medicationRequest.getMedications().forEach(medicationBo ->
                medicationBo.setHealthCondition(
                        healthConditionMap.get(medicationBo.getHealthCondition().getId())));

        medicationRequest.getMedications().forEach(md ->
                Assert.isTrue(md.getHealthCondition().isActive(),
                        "El problema asociado tiene que estar activo"));

        MedicationRequest newMR = createMedicationRequest(medicationRequest);
        medicationRequest.setEncounterId(newMR.getId());
        documentFactory.run(medicationRequest, false);

        return newMR.getId();
    }

    private void assertRequiredFields(MedicationRequestBo medicationRequest) {
        Assert.notNull(medicationRequest, "La receta es obligatoria");
        Assert.notNull(medicationRequest.getInstitutionId(), "El identificador de la institución es obligatorio");
        PatientInfoValidator patientInfoValidator = new PatientInfoValidator();
        patientInfoValidator.isValid(medicationRequest.getPatientInfo());
        Assert.notNull(medicationRequest.getDoctorId(), "El identificador del médico es obligatorio");
        Assert.notEmpty(medicationRequest.getMedications(), "La receta tiene que tener asociada al menos una medicación");

        SnomedValidator snomedValidator = new SnomedValidator();
        DosageValidator dosageValidator = new DosageValidator();
        medicationRequest.getMedications().forEach(md -> {
            snomedValidator.isValid(md.getSnomed());
            Assert.notNull(md.getHealthCondition(), "La medicación tiene que estar asociada a un problema");
            Assert.notNull(md.getHealthCondition().getId(), "La medicación tiene que estar asociada a un problema");
            dosageValidator.isValid(md.getDosage());
        });
    }

    private void assertNoDuplicatedMedications(MedicationRequestBo medicationRequestBo) {
        Map<Pair<Integer, String>, List<MedicationBo>> result = medicationRequestBo.getMedications()
                .stream()
                .collect(Collectors.groupingBy(p -> Pair.of(p.getHealthCondition().getId(), p.getSnomed().getSctid())));
        result.forEach((k,v) -> Assert.isTrue(v.size() == 1, "La receta no puede contener más de un medicamento con el mismo problema y el mismo concepto snomed"));
    }

    private MedicationRequest createMedicationRequest(MedicationRequestBo medicationRequest) {
        MedicationRequest result = new MedicationRequest();
        result.setPatientId(medicationRequest.getPatientId());
        result.setInstitutionId(medicationRequest.getInstitutionId());
        result.setMedicalCoverageId(medicationRequest.getMedicalCoverageId());
        result.setDoctorId(medicationRequest.getDoctorId());
        result.setHasRecipe(medicationRequest.isHasRecipe());
        result = medicationRequestRepository.save(result);
        return result;
    }
}
