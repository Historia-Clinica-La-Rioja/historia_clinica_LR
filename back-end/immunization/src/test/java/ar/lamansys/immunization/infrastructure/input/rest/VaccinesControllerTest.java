package ar.lamansys.immunization.infrastructure.input.rest;

import ar.lamansys.immunization.application.fetchVaccines.FetchVaccineById;
import ar.lamansys.immunization.domain.vaccine.VaccineBo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class VaccinesControllerTest {

    private VaccinesController vaccinesController;

    @Mock
    private FetchVaccineById fetchVaccineById;

    @BeforeEach
    public void setUp() {
        vaccinesController = new VaccinesController(fetchVaccineById);
    }

    @Test
    void success() {
        when(fetchVaccineById.run(any()))
                .thenReturn(new VaccineBo((short)-4, (short)4, "DESC", 4, 5, Collections.emptyList()));
        var result = vaccinesController.vaccineInformation("4");

        Assertions.assertEquals((short)-4, result.getId());
        Assertions.assertEquals("DESC", result.getDescription());
        Assertions.assertTrue(result.getConditions().isEmpty());
    }


    @Test
    void unknownVaccine() {
        when(fetchVaccineById.run(any()))
                .thenReturn(null);
        var result = vaccinesController.vaccineInformation("4");

        Assertions.assertNull(result);
    }

}