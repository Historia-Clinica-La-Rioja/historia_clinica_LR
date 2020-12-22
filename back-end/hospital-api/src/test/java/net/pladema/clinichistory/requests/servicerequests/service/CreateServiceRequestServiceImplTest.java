package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestCategory;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.clinichistory.requests.servicerequests.service.impl.CreateServiceRequestServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CreateServiceRequestServiceImplTest {

    private CreateServiceRequestService  createServiceRequestServiceImpl;


    @MockBean
    private DocumentFactory documentFactory;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;

    @Before
    public void setUp() {
        createServiceRequestServiceImpl = new CreateServiceRequestServiceImpl(serviceRequestRepository, documentFactory);
    }

    @Test
    public void execute_withNullInstitution(){
        ServiceRequestBo serviceRequestBo = validServiceRequest();
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(null,serviceRequestBo)
        );
        String expectedMessage = "El identificador de la institución es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_withNullPatient(){
        ServiceRequestBo serviceRequestBo = validServiceRequest();
        serviceRequestBo.setPatientInfo(new PatientInfoBo());
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(1,serviceRequestBo)
        );
        String expectedMessage = "El paciente es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_withNullDoctorId(){
        ServiceRequestBo serviceRequestBo = validServiceRequest();
        serviceRequestBo.setDoctorId(null);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(1,serviceRequestBo)
        );
        String expectedMessage = "El identificador del médico es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_withEmptyDiagnosticReport(){
        ServiceRequestBo serviceRequestBo = validServiceRequest();
        serviceRequestBo.setDiagnosticReports(null);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(1,serviceRequestBo)
        );
        String expectedMessage = "La orden tiene que tener asociada al menos un estudio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_withInvalidServiceRequest_HealthCondition_Snomed(){
        ServiceRequestBo serviceRequestBo = validServiceRequest();
        SnomedBo snomed = new SnomedBo("1111", "Enfermedad 1");
        DiagnosticReportBo DiagnosticReportBoWithoutHealthConditionId = new DiagnosticReportBo(
                null,
                "jakek",
                snomed);
        serviceRequestBo.setDiagnosticReports(List.of(DiagnosticReportBoWithoutHealthConditionId));
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(1, serviceRequestBo)
        );
        String expectedMessage = "El estudio tiene que estar asociado a un problema";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_success() {
        ServiceRequestBo serviceRequestBo = validServiceRequest();
        Integer medicationRequestId = createServiceRequestServiceImpl.execute(1,serviceRequestBo);
        Assertions.assertEquals(1, serviceRequestRepository.count());
        Assertions.assertNotNull(medicationRequestId);
    }


    private ServiceRequestBo validServiceRequest(){

        SnomedBo snomed1 = new SnomedBo("1111", "Enfermedad 1");
        SnomedBo snomed2 = new SnomedBo("2222", "Enfermedad 2");

        DiagnosticReportBo diagnosticReportBo1 = new DiagnosticReportBo(1, "jakek", snomed1);
        DiagnosticReportBo diagnosticReportBo2 = new DiagnosticReportBo(2, "jakekX2", snomed2);

        var serviceRequestBo = new ServiceRequestBo();
        serviceRequestBo.setPatientInfo(new PatientInfoBo(1, (short) 1, (short) 1));
        serviceRequestBo.setCategoryId(ServiceRequestCategory.LABORATORY_PROCEDURE);
        serviceRequestBo.setEncounterId(1);
        serviceRequestBo.setDoctorId(1);
        serviceRequestBo.setNoteId(1L);
        serviceRequestBo.setMedicalCoverageId(4);
        serviceRequestBo.setDiagnosticReports(List.of(diagnosticReportBo1,diagnosticReportBo2));
        return serviceRequestBo;
    }

}
