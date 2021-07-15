package ar.lamansys.immunization.application.fetchVaccines;

import ar.lamansys.immunization.domain.vaccine.Thresholds;
import ar.lamansys.immunization.domain.vaccine.VaccineDescription;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineStorageMockImpl;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnowstormPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class FetchVaccineByIdTest {

    private FetchVaccineById fetchVaccineById;

    @Mock
    private SharedSnowstormPort sharedSnowstormPort;

    @BeforeEach
    public void setUp() {
        fetchVaccineById = new FetchVaccineById(new VaccineStorageMockImpl(sharedSnowstormPort));
    }

    @Test
    void success() {
        var result = fetchVaccineById.run("130");

        Assertions.assertEquals((short)130, result.getId());
        Assertions.assertEquals(new VaccineDescription("Quintuple"), result.getDescription());
        Assertions.assertEquals(new Thresholds(42, 2189), result.getThreshold());
        Assertions.assertEquals(8, result.getRules().size());
    }

    @Test
    void unknownVaccine() {
        var result = fetchVaccineById.run("-5");
        assertNull(result);
    }
}