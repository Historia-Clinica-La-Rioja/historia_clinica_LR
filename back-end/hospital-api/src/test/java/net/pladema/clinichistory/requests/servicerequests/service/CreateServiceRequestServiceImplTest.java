package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
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
@DataJpaTest(showSql = false)
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
        ServiceRequestBo serviceRequestBo = getValidServiceRequest();
        serviceRequestBo.setInstitutionId(null);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(serviceRequestBo)
        );
        String expectedMessage = "El identificador de la institución es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_withNullPatient(){
        ServiceRequestBo serviceRequestBo = getValidServiceRequest();
        serviceRequestBo.setPatientInfo(new PatientInfoBo());
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(serviceRequestBo)
        );
        String expectedMessage = "El paciente es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_withNullDoctorId(){
        ServiceRequestBo serviceRequestBo = getValidServiceRequest();
        serviceRequestBo.setDoctorId(null);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(serviceRequestBo)
        );
        String expectedMessage = "El identificador del médico es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_withEmptyDiagnosticReport(){
        ServiceRequestBo serviceRequestBo = getValidServiceRequest();
        serviceRequestBo.setDiagnosticReports(null);
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(serviceRequestBo)
        );
        String expectedMessage = "La orden tiene que tener asociada al menos un estudio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_withInvalidServiceRequest_HealthCondition_Snomed(){
        ServiceRequestBo serviceRequestBo = getValidServiceRequest();
        DiagnosticReportBo diagnosticReportBoWithoutHealthConditionId = new DiagnosticReportBo();
        diagnosticReportBoWithoutHealthConditionId.setHealthConditionId(null);
        diagnosticReportBoWithoutHealthConditionId.setObservations("jakek");
        diagnosticReportBoWithoutHealthConditionId.setSnomed(getValidSnomed());
        serviceRequestBo.setDiagnosticReports(List.of(diagnosticReportBoWithoutHealthConditionId));
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(serviceRequestBo)
        );
        String expectedMessage = "El estudio tiene que estar asociado a un problema";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_withDuplicatedStudy() {
        ServiceRequestBo serviceRequestBo = getServiceRequestBoWithDuplicatedStudy();
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                createServiceRequestServiceImpl.execute(serviceRequestBo)
        );
        String expectedMessage = "La orden no puede contener más de un estudio con el mismo problema y el mismo concepto snomed";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void execute_success() {
        ServiceRequestBo serviceRequestBo = getValidServiceRequest();
        Integer medicationRequestId = createServiceRequestServiceImpl.execute(serviceRequestBo);
        Assertions.assertEquals(1, serviceRequestRepository.count());
        Assertions.assertNotNull(medicationRequestId);
    }


    private SnomedBo getValidSnomed(){
        return new SnomedBo(
                "703421000",
                "temperatura (entidad observable)");
    }

    private ServiceRequestBo getValidServiceRequest(){
        DiagnosticReportBo diagnosticReportBo1 = new DiagnosticReportBo();
        diagnosticReportBo1.setHealthConditionId(1);
        diagnosticReportBo1.setObservations("jakek");
        diagnosticReportBo1.setSnomed(getValidSnomed());

        DiagnosticReportBo diagnosticReportBo2 = new DiagnosticReportBo();
        diagnosticReportBo2.setHealthConditionId(2);
        diagnosticReportBo2.setObservations("jakek + fuerte");
        diagnosticReportBo2.setSnomed(getValidSnomed());

        var serviceRequestBo = new ServiceRequestBo();
        serviceRequestBo.setPatientInfo(new PatientInfoBo(1, (short) 1, (short) 1));
        serviceRequestBo.setCategoryId(ServiceRequestCategory.LABORATORY_PROCEDURE);
        serviceRequestBo.setEncounterId(1);
        serviceRequestBo.setInstitutionId(1);
        serviceRequestBo.setDoctorId(1);
        serviceRequestBo.setNoteId(1L);
        serviceRequestBo.setMedicalCoverageId(4);
        serviceRequestBo.setDiagnosticReports(List.of(diagnosticReportBo1, diagnosticReportBo2));
        return serviceRequestBo;
    }

    private ServiceRequestBo getServiceRequestBoWithDuplicatedStudy() {


        DiagnosticReportBo diagnosticReportBo1 = new DiagnosticReportBo();
        diagnosticReportBo1.setHealthConditionId(1);
        diagnosticReportBo1.setObservations("Una observacion");
        diagnosticReportBo1.setSnomed(getValidSnomed());

        DiagnosticReportBo diagnosticReportBo2 = new DiagnosticReportBo();
        diagnosticReportBo2.setHealthConditionId(1);
        diagnosticReportBo2.setObservations("Una observacion diferenTe");
        diagnosticReportBo2.setSnomed(getValidSnomed());

        DiagnosticReportBo diagnosticReportBo3 = new DiagnosticReportBo();
        diagnosticReportBo3.setHealthConditionId(1);
        diagnosticReportBo3.setObservations("Una observacion para el virusOriboca");
        diagnosticReportBo3.setSnomed(
                new SnomedBo(
                        "25582006",
                        "enfermedad por el virus Oriboca"));

        var serviceRequestBo = new ServiceRequestBo();
        serviceRequestBo.setPatientInfo(new PatientInfoBo(1, (short) 1, (short) 1));
        serviceRequestBo.setCategoryId(ServiceRequestCategory.LABORATORY_PROCEDURE);
        serviceRequestBo.setEncounterId(1);
        serviceRequestBo.setInstitutionId(1);
        serviceRequestBo.setDoctorId(1);
        serviceRequestBo.setNoteId(1L);
        serviceRequestBo.setMedicalCoverageId(4);
        serviceRequestBo.setDiagnosticReports(List.of(diagnosticReportBo1, diagnosticReportBo2, diagnosticReportBo3));
        return serviceRequestBo;
    }

}
