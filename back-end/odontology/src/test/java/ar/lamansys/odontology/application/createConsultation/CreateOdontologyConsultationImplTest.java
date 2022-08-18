package ar.lamansys.odontology.application.createConsultation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationException;
import ar.lamansys.odontology.application.odontogram.GetToothService;
import ar.lamansys.odontology.application.odontogram.GetToothSurfacesService;
import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import ar.lamansys.odontology.domain.ESurfacePositionBo;
import ar.lamansys.odontology.domain.OdontologyDocumentStorage;
import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProcedureStorage;
import ar.lamansys.odontology.domain.Publisher;
import ar.lamansys.odontology.domain.consultation.ClinicalSpecialtyBo;
import ar.lamansys.odontology.domain.consultation.ConsultationAllergyBo;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDiagnosticBo;
import ar.lamansys.odontology.domain.consultation.ConsultationMedicationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationPersonalHistoryBo;
import ar.lamansys.odontology.domain.consultation.ConsultationProcedureBo;
import ar.lamansys.odontology.domain.consultation.ConsultationReasonBo;
import ar.lamansys.odontology.domain.consultation.DoctorInfoBo;
import ar.lamansys.odontology.domain.consultation.OdontologyAppointmentStorage;
import ar.lamansys.odontology.domain.consultation.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.consultation.OdontologyDoctorStorage;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;

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
	private SharedAppointmentPort sharedAppointmentPort;

    @Mock
    private CpoCeoIndicesCalculator cpoCeoIndicesCalculator;

    @Mock
    private GetToothSurfacesService getToothSurfacesService;

    @Mock
    private OdontologyAppointmentStorage odontologyAppointmentStorage;

    @Mock
    private GetToothService getToothService;

    @Mock
    private Publisher publisher;

    @BeforeEach
    void setUp() {
        this.createOdontologyConsultation = new CreateOdontologyConsultationImpl(diagnosticStorage,
                proceduresStorage,
                odontologyConsultationStorage,
                dateTimeProvider,
                odontologyDoctorStorage,
                odontologyDocumentStorage,
                drawOdontogramService,
                sharedAppointmentPort, cpoCeoIndicesCalculator,
                getToothSurfacesService, odontologyAppointmentStorage,
                getToothService, publisher);
    }

    @Test
    void shouldThrowErrorWhenConsultationIsNull() {
        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                        createOdontologyConsultation.run(null));

        String expectedMessage = "La información de la consulta es obligatoria";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
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

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El id de la institución es obligatorio";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
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

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El id del paciente es obligatorio";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
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

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El diagnóstico con ID de Snomed: '" + sctidNotFound +
                "' y término: '" + ptNotFound + "' no es un diagnóstico dental aplicable";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
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

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El diagnóstico con ID de Snomed: '" + diagnosticSctid +
                "' y término: '" + diagnosticPt + "' no es aplicable a pieza dental";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
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

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El diagnóstico con ID de Snomed: '" + diagnosticSctid +
                "' y término: '" + diagnosticPt + "' no es aplicable a cara dental";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
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

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El procedimiento con ID de Snomed: '" + sctidNotFound +
                "' y término: '" + ptNotFound + "' no es un procedimiento dental aplicable";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
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

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El procedimiento con ID de Snomed: '" + sctidNotApplicable +
                "' y término: '" + ptNotApplicable + "' no es aplicable a pieza dental";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
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

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El procedimiento con ID de Snomed: '" + sctidNotApplicable +
                "' y término: '" + ptNotApplicable + "' no es aplicable a cara dental";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenDoctorIsNotFound() {
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.empty());

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(255);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El identificador del profesional es inválido";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
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

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El doctor no posee la especialidad indicada";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
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

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "El id de especialidad es obligatorio";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenThereAreRepeatedReasons() {
        ConsultationBo consultation = mockBasicConsultation();

        List<ConsultationReasonBo> reasons = new ArrayList<>();

        ConsultationReasonBo reason1 = new ConsultationReasonBo();
        reason1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        reasons.add(reason1);

        ConsultationReasonBo reason2 = new ConsultationReasonBo();
        reason2.setSnomed(new OdontologySnomedBo("SCTID 2", "PT 2"));
        reasons.add(reason2);

        ConsultationReasonBo reason3 = new ConsultationReasonBo();
        reason3.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        reasons.add(reason3);

        consultation.setReasons(reasons);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "Motivos repetidos";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenThereAreRepeatedPersonalHistories() {
        ConsultationBo consultation = mockBasicConsultation();

        List<ConsultationPersonalHistoryBo> personalHistories = new ArrayList<>();

        ConsultationPersonalHistoryBo personalHistory1 = new ConsultationPersonalHistoryBo(new DateDto(2022, 1,1));
        personalHistory1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        personalHistories.add(personalHistory1);

        ConsultationPersonalHistoryBo personalHistory2 = new ConsultationPersonalHistoryBo(new DateDto(2022, 1,1));
        personalHistory2.setSnomed(new OdontologySnomedBo("SCTID 2", "PT 2"));
        personalHistories.add(personalHistory2);

        ConsultationPersonalHistoryBo personalHistory3 = new ConsultationPersonalHistoryBo(new DateDto(2022, 1,1));
        personalHistory3.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        personalHistories.add(personalHistory3);

        consultation.setPersonalHistories(personalHistories);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "Antecedentes personales repetidos";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenThereAreRepeatedAllergies() {
        ConsultationBo consultation = mockBasicConsultation();

        List<ConsultationAllergyBo> allergies = new ArrayList<>();

        ConsultationAllergyBo allergy1 = new ConsultationAllergyBo();
        allergy1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        allergies.add(allergy1);

        ConsultationAllergyBo allergy2 = new ConsultationAllergyBo();
        allergy2.setSnomed(new OdontologySnomedBo("SCTID 2", "PT 2"));
        allergies.add(allergy2);

        ConsultationAllergyBo allergy3 = new ConsultationAllergyBo();
        allergy3.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        allergies.add(allergy3);

        consultation.setAllergies(allergies);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "Alergias repetidas";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenThereAreRepeatedDiagnostics() {
        ConsultationBo consultation = mockBasicConsultation();

        List<ConsultationDiagnosticBo> diagnostics = new ArrayList<>();

        ConsultationDiagnosticBo diagnostic1 = new ConsultationDiagnosticBo();
        diagnostic1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        diagnostics.add(diagnostic1);

        ConsultationDiagnosticBo diagnostic2 = new ConsultationDiagnosticBo();
        diagnostic2.setSnomed(new OdontologySnomedBo("SCTID 2", "PT 2"));
        diagnostics.add(diagnostic2);

        ConsultationDiagnosticBo diagnostic3 = new ConsultationDiagnosticBo();
        diagnostic3.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        diagnostics.add(diagnostic3);

        consultation.setDiagnostics(diagnostics);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "Diagnósticos repetidos";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenThereAreRepeatedProcedures() {
        ConsultationBo consultation = mockBasicConsultation();

        List<ConsultationProcedureBo> procedures = new ArrayList<>();

        ConsultationProcedureBo procedure1 = new ConsultationProcedureBo();
        procedure1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        procedures.add(procedure1);

        ConsultationProcedureBo procedure2 = new ConsultationProcedureBo();
        procedure2.setSnomed(new OdontologySnomedBo("SCTID 2", "PT 2"));
        procedures.add(procedure2);

        ConsultationProcedureBo procedure3 = new ConsultationProcedureBo();
        procedure3.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        procedures.add(procedure3);

        consultation.setProcedures(procedures);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "Procedimientos repetidos";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenThereAreRepeatedMedications() {
        ConsultationBo consultation = mockBasicConsultation();

        List<ConsultationMedicationBo> medications = new ArrayList<>();

        ConsultationMedicationBo medication1 = new ConsultationMedicationBo();
        medication1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        medications.add(medication1);

        ConsultationMedicationBo medication2 = new ConsultationMedicationBo();
        medication2.setSnomed(new OdontologySnomedBo("SCTID 2", "PT 2"));
        medications.add(medication2);

        ConsultationMedicationBo medication3 = new ConsultationMedicationBo();
        medication3.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        medications.add(medication3);

        consultation.setMedications(medications);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "Medicaciones repetidas";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorWhenThereAreMultipleTypesOfRepeatedConcepts() {
        ConsultationBo consultation = mockBasicConsultation();

        List<ConsultationMedicationBo> medications = new ArrayList<>();
        ConsultationMedicationBo medication1 = new ConsultationMedicationBo();
        medication1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        medications.add(medication1);
        ConsultationMedicationBo medication2 = new ConsultationMedicationBo();
        medication2.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        medications.add(medication2);
        consultation.setMedications(medications);

        List<ConsultationProcedureBo> procedures = new ArrayList<>();
        ConsultationProcedureBo procedure1 = new ConsultationProcedureBo();
        procedure1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        procedures.add(procedure1);
        ConsultationProcedureBo procedure2 = new ConsultationProcedureBo();
        procedure2.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        procedures.add(procedure2);
        consultation.setProcedures(procedures);

        List<ConsultationDiagnosticBo> diagnostics = new ArrayList<>();
        ConsultationDiagnosticBo diagnostic1 = new ConsultationDiagnosticBo();
        diagnostic1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        diagnostics.add(diagnostic1);
        ConsultationDiagnosticBo diagnostic2 = new ConsultationDiagnosticBo();
        diagnostic2.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        diagnostics.add(diagnostic2);
        consultation.setDiagnostics(diagnostics);

        List<ConsultationAllergyBo> allergies = new ArrayList<>();
        ConsultationAllergyBo allergy1 = new ConsultationAllergyBo();
        allergy1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        allergies.add(allergy1);
        ConsultationAllergyBo allergy2 = new ConsultationAllergyBo();
        allergy2.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        allergies.add(allergy2);
        consultation.setAllergies(allergies);

        List<ConsultationPersonalHistoryBo> personalHistories = new ArrayList<>();
        ConsultationPersonalHistoryBo personalHistory1 = new ConsultationPersonalHistoryBo(new DateDto(2022, 1,1));
        personalHistory1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        personalHistories.add(personalHistory1);
        ConsultationPersonalHistoryBo personalHistory2 = new ConsultationPersonalHistoryBo(new DateDto(2022, 1,1));
        personalHistory2.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        personalHistories.add(personalHistory2);
        consultation.setPersonalHistories(personalHistories);

        List<ConsultationReasonBo> reasons = new ArrayList<>();
        ConsultationReasonBo reason1 = new ConsultationReasonBo();
        reason1.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        reasons.add(reason1);
        ConsultationReasonBo reason2 = new ConsultationReasonBo();
        reason2.setSnomed(new OdontologySnomedBo("SCTID 1", "PT 1"));
        reasons.add(reason2);
        consultation.setReasons(reasons);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        assertTrue(exception.getMessages().containsAll(List.of("Medicaciones repetidas",
                "Antecedentes personales repetidos", "Alergias repetidas", "Motivos repetidos",
                "Procedimientos repetidos", "Diagnósticos repetidos")));
    }

    @Test
    void shouldThrowErrorsWhenPermanentTeethQuantityIsNegative() {
        ConsultationBo consultation = mockBasicConsultation();

        consultation.setPermanentTeethPresent(-1);
        consultation.setTemporaryTeethPresent(15);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "La cantidad de dientes permanentes presentes debe estar entre 0 y 99";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorsWhenPermanentTeethQuantityIsMoreThan99() {
        ConsultationBo consultation = mockBasicConsultation();

        consultation.setPermanentTeethPresent(100);
        consultation.setTemporaryTeethPresent(15);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "La cantidad de dientes permanentes presentes debe estar entre 0 y 99";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorsWhenTemporaryTeethQuantityIsNegative() {
        ConsultationBo consultation = mockBasicConsultation();

        consultation.setPermanentTeethPresent(28);
        consultation.setTemporaryTeethPresent(-1);

        CreateConsultationException exception3 = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "La cantidad de dientes temporarios presentes debe estar entre 0 y 99";
        List<String> actualMessages = exception3.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    @Test
    void shouldThrowErrorsWhenTemporaryTeethQuantityIsMoreThan99() {
        ConsultationBo consultation = mockBasicConsultation();

        consultation.setPermanentTeethPresent(28);
        consultation.setTemporaryTeethPresent(100);

        CreateConsultationException exception = Assertions.assertThrows(CreateConsultationException.class, () ->
                createOdontologyConsultation.run(consultation));

        String expectedMessage = "La cantidad de dientes temporarios presentes debe estar entre 0 y 99";
        List<String> actualMessages = exception.getMessages();
        Assertions.assertTrue(actualMessages.contains(expectedMessage));
    }

    private ConsultationBo mockBasicConsultation() {
        Integer clinicalSpecialtyId = 255;
        List<ClinicalSpecialtyBo> clinicalSpecialties = new ArrayList<>();
        clinicalSpecialties.add(new ClinicalSpecialtyBo(clinicalSpecialtyId, "Especialidad 1"));
        when(odontologyDoctorStorage.getDoctorInfo())
                .thenReturn(Optional.of(new DoctorInfoBo(5, clinicalSpecialties)));

        ConsultationBo consultation = new ConsultationBo();
        consultation.setInstitutionId(1);
        consultation.setPatientId(1);
        consultation.setClinicalSpecialtyId(clinicalSpecialtyId);
        return consultation;
    }

}