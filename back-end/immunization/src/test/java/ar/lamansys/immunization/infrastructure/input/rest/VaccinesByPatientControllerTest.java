package ar.lamansys.immunization.infrastructure.input.rest;

import ar.lamansys.immunization.application.FetchVaccines.FetchVaccinesByPatient;
import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.domain.vaccine.VaccineRuleBo;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.immunization.domain.vaccine.conditionapplication.VaccineConditionApplicationBo;
import ar.lamansys.immunization.domain.vaccine.doses.VaccineDoseBo;
import ar.lamansys.immunization.infrastructure.input.rest.dto.VaccineInformationDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VaccinesByPatientControllerTest {

    private VaccinesByPatientController vaccinesByPatientController;

    @Mock
    private FetchVaccinesByPatient fetchVaccinesByPatient;

    @Mock
    private LocalDateMapper localDateMapper;

    @BeforeEach
    public void setUp() {
        vaccinesByPatientController = new VaccinesByPatientController(fetchVaccinesByPatient, localDateMapper);
    }

    @Test
    void patientDontNeedVaccines() {
        when(fetchVaccinesByPatient.run(any(), any()))
                .thenReturn(Collections.emptyList());
        when(localDateMapper.fromStringToLocalDate(any()))
                .thenReturn(LocalDate.of(2020, 12,12));

        List<VaccineInformationDto> result = vaccinesByPatientController.get(1, "2020-12-12");


        Assertions.assertEquals(result.size(), 0);
    }

    @Test
    void patientNeedMeningocócicaVaccines() {
        VaccineSchemeBo asplenicoGT5 = new VaccineSchemeBo((short)437, "Asplénico mayor de 5 años", 1825, 42000);
        VaccineSchemeBo asplenicoLT5 = new VaccineSchemeBo((short)436, "Asplénico menor de 5 años", 42, 1824);
        VaccineSchemeBo atrasadoAdolescente = new VaccineSchemeBo((short)383, "Atrasado Adolescente", 4384, 5474);
        VaccineSchemeBo atrasadoLactante = new VaccineSchemeBo((short)382, "Atrasado Lactantes", 150, 1459);
        VaccineSchemeBo regularAdolescente = new VaccineSchemeBo((short)305, "Regular Adolescentes", 3654, 4383);
        VaccineSchemeBo regularLactante = new VaccineSchemeBo((short)304, "Regular Lactantes", 56, 720);

        when(fetchVaccinesByPatient.run(any(), any()))
                .thenReturn(List.of(
                        new VaccineBo((short)162, "Meningocócica Tetravalente Conjugada", 56, 23725,
                                List.of(new VaccineRuleBo(0, 0, 0, asplenicoGT5 , VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.ASPLENIC),
                                        new VaccineRuleBo(0, 0, 56, asplenicoGT5, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.ASPLENIC),
                                        new VaccineRuleBo(0, 0, 0, asplenicoLT5, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.ASPLENIC),
                                        new VaccineRuleBo(0, 0, 56, asplenicoLT5, VaccineDoseBo.DOSE_2, VaccineConditionApplicationBo.ASPLENIC),
                                        new VaccineRuleBo(0, 0, 56, asplenicoLT5, VaccineDoseBo.DOSE_3, VaccineConditionApplicationBo.ASPLENIC),
                                        new VaccineRuleBo(0, 0, 56, asplenicoLT5, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.ASPLENIC),
                                        new VaccineRuleBo(0, 0, 0, atrasadoAdolescente, VaccineDoseBo.UNIQUE_DOSE, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                        new VaccineRuleBo(150, 1459, 0, atrasadoLactante, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                        new VaccineRuleBo(210, 1459, 56, atrasadoLactante, VaccineDoseBo.DOSE_2, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                        new VaccineRuleBo(730, 1459, 56, atrasadoLactante, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                        new VaccineRuleBo(0, 0, 0, regularAdolescente, VaccineDoseBo.UNIQUE_DOSE, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                        new VaccineRuleBo(56, 149, 0, regularLactante, VaccineDoseBo.DOSE_1, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                        new VaccineRuleBo(150, 209, 56, regularLactante, VaccineDoseBo.DOSE_2, VaccineConditionApplicationBo.NATIONAL_CALENDAR),
                                        new VaccineRuleBo(360, 729, 56, regularLactante, VaccineDoseBo.REINFORCEMENT, VaccineConditionApplicationBo.NATIONAL_CALENDAR)))));
        when(localDateMapper.fromStringToLocalDate(any()))
                .thenReturn(LocalDate.of(2020, 12,12));

        List<VaccineInformationDto> result = vaccinesByPatientController.get(1, "2020-12-12");


        Assertions.assertEquals(result.size(), 1);

        Assertions.assertEquals(result.get(0).getConditions().size(), 2);

        Assertions.assertEquals(result.get(0).getConditions().get(0).getSchemes().size(), 2);

        Assertions.assertEquals(result.get(0).getConditions().get(1).getSchemes().size(), 4);
    }
}