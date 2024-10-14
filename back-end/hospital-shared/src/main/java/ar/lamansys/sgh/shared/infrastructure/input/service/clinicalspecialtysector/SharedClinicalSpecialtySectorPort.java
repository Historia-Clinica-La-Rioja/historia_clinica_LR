package ar.lamansys.sgh.shared.infrastructure.input.service.clinicalspecialtysector;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtySectorDto;

import java.util.Optional;

public interface SharedClinicalSpecialtySectorPort {

	Optional<ClinicalSpecialtySectorDto> getClinicalSpecialtySectorById(Integer id);
}
