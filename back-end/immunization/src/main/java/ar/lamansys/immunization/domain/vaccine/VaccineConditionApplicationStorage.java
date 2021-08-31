package ar.lamansys.immunization.domain.vaccine;

import java.util.Optional;

public interface VaccineConditionApplicationStorage {
    boolean isValidConditionApplication(Short id);

    Optional<VaccineConditionApplicationBo> fetchConditionApplicationById(Short id);
}
