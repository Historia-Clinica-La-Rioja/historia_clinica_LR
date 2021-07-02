package ar.lamansys.immunization.infrastructure.input.rest;

import ar.lamansys.immunization.application.immunizePatient.ImmunizePatient;
import ar.lamansys.immunization.domain.consultation.ImmunizePatientBo;
import ar.lamansys.immunization.domain.snomed.SnomedBo;
import ar.lamansys.immunization.domain.vaccine.conditionapplication.VaccineConditionApplicationBo;
import ar.lamansys.immunization.domain.vaccine.conditionapplication.VaccineConditionApplicationException;
import ar.lamansys.immunization.domain.vaccine.doses.VaccineDoseBo;
import ar.lamansys.immunization.domain.vaccine.doses.VaccineDosisException;
import ar.lamansys.immunization.infrastructure.input.rest.dto.ImmunizePatientDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ImmunizationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ImmunizeControllerTest {

    private ImmunizeController immunizeController;

    @Mock
    private ImmunizePatient immunizePatient;

    @Mock
    private LocalDateMapper localDateMapper;

    @BeforeEach
    public void setUp() {
        immunizeController = new ImmunizeController(immunizePatient, localDateMapper);
    }

    @Test
    void successMapping() {
        when(localDateMapper.fromStringToLocalDate(any())).thenReturn(LocalDate.of(2020,12,12));
        var immunizePatientDto = new ImmunizePatientDto();
        immunizePatientDto.setClinicalSpecialtyId(4);
        immunizePatientDto.setImmunizations(List.of(createImmunization()));
        immunizeController.immunizePatient(1, 2, immunizePatientDto);

        ArgumentCaptor<ImmunizePatientBo> immunizePatientBoArgumentCaptor = ArgumentCaptor.forClass(ImmunizePatientBo.class);
        verify(immunizePatient, times(1)).run(immunizePatientBoArgumentCaptor.capture());
        Assertions.assertEquals(2, immunizePatientBoArgumentCaptor.getValue().getPatientId());
        Assertions.assertEquals(1, immunizePatientBoArgumentCaptor.getValue().getInstitutionId());
        Assertions.assertEquals(4, immunizePatientBoArgumentCaptor.getValue().getClinicalSpecialtyId());
        Assertions.assertEquals(1, immunizePatientBoArgumentCaptor.getValue().getImmunizations().size());
        Assertions.assertNull(immunizePatientBoArgumentCaptor.getValue().getImmunizations().get(0).getId());

        Assertions.assertEquals(LocalDate.of(2020,12,12), immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getAdministrationDate());

        Assertions.assertEquals(VaccineDoseBo.DOSE_0, immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getDose());

        Assertions.assertEquals(VaccineConditionApplicationBo.NATIONAL_CALENDAR, immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getCondition());

        Assertions.assertEquals((short)1, immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getSchemeId());

        Assertions.assertEquals(new SnomedBo(null, "SNOMED_ID", "SNOMED_PT", null, null), immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getVaccine());

        Assertions.assertEquals("Nota de prueba", immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getNote());

        Assertions.assertEquals("BATCH", immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getBatchNumber());
    }


    @Test
    void failVaccineCondition() {
        when(localDateMapper.fromStringToLocalDate(any())).thenReturn(LocalDate.of(2020,12,12));
        var immunizePatientDto = new ImmunizePatientDto();
        immunizePatientDto.setClinicalSpecialtyId(4);
        var immunization = createImmunization();
        immunization.setConditionId((short)1);
        immunizePatientDto.setImmunizations(List.of(immunization));
        Exception exception = Assertions.assertThrows(VaccineConditionApplicationException.class, () ->
                immunizeController.immunizePatient(1, 2, immunizePatientDto)
        );
        assertEquals("La condición de aplicación 1 no existe", exception.getMessage());

    }

    @Test
    void failVaccineDose() {
        when(localDateMapper.fromStringToLocalDate(any())).thenReturn(LocalDate.of(2020,12,12));
        var immunizePatientDto = new ImmunizePatientDto();
        immunizePatientDto.setClinicalSpecialtyId(4);
        var immunization = createImmunization();
        immunization.setDoseId((short)-1);
        immunizePatientDto.setImmunizations(List.of(immunization));
        Exception exception = Assertions.assertThrows(VaccineDosisException.class, () ->
                immunizeController.immunizePatient(1, 2, immunizePatientDto)
        );
        assertEquals("La dosis -1 no existe", exception.getMessage());

    }

    private ImmunizationDto createImmunization() {
        var result = new ImmunizationDto();
        result.setSnomed(new SnomedDto("SNOMED_ID", "SNOMED_PT"));
        result.setAdministrationDate("2020-05-10");
        result.setInstitutionId(null);
        result.setNote("Nota de prueba");
        result.setDoseId((short)1);
        result.setConditionId((short)3);
        result.setSchemeId((short)1);
        result.setBatchNumber("BATCH");
        result.setSnomedCommercial(new SnomedDto("SNOMED_COMERCIAL_ID", "SNOMED_COMERCIAL_PT"));
        return result;
    }


}