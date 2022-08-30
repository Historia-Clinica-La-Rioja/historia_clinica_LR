package net.pladema.patient.service;

import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.UnitRepository;
import net.pladema.audit.repository.HospitalAuditRepository;
import net.pladema.federar.services.FederarService;
import net.pladema.patient.controller.dto.PatientSearchFilter;
import net.pladema.patient.repository.PatientAuditRepository;
import net.pladema.patient.repository.PatientMedicalCoverageRepository;
import net.pladema.patient.repository.PatientRepository;
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
                featureFlagsService
                );
    }

    @Test
    void test_searchPatientOptionalFilters_limitedResultSize(){
        populatePacientRepository(1534);
		when(featureFlagsService.isOn(any())).thenReturn(true);
        PatientSearchFilter patientSearchFilter = new PatientSearchFilter();
        patientSearchFilter.setIdentificationTypeId((short)1);
        var result = patientService.searchPatientOptionalFilters(patientSearchFilter);
        Assertions.assertThat(result.getPatientList())
                .hasSize(PatientServiceImpl.MAX_RESULT_SIZE);
        Assertions.assertThat(result.getActualPatientSearchSize())
                .isEqualTo(1534);
    }

    @Test
    void test_searchPatientOptionalFilters_resultSizeLowerThanMax(){
        populatePacientRepository(49);
		when(featureFlagsService.isOn(any())).thenReturn(true);
        PatientSearchFilter patientSearchFilter = new PatientSearchFilter();
        patientSearchFilter.setIdentificationTypeId((short)1);
        var result = patientService.searchPatientOptionalFilters(patientSearchFilter);
        Assertions.assertThat(result.getPatientList())
                .hasSize(49);
        Assertions.assertThat(result.getActualPatientSearchSize())
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
            save(mockedPatient);

            PersonExtended mockedPersonExtended = new PersonExtended();
            mockedPersonExtended.setId(mockedPersonId);
            save(mockedPersonExtended);
        }
    }
}
