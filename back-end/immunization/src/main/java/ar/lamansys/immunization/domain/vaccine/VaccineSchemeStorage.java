package ar.lamansys.immunization.domain.vaccine;

import java.util.Optional;

public interface VaccineSchemeStorage {
    boolean isValidScheme(Short id);

    Optional<VaccineSchemeBo> fetchSchemeById(Short id);
}
