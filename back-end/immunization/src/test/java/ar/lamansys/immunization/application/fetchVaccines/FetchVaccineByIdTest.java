package ar.lamansys.immunization.application.fetchVaccines;

import ar.lamansys.immunization.application.fetchVaccines.exceptions.FetchVaccineByIdException;
import ar.lamansys.immunization.domain.patient.PatientInfoBo;
import ar.lamansys.immunization.domain.patient.PatientInfoPort;
import ar.lamansys.immunization.domain.vaccine.Thresholds;
import ar.lamansys.immunization.domain.vaccine.VaccineBo;
import ar.lamansys.immunization.domain.vaccine.VaccineDescription;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineStorageMockImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchVaccineByIdTest {

    private FetchVaccineById fetchVaccineById;

    @BeforeEach
    public void setUp() {
        fetchVaccineById = new FetchVaccineById(new VaccineStorageMockImpl());
    }

    @Test
    void success() {
        var result = fetchVaccineById.run((short)130);

        Assertions.assertEquals((short)130, result.getId());
        Assertions.assertEquals(new VaccineDescription("Quintuple"), result.getDescription());
        Assertions.assertEquals(new Thresholds(42, 2189), result.getThreshold());
        Assertions.assertEquals(8, result.getRules().size());
    }

    @Test
    void unknownVaccine() {
        Exception exception = Assertions.assertThrows(FetchVaccineByIdException.class, () ->
                fetchVaccineById.run((short) -5)
        );
        assertEquals("La vacuna con id -5 no existe", exception.getMessage());
    }
}