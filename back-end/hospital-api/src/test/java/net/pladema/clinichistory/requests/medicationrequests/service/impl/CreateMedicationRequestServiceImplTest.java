package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionNewConsultationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class CreateMedicationRequestServiceImplTest extends UnitRepository {

	private CreateMedicationRequestService createMedicationRequestService;

	@Autowired
	private MedicationRequestRepository medicationRequestRepository;

	@Mock
	private DocumentFactory documentFactory;

	@Mock
	private HealthConditionService healthConditionService;

	@BeforeEach
	void setUp() {
		createMedicationRequestService = new CreateMedicationRequestServiceImpl(medicationRequestRepository, documentFactory, healthConditionService);
	}

	@Test
	void execute_withNullInstitution(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setInstitutionId(null);
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		String expectedMessage = "El identificador de la institución es obligatorio";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void execute_withNullMedicationRequest(){
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
			createMedicationRequestService.execute(null)
		);
		String expectedMessage = "La receta es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void execute_withNullPatient(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setInstitutionId(1);
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		String expectedMessage = "La información del paciente es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void execute_withNullDoctorId(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setInstitutionId(1);
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		String expectedMessage = "El identificador del médico es obligatorio";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void execute_withEmptyMedications(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setInstitutionId(1);
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		String expectedMessage = "La receta tiene que tener asociada al menos una medicación";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void execute_withInvalidMedication_startDate(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);
		medicationRequest.setInstitutionId(1);

		MedicationBo medication = new MedicationBo();
		HealthConditionBo hc = new HealthConditionBo();
		hc.setId(1);
		medication.setHealthCondition(hc);
		medication.setSnomed(new SnomedBo("12312", "ANGINA"));

		DosageBo dosage = new DosageBo();
		dosage.setStartDate(null);
		medication.setDosage(dosage);

		medicationRequest.setMedications(List.of(medication));
		when(healthConditionService.getHealthCondition(any())).thenReturn(mockActiveHealthCondition());

		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		String expectedMessage = "La fecha de comienzo para tomar la medicación es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void execute_withInvalidMedication_Snomed(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);
		medicationRequest.setInstitutionId(1);

		MedicationBo medication = new MedicationBo();
		medication.setSnomed(null);
		medicationRequest.setMedications(List.of(medication));
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		String expectedMessage = "La terminología snomed es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));

		medication.setSnomed(new SnomedBo(null, "ANGINA"));
		medicationRequest.setMedications(List.of(medication));
		exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		expectedMessage = "El código identificador de snomed es obligatorio";
		actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));

		medication.setSnomed(new SnomedBo("12314124", null));
		medicationRequest.setMedications(List.of(medication));
		exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		expectedMessage = "El termino preferido de snomed es obligatorio";
		actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void execute_withInvalidMedication_HealthCondition_Snomed(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setInstitutionId(1);
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);


		MedicationBo medication = new MedicationBo();
		medication.setSnomed(new SnomedBo("12314124", "IBUPROFENO 500 mg"));
		medication.setHealthCondition(null);
		medicationRequest.setMedications(List.of(medication));
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		String expectedMessage = "La medicación tiene que estar asociada a un problema";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));

		medication = new MedicationBo();
		medication.setSnomed(new SnomedBo("12314124", "IBUPROFENO 500 mg"));
		HealthConditionBo hc = new HealthConditionBo();
		hc.setId(null);
		medication.setHealthCondition(hc);
		medicationRequest.setMedications(List.of(medication));
		exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		expectedMessage = "La medicación tiene que estar asociada a un problema";
		actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void execute_withNewerSolvedHeathCondition(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setInstitutionId(5);
		medicationRequest.setDoctorId(1);
		medicationRequest.setPatientInfo(new PatientInfoBo(4, (short)1, (short)29));
		medicationRequest.setMedicalCoverageId(5);
		medicationRequest.setMedications(List.of(
				createMedicationBo(
						"IBUPROFENO 500",
						1,
						ConditionClinicalStatus.ACTIVE,
						createDosageBo(15d, 8, EUnitsOfTimeBo.HOUR))));

		when(healthConditionService.getLastHealthCondition(any(), any())).thenReturn(mockHealthConditionMapWithNewerHealthCondition());

		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		String expectedMessage = "El problema asociado tiene que estar activo";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void execute_withDuplicatedStudy() {

		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setInstitutionId(5);
		medicationRequest.setDoctorId(1);
		medicationRequest.setPatientInfo(new PatientInfoBo(4, (short) 1, (short) 29));
		medicationRequest.setMedicalCoverageId(5);

		String repetedSnomedSctid = "IBUPROFENO 500";

		medicationRequest.setMedications(List.of(
				createMedicationBo(
						"Bayaspirina",
						1,
						ConditionClinicalStatus.ACTIVE,
						createDosageBo(30d, 1, EUnitsOfTimeBo.DAY)),
				createMedicationBo(
						repetedSnomedSctid,
						1,
						ConditionClinicalStatus.ACTIVE,
						createDosageBo(15d, 8, EUnitsOfTimeBo.HOUR)),
				createMedicationBo(
						repetedSnomedSctid,
						1,
						ConditionClinicalStatus.ACTIVE,
						createDosageBo(10d, 3, EUnitsOfTimeBo.HOUR))
		));


		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(medicationRequest)
		);
		String expectedMessage = "La receta no puede contener más de un medicamento con el mismo problema y el mismo concepto snomed";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void execute_success() {
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setInstitutionId(5);
		medicationRequest.setDoctorId(1);
		medicationRequest.setPatientInfo(new PatientInfoBo(4, (short)1, (short)29));
		medicationRequest.setMedicalCoverageId(5);
		medicationRequest.setMedications(List.of(
		        createMedicationBo(
		                "IBUPROFENO 500",
                        1,
                        ConditionClinicalStatus.ACTIVE,
                        createDosageBo(15d, 8, EUnitsOfTimeBo.HOUR)),
                createMedicationBo(
                        "CLONA 200",
                        1,
                        ConditionClinicalStatus.ACTIVE,
                        createDosageBo(7d, 12, EUnitsOfTimeBo.HOUR))));
		when(healthConditionService.getHealthCondition(any())).thenReturn(mockActiveHealthCondition());
		when(healthConditionService.getLastHealthCondition(any(), any())).thenReturn(mockHealthConditionMap());
		Integer medicationRequestId = createMedicationRequestService.execute(medicationRequest);

		Assertions.assertEquals(1, medicationRequestRepository.count());
		Assertions.assertNotNull(medicationRequestId);
	}

	private Map<Integer, HealthConditionBo> mockHealthConditionMap() {
		HealthConditionBo hc1 = new HealthConditionBo();
		hc1.setId(1);
		hc1.setStatusId(ConditionClinicalStatus.ACTIVE);
		List<HealthConditionBo> hcs = List.of(hc1);
		HashMap<Integer, HealthConditionBo> result = new HashMap<>();
		result.put(1,hc1);
		return result;
	}

	private Map<Integer, HealthConditionBo> mockHealthConditionMapWithNewerHealthCondition() {
		HealthConditionBo hc1 = new HealthConditionBo();
		hc1.setId(47);
		hc1.setStatusId(ConditionClinicalStatus.SOLVED);

		HashMap<Integer, HealthConditionBo> result = new HashMap<>();
		result.put(1,hc1);
		return result;
	}

	private HealthConditionNewConsultationBo mockActiveHealthCondition(){
		HealthCondition hc = new HealthCondition();
		hc.setStatusId(ConditionClinicalStatus.ACTIVE);
		return new HealthConditionNewConsultationBo(hc);
	}

	private HealthConditionNewConsultationBo mockSolvedHealthCondition(){
		HealthCondition hc = new HealthCondition();
		hc.setStatusId(ConditionClinicalStatus.SOLVED);
		return new HealthConditionNewConsultationBo(hc);
	}

	private DosageBo createDosageBo(Double duration, Integer frequency, EUnitsOfTimeBo unitsOfTimeBo) {
		DosageBo result = new DosageBo();
		result.setStartDate(LocalDateTime.now());
		result.setDuration(duration);
		result.setFrequency(frequency);
		result.setPeriodUnit(unitsOfTimeBo);
		return result;
	}

	private MedicationBo createMedicationBo(String sctid, Integer healthConditionId, String healthConditionStatus, DosageBo dosage) {
		MedicationBo result = new MedicationBo();
		result.setSnomed(new SnomedBo(sctid, sctid));
		HealthConditionBo hc = new HealthConditionBo();
		hc.setId(healthConditionId);
		hc.setStatusId(healthConditionStatus);
		result.setHealthCondition(hc);
		result.setDosage(dosage);
		result.setNote("Probando");
		return result;
	}

}
