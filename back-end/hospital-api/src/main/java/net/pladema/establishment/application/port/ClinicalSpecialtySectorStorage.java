package net.pladema.establishment.application.port;

import net.pladema.establishment.domain.ClinicalSpecialtySectorBo;

import java.util.Optional;

public interface ClinicalSpecialtySectorStorage {

	Optional<ClinicalSpecialtySectorBo> getClinicalSpecialtySectorById(Integer id);
}
