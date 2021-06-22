package ar.lamansys.immunization.domain.vaccine;

import lombok.Getter;

import java.util.Objects;

@Getter
public class VaccineDescription {

    private String value;

    public VaccineDescription(String value) {
        Objects.requireNonNull(value, () -> {
            throw new VaccineException(VaccineExceptionEnum.NULL_VACCINE_DESCRIPTION, "La descripción no puede ser nula");
        });
        this.value = value;
    }
}
