package ar.lamansys.immunization.application.fetchVaccineSchemeInfo;

import ar.lamansys.immunization.application.fetchVaccineSchemeInfo.exceptions.FetchVaccineSchemeException;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineScheme;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineSchemeRepository;
import ar.lamansys.immunization.infrastructure.output.repository.vaccine.VaccineSchemeStorageImpl;
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
class FetchVaccineSchemeInfoTest {

    private FetchVaccineSchemeInfo fetchVaccineSchemeInfo;

    @Mock
    private VaccineSchemeRepository vaccineSchemeRepository;

    @BeforeEach
    void setUp(){
        fetchVaccineSchemeInfo = new FetchVaccineSchemeInfo(new VaccineSchemeStorageImpl(vaccineSchemeRepository));
    }


    @Test
    void success(){
        when(vaccineSchemeRepository.findById(any())).thenReturn(
                Optional.of(new VaccineScheme((short) -4, "DESC", 4, 3222)));
        var result = fetchVaccineSchemeInfo.run((short)5);
        Assertions.assertEquals(result.getId(), (short)-4);
        Assertions.assertEquals(result.getDescription(), "DESC");
        Assertions.assertEquals(result.getThreshold().getMinimum(), 4);
        Assertions.assertEquals(result.getThreshold().getMaximum(), 3222);
    }


    @Test
    void unknownScheme(){
        when(vaccineSchemeRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(FetchVaccineSchemeException.class, () ->
                fetchVaccineSchemeInfo.run((short)5)
        );
        assertEquals("El id 5 es desconocido", exception.getMessage());
    }
}