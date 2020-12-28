package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.domain.DosageBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CreateMedicationRequestServiceImplTest {

	private CreateMedicationRequestService createMedicationRequestService;

	@Autowired
	private MedicationRequestRepository medicationRequestRepository;

	@MockBean
	private DocumentFactory documentFactory;

	@Before
	public void setUp() {
		createMedicationRequestService = new CreateMedicationRequestServiceImpl(medicationRequestRepository, documentFactory);
	}

	@Test
	public void execute_withNullInstitution(){
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(null, null)
		);
		String expectedMessage = "El identificador de la institución es obligatorio";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withNullMedicationRequest(){
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
			createMedicationRequestService.execute(1, null)
		);
		String expectedMessage = "La receta es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withNullPatient(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "La información del paciente es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withNullDoctorId(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "El identificador del médico es obligatorio";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withEmptyMedications(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "La receta tiene que tener asociada al menos una medicación";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withInvalidMedication_startDate(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);

		MedicationBo medication = new MedicationBo();
		HealthConditionBo hc = new HealthConditionBo();
		hc.setId(1);
		medication.setHealthCondition(hc);
		medication.setSnomed(new SnomedBo("12312", "ANGINA"));

		DosageBo dosage = new DosageBo();
		dosage.setStartDate(null);
		medication.setDosage(dosage);

		medicationRequest.setMedications(List.of(medication));
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "La fecha de comienzo para tomar la medicación es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_withInvalidMedication_Snomed(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);

		MedicationBo medication = new MedicationBo();
		medication.setSnomed(null);
		medicationRequest.setMedications(List.of(medication));
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		String expectedMessage = "La terminología snomed es obligatoria";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));

		medication.setSnomed(new SnomedBo(null, "ANGINA"));
		medicationRequest.setMedications(List.of(medication));
		exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		expectedMessage = "El código identificador de snomed es obligatorio";
		actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));

		medication.setSnomed(new SnomedBo("12314124", null));
		medicationRequest.setMedications(List.of(medication));
		exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
		);
		expectedMessage = "El termino preferido de snomed es obligatorio";
		actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	public void execute_withInvalidMedication_HealthCondition_Snomed(){
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setPatientInfo(new PatientInfoBo(1, (short)1, (short)29));
		medicationRequest.setDoctorId(1);
		medicationRequest.setMedicalCoverageId(5);

		MedicationBo medication = new MedicationBo();
		medication.setSnomed(new SnomedBo("12314124", "IBUPROFENO 500 mg"));
		medication.setHealthCondition(null);
		medicationRequest.setMedications(List.of(medication));
		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
				createMedicationRequestService.execute(1, medicationRequest)
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
				createMedicationRequestService.execute(1, medicationRequest)
		);
		expectedMessage = "La medicación tiene que estar asociada a un problema";
		actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void execute_success() {
		Integer institutionId = 5;
		MedicationRequestBo medicationRequest = new MedicationRequestBo();
		medicationRequest.setDoctorId(1);
		medicationRequest.setPatientInfo(new PatientInfoBo(4, (short)1, (short)29));
		medicationRequest.setMedicalCoverageId(5);
		medicationRequest.setMedications(List.of(createMedicationBo("IBUPROFENO 500", 13, createDosageBo(15d, 8, EUnitsOfTimeBo.HOUR))));
		Integer medicationRequestId = createMedicationRequestService.execute(institutionId, medicationRequest);

		Assertions.assertEquals(1, medicationRequestRepository.count());
		Assertions.assertNotNull(medicationRequestId);
	}

	private DosageBo createDosageBo(Double duration, Integer frequency, EUnitsOfTimeBo unitsOfTimeBo) {
		DosageBo result = new DosageBo();
		result.setStartDate(LocalDate.now());
		result.setDuration(duration);
		result.setFrequency(frequency);
		result.setPeriodUnit(unitsOfTimeBo);
		return result;
	}

	private MedicationBo createMedicationBo(String sctid, Integer healthConditionId, DosageBo dosage) {
		MedicationBo result = new MedicationBo();
		result.setSnomed(new SnomedBo(sctid, sctid));
		HealthConditionBo hc = new HealthConditionBo();
		hc.setId(healthConditionId);
		result.setHealthCondition(hc);
		result.setDosage(dosage);
		result.setNote("Probando");
		return result;
	}

}
