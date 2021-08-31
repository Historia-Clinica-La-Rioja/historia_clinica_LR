package ar.lamansys.immunization.domain.vaccine;

import java.util.Optional;

public interface VaccineStorage {

    Optional<VaccineBo> findById(String sctid);
}
