package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationException;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import ar.lamansys.odontology.domain.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.OdontologyDocumentStorage;
import ar.lamansys.odontology.domain.ProceduresStorage;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.infrastructure.repository.DiagnosticStorageMockImpl;
import ar.lamansys.odontology.infrastructure.repository.ProceduresStorageMockImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateConsultationServiceImplTest {

    private CreateConsultationService createConsultationService;

    @Mock
    private OdontologyConsultationStorage odontologyConsultationStorage;

    @Mock
    private OdontologyDocumentStorage odontologyDocumentStorage;

    @BeforeEach
    void setUp() {
        DiagnosticStorage diagnosticStorage = new DiagnosticStorageMockImpl();
        ProceduresStorage proceduresStorage = new ProceduresStorageMockImpl();
        this.createConsultationService = new CreateConsultationServiceImpl(diagnosticStorage,
                proceduresStorage,
                odontologyConsultationStorage,
                odontologyDocumentStorage);
    }

    @Test
    void shouldThrowErrorWhenConsultationIsNull() {
        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                        createConsultationService.run(null));

        String expectedMessage = "La información de la consulta es obligatoria";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenInstitutionIdIsNull() {
        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(null);
        consultation.setPatientId(1);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createConsultationService.run(consultation));

        String expectedMessage = "El id de la institución es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenPatientIdIsNull() {
        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(null);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createConsultationService.run(consultation));

        String expectedMessage = "El id del paciente es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

}