package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MeasuringPointValidator {

    public void assertNotDuplicated(List<MeasuringPointBo> measuringPoints) {
        if (this.hasDuplicatedValues(measuringPoints))
            throw new ConstraintViolationException("Puntos de medición repetidos", Collections.emptySet());
    }

    private boolean hasDuplicatedValues(List<MeasuringPointBo> measuringPoints) {
        if (measuringPoints == null || measuringPoints.isEmpty())
            return false;
        final Set<MeasuringPointBo> set = new HashSet<>();
        for (MeasuringPointBo mp : measuringPoints)
            if (!set.add(mp))
                return true;
        return false;
    }
}
