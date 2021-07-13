package ar.lamansys.immunization.domain.vaccine;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode
public class Thresholds {

    @EqualsAndHashCode.Include
    private final Integer minimum;

    @EqualsAndHashCode.Include
    private final Integer maximum;

    public Thresholds(Integer minimum, Integer maximum) {
        Objects.requireNonNull(minimum, () -> {
            throw new VaccineException(VaccineExceptionEnum.NULL_DAY_THRESHOLD, "Limite mínimo de dias es obligatorio");
        });
        Objects.requireNonNull(minimum, () -> {
            throw new VaccineException(VaccineExceptionEnum.NULL_DAY_THRESHOLD, "Limite máximo de dias es obligatorio");
        });
        if (minimum < 0)
            throw new VaccineException(VaccineExceptionEnum.NEGATIVE_DAY_THRESHOLD, "El límite mínimo de días no puede ser negativo");

        if (minimum.compareTo(maximum) > 0)
            throw new VaccineException(VaccineExceptionEnum.INVALID_THRESHOLD, "La cantidad de días mínimos no puede ser superior al limite máximo");
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public boolean apply(Integer days) {
        return minimum <= days && days <= maximum;
    }
}
