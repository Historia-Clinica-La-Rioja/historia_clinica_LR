package ar.lamansys.sgh.publicapi.application;

import ar.lamansys.sgh.publicapi.application.fetchactivitybyid.FetchActivityById;
import ar.lamansys.sgh.publicapi.application.port.out.ActivityStorage;
import ar.lamansys.sgh.publicapi.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchActivityByIdTest {

    private FetchActivityById fetchActivityById;

    @Mock
    private ActivityStorage activityStorage;

    @BeforeEach
    void setup() {
        fetchActivityById = new FetchActivityById(activityStorage);
    }

    @Test
    void activitySuccess() {
        String refsetCode = "";
        String provinceCode = "";
        Long activityId = 10L;

        when(activityStorage.getActivityById(refsetCode, provinceCode, activityId)).thenReturn(
                Optional.of(new AttentionInfoBo(
                        10, LocalDate.now(),
                        new SnomedBo("1", "1"),
                        new PersonInfoBo("35555555", "Juan", "Perez", LocalDate.ofYearDay(1990, 1), GenderEnum.MALE),
                        new CoverageActivityInfoBo("AN-35555555"), ScopeEnum.AMBULATORIA,
                        new InternmentBo("100", LocalDate.ofYearDay(2020, 1), LocalDate.ofYearDay(2020, 20)),
                        new ProfessionalBo(1, "Juan", "Perez", "DOC-30000000", "30000000"))
        ));

        AttentionInfoBo result = fetchActivityById.run(refsetCode, provinceCode, activityId);
        Assertions.assertNotNull(result);
    }

    @Test
    void activityFailed() {
        String refsetCode = "";
        String provinceCode = "";
        Long activityId = 10L;

        when(activityStorage.getActivityById(refsetCode, provinceCode, activityId)).thenReturn(
                Optional.empty());

        AttentionInfoBo result = fetchActivityById.run(refsetCode, provinceCode, activityId);
        Assertions.assertNull(result);
    }
}
