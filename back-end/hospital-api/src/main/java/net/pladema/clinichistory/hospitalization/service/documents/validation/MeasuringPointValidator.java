package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import java.time.LocalDateTime;
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

    private final Integer BLOOD_PRESSURE_MIN = 20;
    private final Integer BLOOD_PRESSURE_MAX = 140;
    private final String BLOOD_PRESSURE_MIN_NAME = "tensión sanguínea mínima";
    private final String BLOOD_PRESSURE_MAX_NAME = "tensión sanguínea máxima";
    private final Integer BLOOD_PULSE_MIN = 70;
    private final Integer BLOOD_PULSE_MAX = 100;
    private final String BLOOD_PULSE_NAME = "pulso sanguíneo";
    private final Integer O2_SATURATION_MIN = 0;
    private final Integer O2_SATURATION_MAX = 100;
    private final String O2_SATURATION_NAME = "saturación de O2";
    private final Integer CO2_END_TIDAL_MIN = 0;
    private final Integer CO2_END_TIDAL_MAX = 240;
    private final String CO2_END_TIDAL_NAME = "CO2 End tidal";

    public void assertContextValid(List<MeasuringPointBo> measuringPoints) {

        this.assertDuplicatedValues(measuringPoints);

        measuringPoints.forEach(this::assertMeasurementRanges);
    }

    private void assertDuplicatedValues(List<MeasuringPointBo> measuringPoints) {
        if (measuringPoints == null || measuringPoints.isEmpty())
            return;
        final Set<MeasuringPointBo> set = new HashSet<>();
        for (MeasuringPointBo mp : measuringPoints)
            if (!set.add(mp))
                throw new ConstraintViolationException("Puntos de medición repetidos", Collections.emptySet());
    }

    private void assertMeasurementRanges(MeasuringPointBo measuringPointBo) {

        LocalDateTime measurementTime = LocalDateTime.of(measuringPointBo.getDate(), measuringPointBo.getTime());

        Integer bloodPressureMin = measuringPointBo.getBloodPressureMin();
        this.assertBetweenMinMaxValues(BLOOD_PRESSURE_MIN_NAME, bloodPressureMin, BLOOD_PRESSURE_MIN, BLOOD_PRESSURE_MAX, measurementTime);

        Integer bloodPressureMax = measuringPointBo.getBloodPressureMax();
        this.assertBetweenMinMaxValues(BLOOD_PRESSURE_MAX_NAME, bloodPressureMax, BLOOD_PRESSURE_MIN, BLOOD_PRESSURE_MAX, measurementTime);

        Integer bloodPulse = measuringPointBo.getBloodPulse();
        this.assertBetweenMinMaxValues(BLOOD_PULSE_NAME, bloodPulse, BLOOD_PULSE_MIN, BLOOD_PULSE_MAX, measurementTime);

        Integer o2Saturation = measuringPointBo.getO2Saturation();
        this.assertBetweenMinMaxValues(O2_SATURATION_NAME, o2Saturation, O2_SATURATION_MIN, O2_SATURATION_MAX, measurementTime);

        Integer co2EndTidal = measuringPointBo.getCo2EndTidal();
        this.assertBetweenMinMaxValues(CO2_END_TIDAL_NAME, co2EndTidal, CO2_END_TIDAL_MIN, CO2_END_TIDAL_MAX, measurementTime);

    }

    private void assertBetweenMinMaxValues(String concept, Integer value, Integer min, Integer max, LocalDateTime measurementTime) {

        if (value != null && (value < min || value > max))
            throw new ConstraintViolationException(String.format("Medición %s: Valor fuera de rango para %s", measurementTime, concept), Collections.emptySet());

    }
}
