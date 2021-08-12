package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationException;
import ar.lamansys.odontology.application.odontogram.GetToothSurfacesService;
import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import ar.lamansys.odontology.domain.ESurfacePositionBo;
import ar.lamansys.odontology.domain.OdontologyDocumentStorage;
import ar.lamansys.odontology.domain.ProcedureStorage;
import ar.lamansys.odontology.domain.consultation.ClinicalSpecialtyBo;
import ar.lamansys.odontology.domain.consultation.DoctorInfoBo;
import ar.lamansys.odontology.domain.consultation.OdontologyDoctorStorage;
import ar.lamansys.odontology.domain.consultation.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.AppointmentStorage;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOdontologyConsultationImplTest {

    private CreateOdontologyConsultation createOdontologyConsultation;

    @Mock
    private OdontologyConsultationStorage odontologyConsultationStorage;

    @Mock
    private DiagnosticStorage diagnosticStorage;

    @Mock
    private ProcedureStorage proceduresStorage;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @Mock
    private OdontologyDoctorStorage odontologyDoctorStorage;

    @Mock
    private OdontologyDocumentStorage odontologyDocumentStorage;

    @Mock
    private DrawOdontogramService drawOdontogramService;

    @Mock
    private GetToothSurfacesService getToothSurfacesService;

    @Mock
    private AppointmentStorage appointmentStorage;

    @BeforeEach
    void setUp() {
        this.createOdontologyConsultation = new CreateOdontologyConsultationImpl(diagnosticStorage,
                proceduresStorage,
                odontologyConsultationStorage,
                dateTimeProvider,
                odontologyDoctorStorage,
                odontologyDocumentStorage,
                drawOdontogramService,
                getToothSurfacesService,
                appointmentStorage);
    }

    @Test
    void shouldThrowErrorWhenConsultationIsNull() {
        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                        createOdontologyConsultation.run(null));

        String expectedMessage = "La información de la consulta es obligatoria";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenInstitutionIdIsNull() {
        Integer clinicalSpecialtyId = 255;
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(clinicalSpecialtyId, "Especialidad 1"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(null);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(clinicalSpecialtyId);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El id de la institución es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenPatientIdIsNull() {
        Integer clinicalSpecialtyId = 255;
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(clinicalSpecialtyId, "Especialidad 1"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(null);
        consultation.setClinicalSpecialtyId(clinicalSpecialtyId);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El id del paciente es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenDiagnosticIsNotFound() {
        Integer clinicalSpecialtyId = 255;
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(clinicalSpecialtyId, "Especialidad 1"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        when(diagnosticStorage.getDiagnostic("16958000"))
                .thenReturn(Optional.of(new DiagnosticBo("16958000", "ausencia congénita completa de dientes (trastorno)", true, false)));
        when(diagnosticStorage.getDiagnostic("64969001"))
                .thenReturn(Optional.of(new DiagnosticBo("64969001", "ausencia parcial congénita de los dientes (trastorno)", true, false)));
        when(diagnosticStorage.getDiagnostic("699685006"))
                .thenReturn(Optional.of(new DiagnosticBo("699685006", "surco oclusal profundo (hallazgo)", false, true)));
        when(diagnosticStorage.getDiagnostic("9984005"))
                .thenReturn(Optional.empty());

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(clinicalSpecialtyId);

        List<ConsultationDentalActionBo> dentalDiagnosticBos = new ArrayList<>();

        ConsultationDentalActionBo diagnostic1 = new ConsultationDentalActionBo(new OdontologySnomedBo("16958000", "ausencia congénita completa de dientes (trastorno)"), true);
        diagnostic1.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalDiagnosticBos.add(diagnostic1);

        ConsultationDentalActionBo diagnostic2 = new ConsultationDentalActionBo(new OdontologySnomedBo("64969001", "ausencia parcial congénita de los dientes (trastorno)"), true);
        diagnostic2.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalDiagnosticBos.add(diagnostic2);

        ConsultationDentalActionBo diagnostic3 = new ConsultationDentalActionBo(new OdontologySnomedBo("699685006", "surco oclusal profundo (hallazgo)"), true);
        diagnostic3.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        diagnostic3.setSurfacePosition(ESurfacePositionBo.RIGHT);
        dentalDiagnosticBos.add(diagnostic3);

        String sctidNotFound = "9984005";
        String ptNotFound = "ausencia congénita completa de dientes (trastorno)";
        ConsultationDentalActionBo diagnostic4 = new ConsultationDentalActionBo(new OdontologySnomedBo("9984005", "ausencia congénita completa de dientes (trastorno)"), true);
        diagnostic4.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        diagnostic4.setSurfacePosition(ESurfacePositionBo.RIGHT);
        dentalDiagnosticBos.add(diagnostic4);

        consultation.setDentalActions(dentalDiagnosticBos);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El diagnóstico con sctid: " + sctidNotFound +
                " y prefered term: '" + ptNotFound + "' no es un diagnóstico dental aplicable";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenDiagnosticNotApplicableToToothIsAppliedToTooth() {
        Integer clinicalSpecialtyId = 255;
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(clinicalSpecialtyId, "Especialidad 1"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        when(diagnosticStorage.getDiagnostic("16958000"))
                .thenReturn(Optional.of(new DiagnosticBo("16958000", "ausencia congénita completa de dientes (trastorno)", true, false)));
        when(diagnosticStorage.getDiagnostic("64969001"))
                .thenReturn(Optional.of(new DiagnosticBo("64969001", "ausencia parcial congénita de los dientes (trastorno)", true, false)));
        when(diagnosticStorage.getDiagnostic("699685006"))
                .thenReturn(Optional.of(new DiagnosticBo("699685006", "surco oclusal profundo (hallazgo)", false, true)));

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(clinicalSpecialtyId);

        List<ConsultationDentalActionBo> dentalDiagnostics = new ArrayList<>();

        ConsultationDentalActionBo diagnostic1 = new ConsultationDentalActionBo(new OdontologySnomedBo("16958000", "ausencia congénita completa de dientes (trastorno)"), true);
        diagnostic1.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalDiagnostics.add(diagnostic1);

        ConsultationDentalActionBo diagnostic2 = new ConsultationDentalActionBo(new OdontologySnomedBo("64969001", "ausencia parcial congénita de los dientes (trastorno)"), true);
        diagnostic2.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalDiagnostics.add(diagnostic2);

        String diagnosticSctid = "699685006";
        String diagnosticPt = "surco oclusal profundo (hallazgo)";
        ConsultationDentalActionBo diagnostic3 = new ConsultationDentalActionBo(new OdontologySnomedBo(diagnosticSctid, diagnosticPt), true);
        diagnostic3.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalDiagnostics.add(diagnostic3);

        consultation.setDentalActions(dentalDiagnostics);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El diagnóstico con sctid: " + diagnosticSctid +
                " y prefered term: '" + diagnosticPt + "' no es aplicable a pieza dental";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenDiagnosticNotApplicableToSurfaceIsAppliedToSurface() {
        Integer clinicalSpecialtyId = 255;
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(clinicalSpecialtyId, "Especialidad 1"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        when(diagnosticStorage.getDiagnostic("16958000"))
                .thenReturn(Optional.of(new DiagnosticBo("16958000", "ausencia congénita completa de dientes (trastorno)", true, false)));
        when(diagnosticStorage.getDiagnostic("64969001"))
                .thenReturn(Optional.of(new DiagnosticBo("64969001", "ausencia parcial congénita de los dientes (trastorno)", true, false)));

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(clinicalSpecialtyId);

        List<ConsultationDentalActionBo> dentalDiagnostics = new ArrayList<>();

        ConsultationDentalActionBo diagnostic1 = new ConsultationDentalActionBo(new OdontologySnomedBo("16958000", "ausencia congénita completa de dientes (trastorno)"), true);
        diagnostic1.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalDiagnostics.add(diagnostic1);

        String diagnosticSctid = "64969001";
        String diagnosticPt = "ausencia parcial congénita de los dientes (trastorno)";
        ConsultationDentalActionBo diagnostic2 = new ConsultationDentalActionBo(new OdontologySnomedBo(diagnosticSctid, diagnosticPt), true);
        diagnostic2.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        diagnostic2.setSurfacePosition(ESurfacePositionBo.INTERNAL);
        dentalDiagnostics.add(diagnostic2);

        ConsultationDentalActionBo diagnostic3 = new ConsultationDentalActionBo(new OdontologySnomedBo("699685006", "surco oclusal profundo (hallazgo)"), true);
        diagnostic3.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        diagnostic3.setSurfacePosition(ESurfacePositionBo.LEFT);
        dentalDiagnostics.add(diagnostic3);

        consultation.setDentalActions(dentalDiagnostics);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El diagnóstico con sctid: " + diagnosticSctid +
                " y prefered term: '" + diagnosticPt + "' no es aplicable a cara dental";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenProcedureIsNotFound() {
        Integer clinicalSpecialtyId = 255;
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(clinicalSpecialtyId, "Especialidad 1"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        String sctidNotFound = "399041000221101";
        String ptNotFound = "obturación con amalgama cavidad simple";

        when(proceduresStorage.getProcedure("789147006"))
                .thenReturn(Optional.of(new ProcedureBo("789147006", "implante dental", true, false)));
        when(proceduresStorage.getProcedure("278123008"))
                .thenReturn(Optional.of(new ProcedureBo("278123008", "implante osteointegrado con forma radicular", true, true)));
        when(proceduresStorage.getProcedure("4721000221105"))
                .thenReturn(Optional.of(new ProcedureBo("4721000221105", "inactivación de caries", false, true)));
        when(proceduresStorage.getProcedure(sctidNotFound))
                .thenReturn(Optional.empty());

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(clinicalSpecialtyId);

        List<ConsultationDentalActionBo> dentalProcedures = new ArrayList<>();

        ConsultationDentalActionBo procedure1 = new ConsultationDentalActionBo(new OdontologySnomedBo("789147006", "implante dental"), false);
        procedure1.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalProcedures.add(procedure1);

        ConsultationDentalActionBo procedure2 = new ConsultationDentalActionBo(new OdontologySnomedBo("278123008", "implante osteointegrado con forma radicular"), false);
        procedure2.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalProcedures.add(procedure2);

        ConsultationDentalActionBo procedure3 = new ConsultationDentalActionBo(new OdontologySnomedBo("4721000221105", "inactivación de caries"), false);
        procedure3.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        procedure3.setSurfacePosition(ESurfacePositionBo.EXTERNAL);
        dentalProcedures.add(procedure3);

        ConsultationDentalActionBo procedure4 = new ConsultationDentalActionBo(new OdontologySnomedBo(sctidNotFound, ptNotFound), false);
        procedure4.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        procedure4.setSurfacePosition(ESurfacePositionBo.CENTRAL);
        dentalProcedures.add(procedure4);

        consultation.setDentalActions(dentalProcedures);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El procedimiento con sctid: " + sctidNotFound +
                " y prefered term: '" + ptNotFound + "' no es un procedimiento dental aplicable";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void shouldThrowErrorWhenProcedureNotApplicableToToothIsAppliedToTooth() {
        Integer clinicalSpecialtyId = 255;
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(clinicalSpecialtyId, "Especialidad 1"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        String sctidNotApplicable = "4721000221105";
        String ptNotApplicable = "inactivación de caries";

        when(proceduresStorage.getProcedure("789147006"))
                .thenReturn(Optional.of(new ProcedureBo("789147006", "implante dental", true, false)));
        when(proceduresStorage.getProcedure("278123008"))
                .thenReturn(Optional.of(new ProcedureBo("278123008", "implante osteointegrado con forma radicular", true, true)));
        when(proceduresStorage.getProcedure(sctidNotApplicable))
                .thenReturn(Optional.of(new ProcedureBo(sctidNotApplicable, ptNotApplicable, false, true)));

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(clinicalSpecialtyId);

        List<ConsultationDentalActionBo> dentalProcedures = new ArrayList<>();

        ConsultationDentalActionBo procedure1 = new ConsultationDentalActionBo(new OdontologySnomedBo("789147006", "implante dental"), false);
        procedure1.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalProcedures.add(procedure1);

        ConsultationDentalActionBo procedure2 = new ConsultationDentalActionBo(new OdontologySnomedBo("278123008", "implante osteointegrado con forma radicular"), false);
        procedure2.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        procedure2.setSurface(new OdontologySnomedBo("2", "surface 1"));
        dentalProcedures.add(procedure2);

        ConsultationDentalActionBo procedure3 = new ConsultationDentalActionBo(new OdontologySnomedBo(sctidNotApplicable, ptNotApplicable), false);
        procedure3.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalProcedures.add(procedure3);

        consultation.setDentalActions(dentalProcedures);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El procedimiento con sctid: " + sctidNotApplicable +
                " y prefered term: '" + ptNotApplicable + "' no es aplicable a pieza dental";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenProcedureNotApplicableToSurfaceIsAppliedToSurface() {
        Integer clinicalSpecialtyId = 255;
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(clinicalSpecialtyId, "Especialidad 1"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        String sctidNotApplicable = "4721000221105";
        String ptNotApplicable = "inactivación de caries";

        when(proceduresStorage.getProcedure("789147006"))
                .thenReturn(Optional.of(new ProcedureBo("789147006", "implante dental", true, false)));
        when(proceduresStorage.getProcedure("278123008"))
                .thenReturn(Optional.of(new ProcedureBo("278123008", "implante osteointegrado con forma radicular", true, true)));
        when(proceduresStorage.getProcedure(sctidNotApplicable))
                .thenReturn(Optional.of(new ProcedureBo(sctidNotApplicable, ptNotApplicable, true, false)));

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(clinicalSpecialtyId);

        List<ConsultationDentalActionBo> dentalProcedures = new ArrayList<>();

        ConsultationDentalActionBo procedure1 = new ConsultationDentalActionBo(new OdontologySnomedBo("789147006", "implante dental"), false);
        procedure1.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        dentalProcedures.add(procedure1);

        ConsultationDentalActionBo procedure2 = new ConsultationDentalActionBo(new OdontologySnomedBo("278123008", "implante osteointegrado con forma radicular"), false);
        procedure2.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        procedure2.setSurfacePosition(ESurfacePositionBo.INTERNAL);
        dentalProcedures.add(procedure2);

        ConsultationDentalActionBo procedure3 = new ConsultationDentalActionBo(new OdontologySnomedBo(sctidNotApplicable, ptNotApplicable), false);
        procedure3.setTooth(new OdontologySnomedBo("1", "tooth 1"));
        procedure3.setSurfacePosition(ESurfacePositionBo.CENTRAL);
        dentalProcedures.add(procedure3);

        consultation.setDentalActions(dentalProcedures);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El procedimiento con sctid: " + sctidNotApplicable +
                " y prefered term: '" + ptNotApplicable + "' no es aplicable a cara dental";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenDoctorIsNotFound() {
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.empty());

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(255);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El identificador del profesional es inválido";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenDoctorDoesNotHaveSpecialty() {
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(255, "Especialidad 1"));
        clinicalSpecialties.add(new ClinicalSpecialtyBo(156, "Especialidad 2"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(1000);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El doctor no posee la especialidad indicada";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenClinicalSpecialtyIsNull() {
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(255, "Especialidad 1"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(null);

        Exception exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El id de especialidad es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

}