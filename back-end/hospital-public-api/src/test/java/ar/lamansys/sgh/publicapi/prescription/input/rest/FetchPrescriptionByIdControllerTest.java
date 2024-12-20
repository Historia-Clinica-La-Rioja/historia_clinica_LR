package ar.lamansys.sgh.publicapi.prescription.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.lamansys.sgh.publicapi.prescription.application.fetchprescriptionbyidanddniv3.FetchPrescriptionsByIdAndDniV3;
import ar.lamansys.sgh.publicapi.prescription.domain.SuggestedCommercialMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.SuggestedCommercialMedicationDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.prescription.application.fetchprescriptionsbyidanddni.FetchPrescriptionsByIdAndDni;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionIdentifier;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionStorage;
import ar.lamansys.sgh.publicapi.prescription.domain.CommercialMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.GenericMedicationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.InstitutionPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PatientPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionLineBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProblemBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProfessionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProfessionalRegistrationBo;
import ar.lamansys.sgh.publicapi.prescription.domain.ProfessionalPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.BadPrescriptionIdFormatException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionRequestException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.PrescriptionPublicApiPermissions;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.FetchPrescriptionByIdController;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.CommercialMedicationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.GenericMedicationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.InstitutionPrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PatientPrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionLineDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionProblemDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionProfessionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionProfessionalRegistrationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ProfessionalPrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.PrescriptionRequestAccessDeniedException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.mapper.PrescriptionMapper;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

@ExtendWith(MockitoExtension.class)
public class FetchPrescriptionByIdControllerTest {

	private FetchPrescriptionByIdController fetchPrescriptionByIdController;

	@Mock
	private PrescriptionStorage prescriptionStorage;

	@Mock
	private PrescriptionPublicApiPermissions prescriptionPublicApiPermissions;

	@Mock
	private LocalDateMapper localDateMapper;

	@BeforeEach
	public void setup() {
		PrescriptionMapper prescriptionMapper = new PrescriptionMapper(localDateMapper);
		FetchPrescriptionsByIdAndDni fetchPrescriptionsByIdAndDni = new FetchPrescriptionsByIdAndDni(prescriptionStorage);
		FetchPrescriptionsByIdAndDniV3 fetchPrescriptionsByIdAndDniV3 = new FetchPrescriptionsByIdAndDniV3(prescriptionStorage);
		this.fetchPrescriptionByIdController = new FetchPrescriptionByIdController(fetchPrescriptionsByIdAndDni, fetchPrescriptionsByIdAndDniV3, prescriptionMapper, prescriptionPublicApiPermissions);
	}

	@Test
	void successFetchPrescriptionByIdAndDniWithoutEmptyObjects() {
		allowAccessPermission(true);
		when(localDateMapper.fromLocalDateTime(any())).thenReturn(LocalDate.now());
		var patientPrescriptionBo = new PatientPrescriptionBo(
				"Juan Paciente",
				"Test",
				"Juan",
				"M",
				"Masculino",
				LocalDate.now().minusYears(30),
				"DNI",
				"33333333",
				"OSDE",
				"3332323232",
				"Platino",
				"325",
				"Argentina",
				"Buenos Aires",
				"Tandil",
				"Tandil",
				"Calle",
				"123"
		);
		var institutionPrescriptionBo = new InstitutionPrescriptionBo(
				"Test Institution",
				"331203812903",
				"4",
				"Calle Test"
		);
		var commercialMedicationBo = new CommercialMedicationBo(
				"TestComercial",
				"12344444"
		);

		var mockedPrescription = fabricatePrescription(
				patientPrescriptionBo,
				institutionPrescriptionBo,
				commercialMedicationBo
		);

		when(prescriptionStorage.getPrescriptionByIdAndDni(any(), any())).thenReturn(
				Optional.of(mockedPrescription));

		var result = fetchPrescriptionByIdController.prescriptionRequest("0-100", "1");
		Assertions.assertNotNull(result);
		Assertions.assertEquals(mapToPatientPrescriptionBo(result.getPatientPrescriptionDto()), patientPrescriptionBo);
		Assertions.assertEquals(mockedPrescription.getPrescriptionsLineBo().get(0).getPrescriptionProblemBo(),
				mapToPrescriptionProblemBo(result.getPrescriptionsLineDto().get(0).getPrescriptionProblemDto()));
		Assertions.assertEquals(mockedPrescription.getProfessionalPrescriptionBo().getRegistrations().get(0),
				mapToPrescriptionProfessionalRegistrationDto(result.getProfessionalPrescriptionDto().getRegistrations().get(0)));
		Assertions.assertEquals(mockedPrescription.getProfessionalPrescriptionBo().getProfessions().get(0),
				mapToPrescriptionProfessionDto(result.getProfessionalPrescriptionDto().getProfessions().get(0)));
		Assertions.assertEquals(mockedPrescription.getPrescriptionsLineBo().get(0).getCommercialMedicationBo(),
				mapToCommercialMedicationBo(result.getPrescriptionsLineDto().get(0).getCommercialMedicationDto()));
		Assertions.assertEquals(mockedPrescription.getPrescriptionsLineBo().get(0).getGenericMedicationBo(),
				mapToGenericMedicationBo(result.getPrescriptionsLineDto().get(0).getGenericMedicationDto()));
		Assertions.assertEquals(mockedPrescription.getPatientPrescriptionBo(),
				mapToPatientPrescriptionBo(result.getPatientPrescriptionDto()));
		Assertions.assertEquals(mockedPrescription.getProfessionalPrescriptionBo(),
				mapToProfessionalPrescriptionBo(result.getProfessionalPrescriptionDto()));
		Assertions.assertEquals(mockedPrescription.getPrescriptionsLineBo(),
				mapToPrescriptionsLineBo(result.getPrescriptionsLineDto()));
		Assertions.assertEquals(mockedPrescription.getInstitutionPrescriptionBo(),
				mapToInstitutionPrescriptionBo(result.getInstitutionPrescriptionDto()));
		Assertions.assertEquals(mockedPrescription, mapToPrescription(result));

	}

	@Test
	void successFetchPrescriptionByIdAndDniWithEmptyObjects() {
		var mockedPrescription = fabricatePrescription(
				new PatientPrescriptionBo(),
				new InstitutionPrescriptionBo(),
				new CommercialMedicationBo()
		);
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(true);
		when(localDateMapper.fromLocalDateTime(any())).thenReturn(LocalDate.now());
		when(prescriptionStorage.getPrescriptionByIdAndDni(any(), any())).thenReturn(
				Optional.of(mockedPrescription));

		var result = fetchPrescriptionByIdController.prescriptionRequest("0-100", "1");
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getPatientPrescriptionDto().getName(), new PatientPrescriptionDto().getName());
	}

	@Test
	void failDomainNumberParsing(){
		allowAccessPermission(true);
		TestUtils.shouldThrow(BadPrescriptionIdFormatException.class,
				() -> fetchPrescriptionByIdController.prescriptionRequest("2-0-100", "1"));
	}

	@Test
	void failDomainNumberNotMatching(){
		allowAccessPermission(true);
		TestUtils.shouldThrow(PrescriptionNotFoundException.class,
				() -> fetchPrescriptionByIdController.prescriptionRequest("89562-100", "1"));
	}

	@Test
	void failPrescriptionNotExistsException(){
		allowAccessPermission(true);
		when(prescriptionStorage.getPrescriptionByIdAndDni(new PrescriptionIdentifier("0-100", 1), "1"))
				.thenReturn(Optional.empty());
		TestUtils.shouldThrow(PrescriptionRequestException.class,
				() -> fetchPrescriptionByIdController.prescriptionRequest("0-100", "1"));
	}

	@Test
	void failAccessDeniedException() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(PrescriptionRequestAccessDeniedException.class,
				() -> fetchPrescriptionByIdController.prescriptionRequest("0-100", "1"));
	}

	@Test
	void failDomainNumberParsingV3(){
		allowAccessPermission(true);
		TestUtils.shouldThrow(BadPrescriptionIdFormatException.class,
				() -> fetchPrescriptionByIdController.prescriptionRequestV3("2-0-100", "1"));
	}

	@Test
	void failDomainNumberNotMatchingV3(){
		allowAccessPermission(true);
		TestUtils.shouldThrow(PrescriptionNotFoundException.class,
				() -> fetchPrescriptionByIdController.prescriptionRequestV3("89562-100", "1"));
	}

	@Test
	void failAccessDeniedExceptionV3() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(PrescriptionRequestAccessDeniedException.class,
				() -> fetchPrescriptionByIdController.prescriptionRequestV3("0-100", "1"));
	}

	private void allowAccessPermission(boolean canAccess) {
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(canAccess);
	}


	private PrescriptionBo fabricatePrescription(PatientPrescriptionBo patientPrescriptionBo,
										 InstitutionPrescriptionBo institutionPrescriptionBo,
										 CommercialMedicationBo commercialMedicationBo) {
		return new PrescriptionBo(
				"1",
				"100",
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(30),
				"link",
				false,
				patientPrescriptionBo,
				institutionPrescriptionBo,
				new ProfessionalPrescriptionBo(
						"Juan Médico",
						"Test",
						"DNI",
						"22222222",
						"123 132 132",
						"testmedico@mail.com",
						List.of(new PrescriptionProfessionBo(
								"Médico",
								"1111111"
						)),
						List.of(new PrescriptionProfessionalRegistrationBo(
								"1",
								"1"
						))
				),
				List.of(new PrescriptionLineBo(
						1,
						"Status",
						new PrescriptionProblemBo(
								"Dolor general",
								"1",
								"1"
						),
						new GenericMedicationBo(
									"TestGenerico",
								"123123"
						),
						new SuggestedCommercialMedicationBo("SuggestedCommercialMedication", "987654321"),
						commercialMedicationBo,
						1.0,
						1.0,
						2.0,
						"Caja",
						(short) 2,
						(short) 12,
						2.5,
						"comprimido"
				))
		);

	}

	private PrescriptionProfessionalRegistrationBo mapToPrescriptionProfessionalRegistrationDto(PrescriptionProfessionalRegistrationDto prescriptionProfessionalRegistration) {
		return new PrescriptionProfessionalRegistrationBo(
				prescriptionProfessionalRegistration.getRegistrationNumber(),
				prescriptionProfessionalRegistration.getRegistrationType());
	}

	private PrescriptionProblemBo mapToPrescriptionProblemBo(PrescriptionProblemDto prescriptionProblemDto){
		return new PrescriptionProblemBo(
			prescriptionProblemDto.getPt(), prescriptionProblemDto.getSnomedId(), prescriptionProblemDto.getProblemType()
		);
	}

	private PrescriptionProfessionBo mapToPrescriptionProfessionDto(PrescriptionProfessionDto prescriptionProfessionDto) {
		return new PrescriptionProfessionBo(
				prescriptionProfessionDto.getProfession(),
				prescriptionProfessionDto.getSnomedId()
		);
	}

	private CommercialMedicationBo mapToCommercialMedicationBo(CommercialMedicationDto commercialMedicationDto) {
		return new CommercialMedicationBo(
				commercialMedicationDto.getName(),
				commercialMedicationDto.getSnomedId()
		);
	}

	private GenericMedicationBo mapToGenericMedicationBo(GenericMedicationDto genericMedicationDto) {
		return new GenericMedicationBo(
				genericMedicationDto.getName(),
				genericMedicationDto.getSnomedId()
		);
	}

	private PatientPrescriptionBo mapToPatientPrescriptionBo(PatientPrescriptionDto patientPrescriptionDto) {
		return new PatientPrescriptionBo(
				patientPrescriptionDto.getName(),
				patientPrescriptionDto.getLastName(),
				patientPrescriptionDto.getSelfPerceivedName(),
				patientPrescriptionDto.getDniSex(),
				patientPrescriptionDto.getGender(),
				patientPrescriptionDto.getBirthDate(),
				patientPrescriptionDto.getIdentificationType(),
				patientPrescriptionDto.getIdentificationNumber(),
				patientPrescriptionDto.getMedicalCoverage(),
				patientPrescriptionDto.getMedicalCoverageCuit(),
				patientPrescriptionDto.getMedicalCoveragePlan(),
				patientPrescriptionDto.getAffiliateNumber(),
				patientPrescriptionDto.getCountry(),
				patientPrescriptionDto.getProvince(),
				patientPrescriptionDto.getDepartment(),
				patientPrescriptionDto.getCity(),
				patientPrescriptionDto.getStreet(),
				patientPrescriptionDto.getStreetNumber()
		);
	}

	private ProfessionalPrescriptionBo mapToProfessionalPrescriptionBo(ProfessionalPrescriptionDto professionalPrescriptionDto){
		return new ProfessionalPrescriptionBo(
			professionalPrescriptionDto.getName(),
			professionalPrescriptionDto.getLastName(),
			professionalPrescriptionDto.getIdentificationType(),
			professionalPrescriptionDto.getIdentificationNumber(),
			professionalPrescriptionDto.getPhoneNumber(),
			professionalPrescriptionDto.getEmail(),
			mapToListPrescriptionProfessionBo(professionalPrescriptionDto.getProfessions()),
			mapToListPrescriptionProfessionalRegistrationBo(professionalPrescriptionDto.getRegistrations())
		);
	}

	private List<PrescriptionProfessionBo> mapToListPrescriptionProfessionBo(List<PrescriptionProfessionDto> professions) {
		return professions.stream().map(this::mapToPrescriptionProfessionDto).collect(Collectors.toList());
	}

	private List<PrescriptionProfessionalRegistrationBo> mapToListPrescriptionProfessionalRegistrationBo(List<PrescriptionProfessionalRegistrationDto> prescriptionProfessionalRegistrations) {
		return prescriptionProfessionalRegistrations.stream().map(this::mapToPrescriptionProfessionalRegistrationDto).collect(Collectors.toList());
	}

	private List<PrescriptionLineBo> mapToPrescriptionsLineBo(List<PrescriptionLineDto> prescriptionsLineDto) {
		return prescriptionsLineDto.stream().map(this::mapToPrescriptionLineBo).collect(Collectors.toList());
	}

	private PrescriptionLineBo mapToPrescriptionLineBo(PrescriptionLineDto prescriptionLineDto){
		return new PrescriptionLineBo(
			prescriptionLineDto.getPrescriptionLineNumber(),
			prescriptionLineDto.getPrescriptionLineStatus(),
			mapToPrescriptionProblemBo(prescriptionLineDto.getPrescriptionProblemDto()),
			mapToGenericMedicationBo(prescriptionLineDto.getGenericMedicationDto()),
			mapToSuggestedCommercialMedicationBo(prescriptionLineDto.getSuggestedCommercialMedicationDto()),
			mapToCommercialMedicationBo(prescriptionLineDto.getCommercialMedicationDto()),
			prescriptionLineDto.getUnitDosis(),
			prescriptionLineDto.getDayDosis(),
			prescriptionLineDto.getDuration(),
			prescriptionLineDto.getPresentation(),
			prescriptionLineDto.getPresentationQuantity(),
			prescriptionLineDto.getPresentationPackageQuantity(),
			prescriptionLineDto.getQuantity()
		);
	}

	private SuggestedCommercialMedicationBo mapToSuggestedCommercialMedicationBo(SuggestedCommercialMedicationDto suggestedCommercialMedicationDto) {
		return new SuggestedCommercialMedicationBo(suggestedCommercialMedicationDto.getName(), suggestedCommercialMedicationDto.getSnomedId());
	}

	private InstitutionPrescriptionBo mapToInstitutionPrescriptionBo(InstitutionPrescriptionDto institutionPrescriptionDto) {
		return new InstitutionPrescriptionBo(
			institutionPrescriptionDto.getName(),
			institutionPrescriptionDto.getSisaCode(),
			institutionPrescriptionDto.getProvinceCode(),
			institutionPrescriptionDto.getAddress()
		);
	}

	private PrescriptionBo mapToPrescription(PrescriptionDto result) {
		return new PrescriptionBo(
				result.getDomain(),
				result.getPrescriptionId(),
				result.getPrescriptionDate(),
				result.getDueDate(),
				result.getLink(),
				result.getIsArchived(),
				mapToPatientPrescriptionBo(result.getPatientPrescriptionDto()),
				mapToInstitutionPrescriptionBo(result.getInstitutionPrescriptionDto()),
				mapToProfessionalPrescriptionBo(result.getProfessionalPrescriptionDto()),
				mapToPrescriptionsLineBo(result.getPrescriptionsLineDto())
		);
	}

}
