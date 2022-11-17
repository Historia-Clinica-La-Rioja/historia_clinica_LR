package ar.lamansys.immunization.application.immunizePatient;

import ar.lamansys.immunization.application.immunizePatient.exceptions.ImmunizePatientException;
import ar.lamansys.immunization.domain.consultation.ImmunizePatientBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationInfoBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationValidatorException;
import ar.lamansys.immunization.domain.snomed.SnomedBo;
import ar.lamansys.immunization.domain.user.RolePermissionException;
import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleStorage;
import ar.lamansys.immunization.infrastructure.output.repository.appointments.ServeAppointmentStorageImpl;
import ar.lamansys.immunization.infrastructure.output.repository.consultation.DoctorStorageImpl;
import ar.lamansys.immunization.infrastructure.output.repository.consultation.VaccineConsultation;
import ar.lamansys.immunization.infrastructure.output.repository.consultation.VaccineConsultationRepository;
import ar.lamansys.immunization.infrastructure.output.repository.consultation.VaccineConsultationStorageImpl;
import ar.lamansys.immunization.infrastructure.output.repository.document.ImmunizationDocumentStorageImpl;
import ar.lamansys.immunization.infrastructure.output.repository.user.ImmunizationUserStorageImpl;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineConditionApplicationRepository;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineConditionApplicationStorageImpl;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineSchemeRepository;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineSchemeStorageImpl;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.DocumentExternalFactory;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.RoleInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPermissionPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineDoseInfoDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImmunizePatientTest {

    private ImmunizePatient immunizePatient;

    @Mock
    private DocumentExternalFactory documentExternalFactory;

    @Mock
    private LocalDateMapper localDateMapper;

    @Mock
    private SharedStaffPort sharedStaffPort;

    @Mock
    private SharedPermissionPort sharedPermissionPort;

    @Mock
    private VaccineRuleStorage vaccineRuleStorage;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @Mock
    private SharedAppointmentPort sharedAppointmentPort;

    @Mock
    private VaccineConsultationRepository vaccineConsultationRepository;

    @Mock
    private VaccineSchemeRepository vaccineSchemeRepository;

    @Mock
    private VaccineConditionApplicationRepository vaccineConditionApplicationRepository;

    @BeforeEach
    public void setUp() {
        immunizePatient = new ImmunizePatient(
                new ServeAppointmentStorageImpl(false, sharedAppointmentPort),
                dateTimeProvider,
                new DoctorStorageImpl(sharedStaffPort),
                new ImmunizationDocumentStorageImpl(documentExternalFactory, localDateMapper),
                new ImmunizationUserStorageImpl(sharedPermissionPort),
                new VaccineConditionApplicationStorageImpl(vaccineConditionApplicationRepository),
                new VaccineConsultationStorageImpl(vaccineConsultationRepository),
                vaccineRuleStorage,
                new VaccineSchemeStorageImpl(vaccineSchemeRepository)
               );
    }



    @Test
    void successMixBillableAndNonBillableImmunization() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short)45, 20, "ENFERMERO")));
        when(localDateMapper.fromLocalDateToString(any())).thenReturn("2020-12-12");
        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020,12,13));
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE",
                        List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                                new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        when(vaccineSchemeRepository.existsById(any())).thenReturn(true);
        when(vaccineConditionApplicationRepository.existsById(any())).thenReturn(true);
        when(vaccineConsultationRepository.save(any()))
                .thenReturn(new VaccineConsultation(1, 20,14,45,1,65,
                        LocalDate.of(2020,12,12),true));
        when(sharedAppointmentPort.hasCurrentAppointment(any(), any(), any())).thenReturn(true);
        when(vaccineRuleStorage.existRule(any(), any(), any(), any(), any())).thenReturn(true);
        immunizePatient.run(validImmunizePatient());


        ArgumentCaptor<VaccineConsultation> vaccineConsultationArgumentCaptor = ArgumentCaptor.forClass(VaccineConsultation.class);
        verify(vaccineConsultationRepository, times(1)).save(vaccineConsultationArgumentCaptor.capture());
        Assertions.assertNull(vaccineConsultationArgumentCaptor.getValue().getId());
        Assertions.assertEquals(14, vaccineConsultationArgumentCaptor.getValue().getPatientId());
        Assertions.assertEquals(20, vaccineConsultationArgumentCaptor.getValue().getInstitutionId());
        Assertions.assertEquals(1, vaccineConsultationArgumentCaptor.getValue().getDoctorId());
        Assertions.assertTrue(vaccineConsultationArgumentCaptor.getValue().getBillable());
        Assertions.assertEquals(LocalDate.of(2020,12,13), vaccineConsultationArgumentCaptor.getValue().getPerformedDate());
        Assertions.assertEquals(65, vaccineConsultationArgumentCaptor.getValue().getClinicalSpecialtyId());

        ArgumentCaptor<DocumentDto> documentDtoArgumentCaptor = ArgumentCaptor.forClass(DocumentDto.class);
        verify(documentExternalFactory, times(1)).run(documentDtoArgumentCaptor.capture(), eq(true));
        Assertions.assertNull(documentDtoArgumentCaptor.getValue().getId());
        Assertions.assertEquals(14, documentDtoArgumentCaptor.getValue().getPatientId());
        Assertions.assertEquals(20, documentDtoArgumentCaptor.getValue().getInstitutionId());
        Assertions.assertEquals(1, documentDtoArgumentCaptor.getValue().getEncounterId());
        Assertions.assertEquals(DocumentType.IMMUNIZATION, documentDtoArgumentCaptor.getValue().getDocumentType());
        Assertions.assertEquals(SourceType.IMMUNIZATION, documentDtoArgumentCaptor.getValue().getDocumentSource());

        Assertions.assertEquals(2,
                documentDtoArgumentCaptor.getValue().getImmunizations().size());
        Assertions.assertEquals(new SnomedDto("SCTID_1","PT_ANTIGRIPAL"),
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getSnomed());
        Assertions.assertEquals(20,
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getInstitutionId());
        Assertions.assertEquals(new VaccineDoseInfoDto("Dose1", (short)1),
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getDose());
        Assertions.assertEquals((short)3,
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getConditionId());
        Assertions.assertEquals("2020-12-12",
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getAdministrationDate());
        Assertions.assertEquals("LOTE",
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getLotNumber());
        Assertions.assertEquals("Nota de vacuna",
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getNote());
        Assertions.assertTrue(documentDtoArgumentCaptor.getValue().getImmunizations().get(0).isBillable());

        verify(sharedAppointmentPort, times(1)).serveAppointment(14, 1, LocalDate.of(2020,12,13));

    }

    @Test
    void successNonBillableImmunizationCheckBillableConsultation() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short)45, 20, "ENFERMERO")));
        when(localDateMapper.fromLocalDateToString(any())).thenReturn("2020-12-12");
        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 13));
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE",
                        List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                                new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        when(vaccineConsultationRepository.save(any()))
                .thenReturn(new VaccineConsultation(1, 20, 14, 45, 1, 65,
                        LocalDate.of(2020, 12, 12), true));
        immunizePatient.run(new ImmunizePatientBo(14, 20, 65,
                List.of(noBillableImmunizationValid(), noBillableImmunizationValid())));


        ArgumentCaptor<VaccineConsultation> vaccineConsultationArgumentCaptor = ArgumentCaptor.forClass(VaccineConsultation.class);
        verify(vaccineConsultationRepository, times(1)).save(vaccineConsultationArgumentCaptor.capture());
        Assertions.assertFalse(vaccineConsultationArgumentCaptor.getValue().getBillable());
    }

    @Test
    void invalidImmunizePatientData() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short)45, 20, "ENFERMERO")));
        Exception exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(null)
        );
        assertEquals("La información de la inmunización es obligatoria", exception.getMessage());

        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(null);
        exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(validImmunizePatient())
        );
        assertEquals("El identificador del profesional es invalido", exception.getMessage());

        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", Collections.emptyList(), "NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(nullInstitution())
        );
        assertEquals("El id de la institución es obligatorio", exception.getMessage());

        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", Collections.emptyList(), "NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(nullPatient())
        );
        assertEquals("El id del paciente es obligatorio", exception.getMessage());


        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", Collections.emptyList(), "NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(nullClinicalSpecialty())
        );
        assertEquals("El id de la especialidad clínica es obligatorio", exception.getMessage());

        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(1, "Especialidad1"), new ClinicalSpecialtyDto(2, "Especialidad1")), "NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(invalidClinicalSpecialty())
        );
        assertEquals("La especialidad no pertenece al médico", exception.getMessage());
    }

    @Test
    void immunizationInfoWithoutVaccineInformation() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short)45, 20, "ENFERMERO")));
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(1, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        Exception exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(immunizationWithoutVaccine())))
        );
        assertEquals("La información de la vacuna es obligatoria", exception.getMessage());


        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(1, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(immunizationWithoutVaccineSctid())))
        );
        assertEquals("El sctid code de la vacuna es obligatoria", exception.getMessage());


        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(1, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(immunizationWithoutVaccinePreferredTerm())))
        );
        assertEquals("El termino de preferencia de la vacuna es obligatoria", exception.getMessage());
    }

    @Test
    void billableImmunizationInfoWithoutInstitution() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short)45, 20, "ENFERMERO")));
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE",
                        List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                                new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        Exception exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(billableImmunizationWithoutInstitution())))
        );
        assertEquals("La institución es obligatoria para una vacuna facturable", exception.getMessage());
    }


    @Test
    void billableImmunizationInfoWithoutAdministrationDate() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short)45, 20, "ENFERMERO")));
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE",
                        List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                                new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        Exception exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(billableImmunizationWithoutAdministrationDate())))
        );
        assertEquals("La fecha de administración es obligatoria para una vacuna facturable", exception.getMessage());
    }

    @Test
    void immunizationInfoWithInvalidContionApplicationcheme() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short)45, 20, "ENFERMERO")));
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        when(vaccineConditionApplicationRepository.existsById(any())).thenReturn(false);
        Exception exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(validImmunizePatient())
        );
        assertEquals("La vacuna PT_ANTIGRIPAL tiene una condición de aplicación invalida id=3", exception.getMessage());
    }

    @Test
    void immunizationInfoWithInvalidScheme() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short)45, 20, "ENFERMERO")));
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        when(vaccineConditionApplicationRepository.existsById(any())).thenReturn(true);
        when(vaccineSchemeRepository.existsById(any())).thenReturn(false);
        Exception exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(validImmunizePatient())
        );
        assertEquals("La vacuna PT_ANTIGRIPAL tiene un esquema invalido id=1", exception.getMessage());
    }


    @Test
    void immunizationInfoWithInvalidInformationToValidRule() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short)45, 20, "ENFERMERO")));
        when(vaccineConditionApplicationRepository.existsById(any())).thenReturn(true);
        when(vaccineSchemeRepository.existsById(any())).thenReturn(true);
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        Exception exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(immunizationWithoutDoses())))
        );
        assertEquals("Para validar las reglas de cada vacuna todos los datos siguientes son obligatorios: condición(id=3), esquema(id=1), dosis(description=null, order=null)", exception.getMessage());

        when(vaccineSchemeRepository.existsById(any())).thenReturn(true);
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(immunizationWithoutDoseDescription())))
        );
        assertEquals("Para validar las reglas de cada vacuna todos los datos siguientes son obligatorios: condición(id=3), esquema(id=1), dosis(description=null, order=1)", exception.getMessage());

        when(vaccineSchemeRepository.existsById(any())).thenReturn(true);
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(immunizationWithoutDoseOrder())))
        );
        assertEquals("Para validar las reglas de cada vacuna todos los datos siguientes son obligatorios: condición(id=3), esquema(id=1), dosis(description=Dose1, order=null)", exception.getMessage());

        when(vaccineSchemeRepository.existsById(any())).thenReturn(true);
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(immunizationWithoutScheme())))
        );
        assertEquals("Para validar las reglas de cada vacuna todos los datos siguientes son obligatorios: condición(id=3), esquema(id=null), dosis(description=Dose1, order=1)", exception.getMessage());

        when(vaccineSchemeRepository.existsById(any())).thenReturn(true);
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(immunizationWithoutCondition())))
        );
        assertEquals("Para validar las reglas de cada vacuna todos los datos siguientes son obligatorios: condición(id=null), esquema(id=1), dosis(description=Dose1, order=1)", exception.getMessage());

        when(vaccineSchemeRepository.existsById(any())).thenReturn(true);
        when(vaccineRuleStorage.existRule(any(),any(), any(), any(), any())).thenReturn(false);
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE", List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                        new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(new ImmunizePatientBo(14, 20, 2,
                        List.of(validBillableImmunization())))
        );
        assertEquals("La combinación de información no es una regla valida para nomivac -> " +
                "vacuna(sctid=SCTID_1, preferredTerm=PT_ANTIGRIPAL)," +
                " condición(id=3), esquema(id=1), dosis(description=Dose1, order=1)", exception.getMessage());
    }

    @Test
    void invalidPermissionGettingVaccine() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short) 45, 20, "ESPECIALISTA_MEDICO")));
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE",
                        List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                                new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        Exception exception = Assertions.assertThrows(RolePermissionException.class, () ->
                immunizePatient.run(validImmunizePatient())
        );
        assertEquals("El enfermero solo tiene permisos para aplicar vacunas", exception.getMessage());


        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short) 45, 20, "PROFESIONAL_DE_SALUD")));
        exception = Assertions.assertThrows(RolePermissionException.class, () ->
                immunizePatient.run(validImmunizePatient())
        );
        assertEquals("El enfermero solo tiene permisos para aplicar vacunas", exception.getMessage());

        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short) 45, 20, "ADMINISTRATIVO")));
        exception = Assertions.assertThrows(RolePermissionException.class, () ->
                immunizePatient.run(validImmunizePatient())
        );
        assertEquals("No tiene los permisos suficientes para inmunizar un paciente", exception.getMessage());
    }

    @Test
    void notCallAppointmentServeWhenNotGettingVaccine() {
        when(sharedPermissionPort.ferPermissionInfoByUserId(any())).thenReturn(List.of(new RoleInfoDto((short)45, 20, "ENFERMERO")));
        when(localDateMapper.fromLocalDateToString(any())).thenReturn("2020-12-12");
        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020,12,13));
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1, "LICENCE_NUMBER", "FIRST_NAME", "LAST_NAME", "ID_NUMBER", "PHONE",
                        List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                                new ClinicalSpecialtyDto(2, "Especialidad1")),
						"NAME_SELF_DETERMINATION"));
        when(vaccineConsultationRepository.save(any()))
                .thenReturn(new VaccineConsultation(1, 20,14,45,1,65,
                        LocalDate.of(2020,12,12),true));
        immunizePatient.run(new ImmunizePatientBo(14, 20, 65, List.of(noBillableImmunizationValid())));


        ArgumentCaptor<VaccineConsultation> vaccineConsultationArgumentCaptor = ArgumentCaptor.forClass(VaccineConsultation.class);
        verify(vaccineConsultationRepository, times(1)).save(vaccineConsultationArgumentCaptor.capture());
        Assertions.assertNull(vaccineConsultationArgumentCaptor.getValue().getId());
        Assertions.assertEquals(14, vaccineConsultationArgumentCaptor.getValue().getPatientId());
        Assertions.assertEquals(20, vaccineConsultationArgumentCaptor.getValue().getInstitutionId());
        Assertions.assertEquals(1, vaccineConsultationArgumentCaptor.getValue().getDoctorId());
        Assertions.assertFalse(vaccineConsultationArgumentCaptor.getValue().getBillable());
        Assertions.assertEquals(LocalDate.of(2020,12,13), vaccineConsultationArgumentCaptor.getValue().getPerformedDate());
        Assertions.assertEquals(65, vaccineConsultationArgumentCaptor.getValue().getClinicalSpecialtyId());

        ArgumentCaptor<DocumentDto> documentDtoArgumentCaptor = ArgumentCaptor.forClass(DocumentDto.class);
        verify(documentExternalFactory, times(1)).run(documentDtoArgumentCaptor.capture(), eq(true));
        Assertions.assertNull(documentDtoArgumentCaptor.getValue().getId());
        Assertions.assertEquals(14, documentDtoArgumentCaptor.getValue().getPatientId());
        Assertions.assertEquals(20, documentDtoArgumentCaptor.getValue().getInstitutionId());
        Assertions.assertEquals(1, documentDtoArgumentCaptor.getValue().getEncounterId());
        Assertions.assertEquals(DocumentType.IMMUNIZATION, documentDtoArgumentCaptor.getValue().getDocumentType());
        Assertions.assertEquals(SourceType.IMMUNIZATION, documentDtoArgumentCaptor.getValue().getDocumentSource());

        Assertions.assertEquals(1,
                documentDtoArgumentCaptor.getValue().getImmunizations().size());
        Assertions.assertEquals(new SnomedDto("SCTID_1","PT_ANTIGRIPAL"),
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getSnomed());
        Assertions.assertNull(documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getInstitutionId());
        Assertions.assertNull(documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getDose());
        Assertions.assertNull(documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getConditionId());
        Assertions.assertEquals("2020-12-12",
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getAdministrationDate());
        Assertions.assertEquals("LOTE",
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getLotNumber());
        Assertions.assertEquals("Nota de vacuna",
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getNote());
        Assertions.assertFalse(documentDtoArgumentCaptor.getValue().getImmunizations().get(0).isBillable());

        verify(sharedAppointmentPort, times(0)).serveAppointment(14, 1, LocalDate.of(2020,12,13));
    }

    private ImmunizePatientBo validImmunizePatient(){
        return new ImmunizePatientBo(14, 20, 65, List.of(validBillableImmunization(), noBillableImmunizationValid()));
    }

    private ImmunizePatientBo nullInstitution() {
        return new ImmunizePatientBo(14, null, 65, List.of(validBillableImmunization()));
    }

    private ImmunizePatientBo nullPatient() {
        return new ImmunizePatientBo(null, 20, 65, List.of(validBillableImmunization()));
    }

    private ImmunizePatientBo nullClinicalSpecialty() {
        return new ImmunizePatientBo(43, 20, null, List.of(validBillableImmunization()));
    }

    private ImmunizePatientBo invalidClinicalSpecialty() {
        return new ImmunizePatientBo(43, 20, 44, List.of(validBillableImmunization()));
    }

    private ImmunizationInfoBo validBillableImmunization() {
        return new ImmunizationInfoBo(null, 20, "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, "SCTID_1","PT_ANTIGRIPAL", "PARENT_ID", "ANTIGRIPAL_PARENT"),
                (short) 3,
                (short) 1,
                new VaccineDoseBo("Dose1", (short)1),
                LocalDate.of(2020, 12,12),
                "LOTE",
                "Nota de vacuna",
                true);
    }

    private ImmunizationInfoBo billableImmunizationWithoutInstitution() {
        return new ImmunizationInfoBo(null, null, "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, "SCTID_1","PT_ANTIGRIPAL", "PARENT_ID", "ANTIGRIPAL_PARENT"),
                (short) 3,
                (short) 1,
                new VaccineDoseBo("Dose1", (short)1),
                LocalDate.of(2020, 12,12),
                "LOTE",
                "Nota de vacuna",
                true);
    }


    private ImmunizationInfoBo billableImmunizationWithoutAdministrationDate() {
        return new ImmunizationInfoBo(null, 20, "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, "SCTID_1","PT_ANTIGRIPAL", "PARENT_ID", "ANTIGRIPAL_PARENT"),
                (short) 3,
                (short) 1,
                new VaccineDoseBo("Dose1", (short)1),
                null,
                "LOTE",
                "Nota de vacuna",
                true);
    }

    private ImmunizationInfoBo immunizationWithoutCondition() {
        return new ImmunizationInfoBo(null, 20, "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, "SCTID_1","PT_ANTIGRIPAL", "PARENT_ID", "ANTIGRIPAL_PARENT"),
                null,
                (short) 1,
                new VaccineDoseBo("Dose1", (short)1),
                LocalDate.of(2020, 12,12),
                "LOTE",
                "Nota de vacuna",
                true);
    }

    private ImmunizationInfoBo immunizationWithoutScheme() {
        return new ImmunizationInfoBo(null, 20, "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, "SCTID_1","PT_ANTIGRIPAL", "PARENT_ID", "ANTIGRIPAL_PARENT"),
                (short)3,
                null,
                new VaccineDoseBo("Dose1", (short)1),
                LocalDate.of(2020, 12,12),
                "LOTE",
                "Nota de vacuna",
                true);
    }

    private ImmunizationInfoBo immunizationWithoutDoses() {
        return new ImmunizationInfoBo(null, 20, "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, "SCTID_1","PT_ANTIGRIPAL", "PARENT_ID", "ANTIGRIPAL_PARENT"),
                (short)3,
                (short) 1,
                null,
                LocalDate.of(2020, 12,12),
                "LOTE",
                "Nota de vacuna",
                true);
    }

    private ImmunizationInfoBo immunizationWithoutDoseDescription() {
        return new ImmunizationInfoBo(null, 20, "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, "SCTID_1","PT_ANTIGRIPAL", "PARENT_ID", "ANTIGRIPAL_PARENT"),
                (short)3,
                (short) 1,
                new VaccineDoseBo(null, (short)1),
                LocalDate.of(2020, 12,12),
                "LOTE",
                "Nota de vacuna",
                true);
    }

    private ImmunizationInfoBo immunizationWithoutDoseOrder() {
        return new ImmunizationInfoBo(null, 20, "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, "SCTID_1","PT_ANTIGRIPAL", "PARENT_ID", "ANTIGRIPAL_PARENT"),
                (short) 3,
                (short) 1,
                new VaccineDoseBo("Dose1", null),
                LocalDate.of(2020, 12,12),
                "LOTE",
                "Nota de vacuna",
                true);
    }

    private ImmunizationInfoBo noBillableImmunizationValid() {
        return new ImmunizationInfoBo(null, null, "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, "SCTID_1","PT_ANTIGRIPAL", "PARENT_ID", "ANTIGRIPAL_PARENT"),
                null,
                null,
                null,
                null,
                "LOTE",
                "Nota de vacuna",
                false);
    }

    private ImmunizationInfoBo immunizationWithoutVaccine() {
        return new ImmunizationInfoBo(null, null,  "INSTITUTION_INFO", "DOCTOR_INFO",
                null,
                null,
                null,
                null,
                null,
                "LOTE",
                "Nota de vacuna",
                true);
    }

    private ImmunizationInfoBo immunizationWithoutVaccineSctid() {
        return new ImmunizationInfoBo(null, null,  "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, null, "FSN_TERM", null, "PARENT_FSN"),
                null,
                null,
                null,
                null,
                "LOTE",
                "Nota de vacuna",
                true);
    }

    private ImmunizationInfoBo immunizationWithoutVaccinePreferredTerm() {
        return new ImmunizationInfoBo(null, null, "INSTITUTION_INFO", "DOCTOR_INFO",
                new SnomedBo(null, "SCTID", null, null, "PARENT_FSN"),
                null,
                null,
                null,
                null,
                "LOTE",
                "Nota de vacuna",
                true);
    }
}