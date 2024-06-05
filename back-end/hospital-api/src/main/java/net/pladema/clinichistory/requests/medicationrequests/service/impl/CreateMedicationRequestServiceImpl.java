package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgh.clinichistory.application.document.validators.DosageValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.PatientInfoValidator;
import net.pladema.clinichistory.hospitalization.service.documents.validation.SnomedValidator;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.entity.MedicationRequest;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.DocumentRequestBo;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CreateMedicationRequestServiceImpl implements CreateMedicationRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateMedicationRequestServiceImpl.class);

    private final MedicationRequestRepository medicationRequestRepository;

    private final DocumentFactory documentFactory;

    private final HealthConditionService healthConditionService;

	private final FeatureFlagsService featureFlagsService;

    public CreateMedicationRequestServiceImpl(MedicationRequestRepository medicationRequestRepository,
                                              DocumentFactory documentFactory,
                                              HealthConditionService healthConditionService,
											  FeatureFlagsService featureFlagsService) {
        this.medicationRequestRepository = medicationRequestRepository;
        this.documentFactory = documentFactory;
        this.healthConditionService = healthConditionService;
		this.featureFlagsService = featureFlagsService;
    }

    @Override
    public List<DocumentRequestBo> execute(MedicationRequestBo medicationRequest) {
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

        Map<Integer, LocalDate> newMRIds = createMedicationRequest(medicationRequest);
		List<DocumentRequestBo> result = new ArrayList<>();
		newMRIds.forEach((key, value) -> {
			medicationRequest.setEncounterId(key);
			medicationRequest.setRequestDate(value);
			medicationRequest.getMedications().forEach(medication -> {
				medication.setPrescriptionDate(value);
				medication.setDueDate(value.plusDays(30));
				medication.setId(null);
				medication.getHealthCondition().setSnomed(healthConditionService.getHealthCondition(medication.getHealthCondition().getId()).getSnomed());
			});
			Long documentId = documentFactory.run(medicationRequest, true);
			result.add(new DocumentRequestBo(key,documentId));
		});
        return result;
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

    private Map<Integer, LocalDate> createMedicationRequest(MedicationRequestBo medicationRequest) {
		Map<Integer, LocalDate> medicationRequestIds = new HashMap<>();
		LocalDate iterationDate = LocalDate.now();
		if (featureFlagsService.isOn(AppFeature.HABILITAR_RECETA_DIGITAL)) {
			generateMultipleMedicationRequests(medicationRequest, medicationRequestIds, iterationDate);
		}
		else {
			MedicationRequest result = generateBasicMedicationrequest(medicationRequest);
			result.setRequestDate(LocalDate.now());
			UUID randomUuid = UUID.randomUUID();
			result.setUuid(randomUuid);
			medicationRequest.setUuid(randomUuid);
			result = medicationRequestRepository.save(result);
			medicationRequestIds.put(result.getId(), medicationRequest.getRequestDate());
		}
        return medicationRequestIds;
    }

	private void generateMultipleMedicationRequests(MedicationRequestBo medicationRequest, Map<Integer, LocalDate> originalMedicationRequestId, LocalDate iterationDate) {
		for (int currentRequest = 0; currentRequest < medicationRequest.getRepetitions() + 1; currentRequest++) {
			MedicationRequest result = generateBasicMedicationrequest(medicationRequest);
			result.setClinicalSpecialtyId(medicationRequest.getClinicalSpecialtyId());
			result.setRepetitions(currentRequest == 0 ? medicationRequest.getRepetitions() : 0);
			result.setIsPostDated(currentRequest == 0);
			result.setRequestDate(iterationDate);
			UUID randomUuid = UUID.randomUUID();
			result.setUuid(randomUuid);
			medicationRequest.setUuid(randomUuid);
			result = medicationRequestRepository.save(result);
			originalMedicationRequestId.put(result.getId(), iterationDate);
			iterationDate = iterationDate.plusDays(30);
		}
	}

	private static MedicationRequest generateBasicMedicationrequest(MedicationRequestBo medicationRequest) {
		MedicationRequest result = new MedicationRequest();
		result.setPatientId(medicationRequest.getPatientId());
		result.setInstitutionId(medicationRequest.getInstitutionId());
		result.setMedicalCoverageId(medicationRequest.getMedicalCoverageId());
		result.setDoctorId(medicationRequest.getDoctorId());
		result.setHasRecipe(medicationRequest.isHasRecipe());
		result.setIsArchived(medicationRequest.getIsArchived());
		return result;
	}

}
