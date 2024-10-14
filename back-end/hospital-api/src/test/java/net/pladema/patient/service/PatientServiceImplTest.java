package net.pladema.patient.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EAuditType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.UnitRepository;
import net.pladema.audit.repository.HospitalAuditRepository;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.federar.services.FederarService;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.AuditablePatientRepository;
import net.pladema.patient.repository.MergedInactivePatientRepository;
import net.pladema.patient.repository.MergedPatientRepository;
import net.pladema.patient.repository.PatientAuditRepository;
import net.pladema.patient.repository.PatientHistoryRepository;
import net.pladema.patient.repository.PatientMedicalCoverageRepository;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.PatientRepositoryImpl;
import net.pladema.patient.repository.PatientTypeRepository;
import net.pladema.patient.repository.PrivateHealthInsuranceDetailsRepository;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.impl.PatientServiceImpl;
import net.pladema.patient.repository.MedicalCoverageRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.repository.entity.PersonExtended;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class PatientServiceImplIntegrationTest extends UnitRepository {

    @Autowired
    private PatientRepository patientRepository;

    @Mock
    private FederarService federarService;

    @Mock
    private PatientMedicalCoverageRepository patientMedicalCoverageRepository;

    @Mock
    private MedicalCoverageRepository medicalCoverageRepository;

    @Mock
    private PrivateHealthInsuranceDetailsRepository privateHealthInsuranceDetailsRepository;

    @Mock
    private HospitalAuditRepository hospitalAuditRepository;

    @Mock
    private PatientAuditRepository patientAuditRepository;

    private PatientService patientService;

	@Mock
    private FeatureFlagsService featureFlagsService;

	@Mock
	private AuditablePatientRepository auditablePatientRepository;

	@Mock
	private LocalDateMapper localDateMapper;

	@Mock
	private PatientTypeRepository patientTypeRepository;
    
	@MockBean
	private DocumentFileRepository documentFileRepository;

	@Mock
	private EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	@Mock
	private InternmentEpisodeRepository internmentEpisodeRepository;

	@Mock
	private AppointmentRepository appointmentRepository;
	@Mock
    private PatientHistoryRepository patientHistoryRepository;

    @Mock
    private MergedPatientRepository mergedPatientRepository;

	@Mock
	private MergedInactivePatientRepository mergedInactivePatientRepository;

	@Mock
	private PatientRepositoryImpl patientRepositoryCustom;

	@BeforeEach
    void setUp(){
        patientService = new PatientServiceImpl(
                patientRepository,
                patientMedicalCoverageRepository,
                medicalCoverageRepository,
                privateHealthInsuranceDetailsRepository,
                federarService,
                hospitalAuditRepository,
                patientAuditRepository,
                featureFlagsService,
				auditablePatientRepository,
				localDateMapper, patientTypeRepository,
				internmentEpisodeRepository,
				emergencyCareEpisodeRepository,
				appointmentRepository,
				patientHistoryRepository,
                mergedPatientRepository,
				mergedInactivePatientRepository,
				patientRepositoryCustom);
    }

    @Test
    void test_searchPatientOptionalFilters_limitedResultSize(){
        populatePacientRepository(1534);
		when(featureFlagsService.isOn(any())).thenReturn(true);
        PatientSearchFilter patientSearchFilter = new PatientSearchFilter();
        patientSearchFilter.setIdentificationTypeId((short)1);
		Pageable pageable = initializePageable();
        var result = patientService.searchPatientOptionalFilters(patientSearchFilter, pageable);
        Assertions.assertThat(result.getContent())
                .hasSize(1);
        Assertions.assertThat(result.getTotalElements())
                .isEqualTo(1534);
    }

	private PageRequest initializePageable() {
		return PageRequest.of(0, 1);
	}

	@Test
    void test_searchPatientOptionalFilters_resultSizeLowerThanMax(){
        populatePacientRepository(49);
		when(featureFlagsService.isOn(any())).thenReturn(true);
        PatientSearchFilter patientSearchFilter = new PatientSearchFilter();
        patientSearchFilter.setIdentificationTypeId((short)1);
		Pageable pageable = initializePageable();
        var result = patientService.searchPatientOptionalFilters(patientSearchFilter, pageable);
        Assertions.assertThat(result.getContent())
                .hasSize(1);
        Assertions.assertThat(result.getTotalElements())
                .isEqualTo(49);
    }

    private void populatePacientRepository(Integer nbPatients) {

        PatientType patientType = new PatientType((short)1,"", true, true);
        Short patientTypeId = save(patientType).getId();
        for (int i=0; i < nbPatients; i++){

            Person mockedPerson = new Person();
            mockedPerson.setFirstName("Juan");
            mockedPerson.setLastName("Gonzalesz");
            mockedPerson.setIdentificationTypeId((short)1);
            mockedPerson.setIdentificationNumber(Integer.toString(i));
            mockedPerson.setGenderId((short)0);
            Integer mockedPersonId = save(mockedPerson).getId();


            Patient mockedPatient = new Patient();
            mockedPatient.setPersonId(mockedPersonId);
            mockedPatient.setTypeId(patientTypeId);
			mockedPatient.setAuditTypeId(EAuditType.UNAUDITED.getId());
            save(mockedPatient);

            PersonExtended mockedPersonExtended = new PersonExtended();
            mockedPersonExtended.setId(mockedPersonId);
            save(mockedPersonExtended);
        }
    }
}
