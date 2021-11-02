package ar.lamansys.odontology.application.fetchCpoCeoIndices;

import ar.lamansys.odontology.domain.consultation.ConsultationCpoCeoIndicesStorage;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchCpoCeoIndicesTest {

    private FetchCpoCeoIndices fetchCpoCeoIndices;

    @Mock
    private ConsultationCpoCeoIndicesStorage consultationCpoCeoIndicesStorage;

    @BeforeEach
    void setUp() {
        fetchCpoCeoIndices = new FetchCpoCeoIndices(consultationCpoCeoIndicesStorage);
    }

    @Test
    void shouldReturnFirstAndLastConsultationIndices() {
        Integer patientId = 50;
        when(consultationCpoCeoIndicesStorage.getConsultationIndices(patientId)).thenReturn(
                Arrays.asList(
                        new CpoCeoIndicesBo(1, 2, 3,
                                0, 0, 0,
                                null, null, null, null,
                                LocalDateTime.of(2020, 6, 18, 15, 0)),

                        new CpoCeoIndicesBo(0, 2, 1,
                                0, 0, 0,
                                null, null, null, null,
                                LocalDateTime.of(2020, 6, 16, 15, 0)),

                        new CpoCeoIndicesBo(1, 2, 0,
                                0, 0, 0,
                                null, null, null, null,
                                LocalDateTime.of(2020, 5, 5, 12, 0)),

                        new CpoCeoIndicesBo(0, 1, 0,
                                0, 0, 0,
                                null, null, null, null,
                                LocalDateTime.of(2020, 1, 13, 10, 0))
                )
        );

        List<CpoCeoIndicesBo> result = fetchCpoCeoIndices.run(patientId);
        Assertions.assertEquals(2, result.size());

        CpoCeoIndicesBo first = result.get(0);
        CpoCeoIndicesBo second = result.get(1);

        // C P O
        Assertions.assertEquals(1, first.getPermanentC());
        Assertions.assertEquals(2, first.getPermanentP());
        Assertions.assertEquals(3, first.getPermanentO());
        // c e o
        Assertions.assertEquals(0, first.getTemporaryC());
        Assertions.assertEquals(0, first.getTemporaryE());
        Assertions.assertEquals(0, first.getTemporaryO());
        // CPO ceo indices
        Assertions.assertEquals(6, first.getCpoIndex());
        Assertions.assertEquals(0, first.getCeoIndex());
        // Teeth present
        Assertions.assertEquals(null, first.getPermanentTeethPresent());
        Assertions.assertEquals(null, first.getTemporaryTeethPresent());


        // C P O
        Assertions.assertEquals(0, second.getPermanentC());
        Assertions.assertEquals(1, second.getPermanentP());
        Assertions.assertEquals(0, second.getPermanentO());
        // c e o
        Assertions.assertEquals(0, second.getTemporaryC());
        Assertions.assertEquals(0, second.getTemporaryE());
        Assertions.assertEquals(0, second.getTemporaryO());
        // CPO ceo indices
        Assertions.assertEquals(1, second.getCpoIndex());
        Assertions.assertEquals(0, second.getCeoIndex());
        // Teeth present
        Assertions.assertEquals(null, second.getPermanentTeethPresent());
        Assertions.assertEquals(null, second.getTemporaryTeethPresent());
    }

    @Test
    void shouldRunSuccessfullyWhenPatientHasNoPreviousConsultationIndices() {
        Integer patientId = 50;
        when(consultationCpoCeoIndicesStorage.getConsultationIndices(patientId)).thenReturn(
                List.of()
        );

        List<CpoCeoIndicesBo> result = fetchCpoCeoIndices.run(patientId);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void shouldReturnOnlyOneElementWhenPatientHasOnlyOnePreviousConsultationIndices() {
        Integer patientId = 50;
        when(consultationCpoCeoIndicesStorage.getConsultationIndices(patientId)).thenReturn(
                Arrays.asList(
                        new CpoCeoIndicesBo(1, 2, 3,
                                0, 0, 0,
                                null, null, null, null,
                                LocalDateTime.of(2020, 6, 18, 15, 0))
                )
        );

        List<CpoCeoIndicesBo> result = fetchCpoCeoIndices.run(patientId);
        Assertions.assertEquals(1, result.size());

        CpoCeoIndicesBo first = result.get(0);

        // C P O
        Assertions.assertEquals(1, first.getPermanentC());
        Assertions.assertEquals(2, first.getPermanentP());
        Assertions.assertEquals(3, first.getPermanentO());
        // c e o
        Assertions.assertEquals(0, first.getTemporaryC());
        Assertions.assertEquals(0, first.getTemporaryE());
        Assertions.assertEquals(0, first.getTemporaryO());
        // CPO ceo indices
        Assertions.assertEquals(6, first.getCpoIndex());
        Assertions.assertEquals(0, first.getCeoIndex());
        // Teeth present
        Assertions.assertEquals(null, first.getPermanentTeethPresent());
        Assertions.assertEquals(null, first.getTemporaryTeethPresent());
    }


    @Test
    void shouldGetPresentTeethValues() {
        Integer patientId = 50;
        when(consultationCpoCeoIndicesStorage.getConsultationIndices(patientId)).thenReturn(
                Arrays.asList(
                        new CpoCeoIndicesBo(1, 2, 3,
                                0, 0, 0,
                                null, null, 28, 0,
                                LocalDateTime.of(2020, 6, 18, 15, 0)),

                        new CpoCeoIndicesBo(0, 1, 0,
                                0, 0, 0,
                                null, null, 20, 15,
                                LocalDateTime.of(2020, 1, 13, 10, 0))
                )
        );

        List<CpoCeoIndicesBo> result = fetchCpoCeoIndices.run(patientId);
        Assertions.assertEquals(2, result.size());

        CpoCeoIndicesBo first = result.get(0);
        CpoCeoIndicesBo second = result.get(1);

        // C P O
        Assertions.assertEquals(1, first.getPermanentC());
        Assertions.assertEquals(2, first.getPermanentP());
        Assertions.assertEquals(3, first.getPermanentO());
        // c e o
        Assertions.assertEquals(0, first.getTemporaryC());
        Assertions.assertEquals(0, first.getTemporaryE());
        Assertions.assertEquals(0, first.getTemporaryO());
        // CPO ceo indices
        Assertions.assertEquals(6, first.getCpoIndex());
        Assertions.assertEquals(0, first.getCeoIndex());
        // Teeth present
        Assertions.assertEquals(28, first.getPermanentTeethPresent());
        Assertions.assertEquals(0, first.getTemporaryTeethPresent());


        // C P O
        Assertions.assertEquals(0, second.getPermanentC());
        Assertions.assertEquals(1, second.getPermanentP());
        Assertions.assertEquals(0, second.getPermanentO());
        // c e o
        Assertions.assertEquals(0, second.getTemporaryC());
        Assertions.assertEquals(0, second.getTemporaryE());
        Assertions.assertEquals(0, second.getTemporaryO());
        // CPO ceo indices
        Assertions.assertEquals(1, second.getCpoIndex());
        Assertions.assertEquals(0, second.getCeoIndex());
        // Teeth present
        Assertions.assertEquals(20, second.getPermanentTeethPresent());
        Assertions.assertEquals(15, second.getTemporaryTeethPresent());
    }

}