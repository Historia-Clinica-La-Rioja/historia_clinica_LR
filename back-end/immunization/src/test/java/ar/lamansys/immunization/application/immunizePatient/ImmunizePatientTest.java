package ar.lamansys.immunization.application.immunizePatient;

import ar.lamansys.immunization.application.immunizePatient.exceptions.ImmunizePatientException;
import ar.lamansys.immunization.domain.consultation.ImmunizePatientBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationInfoBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationValidatorException;
import ar.lamansys.immunization.domain.snomed.SnomedBo;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeStorage;
import ar.lamansys.immunization.domain.vaccine.conditionapplication.VaccineConditionApplicationBo;
import ar.lamansys.immunization.domain.vaccine.doses.VaccineDoseBo;
import ar.lamansys.immunization.infrastructure.output.repository.appointments.ServeAppointmentStorageImpl;
import ar.lamansys.immunization.infrastructure.output.repository.consultation.DoctorStorageImpl;
import ar.lamansys.immunization.infrastructure.output.repository.consultation.VaccineConsultation;
import ar.lamansys.immunization.infrastructure.output.repository.consultation.VaccineConsultationRepository;
import ar.lamansys.immunization.infrastructure.output.repository.consultation.VaccineConsultationStorageImpl;
import ar.lamansys.immunization.infrastructure.output.repository.document.ImmunizationDocumentStorageImpl;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineSchemeRepository;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineSchemeStorageImpl;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.DocumentExternalFactory;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
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
import static org.mockito.ArgumentMatchers.any;
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
    private VaccineSchemeStorage vaccineSchemeStorage;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @Mock
    private SharedAppointmentPort sharedAppointmentPort;

    @Mock
    private VaccineConsultationRepository vaccineConsultationRepository;

    @Mock
    private VaccineSchemeRepository vaccineSchemeRepository;

    @BeforeEach
    public void setUp() {
        immunizePatient = new ImmunizePatient(
                new VaccineConsultationStorageImpl(vaccineConsultationRepository),
                new ImmunizationDocumentStorageImpl(documentExternalFactory, localDateMapper),
                new DoctorStorageImpl(sharedStaffPort), new VaccineSchemeStorageImpl(vaccineSchemeRepository),
                new ServeAppointmentStorageImpl(false, sharedAppointmentPort),
                dateTimeProvider);
    }



    @Test
    void success() {
        when(localDateMapper.fromLocalDateToString(any())).thenReturn("2020-12-12");
        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020,12,13));
        when(sharedStaffPort.getProfessionalCompleteInfo(any()))
                .thenReturn(new ProfessionalInfoDto(1,
                        List.of(new ClinicalSpecialtyDto(65, "Especialidad1"),
                                new ClinicalSpecialtyDto(2, "Especialidad1"))));
        when(vaccineSchemeRepository.existsById(any())).thenReturn(true);
        when(vaccineConsultationRepository.save(any()))
                .thenReturn(new VaccineConsultation(1, 20,14,1,65,
                        LocalDate.of(2020,12,12),true));
        when(sharedAppointmentPort.hasConfirmedAppointment(any(), any(), any())).thenReturn(true);
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

        Assertions.assertEquals(1,
                documentDtoArgumentCaptor.getValue().getImmunizations().size());
        Assertions.assertEquals(new SnomedDto("SCTID_1","PT_ANTIGRIPAL"),
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getSnomed());
        Assertions.assertEquals(20,
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getInstitutionId());
        Assertions.assertEquals(VaccineDoseBo.DOSE_1.getId(),
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getDoseId());
        Assertions.assertEquals(VaccineConditionApplicationBo.NATIONAL_CALENDAR.getId(),
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getConditionId());
        Assertions.assertEquals("2020-12-12",
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getAdministrationDate());
        Assertions.assertEquals("LOTE",
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getLotNumber());
        Assertions.assertEquals("Nota de vacuna",
                documentDtoArgumentCaptor.getValue().getImmunizations().get(0).getNote());

        verify(sharedAppointmentPort, times(1)).serveAppointment(eq(14), eq(1), eq(LocalDate.of(2020,12,13)));

    }


    @Test
    void invalidInputData() {
        Exception exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(null)
        );
        assertEquals("La información de la inmunización es obligatoria", exception.getMessage());

        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(null);
        exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(validImmunizePatient())
        );
        assertEquals("El identificador del profesional es invalido", exception.getMessage());

        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(new ProfessionalInfoDto(1, Collections.emptyList()));
        exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(nullInstitution())
        );
        assertEquals("El id de la institución es obligatorio", exception.getMessage());

        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(new ProfessionalInfoDto(1, Collections.emptyList()));
        exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(nullPatient())
        );
        assertEquals("El id del paciente es obligatorio", exception.getMessage());


        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(new ProfessionalInfoDto(1, Collections.emptyList()));
        exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(nullClinicalSpecialty())
        );
        assertEquals("El id de la especialidad clínica es obligatorio", exception.getMessage());

        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(new ProfessionalInfoDto(1, List.of(new ClinicalSpecialtyDto(1, "Especialidad1"), new ClinicalSpecialtyDto(2, "Especialidad1"))));
        exception = Assertions.assertThrows(ImmunizePatientException.class, () ->
                immunizePatient.run(invalidClinicalSpecialty())
        );
        assertEquals("La especialidad no pertenece al médico", exception.getMessage());

        when(sharedStaffPort.getProfessionalCompleteInfo(any())).thenReturn(new ProfessionalInfoDto(1, List.of(new ClinicalSpecialtyDto(65, "Especialidad1"), new ClinicalSpecialtyDto(2, "Especialidad1"))));
        when(vaccineSchemeRepository.existsById(any())).thenReturn(false);
        exception = Assertions.assertThrows(ImmunizationValidatorException.class, () ->
                immunizePatient.run(validImmunizePatient())
        );
        assertEquals("La vacuna PT_ANTIGRIPAL tiene un esquema invalido 1", exception.getMessage());
    }

    private ImmunizePatientBo validImmunizePatient(){
        return new ImmunizePatientBo(14, 20, 65, List.of(validImmunization()));
    }

    private ImmunizePatientBo nullInstitution() {
        return new ImmunizePatientBo(14, null, 65, List.of(validImmunization()));
    }

    private ImmunizePatientBo nullPatient() {
        return new ImmunizePatientBo(null, 23, 65, List.of(validImmunization()));
    }

    private ImmunizePatientBo nullClinicalSpecialty() {
        return new ImmunizePatientBo(43, 23, null, List.of(validImmunization()));
    }

    private ImmunizePatientBo invalidClinicalSpecialty() {
        return new ImmunizePatientBo(43, 23, 44, List.of(validImmunization()));
    }

    private ImmunizationInfoBo validImmunization() {
        return new ImmunizationInfoBo(null, 20,
                new SnomedBo(null, "SCTID_1","PT_ANTIGRIPAL", "PARENT_ID", "ANTIGRIPAL_PARENT"),
                VaccineConditionApplicationBo.NATIONAL_CALENDAR.getId(),
                (short) 1,
                VaccineDoseBo.DOSE_1.getId(),
                LocalDate.of(2020, 12,12),
                "LOTE",
                "Nota de vacuna");
    }


}