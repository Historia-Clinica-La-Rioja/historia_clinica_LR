package ar.lamansys.immunization.application.fetchVaccineConditionApplicationInfo;

import ar.lamansys.immunization.application.fetchVaccineConditionApplicationInfo.exceptions.FetchVaccineConditionApplicationException;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineConditionApplication;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineConditionApplicationRepository;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineConditionApplicationStorageImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FetchVaccineConditionApplicationInfoTest {

    private FetchVaccineConditionApplicationInfo fetchVaccineConditionApplicationInfo;

    @Mock
    private VaccineConditionApplicationRepository vaccineConditionApplicationRepository;

    @BeforeEach
    void setUp(){
        fetchVaccineConditionApplicationInfo =
                new FetchVaccineConditionApplicationInfo(new VaccineConditionApplicationStorageImpl(vaccineConditionApplicationRepository));
    }


    @Test
    void success(){
        when(vaccineConditionApplicationRepository.findById(any())).thenReturn(
                Optional.of(new VaccineConditionApplication((short) -4, "CONDITION_APPLICATION")));
        var result = fetchVaccineConditionApplicationInfo.run((short)-4);
        Assertions.assertEquals(result.getId(), (short)-4);
        Assertions.assertEquals(result.getDescription(), "CONDITION_APPLICATION");
    }


    @Test
    void unknownScheme(){
        when(vaccineConditionApplicationRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(FetchVaccineConditionApplicationException.class, () ->
                fetchVaccineConditionApplicationInfo.run((short)5)
        );
        assertEquals("El id 5 es desconocido", exception.getMessage());
    }
}