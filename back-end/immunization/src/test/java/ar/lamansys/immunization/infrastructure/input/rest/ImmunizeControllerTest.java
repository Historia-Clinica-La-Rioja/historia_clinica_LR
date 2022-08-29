package ar.lamansys.immunization.infrastructure.input.rest;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.immunization.application.immunizePatient.ImmunizePatient;
import ar.lamansys.immunization.domain.consultation.ImmunizePatientBo;
import ar.lamansys.immunization.domain.snomed.SnomedBo;
import ar.lamansys.immunization.domain.vaccine.VaccineDoseBo;
import ar.lamansys.immunization.infrastructure.input.rest.dto.ImmunizePatientDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ImmunizationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineDoseInfoDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;


@ExtendWith(MockitoExtension.class)
class ImmunizeControllerTest {

    private ImmunizeController immunizeController;

    @Mock
    private ImmunizePatient immunizePatient;

    @Mock
    private LocalDateMapper localDateMapper;

	@Mock
	private SharedAppointmentPort sharedAppointmentPort;

    @BeforeEach
    public void setUp() {
        immunizeController = new ImmunizeController(immunizePatient, localDateMapper, sharedAppointmentPort);
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

        Assertions.assertEquals(new VaccineDoseBo("dose0", (short)0), immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getDose());

        Assertions.assertEquals((short)3 , immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getConditionId());

        Assertions.assertEquals((short)1, immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getSchemeId());

        Assertions.assertEquals(new SnomedBo(null, "SNOMED_ID", "SNOMED_PT", null, null), immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getVaccine());

        Assertions.assertEquals("Nota de prueba", immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getNote());

        Assertions.assertEquals("BATCH", immunizePatientBoArgumentCaptor.getValue()
                .getImmunizations().get(0).getLotNumber());
    }


    private ImmunizationDto createImmunization() {
        var result = new ImmunizationDto();
        result.setSnomed(new SnomedDto("SNOMED_ID", "SNOMED_PT"));
        result.setAdministrationDate("2020-05-10");
        result.setInstitutionId(null);
        result.setNote("Nota de prueba");
        result.setDose(new VaccineDoseInfoDto("dose0", (short)0));
        result.setConditionId((short)3);
        result.setSchemeId((short)1);
        result.setLotNumber("BATCH");
        return result;
    }


}