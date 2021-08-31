package ar.lamansys.immunization.domain.vaccine;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@EqualsAndHashCode
@ToString
public class VaccineDescription {

    @EqualsAndHashCode.Include
    private String value;

    public VaccineDescription(String value) {
        Objects.requireNonNull(value, () -> {
            throw new VaccineException(VaccineExceptionEnum.NULL_VACCINE_DESCRIPTION, "La descripción no puede ser nula");
        });
        this.value = value;
    }
}
